package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.batchtask;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateExportMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbDeleteExportFileReq;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbExportImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbExportState;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.ImageExportBusinessKey;
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
public class DeleteExportImageBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteExportImageBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CbbImageTemplateExportMgmtAPI cbbImageTemplateExportMgmtAPI;

    private StateMachineFactory stateMachineFactory;

    private String imageName = "";

    private boolean isBatch = true;

    public DeleteExportImageBatchTaskHandler(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI,
                                             CbbImageTemplateExportMgmtAPI cbbImageTemplateExportMgmtAPI,
                                             Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
        this.cbbImageTemplateExportMgmtAPI = cbbImageTemplateExportMgmtAPI;
        this.auditLogAPI = auditLogAPI;
    }

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "BatchTaskItem不能为null");
        UUID id = taskItem.getItemID();
        try {
            CbbExportImageTemplateDTO dto = cbbImageTemplateExportMgmtAPI.findExportFileById(id);
            if (dto == null) {
                auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_FAIL_BY_FILE_NOT_EXIST);
                throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_FAIL_BY_FILE_NOT_EXIST);
            }

            imageName = cbbImageTemplateMgmtAPI.getImageTemplateInfo(dto.getImageTemplateId()).getImageName();
            if (!CbbExportState.AVAILABLE.equals(dto.getExportState())) {
                auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_FAIL_LOG,
                        imageName);
                throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_FAIL_LOG, imageName);
            }
            UUID taskId = UUID.randomUUID();
            CbbDeleteExportFileReq req = new CbbDeleteExportFileReq();
            req.setId(dto.getId());
            req.setTaskId(taskId);
            // deleteExportFile接口将同时删除导出的镜像文件和导出记录
            cbbImageTemplateExportMgmtAPI.deleteExportFile(req);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();

            auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_SUCCESS_LOG,
                    imageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_SUCCESS_LOG).msgArgs(new String[] {imageName}).build();
        } catch (BusinessException e) {
            LOGGER.error("删除导出的镜像文件异常：" + imageName, e);
            if (StringUtils.isEmpty(imageName)) {
                auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_ITEM_FAIL_DESC, e, imageName,
                        e.getI18nMessage());
                throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_ITEM_FAIL_DESC, e, e.getI18nMessage());
            } else {
                auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_ITEM_FAIL_DESC_WITH_NAME, e, imageName,
                        e.getI18nMessage());
                throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_ITEM_FAIL_DESC_WITH_NAME, e, imageName,
                        e.getI18nMessage());
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount,
                    ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_RESULT);
        }

        if (failCount == 1) {
            if (StringUtils.isBlank(imageName)) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_TASK_FAIL)
                        .msgArgs(new String[] {})
                        .build();
            }
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_TASK_FAIL_WITH_NAME)
                    .msgArgs(new String[] {imageName})
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_TASK_SUCCESS)
                    .msgArgs(new String[] {imageName})
                    .build();
        }
    }
}
