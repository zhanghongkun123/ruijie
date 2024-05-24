package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;

/**
 * Description: 获取图形验证码返回
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:33
 *
 * @author wanglianyun
 */
public class CaptchaDataResponse extends Result {

    /**
     * 图形验证码
     */
    private String captchaData;

    /**
     * 图形验证码时间
     */
    private Long timestamp;

    /**
     * 图形验证码的key
     */
    private String captchaKey;

    public String getCaptchaData() {
        return captchaData;
    }

    public void setCaptchaData(String captchaData) {
        this.captchaData = captchaData;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

}
