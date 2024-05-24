package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode;

/**
 * Description: CAS扫码认证日志
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
public interface CasScanCodeAuthBusinessKey {

    /**
     * CAS扫码认证编辑成功
     */
    String RCDC_CAS_SCAN_CODE_AUTH_CONFIG_SUCCESS = "rcdc_cas_scan_code_auth_config_success";

    /**
     * CAS扫码认证编辑失败
     */
    String RCDC_CAS_SCAN_CODE_AUTH_CONFIG_FAIL = "rcdc_cas_scan_code_auth_config_fail";

    /**
     * 用户或用户组批量设置认证策略参数有误
     */
    String RCDC_USER_GROUP_BATCH_CERTIFICATION_PARAM_FAIL = "23200716";

    /**
     * CAS 服务器连接异常
     */
    String RCDC_CAS_SERVER_CONNECT_FAIL = "23200708";

    /**
     * 创建用户失败原因：
     *  全局未开启外部认证策略
     *  全局未开启动态口令策略
     *  全局未开启硬件特征码策略
     */
    String RCDC_RCO_NOT_OPEN_CAS_FAIL_RESULT = "23200710";
    String RCDC_RCO_NOT_OPEN_OTP_FAIL_RESULT = "23200711";
    String RCDC_RCO_NOT_OPEN_RADIUS_FAIL_RESULT = "23200712";
    String RCDC_RCO_NOT_OPEN_OTP_RADIUS_MEANWHILE = "23200713";
    String RCDC_RCO_NOT_HARDWARE_FAIL_RESULT = "23200714";
    String RCDC_RCO_NOT_OPEN_SMS_AUTH_FAIL_RESULT = "23200715";
    String RCDC_RCO_NOT_OPEN_SMS_AND_RADIUS_AUTH_FAIL_RESULT = "23201160";
    String RCDC_RCO_NOT_OPEN_OTP_AUTH_FAIL_RESULT = "23201161";

    String RCDC_RCO_OPEN_CAS = "rcdc_rco_open_cas";
    String RCDC_RCO_EDIT_CAS = "rcdc_rco_edit_cas";
    String RCDC_RCO_CLOSE_CAS = "rcdc_rco_close_cas";
}
