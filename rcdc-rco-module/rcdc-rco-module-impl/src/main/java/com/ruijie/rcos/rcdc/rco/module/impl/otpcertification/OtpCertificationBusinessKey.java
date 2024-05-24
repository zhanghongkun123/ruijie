package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
public interface OtpCertificationBusinessKey {

    /**
     * 重置密钥成功
     */
    String USER_OTP_CERTIFICATION_RESET_SUCCESS = "user_otp_certification_reset_success";

    /**
     * 密钥绑定成功
     */
    String USER_OTP_CERTIFICATION_BIND_SUCCESS = "user_otp_certification_bind_success";

    /**
     * 用户OTP配置为空
     */
    String USER_OTP_CERTIFICATION_RECORD_NULL = "23200349";

    /**
     * OTP全局配置为空
     */
    String GLOBAL_OTP_CERTIFICATION_CONFIG_NULL = "23200350";
}
