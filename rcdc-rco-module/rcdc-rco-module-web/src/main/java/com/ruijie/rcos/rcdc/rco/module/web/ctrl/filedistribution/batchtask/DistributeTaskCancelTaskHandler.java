package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey;
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

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/24 14:31
 *
 * @author zhangyichi
 */
public class DistributeTaskCancelTaskHandler extends AbstractSingleTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;


    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setFileDistributionTaskManageAPI(FileDistributionTaskManageAPI fileDistributionTaskManageAPI) {
        this.fileDistributionTaskManageAPI = fileDistributionTaskManageAPI;
    }

    public DistributeTaskCancelTaskHandler(BatchTaskItem batchTaskItem) {
        super(batchTaskItem);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        UUID taskId = batchTaskItem.getItemID();
        String taskName = batchTaskItem.getItemName();
        try {
            fileDistributionTaskManageAPI.cancelTask(new IdRequest(taskId));
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_SUCCESS_LOG, taskName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[] {taskName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_FAIL_LOG,
                    taskName, e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_ITEM_FAIL_DESC, e, taskName,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_SUCCESS)
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_FAIL)
                    .build();
        }
    }
}
