package com.ruijie.rcos.rcdc.rco.module.def.sms.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 用户绑定手机号请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26
 *
 * @author TD
 */
public class UserBindPhoneRequest {

    /**
     * 用户名称
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 手机号码
     */
    @NotBlank
    @TextShort
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
