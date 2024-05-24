package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.batchtask;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbRemoveDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
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
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;

/**
 * Description: 删除磁盘handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13
 *
 * @author TD
 */
public class DeleteDiskBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDiskBatchTaskHandler.class);

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    private UserDiskMgmtAPI userDiskMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private String diskName;

    private StateMachineFactory stateMachineFactory;

    private Boolean shouldOnlyDeleteDataFromDb;

    private String prefix;

    public DeleteDiskBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI,
                                      BaseAuditLogAPI auditLogAPI, Boolean shouldOnlyDeleteDataFromDb) {
        super(batchTaskItemIterator);
        this.cbbVDIDeskDiskAPI = cbbVDIDeskDiskAPI;
        this.auditLogAPI = auditLogAPI;
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public void setDiskPoolMgmtAPI(CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI) {
        this.cbbDiskPoolMgmtAPI = cbbDiskPoolMgmtAPI;
    }

    public void setUserDiskMgmtAPI(UserDiskMgmtAPI userDiskMgmtAPI) {
        this.userDiskMgmtAPI = userDiskMgmtAPI;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID diskId = batchTaskItem.getItemID();
        CbbDeskDiskDTO deskDiskDTO = null;
        CbbDiskPoolDTO diskPoolDetail = null;
        try {
            deskDiskDTO = cbbVDIDeskDiskAPI.getDiskDetail(diskId);

            // 获取磁盘对应磁盘池信息
            UUID diskPoolId = deskDiskDTO.getDiskPoolId();
            diskPoolDetail = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);

            if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                cbbVDIDeskDiskAPI.deleteDiskFormDb(diskId);
            } else {
                // 校验磁盘是否可删除
                checkDiskForDelete(deskDiskDTO);

                CbbRemoveDeskDiskDTO diskDTO = new CbbRemoveDeskDiskDTO();
                UUID taskId = UUID.randomUUID();
                diskDTO.setTaskId(taskId);
                diskDTO.setDeskId(deskDiskDTO.getDeskId());
                diskDTO.setDiskId(deskDiskDTO.getId());
                LOGGER.info("准备开始删除磁盘池[{}]中磁盘[{}]，信息：{}", diskPoolId, deskDiskDTO.getName(), deskDiskDTO.toString());
                cbbVDIDeskDiskAPI.removeDisk(diskDTO);
                // 等待任务结束
                StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
                stateMachineMgmtAgent.waitForAllProcessFinish();
            }

            // 删除用户与磁盘绑定记录
            userDiskMgmtAPI.deleteUserDisk(diskId);
            // 记录审计日志
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_ITEM_SUCCESS_DESC, diskPoolDetail.getName(), deskDiskDTO.getName(), prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_ITEM_SUCCESS_DESC).msgArgs(diskPoolDetail.getName(), deskDiskDTO.getName(), prefix)
                    .build();
        } catch (Exception e) {
            String signName = Objects.nonNull(deskDiskDTO) ? deskDiskDTO.getName() : diskId.toString();
            LOGGER.error("删除磁盘[{}]失败", signName, e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            // 磁盘池信息不为空则加上磁盘所属磁盘池信息
            if (Objects.nonNull(diskPoolDetail)) {
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_ITEM_FAIL_DESC, e, diskPoolDetail.getName(), signName, message, prefix);
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_ITEM_FAIL_DESC, e, diskPoolDetail.getName(), signName, message, prefix);
            }
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_NOT_POOL_DELETE_ITEM_FAIL_DESC, e, signName, message, prefix);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_NOT_POOL_DELETE_ITEM_FAIL_DESC, e, signName, message, prefix);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (diskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_SINGLE_SUCCESS)
                        .msgArgs(new String[] {diskName, prefix}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_SINGLE_FAIL)
                        .msgArgs(new String[] {diskName, prefix}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return WebBatchTaskUtils.buildDefaultFinishResult(successCount, failCount, DiskPoolBusinessKey.RCDC_RCO_BATCH_DELETE_DISK_TASK_RESULT, prefix);
        }
    }

    private void checkDiskForDelete(CbbDeskDiskDTO cbbDeskDiskDTO) throws BusinessException {
        // 不是池数据盘不能删除
        if (Objects.isNull(cbbDeskDiskDTO.getDiskPoolId())) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_DELETE_UNALLOWED, cbbDeskDiskDTO.getName());
        }
        // 非可用，禁用，故障状态磁盘不能删除
        if (!DiskPoolConstants.REMOVABLE_DISK_STATUS.contains(cbbDeskDiskDTO.getState())) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_STATUS_OPERATION_UNALLOWED, cbbDeskDiskDTO.getName(),
                    DiskBusinessKeyEnums.obtainResolve(cbbDeskDiskDTO.getState().name()),
                    LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK, prefix));
        }
    }
}
