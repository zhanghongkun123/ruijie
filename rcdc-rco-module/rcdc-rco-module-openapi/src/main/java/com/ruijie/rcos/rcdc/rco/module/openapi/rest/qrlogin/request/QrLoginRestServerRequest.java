package com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 扫码登录缓存用户-请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/04/28
 *
 * @author zhang.zhiwen
 */
public class QrLoginRestServerRequest {

    @Nullable
    private UUID id;

    @NotBlank
    private String userName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
