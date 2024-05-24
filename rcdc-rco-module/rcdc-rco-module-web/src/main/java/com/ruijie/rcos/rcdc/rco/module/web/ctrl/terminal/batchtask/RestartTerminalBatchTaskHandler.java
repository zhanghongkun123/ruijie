package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
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
 * 
 * Description: 批量重启终端任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月18日
 * 
 * @author nt
 */
public class RestartTerminalBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestartTerminalBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalOperatorAPI terminalOperatorAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private Map<UUID, String> idMap;

    public RestartTerminalBatchTaskHandler(CbbTerminalOperatorAPI terminalOperatorAPI, Map<UUID, String> idMap,
            Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
        this.terminalOperatorAPI = terminalOperatorAPI;
    }

    public void setCbbTerminalBasicInfoAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "BatchTaskItem不能为null");
        String terminalId = idMap.get(taskItem.getItemID());
        String terminalIdentification = terminalId;
        try {
            CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            terminalOperatorAPI.restart(terminalId);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_RESTART_SUCCESS_LOG, terminalIdentification);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_RESTART_SUCCESS_LOG).msgArgs(new String[] {terminalIdentification}).build();
        } catch (Exception e) {
            LOGGER.error("重启终端失败：" + terminalIdentification, e);
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_RESTART_FAIL_LOG, terminalIdentification, ex.getI18nMessage());
                throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_RESTART_FAIL_LOG, e, terminalIdentification, ex.getI18nMessage());
            } else {
                throw new IllegalStateException("重启终端异常，terminalId为[" + terminalIdentification + "]", e);
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_RESTART_RESULT);
    }
}
