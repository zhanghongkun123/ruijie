package com.ruijie.rcos.rcdc.rco.module.def.enums;

import org.springframework.util.Assert;

/**
 * Description: 主认证和辅助认证方式枚举
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-20 15:13
 *
 * @author wanglianyun
 */
public enum MainAuthAndAssistAuthEnum {

    /**
     * 企业微信
     */
    WORK_WEIXIN,

    /**
     * 钉钉
     */
    DINGDING,

    /**
     * 飞书
     */
    FEISHU,

    /**
     * auth2
     */
    AUTH2,

    /**
     * 动态口令
     */
    MFA_CODE,

    /**
     * 锐捷客户端
     */
    RJ_QRCODE_AUTH,

    /**
     * 动态口令
     */
    MFA_CERTIFICATION,

    /**
     * 辅助认证
     */
    THIRD_PARTY_CERTIFICATION,

    /**
     * 短信
     */
    SMS_CERTIFICATION,

    /**
     * 硬件特征码
     */
    HARDWARE_CERTIFICATION,

    /**
     * 图形验证码
     */
    CAPTCHA_CERTIFICATION;

    /**
     * 获取认证类型枚举
     * @param authType authType
     * @return MainAuthAndAssistAuthEnum
     */
    public static MainAuthAndAssistAuthEnum convert(String authType) {
        Assert.notNull(authType, "authType can not be null");
        for (MainAuthAndAssistAuthEnum temp : MainAuthAndAssistAuthEnum.values()) {
            if (authType.equals(temp.name())) {
                return temp;
            }
        }
        //未匹配返回null
        return null;
    }
}
