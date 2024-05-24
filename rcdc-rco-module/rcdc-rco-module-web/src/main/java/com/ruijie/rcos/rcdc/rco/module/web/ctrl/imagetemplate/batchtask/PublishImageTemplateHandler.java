package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbPublishImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageSyncMode;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.PublishImageTemplateHandlerRequest;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 发布镜像任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年2月1日
 *
 * @author wjp
 */
public class PublishImageTemplateHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishImageTemplateHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CmsUpgradeAPI cmsUpgradeAPI;

    private String imageName = "";

    /**
     * 是否需要强制发布云桌面
     */
    private final Boolean enableForcePublish;

    private String snapshotName;

    private String remark;

    private String versionImageName;

    private String desc;

    private String errorMessage;


    /**
     * 同步模式，立即 or 按需
     */
    private ImageSyncMode imageSyncMode;

    /**
     * 同步目标存储Id集合
     */
    private List<UUID> targetStorageIdList;

    public PublishImageTemplateHandler(PublishImageTemplateHandlerRequest request,
                                       Boolean enableForcePublish, String snapshotName,
                                       String remark) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbImageTemplateMgmtAPI = request.getCbbImageTemplateMgmtAPI();
        this.cmsUpgradeAPI = request.getCmsUpgradeAPI();
        this.enableForcePublish = enableForcePublish;
        this.snapshotName = snapshotName;
        this.versionImageName = request.getVersionImageName();
        this.remark = remark;
        this.desc = request.getDesc();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        try {
            imageName = cbbImageTemplateMgmtAPI.getImageTemplateDetail(taskItem.getItemID()).getImageName();
            CbbPublishImageTemplateDTO request = new CbbPublishImageTemplateDTO(taskItem.getItemID(), StringUtils.EMPTY);
            request.setEnableForcePublish(enableForcePublish);
            request.setSnapshotName(snapshotName);
            request.setRemark(remark);
            request.setBatchTaskItem(taskItem);
            request.setVersionImageName(versionImageName);
            request.setNote(desc);
            request.setImageSyncMode(imageSyncMode);
            request.setTargetStorageIdList(targetStorageIdList);

            cbbImageTemplateMgmtAPI.publishImageTemplate(request);
            cmsUpgradeAPI.setCmIsoRecord(taskItem.getItemID());
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_SUCCESS_LOG, imageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_ITEM_SUCCESS_DESC).msgArgs(imageName).build();
        } catch (BusinessException e) {
            LOGGER.error("发布镜像出错", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_FAIL_LOG, e, imageName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_ITEM_FAIL_DESC, e, imageName, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_TASK_FAIL).msgArgs(new String[] {imageName}).build();
        }
    }

    public ImageSyncMode getImageSyncMode() {
        return imageSyncMode;
    }

    public void setImageSyncMode(ImageSyncMode imageSyncMode) {
        this.imageSyncMode = imageSyncMode;
    }

    public List<UUID> getTargetStorageIdList() {
        return targetStorageIdList;
    }

    public void setTargetStorageIdList(List<UUID> targetStorageIdList) {
        this.targetStorageIdList = targetStorageIdList;
    }
}
