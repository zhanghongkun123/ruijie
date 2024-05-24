package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/28 13:36
 *
 * @author zhangyichi
 */
public class DistributeTaskDeleteTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    private SessionContext sessionContext;

    private GeneralPermissionHelper generalPermissionHelper;


    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setFileDistributionTaskManageAPI(FileDistributionTaskManageAPI fileDistributionTaskManageAPI) {
        this.fileDistributionTaskManageAPI = fileDistributionTaskManageAPI;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setGeneralPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    public DistributeTaskDeleteTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        UUID taskId = batchTaskItem.getItemID();
        String logName = taskId.toString();
        try {

            generalPermissionHelper.checkPermission(sessionContext, taskId, AdminDataPermissionType.FILE_DISTRIBUTION);

            DistributeTaskParameterDTO taskInfo = fileDistributionTaskManageAPI.getBasicInfo(new IdRequest(taskId));
            if (taskInfo != null) {
                logName = taskInfo.getTaskName();
                fileDistributionTaskManageAPI.deleteTask(new IdRequest(taskId));
                generalPermissionHelper.deletePermission(taskId, AdminDataPermissionType.FILE_DISTRIBUTION);
            }
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_TASK_ITEM_SUCCESS_DESC, logName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_TASK_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[]{logName})
                    .build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_TASK_ITEM_FAIL_DESC, logName,
                    e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_TASK_ITEM_FAIL_DESC, e, logName,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(
                successCount, failCount, FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_BATCH_DELETE_TASK_RESULT);

    }
}
