package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.AuthType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;


/**
 * Description: 授权码认证请求DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:25
 *
 * @author wanglianyun
 */
public class AuthCodeRequest {

    @Nullable
    private ClientType source;

    /**
     * 授权码
     */
    @NotNull
    private String authCode;

    /**
     * 授权类型
     */
    @NotNull
    private AuthType authType;

    /**
     * 登录IP，管理登录才需要
     */
    @Nullable
    private String loginIp;

    /**
     * 是否忽略白名单检测，管理员登录才需要
     */
    @Nullable
    private String ignoreIpWhitelistCheck;

    @Nullable
    private String state;

    @Nullable
    private String terminalId;

    @Nullable
    public String getState() {
        return state;
    }

    public void setState(@Nullable String state) {
        this.state = state;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    @Nullable
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(@Nullable String loginIp) {
        this.loginIp = loginIp;
    }

    @Nullable
    public String getIgnoreIpWhitelistCheck() {
        return ignoreIpWhitelistCheck;
    }

    public void setIgnoreIpWhitelistCheck(@Nullable String ignoreIpWhitelistCheck) {
        this.ignoreIpWhitelistCheck = ignoreIpWhitelistCheck;
    }

    public ClientType getSource() {
        return source;
    }

    public void setSource(ClientType source) {
        this.source = source;
    }

    @Nullable
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(@Nullable String terminalId) {
        this.terminalId = terminalId;
    }
}
