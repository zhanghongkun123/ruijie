package com.ruijie.rcos.rcdc.rco.module.def.api.request;


import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月15日
 *
 * @author ljm
 */
public class LoginAdminRequest implements Request {

    @TextShort
    @NotBlank
    @TextName
    private String adminName;

    @NotBlank
    private String password;

    @Nullable
    private String loginIp;


    public LoginAdminRequest() {
    }

    public LoginAdminRequest(String adminName, String password, String loginIp) {
        this.adminName = adminName;
        this.password = password;
        this.loginIp = loginIp;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(@Nullable String loginIp) {
        this.loginIp = loginIp;
    }
}
