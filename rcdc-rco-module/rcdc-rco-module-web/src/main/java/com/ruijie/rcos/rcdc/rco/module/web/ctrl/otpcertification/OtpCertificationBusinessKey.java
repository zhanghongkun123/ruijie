package com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月25日
 *
 * @author Ghang
 */
public interface OtpCertificationBusinessKey {

    /** 认证策略配置成功 */
    String RCDC_OTP_CERTIFICATION_CONFIG_SUCCESS = "rcdc_otp_certification_config_success";

    /** 认证策略配置失败 */
    String RCDC_OTP_CERTIFICATION_CONFIG_FAIL = "rcdc_otp_certification_config_fail";

    /** 重置密钥传入的数组长度必须小于1 */
    String RCDC_OTP_SECRET_KEY_RESET_USER_ID_ARR_MUST_BE_ONE = "23200896";

    /** 重置密钥成功 */
    String RCDC_OTP_SECRET_KEY_RESET_SUCCESS = "rcdc_otp_secret_key_reset_success";

    /** 批量重置密钥 */
    String RCDC_OTP_SECRET_KEY_RESET_TASK_SUCCESS = "rcdc_otp_secret_key_reset_task_success";

    /** 重置密钥失败 */
    String RCDC_OTP_SECRET_KEY_RESET_FAIL = "rcdc_otp_secret_key_reset_fail";

    /** 重置动态口令 */
    String RCDC_RESET_OTP_SECRET_KEY_ITEM_NAME = "rcdc_reset_otp_secret_key_item_name";

    /** 重置单个动态口令 */
    String RCDC_RESET_OTP_SECRET_KEY_SINGLE_TASK_NAME = "rcdc_reset_otp_secret_key_single_task_name";
    /** 正在重置单个动态口令 */
    String RCDC_RESET_OTP_SECRET_KEY_SINGLE_TASK_DESC = "rcdc_reset_otp_secret_key_single_task_desc";
    /** 批量重置动态口令 */
    String RCDC_RESET_OTP_SECRET_KEY_TASK_NAME = "rcdc_reset_otp_secret_key_task_name";
    /** 正在批量重置动态口令 */
    String RCDC_RESET_OTP_SECRET_KEY_TASK_DESC = "rcdc_reset_otp_secret_key_task_desc";

    String RCDC_OPEN_OPT_NOT_OPEN_NTP_FAIL = "rcdc_open_opt_not_open_ntp_fail";
}

