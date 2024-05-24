package com.ruijie.rcos.rcdc.rco.module.def.user.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

import java.util.UUID;

/**
 * Description: 修改用户密码请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/20
 *
 * @author zqj
 */
public class SyncUserPwdRequest {

    /**
     * 用户名称
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 新密码
     */
    @NotBlank
    private String password;

    /**
     * 集群id
     */
    @NotNull
    private UUID clusterId;

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

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }
}
