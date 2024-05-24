package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionFileManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeFileDTO;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 删除分发文件批处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/16 10:49
 *
 * @author zhangyichi
 */
public class DistributeFileDeleteTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private FileDistributionFileManageAPI fileDistributionFileManageAPI;

    public DistributeFileDeleteTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setFileDistributionFileManageAPI(FileDistributionFileManageAPI fileDistributionFileManageAPI) {
        this.fileDistributionFileManageAPI = fileDistributionFileManageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        String logName = batchTaskItem.getItemID().toString();
        UUID fileId = batchTaskItem.getItemID();
        try {
            DistributeFileDTO fileDTO = fileDistributionFileManageAPI.findById(fileId);
            if (fileDTO != null) {
                logName = fileDTO.getFileName();
                fileDistributionFileManageAPI.deleteFile(fileId, Constants.SAMBA_APP_PATH);
            }
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_ITEM_SUCCESS_DESC, logName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[] {logName})
                    .build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_ITEM_FAIL_DESC,logName,e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_ITEM_FAIL_DESC, e, logName, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(
                successCount, failCount, FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_BATCH_DELETE_RESULT);
    }
}
