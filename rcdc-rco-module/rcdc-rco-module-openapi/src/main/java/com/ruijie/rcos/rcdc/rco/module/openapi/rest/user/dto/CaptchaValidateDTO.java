package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.dto;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 22:16
 *
 * @author wanglianyun
 */
public class CaptchaValidateDTO {

    private Boolean isCaptchaRight;

    private String code;

    private String message;

    public Boolean getCaptchaRight() {
        return isCaptchaRight;
    }

    public void setCaptchaRight(Boolean captchaRight) {
        isCaptchaRight = captchaRight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
