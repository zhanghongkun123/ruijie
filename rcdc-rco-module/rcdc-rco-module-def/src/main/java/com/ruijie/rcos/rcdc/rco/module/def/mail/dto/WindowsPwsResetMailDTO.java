package com.ruijie.rcos.rcdc.rco.module.def.mail.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: window密码重置邮件发送
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/28
 *
 * @author zjy
 */
public class WindowsPwsResetMailDTO {

    public static final String SUBJECT = "重置windows密码";

    /**
     * 桌面名
     */
    @NotBlank
    private String desktopName;

    /**
     * windows账户名
     */
    @NotBlank
    @Size(min = 1, max = 20)
    private String windowsUserName;

    /**
     * windows密码
     */
    @NotBlank
    @Size(min = 1, max = 128)
    private String password;

    /**
     * 邮箱地址
     */
    @NotBlank
    @TextShort
    private String email;

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getWindowsUserName() {
        return windowsUserName;
    }

    public void setWindowsUserName(String windowsUserName) {
        this.windowsUserName = windowsUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}