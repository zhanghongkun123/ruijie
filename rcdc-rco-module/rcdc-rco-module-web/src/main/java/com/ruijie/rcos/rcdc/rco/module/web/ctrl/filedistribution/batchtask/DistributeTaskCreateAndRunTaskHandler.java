package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description: 文件分发任务创建并执行批任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/10 11:31
 *
 * @author zhangyichi
 */
public class DistributeTaskCreateAndRunTaskHandler extends AbstractSingleTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    private CreateDistributeTaskRequest apiRequest;

    private GeneralPermissionHelper generalPermissionHelper;

    private SessionContext sessionContext;


    public DistributeTaskCreateAndRunTaskHandler(BatchTaskItem batchTaskItem) {
        super(batchTaskItem);
    }


    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setFileDistributionTaskManageAPI(FileDistributionTaskManageAPI fileDistributionTaskManageAPI) {
        this.fileDistributionTaskManageAPI = fileDistributionTaskManageAPI;
    }

    public void setApiRequest(CreateDistributeTaskRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public void setGeneralPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        String taskName = batchTaskItem.getItemName();
        UUID taskId = null;
        // 创建任务
        try {
            taskId = fileDistributionTaskManageAPI.createTask(apiRequest);
            generalPermissionHelper.savePermission(sessionContext, taskId, AdminDataPermissionType.FILE_DISTRIBUTION);

            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_SUCCESS_LOG, taskName);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_FAIL_LOG, taskName, e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_ITEM_FAIL_DESC, e, taskName,
                    e.getI18nMessage());
        }
        // 下发任务
        try {
            fileDistributionTaskManageAPI.runTask(new IdRequest(taskId));
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RUN_TASK_SUCCESS_LOG, taskName);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RUN_TASK_FAIL_LOG, taskName, e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RUN_TASK_ITEM_FAIL_DESC, e, taskName,
                    e.getI18nMessage());
        }
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_AND_RUN_TASK_ITEM_SUCCESS_DESC).msgArgs(new String[] {taskName})
                .build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_AND_RUN_TASK_SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_AND_RUN_TASK_FAIL).build();
        }
    }
}
