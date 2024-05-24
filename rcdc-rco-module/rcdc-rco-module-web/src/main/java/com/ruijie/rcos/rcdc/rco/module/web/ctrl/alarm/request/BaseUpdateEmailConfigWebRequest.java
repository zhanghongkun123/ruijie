package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @author xgx
 */
public class BaseUpdateEmailConfigWebRequest implements WebRequest {
    /**
     * 邮件服务器地址
     */
    @NotBlank
    @TextMedium
    private String serverAddress;

    /**
     * 邮件发送账号
     */
    @NotBlank
    @Size(max = 64)
    @Email
    private String fromMailAccount;

    /**
     * 登陆账号
     */
    @NotBlank
    @TextMedium
    private String loginName;

    /**
     * 邮件发送账号密码
     */
    @NotBlank
    @TextMedium
    private String loginPassword;

    /**
     * 邮件接收账号
     */
    @NotEmpty
    @Size(max = 20)
    private String[] toMailAccountArr;

    /**
     * 邮件抄送账号
     */
    @Nullable
    @Size(max = 20)
    private String[] copyToMailAccountArr;

    /**
     * 邮件发送等级
     */
    @NotNull
    private AlarmLevel sendLevel;

    /**
     * 邮件开关
     */
    @NotNull
    private Boolean enableSendMail;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
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

    public String[] getToMailAccountArr() {
        return toMailAccountArr;
    }

    public void setToMailAccountArr(String[] toMailAccountArr) {
        this.toMailAccountArr = toMailAccountArr;
    }

    @Nullable
    public String[] getCopyToMailAccountArr() {
        return copyToMailAccountArr;
    }

    public void setCopyToMailAccountArr(@Nullable String[] copyToMailAccountArr) {
        this.copyToMailAccountArr = copyToMailAccountArr;
    }

    public AlarmLevel getSendLevel() {
        return sendLevel;
    }

    public void setSendLevel(AlarmLevel sendLevel) {
        this.sendLevel = sendLevel;
    }

    public Boolean getEnableSendMail() {
        return enableSendMail;
    }

    public void setEnableSendMail(Boolean enableSendMail) {
        this.enableSendMail = enableSendMail;
    }
}
