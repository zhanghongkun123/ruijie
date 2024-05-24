package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

/**
 * Description: 删除用户请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/30
 *
 * @author TD
 */
public class DeleteUserRequest {

    /**
     * 用户名
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 是否强制关机
     */
    @Nullable
    private Boolean enableForceShutdown = Boolean.FALSE;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public Boolean getEnableForceShutdown() {
        return enableForceShutdown;
    }

    public void setEnableForceShutdown(@Nullable Boolean enableForceShutdown) {
        this.enableForceShutdown = enableForceShutdown;
    }
}
