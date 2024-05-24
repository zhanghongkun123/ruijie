package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: 用户登录时postAuth入参
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/15
 *
 * @author linke
 */
public class LoginPostAuthDTO {

    @NotNull
    private String terminalId;

    @Nullable
    private String userName;

    @Nullable
    private Boolean hasResetErrorTimes;

    /**
     * 是否需要辅助认证，默认需要
     */
    @Nullable
    private Boolean needAssistAuth = Boolean.TRUE;

    @Nullable
    private IacUserDetailDTO userDetailDTO;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    @Nullable
    public Boolean getHasResetErrorTimes() {
        return hasResetErrorTimes;
    }

    public void setHasResetErrorTimes(@Nullable Boolean hasResetErrorTimes) {
        this.hasResetErrorTimes = hasResetErrorTimes;
    }

    @Nullable
    public Boolean getNeedAssistAuth() {
        return needAssistAuth;
    }

    public void setNeedAssistAuth(@Nullable Boolean needAssistAuth) {
        this.needAssistAuth = needAssistAuth;
    }

    @Nullable
    public IacUserDetailDTO getUserDetailDTO() {
        return userDetailDTO;
    }

    public void setUserDetailDTO(@Nullable IacUserDetailDTO userDetailDTO) {
        this.userDetailDTO = userDetailDTO;
    }
}