package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.batchtask;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbVDIExpandDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskState;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskBusinessKeyEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;

/**
 * Description: 扩容磁盘handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
public class ExpandDiskBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpandDiskBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    private StateMachineFactory stateMachineFactory;

    private Integer expandCapacity;

    private String diskName;

    private List<UUID> diskList;

    private static final ThreadExecutor CUSTOM_THREAD_EXECUTOR =
            ThreadExecutors.newBuilder("ExpandDiskPoolHandler").maxThreadNum(30).queueSize(50000).build();

    /**
     * 最大超时时间
     */
    private static final int MAX_OVER_TIME = 60;

    /**
     * 记录扩容成功的磁盘数
     */
    private AtomicInteger success = new AtomicInteger();

    public ExpandDiskBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, Integer expandCapacity, BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItemIterator);
        this.expandCapacity = expandCapacity;
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbVDIDeskDiskAPI(CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI) {
        this.cbbVDIDeskDiskAPI = cbbVDIDeskDiskAPI;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public void setDiskList(List<UUID> diskList) {
        this.diskList = diskList;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        // 单个磁盘进行扩容
        if (Objects.nonNull(diskName)) {
            UUID diskId = diskList.get(0);
            CbbDeskDiskDTO deskDiskDTO = null;
            try {
                deskDiskDTO = cbbVDIDeskDiskAPI.getDiskDetail(diskId);
                expandDisk(deskDiskDTO, success);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_SUCCESS_LOG).msgArgs(diskName).build();
            } catch (Exception e) {
                String signName = Objects.nonNull(deskDiskDTO) ? deskDiskDTO.getName() : diskId.toString();
                LOGGER.error("扩容磁盘[{}]失败", signName, e);
                String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_FAIL_LOG, e, signName, message);
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_FAIL_LOG, e, diskName, message);
            }
        }
        // 批量扩容磁盘
        try {
            CountDownLatch countDownLatch = new CountDownLatch(diskList.size());
            batchExpandDisk(countDownLatch);
            // 等待
            if (!countDownLatch.await(MAX_OVER_TIME, TimeUnit.MINUTES)) {
                LOGGER.warn("批量扩容磁盘任务[{}]执行时长已超过60分钟，等待超时。。。。", batchTaskItem.getItemName());
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_OVERTIME_FAIL, String.valueOf(MAX_OVER_TIME));
            }
            // 扩容磁盘数不等于总数，则认为失败
            if (success.intValue() != diskList.size()) {
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_FAIL);
            }
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_BATCH_SUCCESS).build();
        } catch (InterruptedException e1) {
            LOGGER.error("等待批量扩容磁盘[{}]缓存线程执行被中断", e1);
            Thread.currentThread().interrupt();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_BATCH_FAIL, e1, e1.getMessage());
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_BATCH_FAIL, e1, e1.getMessage());
        } catch (Exception e) {
            LOGGER.error("批量任务[{}]执行失败", batchTaskItem.getItemName(), e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_BATCH_FAIL, e, message);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_BATCH_FAIL, e, message);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (diskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_SUCCESS_LOG)
                        .msgArgs(new String[] {diskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_FAIL)
                        .msgArgs(new String[] {diskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(success.intValue(),
                    diskList.size() - success.intValue(), DiskPoolBusinessKey.RCDC_RCO_BATCH_DISK_EXPAND_TASK_RESULT);
        }
    }

    private void batchExpandDisk(CountDownLatch countDownLatch) {
        for (UUID diskId: diskList) {
            CUSTOM_THREAD_EXECUTOR.execute(() -> {
                CbbDeskDiskDTO deskDiskDTO = null;
                try {
                    deskDiskDTO = cbbVDIDeskDiskAPI.getDiskDetail(diskId);
                    expandDisk(deskDiskDTO, success);
                } catch (Exception e) {
                    String signName = Objects.nonNull(deskDiskDTO) ? deskDiskDTO.getName() : diskId.toString();
                    LOGGER.error("扩容磁盘[{}]失败", signName, e);
                    String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
                    auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_FAIL_LOG, e, signName, message);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
    }

    private void expandDisk(CbbDeskDiskDTO deskDiskDTO, AtomicInteger success) throws BusinessException {
        // 校验磁盘是否可扩容
        checkDiskForExpand(deskDiskDTO);
        // 磁盘当前大小与目标大小一致，直接成功
        if (expandCapacity.equals(deskDiskDTO.getCapacity())) {
            LOGGER.info("磁盘[{}]当前大小[{}]，目标大小[{}]，当前大小等于目标大小直接扩容成功", deskDiskDTO.getName(), deskDiskDTO.getCapacity(), expandCapacity);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_SUCCESS_LOG, deskDiskDTO.getName());
            success.incrementAndGet();
            return;
        }
        // 使用中的磁盘，保存目标扩容大小
        if (deskDiskDTO.getState() == CbbDiskState.IN_USE) {
            deskDiskDTO.setDelayCapacity(expandCapacity);
            cbbVDIDeskDiskAPI.updateDisk(deskDiskDTO);
            String resolveName = DiskBusinessKeyEnums.obtainResolve(deskDiskDTO.getState().name());
            LOGGER.info("磁盘[{}]处于[{}]状态，磁盘信息：{}，磁盘卸载后将自动触发延迟扩容操作", deskDiskDTO.getName(), resolveName, deskDiskDTO.toString());
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_DELAY_SUCCESS_LOG, deskDiskDTO.getName(), resolveName);
            success.incrementAndGet();
            return;
        }
        CbbVDIExpandDiskDTO expandDiskDTO = new CbbVDIExpandDiskDTO();
        UUID taskId = UUID.randomUUID();
        expandDiskDTO.setTaskId(taskId);
        expandDiskDTO.setDeskId(deskDiskDTO.getDeskId());
        expandDiskDTO.setDiskId(deskDiskDTO.getId());
        expandDiskDTO.setTargetCapacity(expandCapacity);
        LOGGER.info("准备开始扩容磁盘，磁盘信息：{}", deskDiskDTO.toString());
        cbbVDIDeskDiskAPI.expandDisk(expandDiskDTO);
        // 等待任务结束
        StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
        stateMachineMgmtAgent.waitForAllProcessFinish();
        auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_ITEM_SUCCESS_LOG, deskDiskDTO.getName());
        // 记录成功数
        success.incrementAndGet();
    }

    private void checkDiskForExpand(CbbDeskDiskDTO cbbDeskDiskDTO) throws BusinessException {
        // 不是池数据盘不能进行扩容
        if (Objects.isNull(cbbDeskDiskDTO.getDiskPoolId())) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_UNALLOWED, cbbDeskDiskDTO.getName());
        }
        // 非可用，禁用，使用中状态磁盘不能进行扩容
        if (!DiskPoolConstants.EXPAND_DISK_STATUS.contains(cbbDeskDiskDTO.getState())) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_STATUS_EXPAND_UNALLOWED, cbbDeskDiskDTO.getName(),
                    DiskBusinessKeyEnums.obtainResolve(cbbDeskDiskDTO.getState().name()));
        }
        // 扩容后磁盘容量不能小于等于实际容量
        if (cbbDeskDiskDTO.getCapacity() > expandCapacity) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_CAPACITY_LESS_THAN_ACTUALLY, String.valueOf(expandCapacity),
                    String.valueOf(cbbDeskDiskDTO.getCapacity()));
        }
    }
}
