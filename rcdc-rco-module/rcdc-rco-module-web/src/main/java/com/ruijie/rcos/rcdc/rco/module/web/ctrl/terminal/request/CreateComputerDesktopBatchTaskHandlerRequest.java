package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolComputerMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolThirdPartyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/18
 *
 * @author zqj
 */
public class CreateComputerDesktopBatchTaskHandlerRequest {


    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @NotNull
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @NotNull
    private DesktopPoolBasicDTO desktopPoolDTO;

    @NotNull
    private DesktopPoolComputerMgmtAPI desktopPoolComputerMgmtAPI;

    @NotNull
    private DesktopPoolThirdPartyMgmtAPI desktopPoolThirdPartyMgmtAPI;

    private Boolean hasSecondAdd;

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public DesktopPoolMgmtAPI getDesktopPoolMgmtAPI() {
        return desktopPoolMgmtAPI;
    }

    public void setDesktopPoolMgmtAPI(DesktopPoolMgmtAPI desktopPoolMgmtAPI) {
        this.desktopPoolMgmtAPI = desktopPoolMgmtAPI;
    }

    public CbbDesktopPoolMgmtAPI getCbbDesktopPoolMgmtAPI() {
        return cbbDesktopPoolMgmtAPI;
    }

    public void setCbbDesktopPoolMgmtAPI(CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI) {
        this.cbbDesktopPoolMgmtAPI = cbbDesktopPoolMgmtAPI;
    }


    public DesktopPoolBasicDTO getDesktopPoolDTO() {
        return desktopPoolDTO;
    }

    public void setDesktopPoolDTO(DesktopPoolBasicDTO desktopPoolDTO) {
        this.desktopPoolDTO = desktopPoolDTO;
    }

    public Boolean getHasSecondAdd() {
        return hasSecondAdd;
    }

    public void setHasSecondAdd(Boolean hasSecondAdd) {
        this.hasSecondAdd = hasSecondAdd;
    }

    public DesktopPoolComputerMgmtAPI getDesktopPoolComputerMgmtAPI() {
        return desktopPoolComputerMgmtAPI;
    }

    public void setDesktopPoolComputerMgmtAPI(DesktopPoolComputerMgmtAPI desktopPoolComputerMgmtAPI) {
        this.desktopPoolComputerMgmtAPI = desktopPoolComputerMgmtAPI;
    }

    public DesktopPoolThirdPartyMgmtAPI getDesktopPoolThirdPartyMgmtAPI() {
        return desktopPoolThirdPartyMgmtAPI;
    }

    public void setDesktopPoolThirdPartyMgmtAPI(DesktopPoolThirdPartyMgmtAPI desktopPoolThirdPartyMgmtAPI) {
        this.desktopPoolThirdPartyMgmtAPI = desktopPoolThirdPartyMgmtAPI;
    }
}
