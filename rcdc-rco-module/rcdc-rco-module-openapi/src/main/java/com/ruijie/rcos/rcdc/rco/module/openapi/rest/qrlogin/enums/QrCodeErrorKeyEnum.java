package com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.enums;

/**
 * Description: 扫码二维码错误信息枚举
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-24 16:19:00
 *
 * @author zjy
 */
public enum QrCodeErrorKeyEnum {

    RCDC_USER_UN_SUPPORT_QR_CODE_TYPE("rcdc_user_un_support_qr_code_type"),
    RCDC_USER_QR_CODE_TYPE_SWITCH_IS_CLOSE("rcdc_user_qr_code_type_switch_is_close"),
    RCDC_USER_CLIENT_QR_CODE_NOT_MATCH("rcdc_user_client_qr_code_not_match"),
    RCDC_USER_QR_CODE_STATUS_NOT_EXPECT("rcdc_user_qr_code_status_not_expect"),
    RCDC_USER_QR_CODE_NOT_EXIST_OR_EXPIRE("rcdc_user_qr_code_not_exist_or_expire"),
    RCDC_USER_QR_CODE_USER_NOT_EXISTS("23200187"),
    RCDC_USER_QR_CODE_USER_LOCKED("23200188"),
    RCDC_USER_QR_RCDC_USER_DISABLE("rcdc_rco_user_disabled");

    private String key;

    QrCodeErrorKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
