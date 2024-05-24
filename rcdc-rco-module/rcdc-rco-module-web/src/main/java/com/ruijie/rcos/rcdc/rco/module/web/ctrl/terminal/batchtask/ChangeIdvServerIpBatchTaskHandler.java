package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
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
 * Description: 批量修改IDV终端服务器地址处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-03-15
 *
 * @author shiruifeng
 */
public class ChangeIdvServerIpBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeIdvServerIpBatchTaskHandler.class);

    private final Map<UUID, String> idMap;

    private final UserTerminalMgmtAPI userTerminalMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private final String serverIp;

    public ChangeIdvServerIpBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator,
                                             Map<UUID, String> idMap, UserTerminalMgmtAPI userTerminalMgmtAPI,
                                             BaseAuditLogAPI auditLogAPI, String serverIp) {
        super(iterator);
        Assert.notNull(idMap, "idMap can not be null");
        Assert.notNull(userTerminalMgmtAPI, "userTerminalMgmtAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");

        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
        this.idMap = idMap;
        this.auditLogAPI = auditLogAPI;
        this.serverIp = serverIp;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        String terminalId = idMap.get(batchTaskItem.getItemID());
        try {
            userTerminalMgmtAPI.changeTerminalServerIp(terminalId, serverIp);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_SERVER_IP_SUCCESS, terminalId);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_SERVER_IP_SUCCESS).msgArgs(new String[] {terminalId}).build();
        } catch (BusinessException e) {
            LOGGER.error("发送终端修改云服务器地址给终端[{}]失败，失败原因：[{}]", terminalId, e.getI18nMessage());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_SERVER_IP_FAIL, terminalId, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_SERVER_IP_FAIL, e, terminalId, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_CHANGE_SERVER_IP_RESULT);
    }
}
