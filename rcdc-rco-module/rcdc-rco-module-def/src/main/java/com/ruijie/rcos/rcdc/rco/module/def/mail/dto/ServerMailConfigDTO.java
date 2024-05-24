package com.ruijie.rcos.rcdc.rco.module.def.mail.dto;

import com.ruijie.rcos.sk.base.annotation.Email;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/08 11:49
 *
 * @author guoyongxin
 */
public class ServerMailConfigDTO {

    @NotBlank
    @TextMedium
    private String serverAddress;

    @Nullable
    private Integer serverPort;

    @Email
    private String fromMailAccount;

    @TextMedium
    private String loginName;

    @TextMedium
    private String loginPassword;

    @Nullable
    private Boolean enableSendMail;


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Nullable
    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(@Nullable Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getFromMailAccount() {
        return fromMailAccount;
    }

    public void setFromMailAccount(String fromMailAccount) {
        this.fromMailAccount = fromMailAccount;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    @Nullable
    public Boolean getEnableSendMail() {
        return enableSendMail;
    }

    public void setEnableSendMail(@Nullable Boolean enableSendMail) {
        this.enableSendMail = enableSendMail;
    }
}
