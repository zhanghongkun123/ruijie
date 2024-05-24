package com.ruijie.rcos.rcdc.rco.module.def.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/28 9:58
 *
 * @author zdc
 */
public class LoginFinishRequest {

    /**
     * 用户名
     */
    @NotNull
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
