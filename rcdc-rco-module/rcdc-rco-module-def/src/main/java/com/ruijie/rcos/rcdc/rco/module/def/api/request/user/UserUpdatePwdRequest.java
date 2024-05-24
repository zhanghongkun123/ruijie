package com.ruijie.rcos.rcdc.rco.module.def.api.request.user;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: 普通用户修改密码请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年11月19日10:58:20
 *
 * @author zjy
 */
public class UserUpdatePwdRequest extends CommonUserUpdatePwdRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
