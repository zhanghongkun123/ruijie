package com.ruijie.rcos.rcdc.rco.module.def.api.request;


import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月15日
 *
 * @author ljm
 */
public class LoginUserRequest implements Request {


    @NotBlank
    private String userName;

    @NotBlank
    private String password;


    public LoginUserRequest() {
    }

    public LoginUserRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
