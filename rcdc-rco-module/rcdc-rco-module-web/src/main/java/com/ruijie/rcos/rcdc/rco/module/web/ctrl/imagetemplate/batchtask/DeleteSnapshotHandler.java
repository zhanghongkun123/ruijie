package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.ImageRestorePointDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.ImageTemplateBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.DeleteSnapshotHandlerRequest;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 删除快照任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.10.11
 *
 * @author liangyifeng
 */
public class DeleteSnapshotHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSnapshotHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private String imageName = "";

    private UUID imageId;

    public DeleteSnapshotHandler(DeleteSnapshotHandlerRequest request, UUID imageId) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbImageTemplateMgmtAPI = request.getCbbImageTemplateMgmtAPI();
        this.imageId = imageId;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        ImageRestorePointDTO restorePointDTO = cbbImageTemplateMgmtAPI.getImageSnapshotById(taskItem.getItemID());

        try {
            imageName = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId).getImageName();
            cbbImageTemplateMgmtAPI.deleteImageSnapshot(taskItem.getItemID());
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_SUCCESS_LOG, imageName, restorePointDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_SUCCESS_LOG).msgArgs(imageName, restorePointDTO.getName())
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("删除镜像[" + imageId + "]快照[" + restorePointDTO.getName() + "]出错", e);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_FAIL_LOG, e, imageName, restorePointDTO.getName(),
                    e.getI18nMessage());
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_FAIL_LOG, e, imageName, restorePointDTO.getName(),
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_TASK_FAIL).msgArgs(new String[] {imageName}).build();
        }
    }
}
