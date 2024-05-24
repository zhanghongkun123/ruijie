package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.Iterator;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/18
 *
 * @author zqj
 */
public class ImportComputerBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private ComputerBusinessAPI computerBusinessAPI;

    @NotNull
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @NotNull
    private IacUserMgmtAPI cbbUserAPI;

    @NotNull
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;



    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public void setBatchTaskItemIterator(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        this.batchTaskItemIterator = batchTaskItemIterator;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public ComputerBusinessAPI getComputerBusinessAPI() {
        return computerBusinessAPI;
    }

    public void setComputerBusinessAPI(ComputerBusinessAPI computerBusinessAPI) {
        this.computerBusinessAPI = computerBusinessAPI;
    }

    public CbbTerminalGroupMgmtAPI getCbbTerminalGroupMgmtAPI() {
        return cbbTerminalGroupMgmtAPI;
    }

    public void setCbbTerminalGroupMgmtAPI(CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI) {
        this.cbbTerminalGroupMgmtAPI = cbbTerminalGroupMgmtAPI;
    }

    public IacUserMgmtAPI getCbbUserAPI() {
        return cbbUserAPI;
    }

    public void setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
    }

    public CbbThirdPartyDeskStrategyMgmtAPI getCbbThirdPartyDeskStrategyMgmtAPI() {
        return cbbThirdPartyDeskStrategyMgmtAPI;
    }

    public void setCbbThirdPartyDeskStrategyMgmtAPI(CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI) {
        this.cbbThirdPartyDeskStrategyMgmtAPI = cbbThirdPartyDeskStrategyMgmtAPI;
    }
}
