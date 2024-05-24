package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import java.util.UUID;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbRestoreImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.RestoreSnapshotHandlerRequest;
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
 * Description: 还原快照任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.10.11
 *
 * @author liangyifeng
 */
public class RestoreSnapshotHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestoreSnapshotHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private String imageName = "";

    private String snapshotName = "";

    /**
     * 是否需要强制发布云桌面
     */
    private Boolean enableForceRestore;

    private UUID snapshotId;

    public RestoreSnapshotHandler(RestoreSnapshotHandlerRequest request, Boolean enableForceRestore, UUID snapshotId, String snapshotName) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbImageTemplateMgmtAPI = request.getCbbImageTemplateMgmtAPI();
        this.enableForceRestore = enableForceRestore;
        this.snapshotId = snapshotId;
        this.snapshotName = snapshotName;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        try {
            imageName = cbbImageTemplateMgmtAPI.getImageTemplateDetail(taskItem.getItemID()).getImageName();
            CbbRestoreImageTemplateDTO restoreImageTemplateDTO = new CbbRestoreImageTemplateDTO();
            restoreImageTemplateDTO.setImageId(taskItem.getItemID());
            restoreImageTemplateDTO.setRestorePointId(snapshotId);
            restoreImageTemplateDTO.setEnableForceRestore(enableForceRestore);
            cbbImageTemplateMgmtAPI.restoreImageTemplate(restoreImageTemplateDTO);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_SUCCESS_LOG, imageName, snapshotName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_ITEM_SUCCESS_DESC).msgArgs(imageName, snapshotName).build();
        } catch (BusinessException e) {
            LOGGER.error("恢复镜像[" + imageName + "]快照" + snapshotName + "出错:", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_FAIL_LOG, e, imageName, snapshotName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_ITEM_FAIL_DESC, e, imageName, snapshotName,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_TASK_SUCCESS).msgArgs(new String[] {imageName, snapshotName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_TASK_FAIL).msgArgs(new String[] {imageName, snapshotName}).build();
        }
    }
}
