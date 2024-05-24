package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.enums.AuthTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 * 
 * @author wjp
 */
public class VerifUserRestServerRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @Nullable
    private AuthTypeEnum authType;

    public AuthTypeEnum getAuthType() {
        return authType;
    }

    public void setAuthType(AuthTypeEnum authType) {
        this.authType = authType;
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
