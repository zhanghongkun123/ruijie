package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: CMS修改用户密码请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/28 11:08
 *
 * @author yxq
 */
public class ModifyUserPwdRestServerRequest {

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 用户旧密码，密文
     */
    @NotBlank
    private String oldPassword;

    /**
     * 用户新密码，密文
     */
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
