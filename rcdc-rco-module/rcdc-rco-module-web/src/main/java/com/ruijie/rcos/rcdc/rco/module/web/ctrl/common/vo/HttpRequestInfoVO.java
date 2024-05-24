package com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.vo;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年2月28日
 * 
 * @author zhuangchenwu
 */
public class HttpRequestInfoVO {

    @NotBlank
    private String port;
    
    @NotNull
    private UUID token;
    
    @NotNull
    private UUID userId;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
}
