package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.batchtask;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskState;
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

/**
 * Description: 磁盘故障恢复Handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/26
 *
 * @author TD
 */
public class DiskFaultRecoveryBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskFaultRecoveryBatchTaskHandler.class);

    private String diskName;

    private BaseAuditLogAPI auditLogAPI;

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    public DiskFaultRecoveryBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
            CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.cbbVDIDeskDiskAPI = cbbVDIDeskDiskAPI;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID diskId = batchTaskItem.getItemID();
        CbbDeskDiskDTO diskDetail = null;
        try {
            diskDetail = cbbVDIDeskDiskAPI.getDiskDetail(diskId);
            // 校验磁盘
            checkDiskFaultRecovery(diskDetail);
            UUID deskId = diskDetail.getDeskId();
            // 若磁盘不绑定桌面，直接成功
            if (Objects.isNull(deskId)) {
                cbbVDIDeskDiskAPI.updateDiskState(diskId, CbbDiskState.ACTIVE, null);
                return success(diskDetail);
            }
            // 尝试卸载磁盘
            cbbVDIDeskDiskAPI.deactivateDisk(deskId, diskId);
            return success(diskDetail);
        } catch (Exception e) {
            String signName = Objects.nonNull(diskDetail) ? diskDetail.getName() : diskId.toString();
            LOGGER.error("磁盘[{}]故障恢复失败：", signName, e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY_FAIL_LOG, e, signName, message);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY_FAIL_LOG, e, signName, message);
        }
    }

    private BatchTaskItemResult success(CbbDeskDiskDTO diskDetail) {
        auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY_SUCCESS_LOG, diskDetail.getName());
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY_SUCCESS_LOG).msgArgs(diskDetail.getName()).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (diskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY_SUCCESS_LOG)
                        .msgArgs(new String[] {diskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY_FAIL)
                        .msgArgs(new String[] {diskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, DiskPoolBusinessKey.RCDC_RCO_BATCH_DELETE_DISK_TASK_RESULT);
        }
    }

    private void checkDiskFaultRecovery(CbbDeskDiskDTO diskDetail) throws BusinessException {
        // 非异常状态磁盘不能进行异常恢复
        if (diskDetail.getState() != CbbDiskState.ERROR) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_STATUS_OPERATION_UNALLOWED, diskDetail.getName(),
                    DiskBusinessKeyEnums.obtainResolve(diskDetail.getState().name()),
                    LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY));
        }
    }
}
