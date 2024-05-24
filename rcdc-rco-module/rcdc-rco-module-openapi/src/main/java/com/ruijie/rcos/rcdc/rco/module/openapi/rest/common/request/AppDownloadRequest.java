package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: web客户端用户登录请求参数
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-26
 *
 * @author zqj
 */
public class AppDownloadRequest {

    public static final String PLATFORM = "APP";

    @NotBlank
    private String os;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
