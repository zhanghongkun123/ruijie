package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 变更用户信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/30
 *
 * @author TD
 */
public class ModifyUserRequest {

    /**
     * 用户名
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 用户组名称，多个层级用/分隔
     */
    @NotNull
    private String userGroupName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }
}
