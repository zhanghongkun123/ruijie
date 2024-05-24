package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateStartDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.RunImageTemplateHandlerRequest;
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
 * Description: 编辑启动镜像任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年2月1日
 *
 * @author wjp
 */
public class RunImageTemplateHandler extends AbstractRunVmForEditImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunImageTemplateHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CmsUpgradeAPI cmsUpgradeAPI;

    private String imageName = "";

    public RunImageTemplateHandler(RunImageTemplateHandlerRequest request) {
        super(request.getBatchTaskItemIterator(), request.getCmsUpgradeAPI(), request.getCbbImageDriverMgmtAPI());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbImageTemplateMgmtAPI = request.getCbbImageTemplateMgmtAPI();
        this.cmsUpgradeAPI = request.getCmsUpgradeAPI();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        try {
            CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(taskItem.getItemID());
            imageName = cbbImageTemplateDetailDTO.getImageName();
            CbbImageTemplateStartDTO imageTemplateStartDTO = new CbbImageTemplateStartDTO();
            imageTemplateStartDTO.setImageId(taskItem.getItemID());
            imageTemplateStartDTO.setCdromList(wrapperCdromDTO(cbbImageTemplateDetailDTO));
            imageTemplateStartDTO.setNeedExactMatchVgpu(true);

            cbbImageTemplateMgmtAPI.startVm(imageTemplateStartDTO);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_SUCCESS_LOG, imageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_SUCCESS_LOG).msgArgs(imageName).build();
        } catch (BusinessException e) {
            LOGGER.error("启动镜像出错", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_FAIL_LOG, e, imageName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_FAIL_LOG, e, imageName, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_FAIL).msgArgs(new String[] {imageName}).build();
        }
    }
}
