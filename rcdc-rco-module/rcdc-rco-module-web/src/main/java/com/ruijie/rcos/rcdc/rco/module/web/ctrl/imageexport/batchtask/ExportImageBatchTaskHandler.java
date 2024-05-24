package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.batchtask;

import java.util.List;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateExportMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbExportImageTemplateReq;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.dto.ViewImageExportDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.ImageExportBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request.ExportImageBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/22 13:57
 *
 * @author ketb
 */
public class ExportImageBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportImageBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CbbImageTemplateExportMgmtAPI cbbImageTemplateExportMgmtAPI;

    private ImageExportAPI imageExportAPI;

    private StateMachineFactory stateMachineFactory;

    private String imageName = "";

    private boolean isBatch = true;

    private static final String EXPORT_IMAGE_FILE_SUFFIX = ".qcow2";

    private UUID[] imageDiskIdArr;

    private Boolean reExport;

    private boolean isTemplateType = true;

    public ExportImageBatchTaskHandler(ExportImageBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.cbbImageTemplateMgmtAPI = request.getCbbImageTemplateMgmtAPI();
        this.cbbImageTemplateExportMgmtAPI = request.getCbbImageTemplateExportMgmtAPI();
        this.imageExportAPI = request.getImageExportAPI();
        this.auditLogAPI = request.getAuditLogAPI();
        this.stateMachineFactory = request.getStateMachineFactory();
        this.imageDiskIdArr = request.getImageDiskIdArr();
        this.reExport = request.getReExport();
    }

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "BatchTaskItem不能为null");
        UUID id = taskItem.getItemID();
        imageName = String.valueOf(id);

        try {
            CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(id);
            if (imageTemplateInfoDTO == null || imageTemplateInfoDTO.getId() == null) {
                auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_FAIL_BY_IMAGE_NOT_EXIST);
                throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_FAIL_BY_IMAGE_NOT_EXIST);
            }
            isTemplateType = ImageRoleType.TEMPLATE.equals(imageTemplateInfoDTO.getImageRoleType());
            imageName = imageTemplateInfoDTO.getImageName();
            if (!ImageTemplateState.isInSteadyState(imageTemplateInfoDTO.getImageState())) {
                String description = ImageRoleType.TEMPLATE.equals(imageTemplateInfoDTO.getImageRoleType()) ?
                        ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_FAIL_BY_IMAGE_NOT_STEADY :
                        ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORT_VERSION_FAIL_BY_IMAGE_NOT_STEADY;
                auditLogAPI.recordLog(description, new String[]{imageTemplateInfoDTO.getImageName()});
                throw new BusinessException(description, imageName);
            }
            // 重新导出，直接根据前端传的镜像ID删除旧记录
            if (reExport) {
                for (UUID exportImageDiskId : imageDiskIdArr) {
                    List<ViewImageExportDTO> exportImageList = imageExportAPI.getExportImageByImageDiskId(exportImageDiskId);
                    if (exportImageList != null && exportImageList.size() > 0) {
                        imageExportAPI.deleteExportImage(exportImageList.get(0).getId());
                    }
                }
                //镜像导出，需要根据镜像ID去删除旧记录，因为可能先开启数据分区，后面又关闭数据分区，前端获取不到旧数据分区的ID
            } else {
                List<ViewImageExportDTO> exportImageList = imageExportAPI.getExportImageByTemplateId(imageTemplateInfoDTO.getId());
                if (exportImageList != null && !exportImageList.isEmpty()) {
                    for (ViewImageExportDTO viewImageExportDTO : exportImageList) {
                        imageExportAPI.deleteExportImage(viewImageExportDTO.getId());
                    }
                }
            }

            UUID taskId = UUID.randomUUID();
            exportImageTemplate(imageTemplateInfoDTO, taskId);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            String description = ImageRoleType.TEMPLATE.equals(imageTemplateInfoDTO.getImageRoleType()) ?
                    ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_SUCCESS_LOG :
                    ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORT_VERSION_SUCCESS_LOG;
            auditLogAPI.recordLog(description, imageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(description).msgArgs(new String[]{imageName}).build();
        } catch (BusinessException e) {
            LOGGER.error("导出镜像[{}]文件异常：", imageName, e);
            String description = isTemplateType ? ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_ITEM_FAIL_DESC_WITH_NAME :
                    ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORT_VERSION_ITEM_FAIL_DESC_WITH_NAME;
            auditLogAPI.recordLog(description, e, imageName, e.getI18nMessage());
            throw new BusinessException(description, e, imageName, e.getI18nMessage());
        }
    }

    private void exportImageTemplate(CbbGetImageTemplateInfoDTO imageTemplateInfoDTO, UUID taskId) throws BusinessException {
        CbbExportImageTemplateReq req = new CbbExportImageTemplateReq();

        req.setExportFileName(imageTemplateInfoDTO.getImageName() + DateUtil.getTimeMark() + EXPORT_IMAGE_FILE_SUFFIX);
        req.setTaskId(taskId);
        req.setExportFileDesc(LocaleI18nResolver.resolve(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DESCRIPTION,
                new String[]{imageTemplateInfoDTO.getCbbImageType().toString(), imageName}));

        req.setCpuArch(imageTemplateInfoDTO.getCpuArch());
        req.setImageDiskIdArr(imageDiskIdArr);
        cbbImageTemplateExportMgmtAPI.exportImageTemplate(req);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_RESULT);
        }

        if (failCount == 1) {
            String description = isTemplateType ? ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_TASK_FAIL :
                    ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORT_VERSION_TASK_FAIL;
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(description).msgArgs(new String[]{imageName}).build();
        } else {
            String description = isTemplateType ? ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_TASK_SUCCESS :
                    ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORT_VERSION_TASK_SUCCESS;
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(description).msgArgs(new String[]{imageName}).build();
        }
    }
}
