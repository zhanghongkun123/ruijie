package com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.response;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.enums.QrLoginRespEnum;

/**
 * Description: 扫码登录缓存用户-响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/04/28
 *
 * @author zhang.zhiwen
 */
public class QrLoginRestServerResponse {

    private QrLoginRespEnum cacheRes;

    public QrLoginRestServerResponse(QrLoginRespEnum cacheRes) {
        this.cacheRes = cacheRes;
    }

    public QrLoginRespEnum getCacheRes() {
        return cacheRes;
    }

    public void setCacheRes(QrLoginRespEnum cacheRes) {
        this.cacheRes = cacheRes;
    }
}
