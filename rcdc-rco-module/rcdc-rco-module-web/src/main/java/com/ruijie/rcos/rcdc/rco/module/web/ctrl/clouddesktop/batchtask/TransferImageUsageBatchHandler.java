package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbTransferImageTemplateUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.TransferImageUsageWebRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 切换镜像使用类型（应用镜像、桌面镜像互相切换）
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024-01-08
 *
 * @author liuwc
 */
public class TransferImageUsageBatchHandler extends AbstractSingleTaskHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TransferImageUsageBatchHandler.class);

    private String imageName = "";

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private TransferImageUsageWebRequest webRequest;

    public TransferImageUsageBatchHandler(BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI
            , CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI, TransferImageUsageWebRequest webRequest) {
        super(batchTaskItem);
        this.auditLogAPI = auditLogAPI;
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
        this.webRequest = webRequest;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        try {
            CbbTransferImageTemplateUsageDTO cbbTransferImageTemplateUsageDTO = new CbbTransferImageTemplateUsageDTO();
            cbbTransferImageTemplateUsageDTO.setImageTemplateId(webRequest.getId());
            cbbTransferImageTemplateUsageDTO.setToImageUsage(webRequest.getToImageUsage());

            cbbImageTemplateMgmtAPI.transferImageTemplateUsage(cbbTransferImageTemplateUsageDTO);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_SUCCESS).msgArgs(new String[] {imageName}).build();
        } catch (BusinessException e) {
            LOGGER.error("切换镜像用途出错", e);
            String errorMsg = e.getI18nMessage();
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_FAIL_LOG,
                    webRequest.getImageName(), e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_FAIL).msgArgs(new String[] {imageName, errorMsg}).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_TASK_FAIL).msgArgs(new String[] {imageName}).build();
        }    }
}
