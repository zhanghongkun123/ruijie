package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalStartMode;
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

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/15
 *
 * @author xwx
 */
public class ConfigStartModeBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigStartModeBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    private CbbTerminalStartMode terminalStartMode;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private String startMode;

    public ConfigStartModeBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator,
                                           BaseAuditLogAPI auditLogAPI,
                                           UserTerminalMgmtAPI userTerminalMgmtAPI,
                                           CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    public void setTerminalStartMode(CbbTerminalStartMode terminalStartMode) {
        this.terminalStartMode = terminalStartMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "taskItem is not null");
        ConfigStartModeBatchTaskItem taskItem = (ConfigStartModeBatchTaskItem)item;
        CbbTerminalBasicInfoDTO basicInfoByTerminalId = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(taskItem.getTerminalId());

        String logTerminalId = basicInfoByTerminalId.getMacAddr().toUpperCase();
        try {
            LOGGER.info("发送设置开机模式命令，终端ID" + logTerminalId);
            userTerminalMgmtAPI.configTerminalStartMode(taskItem.getTerminalId(), terminalStartMode);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_SUCCESS,
                    logTerminalId, startMode);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_SUCCESS)
                    .msgArgs(new String[]{logTerminalId, startMode}).build();
        } catch (BusinessException e) {
            LOGGER.error("发送设置开机模式命令失败，终端：" + taskItem.getTerminalId(), e);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_FAIL,
                    logTerminalId, startMode, e.getI18nMessage());

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_FAIL)
                    .msgArgs(new String[]{logTerminalId, startMode, e.getI18nMessage()}).build();
        }
    }


    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_ALL_SUCCESS)
                    .msgArgs(new String[]{startMode}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else if (sucCount == 0) {
            return DefaultBatchTaskFinishResult.builder().msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_PART_SUCCESS)
                    .msgArgs(new String[]{startMode, Integer.toString(sucCount), Integer.toString(failCount)})
                    .batchTaskStatus(BatchTaskStatus.FAILURE).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_PART_SUCCESS)
                    .msgArgs(new String[]{startMode, Integer.toString(sucCount), Integer.toString(failCount)})
                    .batchTaskStatus(BatchTaskStatus.PARTIAL_SUCCESS).build();
        }
    }

}
