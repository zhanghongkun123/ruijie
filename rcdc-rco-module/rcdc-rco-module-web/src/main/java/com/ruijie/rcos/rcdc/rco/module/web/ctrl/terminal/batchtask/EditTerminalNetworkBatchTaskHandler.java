package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.EditTerminalNetworkWebRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbSetIDVTerminalNetworkDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
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
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/16
 *
 * @author hry
 */
public class EditTerminalNetworkBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditTerminalNetworkBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private EditTerminalNetworkWebRequest editTerminalNetworkWebRequest;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private String terminalMac = StringUtils.EMPTY;

    public EditTerminalNetworkBatchTaskHandler(EditTerminalNetworkWebRequest editTerminalNetworkWebRequest,
                                               BatchTaskItem batchTaskItem,
                                               BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        this.editTerminalNetworkWebRequest = editTerminalNetworkWebRequest;
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "taskItem is not null");
        String terminalId = editTerminalNetworkWebRequest.getId();
        this.terminalMac = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            this.terminalMac = terminalBasicInfo.getUpperMacAddrOrTerminalId();
            CbbSetIDVTerminalNetworkDTO cbbReq = new CbbSetIDVTerminalNetworkDTO();
            cbbReq.setAutoDhcp(editTerminalNetworkWebRequest.getAutoDhcp());
            cbbReq.setIpAddr(editTerminalNetworkWebRequest.getIpAddr());
            cbbReq.setMask(editTerminalNetworkWebRequest.getMask());
            cbbReq.setGateway(editTerminalNetworkWebRequest.getGateway());
            cbbReq.setAutoDns(editTerminalNetworkWebRequest.getAutoDns());
            cbbReq.setDns(editTerminalNetworkWebRequest.getDns());
            cbbReq.setDnsBack(editTerminalNetworkWebRequest.getDnsBack());

            cbbTerminalOperatorAPI.setIDVTerminalNetwork(terminalId, cbbReq);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_SUC_LOG, this.terminalMac);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_SUC_LOG).msgArgs(new String[] {this.terminalMac}).build();
        } catch (BusinessException e) {
            LOGGER.error("编辑终端网络配置失败，终端id=" + terminalId, e);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_FAIL_LOG, this.terminalMac, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_FAIL_LOG, e, this.terminalMac, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_RESULT_SUC) //
                    .msgArgs(new String[] {this.terminalMac}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_RESULT_FAIL) //
                    .msgArgs(new String[] {this.terminalMac}).build();
        }
    }
}
