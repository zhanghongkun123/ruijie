package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import org.springframework.lang.Nullable;


/**
 * Description: 获取图形验证码请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:29
 *
 * @author wanglianyun
 */
public class ObtainCaptchaRequest {

    /**
     *
     * 图形验证码的key
     */
    @Nullable
    private String invalidateCode;

    /**
     *
     * 时间戳
     */
    @Nullable
    private Long timestamp;

    @Nullable
    public String getInvalidateCode() {
        return invalidateCode;
    }

    public void setInvalidateCode(@Nullable String invalidateCode) {
        this.invalidateCode = invalidateCode;
    }

    @Nullable
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@Nullable Long timestamp) {
        this.timestamp = timestamp;
    }

}
