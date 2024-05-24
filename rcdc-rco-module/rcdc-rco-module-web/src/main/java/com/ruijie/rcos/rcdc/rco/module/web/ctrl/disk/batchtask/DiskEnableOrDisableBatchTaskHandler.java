package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.batchtask;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
 * Description: 磁盘启用或禁用操作
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/16
 *
 * @author TD
 */
public class DiskEnableOrDisableBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskEnableOrDisableBatchTaskHandler.class);

    /**
     * 可启用-禁用的磁盘状态
     */
    private static final List<CbbDiskState> DISK_STATUS = Arrays.asList(CbbDiskState.ACTIVE, CbbDiskState.DISABLE);

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbDiskState diskState;

    private String diskName;

    private String operationContent;

    public DiskEnableOrDisableBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
            CbbDiskState diskState) {
        super(batchTaskItemIterator);
        this.diskState = diskState;
        this.auditLogAPI = auditLogAPI;
        if (diskState == CbbDiskState.ACTIVE) {
            operationContent = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_ENABLE_DISK);
        } else {
            operationContent = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISABLE_DISK);
        }
    }

    /**
     * setCbbVDIDeskDiskAPI
     * 
     * @param cbbVDIDeskDiskAPI 操作磁盘API
     * @return DiskEnableOrDisableBatchTaskHandler
     */
    public DiskEnableOrDisableBatchTaskHandler setCbbVDIDeskDiskAPI(CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI) {
        this.cbbVDIDeskDiskAPI = cbbVDIDeskDiskAPI;
        return this;
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
            checkDiskForEnable(diskDetail);
            diskDetail.setState(diskState);
            cbbVDIDeskDiskAPI.updateDisk(diskDetail);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_OPERATION_DISK_SUCCESS_LOG, operationContent, diskDetail.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_RCO_OPERATION_DISK_SUCCESS_LOG).msgArgs(operationContent, diskDetail.getName()).build();
        } catch (Exception e) {
            String signName = Objects.nonNull(diskDetail) ? diskDetail.getName() : diskId.toString();
            LOGGER.error("{}[{}]失败：", operationContent, signName, e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_OPERATION_DISK_FAIL_LOG, e, operationContent, signName, message);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_OPERATION_DISK_FAIL_LOG, e, operationContent, signName, message);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (diskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_OPERATION_DISK_SUCCESS_LOG)
                        .msgArgs(new String[] {operationContent, diskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_OPERATION_DISK_FAIL)
                        .msgArgs(new String[] {operationContent, diskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount,
                    diskState == CbbDiskState.DISABLE ? DiskPoolBusinessKey.RCDC_RCO_BATCH_DISABLE_DISK_TASK_RESULT
                            : DiskPoolBusinessKey.RCDC_RCO_BATCH_ENABLE_DISK_TASK_RESULT);
        }
    }

    private void checkDiskForEnable(CbbDeskDiskDTO cbbDeskDiskDTO) throws BusinessException {
        // 非可用，禁用状态磁盘不能进行禁用/启用
        if (!DISK_STATUS.contains(cbbDeskDiskDTO.getState())) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_STATUS_OPERATION_UNALLOWED, cbbDeskDiskDTO.getName(),
                    DiskBusinessKeyEnums.obtainResolve(cbbDeskDiskDTO.getState().name()), operationContent);
        }
    }
}
