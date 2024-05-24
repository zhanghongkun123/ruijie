package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy;

/**
 * Description: 查询透明加解密的密钥响应体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/6
 *
 * @author WuShengQiang
 */
public class EncryptionDetailResponse {

    /**
     * true:启用 false:停用
     */
    private Boolean enable;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
