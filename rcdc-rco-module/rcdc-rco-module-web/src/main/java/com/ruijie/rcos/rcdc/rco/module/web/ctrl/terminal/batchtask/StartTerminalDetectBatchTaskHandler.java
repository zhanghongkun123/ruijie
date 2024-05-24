package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalDetectAPI;
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
 * Description: 开始终端检测handler
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 * 
 * @author nt
 */
public class StartTerminalDetectBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartTerminalDetectBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalOperatorAPI operatorAPI;

    private CbbTerminalDetectAPI terminalDetectAPI;

    private static final String CHECKING_ERROR_KEY = "23260050";

    public StartTerminalDetectBatchTaskHandler(CbbTerminalOperatorAPI operatorAPI, CbbTerminalDetectAPI terminalDetectAPI,
            Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(operatorAPI, "operatorAPI can not be null");
        Assert.notNull(terminalDetectAPI, "terminalDetectAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");

        this.auditLogAPI = auditLogAPI;
        this.operatorAPI = operatorAPI;
        this.terminalDetectAPI = terminalDetectAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem不能为null");

        TerminalIdBatchTaskItem terminalIdBatchTaskItem = (TerminalIdBatchTaskItem) taskItem;
        String terminalIdentification = terminalIdBatchTaskItem.getTerminalId();

        try {
            CbbTerminalBasicInfoDTO response = operatorAPI.findBasicInfoByTerminalId(terminalIdBatchTaskItem.getTerminalId());
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            terminalDetectAPI.singleDetect(terminalIdBatchTaskItem.getTerminalId());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_START_DETECT_SUCCESS_LOG, terminalIdentification);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_START_DETECT_RESULT_SUCCESS).msgArgs(new String[] {terminalIdentification})
                    .build();
        } catch (BusinessException ex) {
            LOGGER.error("start terminal detect fail", ex);
            if (BusinessException.isSameBusinessException(ex, CHECKING_ERROR_KEY)) {
                // 此处是通过捕获KEY值的方式，此方式与CBB耦合严重，他们KEY修改时必须同步RCO
                LOGGER.info("检查到有记录，认为检测成功");
                auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_START_DETECT_SUCCESS_LOG, terminalIdentification);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(TerminalBusinessKey.RCDC_TERMINAL_START_DETECT_RESULT_SUCCESS).msgArgs(new String[] {terminalIdentification})
                        .build();
            }
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_START_DETECT_FAIL_LOG, terminalIdentification, ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_START_DETECT_RESULT_FAIL, ex, terminalIdentification, ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_DETECT_BATCH_TASK_RESULT);
    }
}
