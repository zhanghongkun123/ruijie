package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbSetIDVTerminalNetworkDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/16
 *
 * @author hry
 */
public class EditTerminalIpBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditTerminalIpBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    public EditTerminalIpBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "BatchTaskItem不能为null");
        EditTerminalIpBatchTaskItem taskItem = (EditTerminalIpBatchTaskItem) batchTaskItem;
        String terminalId = taskItem.getTerminalId();
        CbbTerminalBasicInfoDTO terminalBasicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        String terminalMac = terminalId;
        try {
            terminalMac = terminalBasicInfo.getUpperMacAddrOrTerminalId();
            // 校验终端类型IDV TCI(VOI)
            CbbTerminalPlatformEnums terminalType = terminalBasicInfo.getTerminalPlatform();
            if (terminalType == CbbTerminalPlatformEnums.IDV || terminalType == CbbTerminalPlatformEnums.VOI) {
                CbbSetIDVTerminalNetworkDTO cbbReq = new CbbSetIDVTerminalNetworkDTO();
                cbbReq.setAutoDhcp(taskItem.getAutoDhcp());
                cbbReq.setMask(taskItem.getMask());
                cbbReq.setGateway(taskItem.getGateway());
                // cbbReq剩余参数理论上为空
                cbbTerminalOperatorAPI.setIDVTerminalNetwork(terminalId, cbbReq);
            } else {
                throw new BusinessException("编辑终端IP不支持终端类型类型:{" + terminalType.name() + "}");
            }
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_SUC_LOG, terminalMac);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_SUC_LOG)
                    .msgArgs(new String[]{terminalMac}).build();
        } catch (Exception e) {
            LOGGER.error("编辑终端IP失败，终端id=" + terminalId, e);
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_FAIL_LOG, terminalMac, ex.getI18nMessage());
                throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_FAIL_LOG, e, terminalMac, ex.getI18nMessage());
            } else {
                throw new IllegalStateException("发送编辑终端IP命令异常，终端id=" + terminalId, e);
            }
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_BATCH_RESULT);
    }
}
