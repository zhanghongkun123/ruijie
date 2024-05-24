package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VisitorSettingDTO;
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
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/5
 *
 * @author nt
 */
public class EditTerminalVisitorSettingBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestartTerminalBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private Map<UUID, String> idMap;

    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    private CbbTerminalOperatorAPI terminalOperatorAPI;

    private Boolean enableAutoLogin;

    private Boolean enableVisitorLogin;

    public EditTerminalVisitorSettingBatchTaskHandler(UserTerminalMgmtAPI userTerminalMgmtAPI, BaseAuditLogAPI auditLogAPI,
                                                      Map<UUID, String> idMap, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        Assert.notNull(userTerminalMgmtAPI, "userTerminalMgmtAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        Assert.notNull(idMap, "idMap can not be null");

        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
    }

    /**
     * 设置修改参数
     *
     * @param enableVisitorLogin 是否开启访客登录
     * @param enableAutoLogin    是否自动进入云桌面
     */
    public void setEditVisitorSettings(Boolean enableVisitorLogin, Boolean enableAutoLogin) {
        Assert.notNull(enableVisitorLogin, "enableVisitorLogin can not be null");
        Assert.notNull(enableAutoLogin, "enableAutoLogin can not be null");

        this.enableVisitorLogin = enableVisitorLogin;
        this.enableAutoLogin = enableAutoLogin;
    }

    /**
     * 设置basicInfoAPI
     *
     * @param terminalOperatorAPI terminalOperatorAPI
     */
    public void setBasicInfoAPI(CbbTerminalOperatorAPI terminalOperatorAPI) {
        Assert.notNull(terminalOperatorAPI, "terminalOperatorAPI can not be null");
        this.terminalOperatorAPI = terminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "BatchTaskItem不能为null");

        String terminalId = idMap.get(batchTaskItem.getItemID());
        String terminalIdentification = terminalId;
        try {
            CbbTerminalBasicInfoDTO response = terminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            VisitorSettingDTO visitorSettingRequest = new VisitorSettingDTO(enableAutoLogin, enableVisitorLogin, terminalId);
            userTerminalMgmtAPI.updateVisitorSetting(visitorSettingRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_VISITOR_SETTING_SUCCESS_LOG, terminalIdentification);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_EDIT_VISITOR_SETTING_SUCCESS_LOG).msgArgs(new String[]{terminalIdentification})
                    .build();
        } catch (BusinessException ex) {
            LOGGER.error("发送编辑IDV终端访客登录设置消息异常", ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_VISITOR_SETTING_FAIL_LOG, terminalIdentification, ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_EDIT_VISITOR_SETTING_FAIL_LOG, ex, terminalIdentification,
                    ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_EDIT_VISITOR_SETTING_RESULT);
    }
}
