package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

/**
 * 全局策略国际化常量
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月12日
 *
 * @author lihengjing
 */
public interface GlobalStrategyBussinessKey {

    /**
     * RCCM开启多集群统一登录功能 限制编辑辅助认证策略
     */
    String RCCM_OPEN_UNIFIED_LOGIN_CAN_NOT_EDIT_SETTING = "23200993";

    /**
     * 自动扩容全局配置
     */
    String RCDC_EDIT_FULL_SYSTEM_DISK_SUCCESS = "rcdc_edit_full_system_disk_success";
    String RCDC_EDIT_FULL_SYSTEM_DISK_FAIL = "23200996";

    /**
     * VDI授权设置成高级授权
     */
    String RCDC_OPEN_ENABLE_AUTH_COMPATIBLE_SUCCESS = "rcdc_open_enable_auth_compatible_success";
    String RCDC_CLOSE_ENABLE_AUTH_COMPATIBLE_SUCCESS = "rcdc_close_enable_auth_compatible_success";


    String RCDC_RCO_IP_ILLEGAL = "23201015";

    String RCDC_RCO_PORT_ILLEGAL = "23201016";

    String RCDC_RCO_EDIT_THIRD_PARTY_AUTH_CODE_CONFIG_SUCCESS_LOG = "rcdc_rco_edit_third_party_auth_code_config_success_log";

    String RCDC_RCO_ENABLE_THIRD_PARTY_AUTH_CODE_CONFIG_SUCCESS = "rcdc_rco_enable_third_part_auth_code_config_success";

    String RCDC_RCO_DISABLE_THIRD_PARTY_AUTH_CODE_CONFIG_SUCCESS = "rcdc_rco_disable_third_part_auth_code_config_success";

    String TEST_IP_PORT_CONNECTION_SUCCESS = "test_ip_port_connection_success";

    String TEST_CONNECTION_FAIL = "23201116";

    String RCDC_RCO_SAVE_ENCRYPTION = "23201176";
    String RCDC_RCO_SAVE_ENCRYPTION_KEY_UNABLE_REQUIRE = "23201178";
    String RCDC_RCO_NOT_DEFAULT_ADMIN_NOT_ALLOW = "23201182";
    String RCDC_RCO_ENCRYPTION_NOT_SUPPORT_SERVER_MODEL = "23201187";



    String RCDC_IMAGE_SYNC_STRATEGY_OP_SUCCESS_LOG = "rcdc_image_sync_strategy_op_success_log";
    String RCDC_IMAGE_SYNC_STRATEGY_OP_FAIL_LOG = "rcdc_image_sync_strategy_op_fail_log";
    String RCDC_IMAGE_SYNC_QOS_LOG = "rcdc_image_sync_qos_log";
    String RCDC_IMAGE_SYNC_TASK_NUM_LOG = "rcdc_image_sync_task_num_log";
    String RCDC_IMAGE_SYNC_QOS_NOT_LIMIT = "rcdc_image_sync_qos_not_limit";
}
