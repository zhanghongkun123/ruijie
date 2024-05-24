package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.imagedriver;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 
 * Description: 删除镜像驱动handler
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 * 
 * @author nt
 */
public class DeleteDriverIsoBatchHandler extends AbstractSingleTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    private String driverName = "";

    public DeleteDriverIsoBatchHandler(CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI, BatchTaskItem batchTaskItem,
                                       BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        Assert.notNull(cbbImageDriverMgmtAPI, "cbbImageDriverMgmtAPI is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");

        this.cbbImageDriverMgmtAPI = cbbImageDriverMgmtAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SUCCESS_RESULT)
                    .msgArgs(new String[] {driverName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_FAIL_RESULT)
                    .msgArgs(new String[] {driverName}).build();
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item is null");

        String localDriverName = item.getItemID().toString();
        try {
            CbbImageDriverDTO imageDriverDTO = cbbImageDriverMgmtAPI.findImageDriverInfo(item.getItemID());
            localDriverName = imageDriverDTO.getDriverName();
            cbbImageDriverMgmtAPI.deleteImageDriverFile(item.getItemID());
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_SUCCESS, localDriverName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_SUCCESS)
                    .msgArgs(new String[] {localDriverName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_FAIL, e, localDriverName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_FAIL, e, localDriverName,
                    e.getI18nMessage());
        } finally {
            driverName = localDriverName;
        }
    }

}
