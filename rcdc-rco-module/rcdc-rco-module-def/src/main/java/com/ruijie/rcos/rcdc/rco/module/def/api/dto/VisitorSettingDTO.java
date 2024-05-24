package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/30 16:48
 *
 * @author conghaifeng
 */
public class VisitorSettingDTO implements Request {

    @NotNull
    Boolean enableVisitorLogin;

    @NotNull
    Boolean enableAutoLogin;

    @NotBlank
    String terminalId;

    protected VisitorSettingDTO() {

    }

    public VisitorSettingDTO(Boolean enableVisitorLogin, Boolean enableAutoLogin, String terminalId) {
        Assert.notNull(enableVisitorLogin, "enableVisitorLogin can not be null");
        Assert.notNull(enableAutoLogin, "enableVisitorLogin can not be null");
        Assert.notNull(terminalId, "terminalId can not be blank");
        this.enableVisitorLogin = enableVisitorLogin;
        this.enableAutoLogin = enableAutoLogin;
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Boolean getEnableVisitorLogin() {
        return enableVisitorLogin;
    }

    public void setEnableVisitorLogin(Boolean enableVisitorLogin) {
        this.enableVisitorLogin = enableVisitorLogin;
    }

    public Boolean getEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(Boolean enableAutoLogin) {
        this.enableAutoLogin = enableAutoLogin;
    }
}
