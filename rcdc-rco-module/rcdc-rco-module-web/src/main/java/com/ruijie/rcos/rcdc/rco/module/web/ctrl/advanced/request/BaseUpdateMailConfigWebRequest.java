package com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 更新邮件服务器请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/08 11:49
 *
 * @author guoyongxin
 */
@ApiModel("更新邮件服务器请求体")
public class BaseUpdateMailConfigWebRequest implements WebRequest {
    /**
     * 邮件服务器地址
     */
    @TextMedium
    @ApiModelProperty(value = "邮件服务器地址", name = "serverAddress", example = "smtp.163.com", required = true)
    private String serverAddress;

    /**
     * 邮件服务器端口
     */
    @Nullable
    @ApiModelProperty(value = "邮件服务器端口", name = "serverPort", example = "25")
    private Integer serverPort;

    /**
     * 邮件发送账号
     */
    @Size(max = 64)
    @Email
    @ApiModelProperty(value = "发送方邮箱账号", name = "fromMailAccount", example = "test@163.com", required = true)
    private String fromMailAccount;

    /**
     * 登陆账号
     */
    @TextMedium
    @ApiModelProperty(value = "登录账号", name = "loginName", example = "test@163.com")
    private String loginName;

    /**
     * 邮件发送账号密码
     */
    @TextMedium
    @ApiModelProperty(value = "发送方密码", name = "loginPassword", example = "123456")
    private String loginPassword;

    /**
     * 邮件开关
     */
    @NotNull
    @ApiModelProperty(value = "邮件开关", name = "enableSendMail", example = "true", required = true)
    private Boolean enableSendMail;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
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

    public Boolean getEnableSendMail() {
        return enableSendMail;
    }

    public void setEnableSendMail(Boolean enableSendMail) {
        this.enableSendMail = enableSendMail;
    }
}
