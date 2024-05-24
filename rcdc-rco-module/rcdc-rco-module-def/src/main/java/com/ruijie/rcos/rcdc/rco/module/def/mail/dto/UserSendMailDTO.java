package com.ruijie.rcos.rcdc.rco.module.def.mail.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 用户邮件发送
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/25
 *
 * @author liusd
 */
public class UserSendMailDTO {

    /**
     * 用户名
     */
    @NotBlank
    @TextShort
    private String userName;

    /**
     * 用户姓名
     */
    @NotBlank
    @TextShort
    private String realName;

    /**
     * 发送内容
     */
    @NotBlank
    @TextShort
    private String content;

    /**
     * 邮箱地址
     */
    @NotBlank
    @TextShort
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
