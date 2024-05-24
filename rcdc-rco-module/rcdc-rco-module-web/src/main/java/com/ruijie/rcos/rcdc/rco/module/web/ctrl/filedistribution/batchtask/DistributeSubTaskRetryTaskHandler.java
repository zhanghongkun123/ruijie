package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.item.DistributeSubTaskOperateBatchItem;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: 重试文件分发子任务批处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/24 15:50
 *
 * @author zhangyichi
 */
public class DistributeSubTaskRetryTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setFileDistributionTaskManageAPI(FileDistributionTaskManageAPI fileDistributionTaskManageAPI) {
        this.fileDistributionTaskManageAPI = fileDistributionTaskManageAPI;
    }

    public DistributeSubTaskRetryTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        UUID subTaskId = batchTaskItem.getItemID();
        DistributeSubTaskOperateBatchItem subTaskOperateBatchItem = (DistributeSubTaskOperateBatchItem) batchTaskItem;
        String parentTaskName = subTaskOperateBatchItem.getParentTask().getTaskName();
        String targetName = subTaskOperateBatchItem.getTargetName();
        try {
            fileDistributionTaskManageAPI.retrySubTask(new IdRequest(subTaskId));
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_SUCCESS_LOG,
                    parentTaskName, targetName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_ITEM_SUCCESS_DESC)
                    .msgArgs(parentTaskName, targetName)
                    .build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_FAIL_LOG,
                    parentTaskName,
                    targetName,
                    e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_ITEM_FAIL_DESC, e, parentTaskName,
                    targetName, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(
                successCount, failCount, FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_RESULT);
    }
}
