package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Description: 扫码二维码错误信息枚举
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-18 19:19:00
 *
 * @author zjy
 */
public enum QrCodeErrorEnum {

    RCDC_USER_UN_SUPPORT_QR_CODE_TYPE("rcdc_user_un_support_qr_code_type", 10001),
    RCDC_USER_QR_CODE_TYPE_SWITCH_IS_CLOSE("rcdc_user_qr_code_type_switch_is_close", 10002),
    RCDC_USER_CLIENT_QR_CODE_NOT_MATCH("rcdc_user_client_qr_code_not_match", 10003),
    RCDC_USER_QR_CODE_STATUS_NOT_EXPECT("rcdc_user_qr_code_status_not_expect", 10004),
    RCDC_USER_QR_CODE_NOT_EXIST_OR_EXPIRE("rcdc_user_qr_code_not_exist_or_expire", 10005)
    ;

    private String key;

    private Integer code;

    QrCodeErrorEnum(String key, Integer code) {
        this.key = key;
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据二维码错误key返回code
     *
     * @param key key
     * @return 返回值
     * @Date 2022/2/18 19:41
     * @Author zjy
     **/
    public static QrCodeErrorEnum getQrCodeErrorEnum(String key) {
        Assert.hasText(key, "key must not be null");

        return Arrays.stream(QrCodeErrorEnum.values())
                .filter(item -> item.getKey().equals(key))
                .findFirst().orElse(null);
    }
}