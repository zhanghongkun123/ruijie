package com.ruijie.rcos.rcdc.rco.module.def.aaa.dto;

import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:16
 *
 * @author wanglianyun
 */
public class CaptchaDTO {
    /**
     * 校验码
     */
    @Nullable
    private String captchaCode;

    /**
     * 校验码的key
     */
    @Nullable
    private String captchaKey;

    @Nullable
    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(@Nullable String captchaCode) {
        this.captchaCode = captchaCode;
    }

    @Nullable
    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(@Nullable String captchaKey) {
        this.captchaKey = captchaKey;
    }

}
