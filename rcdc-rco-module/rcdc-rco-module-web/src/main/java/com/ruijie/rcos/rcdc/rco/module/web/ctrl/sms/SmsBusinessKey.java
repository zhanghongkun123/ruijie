package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms;

/**
 * Description: 短信相关key
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public interface SmsBusinessKey {

    String RCDC_RCO_SMS_GATEWAY_UPDATE_CONFIG_SUCCESS = "rcdc_rco_sms_gateway_update_config_success";
    String RCDC_RCO_SMS_GATEWAY_CONFIG_TEST_SUCCESS = "rcdc_rco_sms_gateway_config_test_success";
    String RCDC_RCO_UPDATE_SMS_CERTIFICATION_SUCCESS = "rcdc_rco_update_sms_certification_success";
    String RCDC_RCO_UPDATE_SMS_PWD_RECOVER_SUCCESS = "rcdc_rco_update_sms_pwd_recover_success";
    String RCDC_RCO_TEST_SMS_SMS_AUTH_CERTIFICATION_SUCCESS = "rcdc_rco_test_sms_sms_auth_certification_success";
    String RCDC_RCO_TEST_SMS_PWD_RECOVER_CERTIFICATION_SUCCESS = "rcdc_rco_test_sms_pwd_recover_certification_success";
    String RCDC_RCO_SMS_GATEWAY_CONFIG_RESET_SUCCESS = "rcdc_rco_sms_gateway_config_reset_success";
    String RCDC_RCO_SMS_TEMPLATE_CONTENT_PLACEHOLDER_ERROR = "23201025";
    String RCDC_RCO_SMS_GATEWAY_PLATFORM_IS_HTTP_CONFIG_ERROR = "23201026";
    String RCDC_RCO_ENCODING_TYPE_ERROR = "23201027";
    String RCDC_RCO_HTTP_REQUEST_HEADER_MAX_LIMIT_ERROR = "23201028";
    String RCDC_RCO_HTTP_REQUEST_BODY_CONTAIN_PHONE_CONTENT_PLACEHOLDER = "23201029";
    String RCDC_RCO_HTTP_PARSE_CONFIG_PARAMS_ERROR = "23201030";
    String RCDC_RCO_HTTP_PARSE_CONFIG_AUTH_RESULT_ERROR = "23201031";
    String RCDC_RCO_SMS_GATEWAY_NOT_OPEN_NOT_ALLOWED_ERROR = "23201032";
}
