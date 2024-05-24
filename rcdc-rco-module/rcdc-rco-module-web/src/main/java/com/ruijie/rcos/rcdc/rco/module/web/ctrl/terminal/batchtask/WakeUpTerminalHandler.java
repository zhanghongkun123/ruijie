package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalWakeUpAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 唤醒终端批处理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/9 22:58
 *
 * @author yxq
 */
public class WakeUpTerminalHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditFullSystemDiskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private TerminalWakeUpAPI terminalWakeUpAPI;

    private Map<UUID, String> idMap;


    public WakeUpTerminalHandler(Map<UUID, String> idMap, Iterator<DefaultBatchTaskItem> iterator) {
        super(iterator);
        this.idMap = idMap;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem must not be null");

        String terminalId = idMap.get(batchTaskItem.getItemID());
        String terminalIdentification = terminalId;

        try {
            CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            //获取终端操作LOG 用于打印日志
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            terminalWakeUpAPI.wakeUpTerminal(terminalId, Boolean.FALSE);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_SUCCESS, terminalIdentification);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_SUCCESS).msgArgs(terminalIdentification).build();
        } catch (BusinessException e) {
            LOGGER.error("唤醒终端[" + terminalIdentification + "]失败，失败原因：", e);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_FAIL, terminalIdentification, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_FAIL, e, terminalIdentification, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_RESULT);
    }


    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    public void setTerminalWakeUpAPI(TerminalWakeUpAPI terminalWakeUpAPI) {
        this.terminalWakeUpAPI = terminalWakeUpAPI;
    }
}