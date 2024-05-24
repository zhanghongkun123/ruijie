package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums;

import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/29
 *
 * @author TD
 */
public enum CasQrCodeEnum {

    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 失败
     */
    FAIL_CODE(-1),

    /**
     * RCDC返回数据异常
     */
    RCDC_RETURN_FAILURE(-3),

    /**
     * CAS 配置信息不存在
     */
    CAS_CONFIG_NON_EXISTENT(-33),

    /**
     * 二维码获取失败
     */
    QR_CODE_FAILURE(-34),

    /**
     * 二维码未被扫码
     */
    QR_CODE_NOT_SCAN_CODE(-35),

    /**
     * 二维码过期
     */
    QR_CODE_EXPIRE(-36),

    /**
     * CAS服务器异常
     */
    API_FAILURE(-37),

    /**
     * 输入参数错误
     */
    PARAM_ERROR(-38),

    /**
     * 令牌不存在
     */
    TICKET_NOT_EXISTS(-39),

    /**
     * 设备变更
     */
    ACCOUNT_LOCKED(-40),

    /**
     * 账号被锁
     */
    DEVICE_CHANGED(-41),

    /**
     * 密码过期
     */
    PASSWORD_EXPIRED(-42),

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(-43),

    /**
     * 服务端异常
     */
    SERVER_EXCEPTION(-44),

    /**
     * TICKET验证失败
     */
    TICKET_VERIFY_FAIL(-45),

    /**
     * 未知
     */
    UNKNOWN(-66);

    private Integer code;

    CasQrCodeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 获取枚举
     * @param value 枚举名称
     * @return 枚举值
     */
    public static CasQrCodeEnum getCasQrCodeEnum(String value) {
        Assert.notNull(value, "value is not null");
        CasQrCodeEnum[] casQrCodeEnums = CasQrCodeEnum.values();
        for (CasQrCodeEnum casQrCodeEnum:casQrCodeEnums) {
            if (value.equals(casQrCodeEnum.name())) {
                return casQrCodeEnum;
            }
        }
        return CasQrCodeEnum.UNKNOWN;
    }

    /**
     * 获取错误枚举名称
     * @param value 枚举名称
     * @param defaultEnum 默认枚举名称
     * @return 枚举值
     */
    public static CasQrCodeEnum getCasQrCodeEnum(String value, CasQrCodeEnum defaultEnum) {
        Assert.notNull(value, "value is not null");
        Assert.notNull(defaultEnum, "defaultEnum is not null");
        CasQrCodeEnum[] casQrCodeEnums = CasQrCodeEnum.values();
        for (CasQrCodeEnum casQrCodeEnum:casQrCodeEnums) {
            if (value.equals(casQrCodeEnum.name())) {
                return casQrCodeEnum;
            }
        }
        return defaultEnum;
    }
}
