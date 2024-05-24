package com.ruijie.rcos.rcdc.rco.module.def.sms.constnts;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月15日
 *
 * @author zhanghongkun
 */
public interface SmsAndScanCodeCheckConstants {
    /**
     * 短信校验开关-配置数据
     */
    String SMS_AND_SCANCODE_SWITCH = "sms_and_scancode_switch";

    /**
     * 短信网关配置
     */
    String SMS_GATEWAY_CONFIG = "sms_gateway_config";    
    
    /**
     * 短信认证配置
     */
    String SMS_AUTH_CONFIG = "sms_auth_config";

    /**
     * 找回密码配置
     */
    String SMS_RECOVER_PWD_CONFIG = "sms_recover_pwd_config";

    /**
     * 手机号码占位符
     */
    String MOBILE_NUM = "MOBILE_NUM";

    /**
     * 短信内容占位符
     */
    String SMS_CONTENT = "SMS_CONTENT";

    /**
     * 短信密码找回推送
     */
    String PUSH_SMS_RECOVER_PWD = "push_sms_recover_pwd";

    /**
     * 短信验证码占位符key
     */
    String VERIFY_CODE = "VERIFY_CODE";    
    
    /**
     * 时间占位符key
     */
    String MINUTE = "MINUTE";

    /**
     * 短信验证码占位符
     */
    String VERIFY_CODE_PLACEHOLDER = "<" + VERIFY_CODE + ">";

    /**
     * 时间占位符
     */
    String MINUTE_PLACEHOLDER = "<" + MINUTE + ">";

    /**
     * 用户名称
     */
    String USER_NAME = "USER_NAME";

    String RCDC_RCO_SMS_RESULT_RESOLVING_FAIL = "23201900";

    /**
     * 短信解析占位符
     */
    String STRING_CODE = "CODE";

    /**
     * STRING类型返回分割符
     */
    String PLACEHOLDER_RESOLVING = "$$";

    /**
     * 请求头最大限制个数
     */
    int HEADER_MAX_LIMIT = 10;

    /**
     * 请求体最大限制个数
     */
    int BODY_MAX_LIMIT = 20;

    /**
     * 用户绑定手机锁KEY
     */
    String USER_BIND_PHONE_LOCK = "DISK_POOL_ASSIGN_LOCK_";

    /**
     * 用户短信校验锁KEY
     */
    String USER_VERIFY_CODE_AUTH_LOCK = "USER_VERIFY_CODE_AUTH_LOCK_";
}
