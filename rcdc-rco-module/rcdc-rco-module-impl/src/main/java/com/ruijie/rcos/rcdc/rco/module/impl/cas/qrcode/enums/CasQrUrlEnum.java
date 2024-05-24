package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums;

/**
 * Description: CAS扫码认证URL枚举
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/13
 *
 * @author TD
 */
public enum CasQrUrlEnum {

    /**
     * 初始化请求
     */
    GET_CONFIG("cas/app/getConfig") ,

    /**
     * 获取二维码
     */
    GET_QR_CODE("cas/qrcode?type=cXJjb2Rl") ,

    /**
     * 监听二维码
     */
    MONITOR("cas/qrcode?type=dmFsaWRhdGlvbg&id={id}"),

    /**
     * 校验票据
     */
    VALIDATE("cas/app/ticketValidate3.0");

    private String name;


    CasQrUrlEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
