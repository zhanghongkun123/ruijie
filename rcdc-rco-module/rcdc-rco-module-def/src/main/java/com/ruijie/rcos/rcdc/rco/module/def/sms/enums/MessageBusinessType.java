package com.ruijie.rcos.rcdc.rco.module.def.sms.enums;

/**
 * Description: 消息业务类型
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14
 *
 * @author TD
 */
public enum MessageBusinessType {

    /**
     * 短信认证
     */
    SMS_AUTH("rcdc_rco_test_sms_sms_auth_certification_success"),

    /**
     * 密码找回
     */
    PWD_RECOVER("rcdc_rco_test_sms_pwd_recover_certification_success");

    private String smsBusinessKey;

    MessageBusinessType(String smsBusinessKey) {
        this.smsBusinessKey = smsBusinessKey;
    }

    public String getSmsBusinessKey() {
        return smsBusinessKey;
    }
}
