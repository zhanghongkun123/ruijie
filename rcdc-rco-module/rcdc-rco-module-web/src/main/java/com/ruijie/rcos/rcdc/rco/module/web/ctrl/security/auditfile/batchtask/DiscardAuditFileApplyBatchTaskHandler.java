package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 废弃申请单批处理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/19
 *
 * @author TD
 */
public class DiscardAuditFileApplyBatchTaskHandler extends AbstractBatchTaskHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscardAuditFileApplyBatchTaskHandler.class);

    private final BaseAuditLogAPI auditLogAPI;

    private final AuditApplyMgmtAPI auditApplyMgmtAPI;

    private final String failReason;
    
    private final String applySerialNumber;

    public DiscardAuditFileApplyBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, String failReason, String applySerialNumber) {
        super(iterator);
        this.auditApplyMgmtAPI = SpringBeanHelper.getBean(AuditApplyMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.failReason = failReason;
        this.applySerialNumber = applySerialNumber;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        UUID applyId = taskItem.getItemID();
        String applySerialNumber = null;
        try {
            applySerialNumber = auditApplyMgmtAPI.findAuditApplyDetailById(applyId).getApplySerialNumber();

            auditApplyMgmtAPI.discardAuditApply(applyId, failReason);

            auditLogAPI.recordLog(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_SUCCESS_LOG, applySerialNumber);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_SUCCESS_LOG).msgArgs(applySerialNumber).build();
        } catch (BusinessException e) {
            LOGGER.error("申请单[{}]废弃失败：", applyId, e);
            String applyKey = StringUtils.hasText(applySerialNumber) ? applySerialNumber : String.valueOf(applyId);
            auditLogAPI.recordLog(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_FAILURE_LOG, applyKey, e.getI18nMessage());
            throw new BusinessException(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_FAILURE_LOG, e, applyKey, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (Objects.nonNull(applySerialNumber)) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_ITEM_SUCCESS_DESC)
                        .msgArgs(new String[] {applySerialNumber}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_ITEM_FAILURE_DESC)
                        .msgArgs(new String[] {applySerialNumber}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, AuditFileBusinessKey.RCDC_AUDIT_FILE_BATCH_DISCARD_SUCCESS_RESULT);
        }
    }
}
