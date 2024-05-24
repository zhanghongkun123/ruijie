package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
public class ImportUserDataRequest {


    @NotBlank
    @Size(max = 64)
    private String userId;

    @NotBlank
    @Size(max = 64)
    private String userName;

    @NotBlank
    private String userGroupId;

    @NotBlank
    private String realName;

    @NotBlank
    private String password;

    @Nullable
    private String phoneNum;

    @NotNull
    private AuthType authType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(@Nullable String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }
}
