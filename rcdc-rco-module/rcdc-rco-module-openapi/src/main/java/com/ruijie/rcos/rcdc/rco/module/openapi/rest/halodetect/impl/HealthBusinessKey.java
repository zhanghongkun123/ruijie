package com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect.impl;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/22 17:07
 *
 * @author ketb
 */
public interface HealthBusinessKey {

    String RCO_CHECK_IMAGE_GUESTTOOL_VERSION_FAIL = "rco_check_image_guesttool_version_fail";

    String RCO_CHECK_IDV_LICENSE_AUTH_STATE_FAIL = "rco_check_idv_license_auth_state_fail";

    /** 导入终端配置结果 - 成功*/
    String RCDC_TERMINAL_CONFIG_IMPORT_RESULT_SUCCESS = "rcdc_terminal_config_import_result_success";

    /** 导入终端配置结果 - 失败*/
    String RCDC_TERMINAL_CONFIG_IMPORT_RESULT_FAIL = "rcdc_terminal_config_import_result_fail";

    String RCDC_TERMINAL_CONFIG_PACKAGE_NOT_EXIST = "rcdc_terminal_config_package_not_exist";

    String RCDC_TERMINAL_CONFIG_IMPORT_CHECK_VERSION_FAIL = "rcdc_terminal_config_import_check_version_fail";
}
