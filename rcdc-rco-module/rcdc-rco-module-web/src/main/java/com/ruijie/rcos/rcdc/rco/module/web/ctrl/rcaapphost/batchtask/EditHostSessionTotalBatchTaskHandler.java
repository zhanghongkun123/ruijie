package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaTrusteeshipHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaTrusteeshipHostDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 编辑应用主机最大会话数Handler
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author liuwc
 */
public class EditHostSessionTotalBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditHostSessionTotalBatchTaskHandler.class);

    private RcaHostAPI rcaHostAPI;

    private BaseAuditLogAPI auditLogAPI;

    private Integer sessionTotal;

    public EditHostSessionTotalBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
                                                RcaHostAPI rcaHostAPI) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.rcaHostAPI = rcaHostAPI;
    }

    public void setSessionTotal(Integer sessionTotal) {
        this.sessionTotal = sessionTotal;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID hostId = batchTaskItem.getItemID();
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
        try {
            rcaHostAPI.editSessionTotal(hostId, sessionTotal);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_SUCCESS, rcaHostDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_SUCCESS)
                    .msgArgs(new String[] {rcaHostDTO.getName()}).build();
        } catch (BusinessException e) {
            LOGGER.error("应用主机[{}]编辑最大会话数失败，异常原因：", hostId, e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_FAIL, rcaHostDTO.getName(), e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_FAIL)
                    .msgArgs(new String[] {rcaHostDTO.getName(), e.getI18nMessage()}).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_TASK_RESULT);
    }
}
