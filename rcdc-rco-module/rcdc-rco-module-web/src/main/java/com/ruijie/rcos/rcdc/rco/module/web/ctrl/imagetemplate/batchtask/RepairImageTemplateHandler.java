package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import java.util.Iterator;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbRepairImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
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
 * Description: 镜像修复功能处理器
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年3月29日
 *
 * @author zjy
 */
public class RepairImageTemplateHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepairImageTemplateHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private String imageName = "";

    private String errorMessage;

    public RepairImageTemplateHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, CbbImageTemplateMgmtAPI imageTemplateMgmtAPI,
                                      BaseAuditLogAPI auditLogAPI, String imageName) {
        super(batchTaskItemIterator);
        this.imageName = imageName;
        this.auditLogAPI = auditLogAPI;
        this.cbbImageTemplateMgmtAPI = imageTemplateMgmtAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        try {
            CbbRepairImageTemplateDTO repairImageTemplateDTO = new CbbRepairImageTemplateDTO();
            repairImageTemplateDTO.setImageId(taskItem.getItemID());
            cbbImageTemplateMgmtAPI.repairImageTemplate(repairImageTemplateDTO);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_IMAGE_REPAIR_SUCCESS_LOG, imageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_IMAGE_REPAIR_TASK_SUCCESS).msgArgs(imageName).build();
        } catch (BusinessException e) {
            LOGGER.error("修复镜像出错，镜像id为:[{}]", taskItem.getItemID(), e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_IMAGE_REPAIR_FAIL_LOG, e, imageName, e.getI18nMessage());
            this.errorMessage = e.getI18nMessage();
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_REPAIR_TASK_FAIL, e, imageName, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_IMAGE_REPAIR_TASK_SUCCESS).msgArgs(new String[]{imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(BusinessKey.RCDC_RCO_IMAGE_REPAIR_TASK_FAIL).msgArgs(new String[]{imageName, errorMessage}).build();
        }
    }
}
