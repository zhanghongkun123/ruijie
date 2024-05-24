package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import java.util.Collection;
import java.util.UUID;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 应用池批量开启或关闭维护模式
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/6
 *
 * @author zhengjingyong
 */
public class ChangeAppPoolMaintenanceBatchHandler extends AbstractBatchTaskHandler {

    private RcaAppPoolAPI rcaAppPoolAPI;

    private BaseAuditLogAPI auditLogAPI;

    private boolean isOpen;

    private boolean isSingleTask;

    private String appPoolName;
    
    private UUID adminId;
    
    private PermissionHelper permissionHelper;

    public ChangeAppPoolMaintenanceBatchHandler(boolean isOpen, Collection<? extends BatchTaskItem> batchTaskItemCollection,
            RcaAppPoolAPI rcaAppPoolAPI, BaseAuditLogAPI auditLogAPI, UUID adminId) {
        super(batchTaskItemCollection);
        this.isOpen = isOpen;
        this.isSingleTask = batchTaskItemCollection.size() == 1;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.auditLogAPI = auditLogAPI;
        this.adminId = adminId;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        UUID appPoolId = taskItem.getItemID();

        if (!permissionHelper.isAllGroupPermission(adminId)
                && !permissionHelper.hasDataPermission(adminId, String.valueOf(appPoolId), AdminDataPermissionType.APP_POOL)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_RCA_HAS_NO_DATA_PERMISSION);
        }
        
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(appPoolId);

        appPoolName = appPoolBaseDTO.getName();
        rcaAppPoolAPI.changeMaintenanceModel(Lists.newArrayList(appPoolId), isOpen);
        auditLogAPI.recordLog(getSuccessDescI18nKey(isOpen), appPoolName);
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).msgKey(getSuccessDescI18nKey(isOpen))
                .msgArgs(new String[]{appPoolName}).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            // 成功
            return buildTaskSuccessResult();
        }
        return buildTaskFailResult();
    }

    private String getFailDescI18nKey(boolean isOpen) {
        if (isOpen) {
            return RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE_FAIL_DESC;
        }
        return RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE_FAIL_DESC;
    }

    private String getSuccessDescI18nKey(boolean isOpen) {
        if (isOpen) {
            return RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE_ITEM_SUCCESS_DESC;
        }
        return RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE_ITEM_SUCCESS_DESC;
    }

    private DefaultBatchTaskFinishResult buildTaskSuccessResult() {
        if (isSingleTask) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(getFinishSuccessDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{appPoolName}).build();
        }
        return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                .msgKey(getFinishSuccessDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{}).build();
    }

    private String getFinishSuccessDescI18nKey(boolean isOpen, boolean isSingleTask) {
        if (isOpen) {
            return isSingleTask ? RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE_SINGLE_TASK_SUCCESS :
                    RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE_TASK_SUCCESS;
        }
        return isSingleTask ? RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE_SINGLE_TASK_SUCCESS :
                RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE_TASK_SUCCESS;
    }

    private DefaultBatchTaskFinishResult buildTaskFailResult() {
        if (isSingleTask) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(getFinishFailDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{appPoolName}).build();
        }
        return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                .msgKey(getFinishFailDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{}).build();
    }

    private String getFinishFailDescI18nKey(boolean isOpen, boolean isSingleTask) {
        if (isOpen) {
            return isSingleTask ? RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE_SINGLE_TASK_FAIL :
                    RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE_TASK_FAIL;
        }
        return isSingleTask ? RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE_SINGLE_TASK_FAIL :
                RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE_TASK_FAIL;
    }

    public PermissionHelper getPermissionHelper() {
        return permissionHelper;
    }

    public void setPermissionHelper(PermissionHelper permissionHelper) {
        this.permissionHelper = permissionHelper;
    }
}
