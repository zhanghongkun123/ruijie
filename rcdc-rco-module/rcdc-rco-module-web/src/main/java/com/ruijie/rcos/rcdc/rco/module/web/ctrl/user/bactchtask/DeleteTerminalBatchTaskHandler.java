package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalManagePwdAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 批量删除终端处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月14日
 *
 * @author nt
 */
public class DeleteTerminalBatchTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    private CbbTerminalOperatorAPI terminalOperatorAPI;
    
    private Map<UUID, String> idMap;

    private TerminalManagePwdAPI terminalManagePwdAPI;

    public DeleteTerminalBatchTaskHandler(UserTerminalMgmtAPI userTerminalMgmtAPI, Map<UUID, String> idMap,
                                          Iterator<? extends BatchTaskItem> iterator, //
                                          BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(userTerminalMgmtAPI, "the userTerminalMgmtAPI is null");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null");
        Assert.notNull(idMap, "idMap is null");
        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
    }

    public void setCbbTerminalBasicInfoAPI(CbbTerminalOperatorAPI terminalOperatorAPI) {
        this.terminalOperatorAPI = terminalOperatorAPI;
    }

    public void setTerminalManagePwdAPI(TerminalManagePwdAPI terminalManagePwdAPI) {
        this.terminalManagePwdAPI = terminalManagePwdAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        return deleteAndAddOptLog(taskItem.getItemID());

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_TERMINAL_BATCH_DELETE_TERMINAL_RESULT);
    }


    private DefaultBatchTaskItemResult deleteAndAddOptLog(UUID id) throws BusinessException {
        
        String terminalId = idMap.get(id);
        String terminalIdentification = terminalId;

        try {
            CbbTerminalBasicInfoDTO response = terminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            UUID realTerminalId = userTerminalMgmtAPI.getTerminalById(terminalId).getRealTerminalId();
            terminalManagePwdAPI.deleteAuthenticationByTerminalId(realTerminalId);
            userTerminalMgmtAPI.delete(terminalId);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_DELETE_TERMINAL_SUCCESS_LOG, terminalIdentification);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_TERMINAL_DELETE_TERMINAL_RESULT_SUCCESS)
                    .msgArgs(new String[]{terminalIdentification})
                    .build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_DELETE_TERMINAL_FAIL_LOG, terminalIdentification, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_TERMINAL_DELETE_TERMINAL_RESULT_FAIL, e, terminalIdentification, e.getI18nMessage());
        }
    }


}
