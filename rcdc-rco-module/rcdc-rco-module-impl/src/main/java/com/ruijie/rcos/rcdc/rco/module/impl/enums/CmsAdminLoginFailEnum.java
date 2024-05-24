package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import org.springframework.util.Assert;

/**
 * Description: CMS管理员登录失败
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/26 15:45
 *
 * @author yxq
 */
public enum  CmsAdminLoginFailEnum {
    /**
     * AD域用户当前时间不允许登录
     */
    RCDC_RCO_AAA_ADMIN_NOT_ALLOW_LOGIN_THIS_TIME(BusinessKey.RCDC_RCO_AAA_ADMIN_NOT_ALLOW_LOGIN_THIS_TIME, 14),

    /**
     * AD域用户已经过期
     */
    RCDC_RCO_AAA_ADMIN_EXPIRED(BusinessKey.RCDC_RCO_AAA_ADMIN_EXPIRED, 15);

    private String key;

    private Integer code;

    private static final Integer FAILURE = 99;

    CmsAdminLoginFailEnum(String key, Integer code) {
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
     * 获取异常key值对应的响应给shine的code
     * @param key 异常key值
     * @return 异常key值对应的响应给CMS的code值
     */
    public static Integer getCorrespondingCode(String key) {
        Assert.hasText(key, "key can not be blank");
        for (AdminLoginExceptionEnum adminLoginExceptionEnum : AdminLoginExceptionEnum.values()) {
            if (key.equals(adminLoginExceptionEnum.getKey())) {
                return adminLoginExceptionEnum.getCode();
            }
        }
        // 预期之外的异常，返回99
        return FAILURE;
    }
}
