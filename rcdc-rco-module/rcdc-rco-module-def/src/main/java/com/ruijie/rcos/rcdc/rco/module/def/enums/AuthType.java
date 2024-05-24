package com.ruijie.rcos.rcdc.rco.module.def.enums;

/**
 * Description: 授权类型枚举
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:36
 *
 * @author wanglianyun
 */
public enum AuthType {

    /**
     * 企业微信
     */
    WORK_WEIXIN,
    /**
     * 飞书
     */
    FEISHU,
    /**
     * 钉钉
     */
    DINGDING,

    /**
     * 通过配置支持的标准auth2
     */
    AUTH2,
    /**
     * 动态口令
     */
    MFA_CODE
}
