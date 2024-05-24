package com.ruijie.rcos.rcdc.rco.module.def.sms.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacMessageBusinessType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 短信验证码校验请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26
 *
 * @author TD
 */
public class SmsCodeVerifyRequest {

    /**
     * 用户名称
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 业务类型
     */
    @NotNull
    private IacMessageBusinessType businessType;

    /**
     * 短信验证码
     */
    @NotBlank
    private String verifyCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public IacMessageBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(IacMessageBusinessType businessType) {
        this.businessType = businessType;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
