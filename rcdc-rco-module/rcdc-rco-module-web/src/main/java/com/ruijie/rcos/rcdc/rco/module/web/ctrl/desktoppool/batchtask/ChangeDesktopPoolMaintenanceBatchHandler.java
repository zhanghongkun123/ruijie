package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import java.util.Collection;
import java.util.UUID;

import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
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
 * Description: 桌面池变更维护模式批处理handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/26
 *
 * @author linke
 */
public class ChangeDesktopPoolMaintenanceBatchHandler extends AbstractBatchTaskHandler {

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private boolean isOpen;

    private boolean isSingleTask;

    private String desktopPoolName;

    public ChangeDesktopPoolMaintenanceBatchHandler(boolean isOpen, Collection<? extends BatchTaskItem> batchTaskItemCollection) {
        super(batchTaskItemCollection);
        this.isOpen = isOpen;
        this.isSingleTask = batchTaskItemCollection.size() == 1;
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {

        Assert.notNull(taskItem, "taskItem can not be null");

        UUID desktopPoolId = taskItem.getItemID();
        CbbDesktopPoolDTO desktopPoolDTO;
        desktopPoolName = desktopPoolId.toString();
        try {
            desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(getFailDescI18nKey(isOpen), desktopPoolName, e.getI18nMessage());
            throw new BusinessException(getFailDescI18nKey(isOpen), e, desktopPoolName, e.getI18nMessage());
        }

        desktopPoolName = desktopPoolDTO.getName();
        cbbDesktopPoolMgmtAPI.changeMaintenanceModel(Lists.newArrayList(desktopPoolId), isOpen);
        auditLogAPI.recordLog(getSuccessDescI18nKey(isOpen), desktopPoolName);
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).msgKey(getSuccessDescI18nKey(isOpen))
                .msgArgs(new String[]{desktopPoolName}).build();
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
            return DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE_FAIL_DESC;
        }
        return DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE_FAIL_DESC;
    }

    private String getSuccessDescI18nKey(boolean isOpen) {
        if (isOpen) {
            return DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE_ITEM_SUCCESS_DESC;
        }
        return DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE_ITEM_SUCCESS_DESC;
    }

    private DefaultBatchTaskFinishResult buildTaskSuccessResult() {
        if (isSingleTask) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(getFinishSuccessDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{desktopPoolName}).build();
        }
        return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                .msgKey(getFinishSuccessDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{}).build();
    }

    private String getFinishSuccessDescI18nKey(boolean isOpen, boolean isSingleTask) {
        if (isOpen) {
            return isSingleTask ? DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE_SINGLE_TASK_SUCCESS :
                    DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE_TASK_SUCCESS;
        }
        return isSingleTask ? DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE_SINGLE_TASK_SUCCESS :
                DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE_TASK_SUCCESS;
    }

    private DefaultBatchTaskFinishResult buildTaskFailResult() {
        if (isSingleTask) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(getFinishFailDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{desktopPoolName}).build();
        }
        return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                .msgKey(getFinishFailDescI18nKey(isOpen, isSingleTask)).msgArgs(new String[]{}).build();
    }

    private String getFinishFailDescI18nKey(boolean isOpen, boolean isSingleTask) {
        if (isOpen) {
            return isSingleTask ? DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE_SINGLE_TASK_FAIL :
                    DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE_TASK_FAIL;
        }
        return isSingleTask ? DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE_SINGLE_TASK_FAIL :
                DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE_TASK_FAIL;
    }
}
