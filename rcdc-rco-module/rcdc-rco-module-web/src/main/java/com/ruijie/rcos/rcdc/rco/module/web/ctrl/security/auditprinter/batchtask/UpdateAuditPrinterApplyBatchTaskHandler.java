package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditprinter.batchtask;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditprinter.AuditPrinterBusinessKey;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 创建桌面快照任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class UpdateAuditPrinterApplyBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAuditPrinterApplyBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    private AuditApplyAuditLogDTO auditApplyAuditLogDTO;


    public UpdateAuditPrinterApplyBatchTaskHandler(AuditApplyMgmtAPI auditApplyMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                   BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(auditApplyMgmtAPI, "the auditFileMgmtAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.auditApplyMgmtAPI = auditApplyMgmtAPI;
        this.auditLogAPI = auditLogAPI;


    }

    public void setAuditApplyAuditLogDTO(AuditApplyAuditLogDTO auditApplyAuditLogDTO) {
        this.auditApplyAuditLogDTO = auditApplyAuditLogDTO;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        UUID applyId = taskItem.getItemID();
        try {
            auditApplyAuditLogDTO.setApplyId(applyId);
            String applySerialNumber = auditApplyMgmtAPI.handleAuditApplyByAuditor(auditApplyAuditLogDTO);

            auditLogAPI.recordLog(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_AUDITOR_UPDATE_OPERATE_SUCCESS_LOG, applySerialNumber,
                    auditApplyAuditLogDTO.getAuditorState().getMessage());

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_UPDATE_SUCCESS_LOG)
                    .msgArgs(new String[] {applySerialNumber, auditApplyAuditLogDTO.getAuditorState().getMessage()}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_AUDITOR_UPDATE_OPERATE_FAILURE_LOG, String.valueOf(applyId),
                    auditApplyAuditLogDTO.getAuditorState().getMessage(), e.getI18nMessage());
            throw new BusinessException(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_UPDATE_FAILURE_LOG, e, e.getI18nMessage());
        }

    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_BATCH_UPDATE_SUCCESS_RESULT);

    }
}
