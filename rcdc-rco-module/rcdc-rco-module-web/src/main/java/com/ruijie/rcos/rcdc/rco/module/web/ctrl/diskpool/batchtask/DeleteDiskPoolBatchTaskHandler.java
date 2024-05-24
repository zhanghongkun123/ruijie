package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask;


import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbRemoveDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformValidateAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskBusinessKeyEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
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

/**
 * Description: 删除磁盘池批处理任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author TD
 */
public class DeleteDiskPoolBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDiskPoolBatchTaskHandler.class);

    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    private CbbVDIDeskDiskAPI deskDiskAPI;

    private BaseAuditLogAPI auditLogAPI;

    private UserDiskMgmtAPI userDiskMgmtAPI;

    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    private StateMachineFactory stateMachineFactory;

    private AdminDataPermissionAPI adminDataPermissionAPI;

    private String diskPoolName;

    private Boolean shouldOnlyDeleteDataFromDb;

    private CloudPlatformValidateAPI cloudPlatformValidateAPI;

    /**
     * 最大超时时间
     */
    private static final int MAX_OVER_TIME = 120;

    private static final ThreadExecutor CUSTOM_THREAD_EXECUTOR =
            ThreadExecutors.newBuilder("DeleteDiskPoolHandler").maxThreadNum(30).queueSize(50000).build();

    private String prefix;

    public DeleteDiskPoolBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI,
            CbbVDIDeskDiskAPI deskDiskAPI, Boolean shouldOnlyDeleteDataFromDb) {
        super(batchTaskItemIterator);
        this.cbbDiskPoolMgmtAPI = cbbDiskPoolMgmtAPI;
        this.deskDiskAPI = deskDiskAPI;
        this.adminDataPermissionAPI = SpringBeanHelper.getBean(AdminDataPermissionAPI.class);
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setUserDiskMgmtAPI(UserDiskMgmtAPI userDiskMgmtAPI) {
        this.userDiskMgmtAPI = userDiskMgmtAPI;
    }

    public void setDiskPoolMgmtAPI(DiskPoolMgmtAPI diskPoolMgmtAPI) {
        this.diskPoolMgmtAPI = diskPoolMgmtAPI;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public void setDiskPoolName(String diskPoolName) {
        this.diskPoolName = diskPoolName;
    }

    public void setCloudPlatformValidateAPI(CloudPlatformValidateAPI cloudPlatformValidateAPI) {
        this.cloudPlatformValidateAPI = cloudPlatformValidateAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID diskPoolId = batchTaskItem.getItemID();
        CbbDiskPoolDTO diskPoolDetail = null;
        try {
            diskPoolDetail = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
            // 校验桌面池状态
            checkDiskPoolForDelete(diskPoolId);
            if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                // 将磁盘池磁盘从数据库中删除
                deleteDiskPoolDiskFromDb(diskPoolDetail);

            } else {
                // 更新磁盘池为删除中
                cbbDiskPoolMgmtAPI.updateState(diskPoolId, CbbDiskPoolState.DELETING);
                List<CbbDeskDiskDTO> diskDTOList = deskDiskAPI.listDiskInfoByDiskPoolId(diskPoolId);
                CountDownLatch countDownLatch = new CountDownLatch(diskDTOList.size());
                // 删除磁盘
                AtomicInteger successDisk = deleteDiskListCall(diskPoolDetail, diskDTOList, countDownLatch);
                // 等待
                if (!countDownLatch.await(MAX_OVER_TIME, TimeUnit.MINUTES)) {
                    LOGGER.warn("[{}]任务执行时长已超过120分钟，等待超时。。。。", batchTaskItem.getItemName());
                    // 回滚磁盘池状态为可用
                    cbbDiskPoolMgmtAPI.updateState(diskPoolId, CbbDiskPoolState.AVAILABLE);
                    throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_OVERTIME_FAIL, String.valueOf(MAX_OVER_TIME));
                }
                // 删除成功磁盘数不等于总数，则认为失败
                if (successDisk.intValue() != diskDTOList.size()) {
                    // 更新磁盘池为可用
                    cbbDiskPoolMgmtAPI.updateState(diskPoolId, CbbDiskPoolState.AVAILABLE);
                    throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_FAIL);
                }
            }
            diskPoolMgmtAPI.deleteDiskPool(diskPoolId);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_ITEM_SUCCESS_DESC, diskPoolDetail.getName(), prefix);
            // 删除数据权限
            adminDataPermissionAPI.deleteByPermissionDataId(diskPoolId.toString());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_ITEM_SUCCESS_DESC).msgArgs(diskPoolDetail.getName(), prefix).build();
        } catch (InterruptedException e1) {
            String signName = Objects.nonNull(diskPoolDetail) ? diskPoolDetail.getName() : diskPoolId.toString();
            LOGGER.error("等待删除磁盘池[{}]缓存线程执行被中断", signName, e1);
            Thread.currentThread().interrupt();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_ITEM_FAIL_DESC, signName, e1.getMessage(), prefix);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_ITEM_FAIL_DESC, e1, signName, e1.getMessage(), prefix);
        } catch (Exception e) {
            String signName = Objects.nonNull(diskPoolDetail) ? diskPoolDetail.getName() : diskPoolId.toString();
            LOGGER.error("删除磁盘池[{}]失败", signName, e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_ITEM_FAIL_DESC, signName, message, prefix);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_ITEM_FAIL_DESC, e, signName, message, prefix);
        }
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (diskPoolName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_ITEM_SUCCESS_DESC)
                        .msgArgs(new String[] {diskPoolName, prefix}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_FAIL)
                        .msgArgs(new String[] {diskPoolName, prefix}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return WebBatchTaskUtils.buildDefaultFinishResult(successCount, failCount, DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK_POOL_TASK_RESULT, prefix);
        }
    }

    private AtomicInteger deleteDiskListCall(final CbbDiskPoolDTO diskPoolDetail, List<CbbDeskDiskDTO> diskDTOList, CountDownLatch countDownLatch) {
        AtomicInteger success = new AtomicInteger();
        for (CbbDeskDiskDTO cbbDeskDiskDTO : diskDTOList) {
            final String diskName = cbbDeskDiskDTO.getName();
            CUSTOM_THREAD_EXECUTOR.execute(() -> {
                try {
                    UUID removeTaskId = UUID.randomUUID();
                    CbbRemoveDeskDiskDTO removeDeskDiskDTO = new CbbRemoveDeskDiskDTO();
                    removeDeskDiskDTO.setTaskId(removeTaskId);
                    removeDeskDiskDTO.setDiskId(cbbDeskDiskDTO.getId());
                    removeDeskDiskDTO.setDeskId(cbbDeskDiskDTO.getDeskId());
                    LOGGER.info("开始删除磁盘[{}]，任务流ID[{}]", diskName, removeTaskId);
                    deskDiskAPI.removeDisk(removeDeskDiskDTO);
                    // 等待任务结束
                    stateMachineFactory.findAgentById(removeTaskId).waitForAllProcessFinish();
                    // 解除绑定关系
                    userDiskMgmtAPI.deleteUserDisk(cbbDeskDiskDTO.getId());
                    // 记录成功数
                    success.incrementAndGet();
                    auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_ITEM_SUCCESS_DESC, diskPoolDetail.getName(), diskName, prefix);
                } catch (BusinessException e) {
                    auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_ITEM_FAIL_DESC, diskPoolDetail.getName(), diskName,
                            e.getI18nMessage(), prefix);
                    LOGGER.error("删除磁盘[{}]失败:", diskName, e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        return success;
    }

    private void checkDiskPoolForDelete(UUID diskPoolId) throws BusinessException {
        CbbDiskPoolDTO diskPool = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
        if (diskPool.getPoolState() != CbbDiskPoolState.AVAILABLE) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UNAVAILABLE_DELETE_UNALLOWED, diskPool.getName(),
                    DiskBusinessKeyEnums.obtainResolve(diskPool.getPoolState().name()));
        }

        List<CbbDeskDiskDTO> cbbDeskDiskList = deskDiskAPI.listDiskInfoByDiskPoolId(diskPoolId);

        if (cbbDeskDiskList.stream().anyMatch(cbbDeskDiskDTO -> !DiskPoolConstants.REMOVABLE_DISK_STATUS.contains(cbbDeskDiskDTO.getState()))) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_IN_DISK_DELETE_UNALLOWED, diskPool.getName());
        }
    }


    private void deleteDiskPoolDiskFromDb(CbbDiskPoolDTO diskPoolDetail) throws BusinessException {
        List<CbbDeskDiskDTO> diskDTOList = deskDiskAPI.listDiskInfoByDiskPoolId(diskPoolDetail.getId());

        // 检验云平台状态
        UUID platformId = diskPoolDetail.getPlatformId();
        if (platformId != null) {
            cloudPlatformValidateAPI.validateCloudPlatformStatus(platformId, CloudPlatformStatus.OFFLINE);
        }
        // 校验磁盘状态
        for (CbbDeskDiskDTO cbbDeskDiskDTO : diskDTOList) {
            if (CbbDiskState.isInMiddleStatus(cbbDeskDiskDTO.getState())) {
                throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_DESK_DISK_IN_MIDDLE_STATUS);
            }
        }
        // 删除磁盘
        for (CbbDeskDiskDTO cbbDeskDiskDTO : diskDTOList) {
            deskDiskAPI.deleteDiskFormDb(cbbDeskDiskDTO.getId());
        }
    }
}
