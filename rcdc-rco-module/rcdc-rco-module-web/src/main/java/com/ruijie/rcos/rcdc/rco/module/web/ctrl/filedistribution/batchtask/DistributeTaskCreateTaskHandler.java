package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 文件分发任务创建批任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/10 11:31
 *
 * @author zhangyichi
 */
public class DistributeTaskCreateTaskHandler extends AbstractSingleTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    private CreateDistributeTaskRequest apiRequest;

    private SessionContext sessionContext;

    private GeneralPermissionHelper generalPermissionHelper;

    public DistributeTaskCreateTaskHandler(BatchTaskItem batchTaskItem) {
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

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setGeneralPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        String taskName = batchTaskItem.getItemName();
        try {
            UUID taskId = fileDistributionTaskManageAPI.createTask(apiRequest);
            // 保存权限数据
            generalPermissionHelper.savePermission(sessionContext, taskId, AdminDataPermissionType.FILE_DISTRIBUTION);

            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_SUCCESS_LOG, taskName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_ITEM_SUCCESS_DESC).msgArgs(new String[] {taskName})
                    .build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_FAIL_LOG, taskName, e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_ITEM_FAIL_DESC, e, taskName,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_TASK_FAIL).build();
        }
    }
}
