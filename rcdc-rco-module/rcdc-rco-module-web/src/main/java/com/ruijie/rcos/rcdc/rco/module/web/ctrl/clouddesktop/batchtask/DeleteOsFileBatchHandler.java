package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetOsFileResultDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 
 * Description: 删除安装包批量handler
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月17日
 * 
 * @author Ghang
 */
public class DeleteOsFileBatchHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private CbbOsFileMgmtAPI osFileMgmtAPI;

    public DeleteOsFileBatchHandler(CbbOsFileMgmtAPI osFileMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                    BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(osFileMgmtAPI, "osFileMgmtAPI is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");

        this.osFileMgmtAPI = osFileMgmtAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_BATCH_DELETE_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item is null");
        String logName = item.getItemID().toString();
        try {
            //查询删除镜像的文件名
            CbbGetOsFileResultDTO response = osFileMgmtAPI.getOsFile(item.getItemID());
            logName = response.getImageFileName();

            //删除镜像文件
            osFileMgmtAPI.deleteOsFile(item.getItemID());
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_SUCCESS_DESC, logName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[] {logName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_FAIL_DESC, e, logName,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_FAIL_DESC, e, logName, e.getI18nMessage());
        }
    }

}
