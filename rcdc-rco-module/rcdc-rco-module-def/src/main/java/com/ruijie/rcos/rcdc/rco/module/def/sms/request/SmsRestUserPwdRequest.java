package com.ruijie.rcos.rcdc.rco.module.def.sms.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

import java.util.UUID;

/**
 * Description: 短信修改用户密码请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26
 *
 * @author TD
 */
public class SmsRestUserPwdRequest {

    /**
     * 用户名称
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 新密码
     */
    @NotBlank
    private String newPassword;

    /**
     * 密钥
     */
    @NotNull
    private UUID token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
