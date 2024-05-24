package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 获取二维码请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-16 10:54
 *
 * @author wanglianyun
 */
public class MobileClientQrCodeRequest {

    @NotNull
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
