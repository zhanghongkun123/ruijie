package com.ruijie.rcos.rcdc.rco.module.def.api.request.user;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 动态口令码登录请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/5
 *
 * @author WuShengQiang
 */
public class OtpCodeLoginRequest {

    @NotBlank
    @TextShort
    private String userName;

    @NotBlank
    private String otpCode;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
