package com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public class UpdateAdminRequest extends UpdateAdminDataPermissionRequest implements Request, Serializable {

    @NotBlank
    @TextShort
    @TextName
    private String userName;

    @NotBlank
    @TextShort
    private String realName;

    @Email
    private String email;

    @TextMedium
    private String describe;

    @NotNull
    private UUID roleId;

    @Nullable
    private String[] menuNameArr;

    @NotNull
    private Boolean enabled;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }


    public String[] getMenuNameArr() {
        return menuNameArr;
    }

    public void setMenuNameArr(String[] menuNameArr) {
        this.menuNameArr = menuNameArr;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
