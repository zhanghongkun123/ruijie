package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

/**
 * Description: web工程国际化
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月13日
 *
 * @author chenzj
 */
public interface UserBusinessKey {

    /**
     * 操作成功
     */
    String RCDC_RCO_MODULE_OPERATE_SUCCESS = "rcdc_rco_module_operate_success";
    /**
     * 操作失败
     */
    String RCDC_RCO_MODULE_OPERATE_FAIL = "rcdc_rco_module_operate_fail";

    /**
     * 用户组模块
     */
    String RCDC_RCO_USER_IMPORT_NOT_USER_DATA = "23200931";
    String RCDC_RCO_USER_GROUP_IMPORT_NOT_GROUP_DATA = "23200937";
    String RCDC_RCO_USER_IMPORT_FAIL = "23200930";
    String RCDC_RCO_USER_GROUP_IMPORT_FAIL = "23200936";
    String RCDC_RCO_USER_IMPORT_INVALID_FORMAT = "23200932";
    String RCDC_RCO_USER_GROUP_IMPORT_ANALYSIS_FAIL = "23201657";
    String RCDC_RCO_USER_IMPORT_USERNAME_INCORRECT = "rcdc_rco_user_import_username_incorrect";
    String RCDC_RCO_USER_GROUP_IMPORT_IMAGE_TEMPLATE_NAME_INCORRECT = "rcdc_rco_user_group_import_image_template_name_incorrect";
    String RCDC_RCO_USER_GROUP_IMPORT_STRATEGY_NAME_INCORRECT = "rcdc_rco_user_group_import_strategy_name_incorrect";
    String RCDC_RCO_USER_GROUP_IMPORT_NETWORK_NAME_INCORRECT = "rcdc_rco_user_group_import_network_name_incorrect";

    String RCDC_RCO_USER_GROUP_IMPORT_VALIDATE_EXPIRE_DATE_INCORRECT = "rcdc_rco_user_group_import_validate_expire_date_incorrect";

    String RCDC_RCO_USER_GROUP_IMPORT_STORAGE_POOL_NAME_INCORRECT = "rcdc_rco_user_group_import_storage_pool_name_incorrect";
    String RCDC_RCO_USER_GROUP_IMPORT_STORAGE_POOL_NAME_TOO_LENGTH = "rcdc_rco_user_group_import_storage_pool_name_too_length";
    String RCDC_RCO_BATCH_IMPORT_STORAGE_POOL_NAME_NOT_EXIST = "rcdc_rco_batch_import_storage_pool_name_not_exist";
    String RCDC_RCO_BATCH_IMPORT_CLUSTER_NAME_NOT_EXIST = "23201009";
    String RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_NOT_EXIST = "23201201";
    String RCDC_RCO_USER_IMPORT_USERNAME_TOO_LENGTH = "rcdc_rco_user_import_username_too_length";
    String RCDC_RCO_USER_GROUP_IMPORT_IMAGE_TEMPLATE_NAME_TOO_LENGTH = "rcdc_rco_user_group_import_image_template_name_too_length";
    String RCDC_RCO_USER_IMPORT_USERNAME_NOT_ALLOW_RESERVED = "rcdc_rco_user_import_username_not_allow_reserved";
    String RCDC_RCO_USER_IMPORT_USERNAME_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_username_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_USERSTATE_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_userstate_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_GROUPNAME_INCORRECT = "rcdc_rco_user_import_groupname_incorrect";
    String RCDC_RCO_USER_GROUP_IMPORT_GROUPNAME_INCORRECT = "rcdc_rco_user_group_import_groupname_incorrect";
    String RCDC_RCO_USER_IMPORT_GROUP_OVER_COUNT = "rcdc_rco_user_import_group_over_count";
    String RCDC_RCO_USER_GROUP_IMPORT_GROUP_OVER_COUNT = "rcdc_rco_user_group_import_group_over_count";
    String RCDC_RCO_USER_IMPORT_GROUPNAME_TOO_LENGTH = "rcdc_rco_user_import_groupname_too_length";
    String RCDC_RCO_USER_GROUP_IMPORT_GROUPNAME_TOO_LENGTH = "rcdc_rco_user_group_import_groupname_too_length";
    String RCDC_RCO_USER_IMPORT_GROUPNAME_NOT_ALLOW_RESERVED = "rcdc_rco_user_import_groupname_not_allow_reserved";
    String RCDC_RCO_USER_GROUP_IMPORT_GROUPNAME_NOT_ALLOW_RESERVED = "rcdc_rco_user_group_import_groupname_not_allow_reserved";
    String RCDC_RCO_USER_IMPORT_REALNAME_TOO_LENGTH = "rcdc_rco_user_import_realname_too_length";
    String RCDC_RCO_USER_GROUP_IMPORT_STRATEGY_NAME_TOO_LENGTH = "rcdc_rco_user_group_import_strategy_name_too_length";
    String RCDC_RCO_USER_GROUP_IMPORT_NETWORK_NAME_TOO_LENGTH = "rcdc_rco_user_group_import_network_name_too_length";
    String RCDC_RCO_USER_IMPORT_REALNAME_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_realname_not_allow_empty";
    String RCDC_RCO_USER_GROUP_IMPORT_GROUP_NAME_NOT_ALLOW_EMPTY = "rcdc_rco_user_group_import_group_name_not_allow_empty";
    String RCDC_RCO_BATCH_IMPORT_CLUSTER_NAME_NOT_ALLOW_EMPTY = "rcdc_rco_batch_import_group_cluster_name_not_allow_empty";
    String RCDC_RCO_BATCH_IMPORT_GROUP_SYSTEM_STORAGE_NOT_ALLOW_EMPTY = "rcdc_rco_batch_import_group_system_storage_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_PHONE_ILLEGAL = "rcdc_rco_user_import_phone_illegal";
    String RCDC_RCO_USER_IMPORT_EMAIL_ILLEGAL = "rcdc_rco_user_import_email_illegal";
    String RCDC_RCO_USER_IMPORT_USERSTATE_ILLEGAL = "rcdc_rco_user_import_userstate_illegal";
    String RCDC_RCO_USER_IMPORT_PASSWORD_ILLEGAL = "rcdc_rco_user_import_password_illegal";
    String RCDC_RCO_USER_IMPORT_USERGROUP_NOT_EMPTY = "rcdc_rco_user_import_usergroup_not_empty";
    String RCDC_RCO_USER_GROUP_IMPORT_PARENT_GROUP_NOT_ALLOWED_EMPTY = "rcdc_rco_user_group_import_parent_group_not_allowed_empty";
    String RCDC_RCO_USER_IMPORT_ROW_OVER = "23200944";
    String RCDC_RCO_IMPORT_USER_ITEM_NAME = "rcdc_rco_import_user_item_name";
    String RCDC_RCO_USER_GROUP_IMPORT_ROW_OVER = "23200938";
    String RCDC_RCO_IMPORT_USER_TASK_NAME = "rcdc_rco_import_user_task_name";
    String RCDC_RCO_IMPORT_USER_GROUP_TASK_NAME = "rcdc_rco_import_user_group_task_name";
    String RCDC_RCO_IMPORT_USER_TASK_DESC = "rcdc_rco_import_user_task_desc";
    String RCDC_RCO_IMPORT_USER_GROUP_TASK_DESC = "rcdc_rco_import_user_group_task_desc";
    String RCDC_RCO_IMPORT_USER_FAIL_LOG = "rcdc_rco_import_user_fail_log";
    String RCDC_RCO_IMPORT_USER_GROUP_FAIL_LOG = "rcdc_rco_import_user_group_fail_log";
    String RCDC_RCO_IMPORT_USER_SUCCESS_LOG = "rcdc_rco_import_user_success";
    String RCDC_RCO_IMPORT_USER_GROUP_SUCCESS_LOG = "rcdc_rco_import_user_group_success";
    String RCDC_RCO_IMPORT_USER_APPLY_GROUP_IDV_CONFIG_SUCCESS_LOG = "rcdc_rco_import_user_apply_group_idv_config_success";
    String RCDC_RCO_IMPORT_USER_APPLY_GROUP_VOI_CONFIG_SUCCESS_LOG = "rcdc_rco_import_user_apply_group_voi_config_success";
    String RCDC_RCO_IMPORT_USER_APPLY_GROUP_IDV_CONFIG_FAIL_LOG = "rcdc_rco_import_user_apply_group_idv_config_fail";
    String RCDC_RCO_IMPORT_USER_APPLY_GROUP_VOI_CONFIG_FAIL_LOG = "rcdc_rco_import_user_apply_group_voi_config_fail";
    String RCDC_RCO_IMPORT_USER_APPLY_GROUP_IDV_CONFIG_FAIL_DESC = "rcdc_rco_import_user_apply_group_idv_config_fail_desc";
    String RCDC_RCO_IMPORT_USER_APPLY_GROUP_VOI_CONFIG_FAIL_DESC = "rcdc_rco_import_user_apply_group_voi_config_fail_desc";
    String RCDC_RCO_IMPORT_USER_SEND_RANDOM_PASSWORD_FAIL_DESC = "rcdc_rco_import_user_send_random_password_fail_desc";
    String RCDC_RCO_BATCH_APPLY_GROUP_IDV_CONFIG_SUCCESS_LOG = "rcdc_rco_batch_apply_group_idv_config_success";
    String RCDC_RCO_BATCH_APPLY_GROUP_VOI_CONFIG_SUCCESS_LOG = "rcdc_rco_batch_apply_group_voi_config_success";
    String RCDC_RCO_BATCH_APPLY_GROUP_IDV_CONFIG_FAIL_LOG = "rcdc_rco_batch_apply_group_idv_config_fail";
    String RCDC_RCO_BATCH_APPLY_GROUP_VOI_CONFIG_FAIL_LOG = "rcdc_rco_batch_apply_group_voi_config_fail";
    String RCDC_RCO_BATCH_APPLY_GROUP_IDV_CONFIG_FAIL_DESC = "rcdc_rco_batch_apply_group_idv_config_fail_desc";
    String RCDC_RCO_BATCH_APPLY_GROUP_VOI_CONFIG_FAIL_DESC = "rcdc_rco_batch_apply_group_voi_config_fail_desc";
    String RCDC_RCO_IMPORT_USER_FAIL = "rcdc_rco_import_user_fail";
    String RCDC_RCO_IMPORT_USER_GROUP_FAIL = "rcdc_rco_import_user_group_fail";
    String RCDC_RCO_IMPORT_USER_BATCH_TASK_SUCCESS = "rcdc_rco_import_user_batch_task_success";
    String RCDC_RCO_IMPORT_USER_NOT_SUPPORT_DESKTOP_TYPE = "23200949";
    String RCDC_RCO_IMPORT_USER_NOT_FOUND_USER_INFO = "23200950";
    String RCDC_RCO_USER_IMPORT_ACCOUNTPASSWORDCERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_accountpasswordcetification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_ACCOUNTPASSWORDCERTIFICATION_ILLEGAL = "rcdc_rco_user_import_accountpasswordcetification_illegal";
    String RCDC_RCO_USER_IMPORT_HARDWARECERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_openhardwarecertification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_HARDWARECERTIFICATION_ILLEGAL = "rcdc_rco_user_import_openhardwarecertification_illegal";
    String RCDC_RCO_USER_IMPORT_MAX_HARDWARE_NUM_ILLEGAL = "rcdc_rco_user_import_max_hardware_num_illegal";
    String RCDC_RCO_USER_IMPORT_MAX_HARDWARE_NUM_MUST_NOT_NULL = "rcdc_rco_user_import_max_hardware_num_must_not_null";
    String RCDC_RCO_USER_IMPORT_OPTCERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_openotpcertification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_OPENRADIUSCERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_openradiuscertification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_OPTCERTIFICATION_ILLEGAL = "rcdc_rco_user_import_openotpcertification_illegal";
    String RCDC_RCO_USER_IMPORT_OPENRADIUSERTIFICATION_ILLEGAL = "rcdc_rco_user_import_openradiusertification_illegal";
    String RCDC_RCO_USER_IMPORT_CASCERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_opencascertification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_CASCERTIFICATION_ILLEGAL = "rcdc_rco_user_import_opencascertification_illegal";
    String RCDC_RCO_USER_IMPORT_SMS_CERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_sms_certification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_SMS_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_sms_certification_illegal";
    String RCDC_RCO_USER_IMPORT_BINDOPT_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_bindotp_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_BINDOPT_ILLEGAL = "rcdc_rco_user_import_bindotp_illegal";
    String RCDC_RCO_USER_IMPORT_OPT_RADIUS_CANT_OPEN_MEANWHILE = "23200934";
    String RCDC_RCO_USER_IMPORT_ACCOUNT_PASSWORD_CERTIFICATION_NOT_ALLOW_EMPTY =
            "rcdc_rco_user_import_account_password_certification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_ACCOUNT_PASSWORD_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_account_password_certification_illegal";
    String RCDC_RCO_USER_IMPORT_WORK_WEIXIN_CERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_work_weixin_certification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_WORK_WEIXIN_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_work_weixin_certification_illegal";
    String RCDC_RCO_USER_IMPORT_FEISHU_CERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_feishu_certification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_FEISHU_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_feishu_certification_illegal";
    String RCDC_RCO_USER_IMPORT_DINGDING_CERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_dingding_certification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_DINGDING_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_dingding_certification_illegal";
    String RCDC_RCO_USER_IMPORT_OAUTH2_CERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_oauth2_certification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_OAUTH2_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_oauth2_certification_illegal";
    String RCDC_RCO_USER_IMPORT_RJCLIENT_CERTIFICATION_NOT_ALLOW_EMPTY = "rcdc_rco_user_import_rjclient_certification_not_allow_empty";
    String RCDC_RCO_USER_IMPORT_RJCLIENT_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_rjclient_certification_illegal";
    String RCDC_RCO_USER_IMPORT_MAIN_CERTIFICATION_ILLEGAL = "rcdc_rco_user_import_main_certification_illegal";

    String RCDC_RCO_USER_IMPORT_USER_ANALYSIS_FAIL = "23201656";

    String RCDC_RCO_DELETE_USER_GROUP_SUCCESS_LOG = "rcdc_rco_delete_user_group_success_log";
    String RCDC_RCO_DELETE_USER_GROUP_FAIL_LOG = "rcdc_rco_delete_user_group_fail_log";
    String RCDC_RCO_USER_GROUP_DELETE_ITEM_NAME = "rcdc_rco_user_group_delete_item_name";
    String RCDC_RCO_USER_GROUP_DELETE_TASK_NAME = "rcdc_rco_user_group_delete_task_name";
    String RCDC_RCO_USER_GROUP_DELETE_TASK_DESC = "rcdc_rco_user_group_delete_task_desc";
    String RCDC_RCO_USER_GROUP_DELETE_RESULT = "rcdc_rco_user_group_delete_result";
    String RCDC_RCO_USER_GROUP_DELETE_SUCCESS_RESULT = "rcdc_rco_user_group_delete_success_result";
    String RCDC_RCO_USER_GROUP_DELETE_FAIL_RESULT = "rcdc_rco_user_group_delete_fail_result";

    String RCDC_RCO_CREATE_USER_GROUP_SUCCESS_LOG = "rcdc_rco_create_user_group_success_log";
    String RCDC_RCO_CREATE_USER_GROUP_FAIL_LOG = "rcdc_rco_create_user_group_fail_log";
    String RCDC_RCO_RESERVED_GROUP_NAME_UNGROUPED = "rcdc_rco_reserved_group_name_ungrouped";
    String RCDC_RCO_RESERVED_GROUP_NAME_OVERVIEW = "rcdc_rco_reserved_group_name_overview";
    String RCDC_RCO_RESERVED_AD_GROUP_NAME = "rcdc_rco_reserved_ad_group_name";
    String RCDC_RCO_RESERVED_LDAP_GROUP_NAME = "rcdc_rco_reserved_ldap_group_name";
    String RCDC_RCO_EDIT_USER_GROUP_SUCCESS_LOG = "rcdc_rco_edit_user_group_success_log";
    String RCDC_RCO_EDIT_USER_GROUP_FAIL_LOG = "rcdc_rco_edit_user_group_fail_log";
    String RCDC_RCO_USER_HAS_LOGIN_NOT_ALLOW_DELETE = "23200957";
    String RCDC_RCO_USER_CREATING_DESKTOP_NOT_ALLOW_DELETE = "23200961";
    String RCDC_RCO_USER_IS_AD_USER_NOT_ALLOW_DELETE = "23200962";
    String RCDC_RCO_USER_IS_MAPPING_AD_USER_NOT_ALLOW_DELETE = "23200964";
    String RCDC_RCO_USER_IS_ADMIN_NOT_ALLOW_DELETE = "23200965";
    String RCDC_RCO_USER_GROUP_IMPORT_INCOMPLETE_CONFIG_DESKTOP = "rcdc_rco_user_group_import_incomplete_config_desktop";
    String RCDC_RCO_IMPORT_USER_GROUP_BATCH_TASK_SUCCESS = "rcdc_rco_import_user_group_batch_task_success";
    String RCDC_RCO_IMPORT_USER_GROUP_TEMPLATE_IS_INVALIDATE = "23200966";
    String RCDC_RCO_IMPORT_USER_TEMPLATE_IS_INVALIDATE = "23200967";

    String RCDC_RCO_USER_CONFIG_VDI_DESKTOP_NUM_NOT_RIGHT = "23200933";

    String RCDC_RCO_INVALID_IP = "23200939";
    String RCDC_RCO_INVALID_EMAIL = "23200940";
    String RCDC_RCO_SERVER_ADDRESS_NOT_BLANK = "23200942";
    String RCDC_RCO_FROM_MAIL_ACCOUNT_NOT_BLANK = "23200943";
    String RCDC_RCO_INVALID_PHONE = "23200941";
    String RCDC_RCO_USER_DELETE_ITEM_NAME = "rcdc_rco_user_delete_item_name";
    String RCDC_RCO_USER_DELETE_TASK_NAME = "rcdc_rco_user_delete_task_name";
    String RCDC_RCO_USER_DELETE_TASK_DESC = "rcdc_rco_user_delete_task_desc";
    String RCDC_RCO_USER_DELETE_SINGLE_TASK_NAME = "rcdc_rco_user_delete_single_task_name";
    String RCDC_RCO_USER_DELETE_SINGLE_TASK_DESC = "rcdc_rco_user_delete_single_task_desc";
    String RCDC_RCO_USER_DELETE_FAIL_LOG = "rcdc_rco_user_delete_fail_log";
    String RCDC_RCO_USER_DELETE_SUCCESS_LOG = "rcdc_rco_user_delete_success_log";
    String RCDC_RCO_USER_DELETE_RESULT_SUCCESS = "rcdc_rco_user_delete_result_success";
    String RCDC_RCO_USER_DELETE_RESULT_FAIL = "rcdc_rco_user_delete_result_fail";
    String RCDC_RCO_USER_DELETE_RESULT = "rcdc_rco_user_delete_success_result";
    String RCDC_RCO_USER_DELETE_SINGLE_SUCCESS_RESULT = "rcdc_rco_user_delete_single_success_result";
    String RCDC_RCO_USER_DELETE_SINGLE_FAIL_RESULT = "rcdc_rco_user_delete_single_fail_result";
    String RCDC_RCO_USER_DELETE_ERROR_DESK_NOT_CLOSE = "23200945";
    String RCDC_RCO_USER_DELETE_ERROR_EXIST_DESK_SESSION = "23201518";
    String RCDC_RCO_USER_RESET_PASSWORD_RESULT = "rcdc_rco_user_reset_password_result";
    String RCDC_RCO_USER_RESET_PASSWORD_SUCCESS_RESULT = "rcdc_rco_user_reset_password_success_result";
    String RCDC_RCO_USER_RESET_PASSWORD_FAIL_RESULT = "rcdc_rco_user_reset_password_fail_result";
    String RCDC_RCO_USER_RESET_PASSWORD_SUCCESS_LOG = "rcdc_rco_user_reset_password_success_log";
    String RCDC_RCO_USER_RESET_PASSWORD_FAIL_LOG = "rcdc_rco_user_reset_password_fail_log";
    String RCDC_RCO_USER_RESET_PASSWORD_UPDATE_FAIL_LOG = "23200946";
    String RCDC_RCO_USER_RESET_PASSWORD_BUT_SEND_EMAIL_FAIL_LOG = "rcdc_rco_user_reset_password_but_send_email_fail_log";
    String RCDC_RCO_USER_SEND_EMAIL_FAIL_LOG = "rcdc_rco_user_send_email_fail_log";
    String RCDC_RCO_USER_RESET_PASSWORD_TASK_NAME = "rcdc_rco_user_reset_password_task_name";
    String RCDC_RCO_USER_RESET_PASSWORD_TASK_DESC = "rcdc_rco_user_reset_password_task_desc";
    String RCDC_RCO_USER_IS_ADMIN_NOT_ALLOW_RESET = "23200947";
    String RCDC_RCO_USER_IS_THIRD_PARTY_NOT_ALLOW_RESET = "23200948";
    String RCDC_RCO_APPLY_DESKTOP_BATCH_RESULT = "rcdc_rco_apply_desktop_batch_result";
    String RCDC_RCO_APPLY_IDV_DESKTOP_BATCH_RESULT = "rcdc_rco_apply_idv_desktop_batch_result";
    String RCDC_RCO_APPLY_VOI_DESKTOP_BATCH_RESULT = "rcdc_rco_apply_voi_desktop_batch_result";
    String RCDC_RCO_APPLY_DESKTOP_IGNORE = "rcdc_rco_apply_desktop_ignore";
    String RCDC_RCO_APPLY_IDV_DESKTOP_IGNORE = "rcdc_rco_apply_idv_desktop_ignore";
    String RCDC_RCO_APPLY_VOI_DESKTOP_IGNORE = "rcdc_rco_apply_voi_desktop_ignore";
    String RCDC_RCO_APPLY_DESKTOP_RESULT_SUCCESS = "rcdc_rco_apply_desktop_result_success";
    String RCDC_RCO_APPLY_IDV_DESKTOP_RESULT_SUCCESS = "rcdc_rco_apply_idv_desktop_result_success";
    String RCDC_RCO_APPLY_VOI_DESKTOP_RESULT_SUCCESS = "rcdc_rco_apply_voi_desktop_result_success";
    String RCDC_RCO_APPLY_DESKTOP_RESULT_FAIL = "rcdc_rco_apply_desktop_result_fail";
    String RCDC_RCO_APPLY_DESKTOP_TASK_NAME = "rcdc_rco_apply_desktop_task_name";
    String RCDC_RCO_APPLY_IDV_DESKTOP_TASK_NAME = "rcdc_rco_apply_idv_desktop_task_name";
    String RCDC_RCO_APPLY_VOI_DESKTOP_TASK_NAME = "rcdc_rco_apply_voi_desktop_task_name";
    String RCDC_RCO_APPLY_DESKTOP_TASK_DESC = "rcdc_rco_apply_desktop_task_desc";
    String RCDC_RCO_APPLY_DESKTOP_SUCCESS_LOG = "rcdc_rco_apply_desktop_success_log";
    String RCDC_RCO_APPLY_DESKTOP_FAIL_LOG = "rcdc_rco_apply_desktop_fail_log";
    String RCDC_RCO_USER_GROUP_NOT_CONFIG_VDI_DESKTOP = "23201113";
    String RCDC_RCO_USER_GROUP_NOT_CONFIG_IDV_DESKTOP = "23201114";
    String RCDC_RCO_USER_GROUP_NOT_CONFIG_VOI_DESKTOP = "23201115";
    String RCDC_RCO_EDIT_USER_SUCCESS_LOG = "rcdc_rco_edit_user_success_log";
    String RCDC_RCO_EDIT_USER_FAIL_LOG = "rcdc_rco_edit_user_fail_log";
    String RCDC_RCO_CREATE_USER_SUCCESS_LOG = "rcdc_rco_create_user_success_log";
    String RCDC_RCO_CREATE_USER_FAIL_LOG = "rcdc_rco_create_user_fail_log";
    String RCDC_RCO_CREATE_USER_NAME_CONTAIN_DEFAULT = "23200951";
    String RCDC_RCO_CREATE_USER_PASSWORD_NOT_EQUAL_CONFIRM_PWD = "23200952";
    String RCDC_RCO_CREATE_USER_PASSWORD_NOT_EFFECT = "23200953";
    String RCDC_RCO_CREATE_USER_IMAGE_ERROR = "23200954";
    String RCDC_RCO_CREATE_USER_STRATEGY_ERROR = "23200955";
    String RCDC_RCO_CREATE_USER_CONFIG_SYSTEM_SIZE_ERROR = "23200956";
    String RCDC_RCO_USER_GROUP_NOT_EXIST_NEED_CONFIG_DESKTOP = "23200958";
    String RCDC_RCO_USER_GROUP_NOT_EXIST_NEED_CONFIG_IDV_DESKTOP = "23200959";
    String RCDC_RCO_USER_GROUP_NOT_EXIST_NEED_CONFIG_VOI_DESKTOP = "23200960";
    String RCDC_RCO_USER_UPDATE_USER_GROUP_SUCCESS_LOG = "rcdc_rco_user_update_user_group_success_log";
    String RCDC_RCO_USER_UPDATE_USER_GROUP_FAIL_LOG = "rcdc_rco_user_update_user_group_fail_log";
    String RCDC_RCO_USER_UPDATE_USER_GROUP_RESULT = "rcdc_rco_user_update_user_group_result";
    String RCDC_RCO_USER_UPDATE_AUTH_SINGLE_SUCCESS_RESULT = "rcdc_rco_user_update_auth_single_success_result";
    String RCDC_RCO_USER_UPDATE_AUTH_SINGLE_FAIL_RESULT = "rcdc_rco_user_update_auth_single_fail_result";
    String RCDC_RCO_USER_UPDATE_AUTH_SINGLE_TASK_NAME = "rcdc_rco_user_update_auth_single_task_name";
    String RCDC_RCO_USER_UPDATE_AUTH_SINGLE_TASK_DESC = "rcdc_rco_user_update_auth_single_task_desc";
    String RCDC_RCO_USER_CREATE_WITHOUT_LOGIN_IDENTITY_LEVEL = "23200968";
    String RCDC_RCO_USER_TYPE_NOT_ALLOW_CHANGE = "23200969";
    String RCDC_RCO_USER_NAME_NOT_ALLOW_CHANGE = "23200970";
    String RCDC_RCO_CREATE_USER_CONFIG_OS_TYPE_PERSONAL_STRATEGY_ERROR = "23200971";

    String RCDC_RCO_USER_GROUP_SEND_MESSAGE_ITEM_NAME = "rcdc_rco_user_group_send_message_item_name";
    String RCDC_RCO_USER_GROUP_SEND_MESSAGE_TASK_NAME = "rcdc_rco_user_group_send_message_task_name";
    String RCDC_RCO_USER_GROUP_SEND_MESSAGE_ITEM_DESC = "rcdc_rco_user_group_send_message_item_desc";
    String RCDC_RCO_USER_GROUP_SEND_MESSAGE_RESULT = "rcdc_rco_user_group_send_message_result";
    /**
     * 用户消息模块
     */
    String RCDC_RCO_USER_CREATE_USER_MESSAGE_SUCCESS_LOG = "rcdc_rco_user_create_user_message_success_log";
    String RCDC_RCO_USER_CREATE_USER_GROUP_MESSAGE_RESULT = "rcdc_rco_user_create_user_group_message_result";
    String RCDC_RCO_USER_CREATE_USER_MESSAGE_SUCCESS = "rcdc_rco_user_create_user_message_success";
    String RCDC_RCO_USER_CREATE_USER_MESSAGE_FAIL = "rcdc_rco_user_create_user_message_fail";
    String RCDC_RCO_USER_CREATE_USER_MESSAGE_FAIL_LOG = "rcdc_rco_user_create_user_message_fail_log";

    String RCDC_RCO_USER_DELETE_USER_MESSAGE_ITEM_NAME = "rcdc_rco_user_delete_user_message_item_name";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_TASK_NAME = "rcdc_rco_user_delete_user_message_task_name";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_ITEM_DESC = "rcdc_rco_user_delete_user_message_item_desc";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_RESULT = "rcdc_rco_user_batch_delete_user_message_result";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_RESULT_SUCCESS = "rcdc_rco_user_delete_user_message_result_success";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_RESULT_FAIL = "rcdc_rco_user_delete_user_message_result_fail";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_SUCCESS = "rcdc_rco_user_delete_user_message_success";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_FAIL = "rcdc_rco_user_delete_user_message_fail";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_SUCCESS_LOG = "rcdc_rco_user_delete_user_message_success_log";
    String RCDC_RCO_USER_DELETE_USER_MESSAGE_FAIL_LOG = "rcdc_rco_user_delete_user_message_fail_log";


    String RCDC_RCO_USER_MESSAGE_RELEASE_SUCCESS_LOG = "rcdc_rco_user_message_release_success_log";
    String RCDC_RCO_USER_MESSAGE_RELEASE_FAIL_LOG = "rcdc_rco_user_message_release_fail_log";

    /**
     * 云桌面管理模块
     */
    String RCDC_RCO_USER_USER_DEFAULT_USER_GROUP_NAME_1 = "rcdc_rco_user_default_user_group_name_1";
    String RCDC_RCO_USER_USER_DEFAULT_USER_GROUP_NAME_2 = "rcdc_rco_user_default_user_group_name_2";

    /**
     * 云桌面管理模块
     */
    // 1.云桌面创建
    String RCDC_RCO_DESKTOP_CREATE_SUC_LOG = "rcdc_rco_desktop_create_suc_log";
    String RCDC_RCO_DESKTOP_CREATE_FAIL_LOG = "rcdc_rco_desktop_create_fail_log";
    String RCDC_RCO_DESKTOP_CREATE_SINGLE_TASK_NAME = "rcdc_rco_desktop_create_single_task_name";
    String RCDC_RCO_DESKTOP_CREATE_SINGLE_TASK_DESC = "rcdc_rco_desktop_create_single_task_desc";
    String RCDC_RCO_DESKTOP_CREATE_SINGLE_SUC = "rcdc_rco_desktop_create_single_suc";
    String RCDC_RCO_DESKTOP_CREATE_SINGLE_FAIL = "rcdc_rco_desktop_create_single_fail";
    String RCDC_RCO_DESKTOP_CREATE_BATCH_TASK_NAME = "rcdc_rco_desktop_create_batch_task_name";
    String RCDC_RCO_DESKTOP_CREATE_BATCH_TASK_DESC = "rcdc_rco_desktop_create_batch_task_desc";
    String RCDC_RCO_DESKTOP_CREATE_BATCH_RESULT = "rcdc_rco_desktop_create_batch_result";
    String RCDC_RCO_DESKTOP_CREATE_ITEM_NAME = "rcdc_rco_desktop_create_item_name";
    String RCDC_RCO_DESKTOP_CREATE_ITEM_SUC_DESC = "rcdc_rco_desktop_create_item_suc_desc";
    String RCDC_RCO_DESKTOP_CREATE_ITEM_FAIL_DESC = "rcdc_rco_desktop_create_item_fail_desc";
    String RCDC_RCO_IMPORT_USER_SUCCESS_AND_CREATE_DESKTOP_FAIL = "rcdc_rco_import_user_success_and_create_desktop_fail";
    // 2.云桌面删除
    String RCDC_RCO_DESKTOP_DELETE_SUC_LOG = "rcdc_rco_desktop_delete_suc_log";
    String RCDC_RCO_DESKTOP_DELETE_FAIL_LOG = "rcdc_rco_desktop_delete_fail_log";
    String RCDC_RCO_DESKTOP_DELETE_SINGLE_TASK_NAME = "rcdc_rco_desktop_delete_single_task_name";
    String RCDC_RCO_DESKTOP_DELETE_SINGLE_TASK_DESC = "rcdc_rco_desktop_delete_single_task_desc";
    String RCDC_RCO_DESKTOP_DELETE_SINGLE_SUC = "rcdc_rco_desktop_delete_single_suc";
    String RCDC_RCO_DESKTOP_DELETE_SINGLE_FAIL = "rcdc_rco_desktop_delete_single_fail";
    String RCDC_RCO_DESKTOP_DELETE_BATCH_TASK_NAME = "rcdc_rco_desktop_delete_batch_task_name";
    String RCDC_RCO_DESKTOP_DELETE_BATCH_TASK_DESC = "rcdc_rco_desktop_delete_batch_task_desc";
    String RCDC_RCO_DESKTOP_DELETE_BATCH_RESULT = "rcdc_rco_desktop_delete_batch_result";
    String RCDC_RCO_DESKTOP_DELETE_ITEM_NAME = "rcdc_rco_desktop_delete_item_name";
    String RCDC_RCO_DESKTOP_DELETE_ITEM_SUC_DESC = "rcdc_rco_desktop_delete_item_suc_desc";
    String RCDC_RCO_DESKTOP_DELETE_ITEM_FAIL_DESC = "rcdc_rco_desktop_delete_item_fail_desc";
    // 3.还原云桌面
    String RCDC_RCO_DESKTOP_REVERT_SUC_LOG = "rcdc_rco_desktop_revert_suc_log";
    String RCDC_RCO_DESKTOP_REVERT_FAIL_LOG = "rcdc_rco_desktop_revert_fail_log";
    String RCDC_RCO_DESKTOP_REVERT_SUC_LOG_STORE_SYSTEM_DISK = "rcdc_rco_desktop_revert_suc_log_store_system_disk";
    String RCDC_RCO_DESKTOP_REVERT_FAIL_LOG_STORE_SYSTEM_DISK = "rcdc_rco_desktop_revert_fail_log_store_system_disk";
    String RCDC_RCO_DESKTOP_REVERT_SINGLE_TASK_NAME = "rcdc_rco_desktop_revert_single_task_name";
    String RCDC_RCO_DESKTOP_REVERT_SINGLE_TASK_DESC = "rcdc_rco_desktop_revert_single_task_desc";
    String RCDC_RCO_DESKTOP_REVERT_SINGLE_SUC = "rcdc_rco_desktop_revert_single_suc";
    String RCDC_RCO_DESKTOP_REVERT_SINGLE_FAIL = "rcdc_rco_desktop_revert_single_fail";
    String RCDC_RCO_DESKTOP_REVERT_BATCH_TASK_NAME = "rcdc_rco_desktop_revert_batch_task_name";
    String RCDC_RCO_DESKTOP_REVERT_BATCH_TASK_DESC = "rcdc_rco_desktop_revert_batch_task_desc";
    String RCDC_RCO_DESKTOP_REVERT_BATCH_RESULT = "rcdc_rco_desktop_revert_batch_result";
    String RCDC_RCO_DESKTOP_REVERT_ITEM_NAME = "rcdc_rco_desktop_revert_item_name";
    String RCDC_RCO_DESKTOP_REVERT_ITEM_SUC_DESC = "rcdc_rco_desktop_revert_item_suc_desc";
    String RCDC_RCO_DESKTOP_REVERT_ITEM_FAIL_DESC = "rcdc_rco_desktop_revert_item_fail_desc";
    String RCDC_RCO_DESKTOP_REVERT_DYNAMIC_POOL_NOT_SUPPORT = "rcdc_rco_desktop_revert_dynamic_pool_not_support";
    String RCDC_RCO_DESKTOP_REVERT_SUC_LOG_RESTORE_DATA = "rcdc_rco_desktop_revert_suc_log_restore_data";
    String RCDC_RCO_DESKTOP_REVERT_SUC_LOG_RESTORE_ONLY_SYSTEM = "rcdc_rco_desktop_revert_suc_log_restore_only_system";
    String RCDC_RCO_DESKTOP_REVERT_FAIL_LOG_RESTORE_DATA = "rcdc_rco_desktop_revert_fail_log_restore_data";
    String RCDC_RCO_DESKTOP_REVERT_FAIL_LOG_RESTORE_ONLY_SYSTEM = "rcdc_rco_desktop_revert_fail_log_restore_only_system";
    String RCDC_RCO_DESKTOP_REVERT_FAIL_THIRD_PARTY = "23201461";

    // 云桌面管理-维护模式
    String RCDC_DESKTOP_OPEN_MAINTENANCE_ITEM_SUCCESS_DESC = "rcdc_desktop_open_maintenance_item_success_desc";
    String RCDC_DESKTOP_CLOSE_MAINTENANCE_ITEM_SUCCESS_DESC = "rcdc_desktop_close_maintenance_item_success_desc";
    String RCDC_DESKTOP_MAINTENANCE_ITEM_BATCH_RESULT = "rcdc_desktop_maintenance_item_batch_result";
    String RCDC_DESKTOP_OPEN_MAINTENANCE_SINGLE_SUC = "rcdc_desktop_open_maintenance_single_suc";
    String RCDC_DESKTOP_OPEN_MAINTENANCE_ITEM_SUC = "rcdc_desktop_open_maintenance_item_suc";
    String RCDC_DESKTOP_CLOSE_MAINTENANCE_SINGLE_SUC = "rcdc_desktop_close_maintenance_single_suc";
    String RCDC_DESKTOP_CLOSE_MAINTENANCE_ITEM_SUC = "rcdc_desktop_close_maintenance_item_suc";
    String RCDC_DESKTOP_OPEN_MAINTENANCE_SINGLE_FAIL = "rcdc_desktop_open_maintenance_single_fail";
    String RCDC_DESKTOP_OPEN_MAINTENANCE_ITEM_FAIL = "rcdc_desktop_open_maintenance_item_fail";
    String RCDC_DESKTOP_CLOSE_MAINTENANCE_SINGLE_FAIL = "rcdc_desktop_close_maintenance_single_fail";
    String RCDC_DESKTOP_CLOSE_MAINTENANCE_ITEM_FAIL = "rcdc_desktop_close_maintenance_item_fail";
    String RCDC_DESKTOP_MAINTENANCE_ITEM_NAME = "rcdc_rco_desktop_maintenance_item_name";
    String RCDC_DESKTOP_OPEN_MAINTENANCE = "rcdc_desktop_open_maintenance";
    String RCDC_DESKTOP_OPEN_MAINTENANCE_TASK_DESC = "rcdc_desktop_open_maintenance_task_desc";
    String RCDC_DESKTOP_CLOSE_MAINTENANCE = "rcdc_desktop_close_maintenance";
    String RCDC_DESKTOP_CLOSE_MAINTENANCE_TASK_DESC = "rcdc_desktop_close_maintenance_task_desc";
    String RCDC_DESKTOP_MAINTENANCE_NOT_SUPPORT_IDV_OR_DESKTOPPOOL = "23201012";
    // 4.关闭云桌面
    String RCDC_RCO_DESKTOP_SHUTDOWN_SUC_LOG = "rcdc_rco_desktop_shutdown_suc_log";
    String RCDC_RCO_DESKTOP_SHUTDOWN_FAIL_LOG = "rcdc_rco_desktop_shutdown_fail_log";
    String RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_TASK_NAME = "rcdc_rco_desktop_shutdown_single_task_name";
    String RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_TASK_DESC = "rcdc_rco_desktop_shutdown_single_task_desc";
    String RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_SUC = "rcdc_rco_desktop_shutdown_single_suc";
    String RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_FAIL = "rcdc_rco_desktop_shutdown_single_fail";
    String RCDC_RCO_DESKTOP_SHUTDOWN_BATCH_TASK_NAME = "rcdc_rco_desktop_shutdown_batch_task_name";
    String RCDC_RCO_DESKTOP_SHUTDOWN_BATCH_TASK_DESC = "rcdc_rco_desktop_shutdown_batch_task_desc";
    String RCDC_RCO_DESKTOP_SHUTDOWN_BATCH_RESULT = "rcdc_rco_desktop_shutdown_batch_result";
    String RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_NAME = "rcdc_rco_desktop_shutdown_item_name";
    String RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_SUC_DESC = "rcdc_rco_desktop_shutdown_item_suc_desc";
    String RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_FAIL_DESC = "rcdc_rco_desktop_shutdown_item_fail_desc";
    // 5.强制关闭云桌面
    String RCDC_RCO_DESKTOP_POWER_OFF_SUC_LOG = "rcdc_rco_desktop_power_off_suc_log";
    String RCDC_RCO_DESKTOP_POWER_OFF_FAIL_LOG = "rcdc_rco_desktop_power_off_fail_log";
    String RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_TASK_NAME = "rcdc_rco_desktop_power_off_single_task_name";
    String RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_TASK_DESC = "rcdc_rco_desktop_power_off_single_task_desc";
    String RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_SUC = "rcdc_rco_desktop_power_off_single_suc";
    String RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_FAIL = "rcdc_rco_desktop_power_off_single_fail";
    String RCDC_RCO_DESKTOP_POWER_OFF_BATCH_TASK_NAME = "rcdc_rco_desktop_power_off_batch_task_name";
    String RCDC_RCO_DESKTOP_POWER_OFF_BATCH_TASK_DESC = "rcdc_rco_desktop_power_off_batch_task_desc";
    String RCDC_RCO_DESKTOP_POWER_OFF_BATCH_RESULT = "rcdc_rco_desktop_power_off_batch_result";
    String RCDC_RCO_DESKTOP_POWER_OFF_ITEM_NAME = "rcdc_rco_desktop_power_off_item_name";
    String RCDC_RCO_DESKTOP_POWER_OFF_ITEM_SUC_DESC = "rcdc_rco_desktop_power_off_item_suc_desc";
    String RCDC_RCO_DESKTOP_POWER_OFF_ITEM_FAIL_DESC = "rcdc_rco_desktop_power_off_item_fail_desc";
    String RCDC_RCO_DESKTOP_POWER_OFF_FAIL_THIRD_PARTY = "23201462";

    String RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_NAME = "rcdc_rco_desktop_update_spec_item_name";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_SINGLE_TASK_NAME = "rcdc_rco_desktop_update_spec_single_task_name";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_SINGLE_TASK_DESC = "rcdc_rco_desktop_update_spec_single_task_desc";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_TASK_NAME = "rcdc_rco_desktop_update_spec_batch_task_name";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_TASK_DESC = "rcdc_rco_desktop_update_spec_batch_task_desc";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_SUC_LOG = "rcdc_rco_desktop_update_spec_suc_log";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_NOT_CLOSE_SUC_LOG = "rcdc_rco_desktop_update_spec_not_close_suc_log";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_SUC_DESC = "rcdc_rco_desktop_update_spec_item_suc_desc";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_update_spec_single_result_fail";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_FAIL_LOG = "rcdc_rco_desktop_update_spec_fail_log";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_RESULT = "rcdc_rco_desktop_update_spec_batch_result";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_FAIL_DESC = "rcdc_rco_desktop_update_spec_item_fail_desc";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_NO_CHANGE_LOG = "rcdc_rco_desktop_update_spec_no_change_log";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_ENABLE_CUSTOM = "rcdc_rco_desktop_update_spec_desk_enable_custom";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_STATIC_POOL_NOT_CLOSE = "rcdc_rco_desktop_update_spec_desk_static_pool_not_close";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_DYNAMIC_POOL_NOT_SUPPORT = "23200912";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_STATE_NOT_SUPPORT = "23201539";
    String I18N_DESK_STATE_PREFIX = "rcdc-clouddesktop_desk_state_";
    String RCDC_RCO_DESKTOP_UPDATE_SPEC_NO_CHANGE_THIRD_PARTY = "23201465";

    /**
     * 变更云桌面镜像模板
     */
    String RCDC_RCO_DESKTOP_UPDATE_IMAGE_TEMPLATE_THIRD_PARTY = "23201466";

    /**
     * 快照制作
     */
    String RCDC_RCO_CREATE_DESKTOP_SNAPSHOT_THIRD_PARTY = "23201469";

    // 取消云桌面报障

    /**
     * 取消用户[{0}]的{2}[{1}]报障成功
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_SUCCESS = "rcdc_rco_desktop_relieve_fault_success";

    /**
     * 未找到报障信息
     */
    String RCDC_RCO_DESKTOP_FAULT_NULL = "23200913";

    /**
     * 取消用户[{0}]的{3}[{1}]报障失败，失败原因：{2}
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_FAIL = "rcdc_rco_desktop_relieve_fault_fail";

    /**
     * 批量取消云桌面报障，成功[{0}]条，失败[{1}]条
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_RESULT = "rcdc_rco_desktop_relieve_fault_result";

    /**
     * 取消用户[{0}]的云桌面[{1}]报障失败
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_FAIL_RESULT = "rcdc_rco_desktop_relieve_fault_fail_result";

    /**
     * 取消用户[{0}]的云桌面[{1}]报障成功
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_SUCCESS_RESULT = "rcdc_rco_desktop_relieve_fault_success_result";

    /**
     * 取消云桌面报障
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_TASK_NAME = "rcdc_rco_desktop_relieve_fault_task_name";

    /**
     * 正在取消云桌面报障
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_TASK_DESC = "rcdc_rco_desktop_relieve_fault_task_desc";

    /**
     * 正在取消用户[{0}]的{2}[{1}]报障
     */
    String RCDC_RCO_DESKTOP_RELIEVE_FAULT_ONE_TASK_DESC = "rcdc_rco_desktop_relieve_fault_one_task_desc";


    // 5.启动云桌面
    String RCDC_RCO_DESKTOP_START_SUC_LOG = "rcdc_rco_desktop_start_suc_log";
    String RCDC_RCO_DESKTOP_START_FAIL_LOG = "rcdc_rco_desktop_start_fail_log";
    String RCDC_RCO_DESKTOP_START_SINGLE_TASK_NAME = "rcdc_rco_desktop_start_single_task_name";
    String RCDC_RCO_DESKTOP_START_SINGLE_TASK_DESC = "rcdc_rco_desktop_start_single_task_desc";
    String RCDC_RCO_DESKTOP_START_SINGLE_SUC = "rcdc_rco_desktop_start_single_suc";
    String RCDC_RCO_DESKTOP_START_SINGLE_FAIL = "rcdc_rco_desktop_start_single_fail";
    String RCDC_RCO_DESKTOP_START_BATCH_TASK_NAME = "rcdc_rco_desktop_start_batch_task_name";
    String RCDC_RCO_DESKTOP_START_BATCH_TASK_DESC = "rcdc_rco_desktop_start_batch_task_desc";
    String RCDC_RCO_DESKTOP_START_BATCH_RESULT = "rcdc_rco_desktop_start_batch_result";
    String RCDC_RCO_DESKTOP_START_ITEM_NAME = "rcdc_rco_desktop_start_item_name";
    String RCDC_RCO_DESKTOP_START_ITEM_SUC_DESC = "rcdc_rco_desktop_start_item_suc_desc";
    String RCDC_RCO_DESKTOP_START_ITEM_FAIL_DESC = "rcdc_rco_desktop_start_item_fail_desc";

    // 强制唤醒
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SUC_LOG = "rcdc_rco_desktop_force_wake_up_suc_log";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_FAIL_LOG = "rcdc_rco_desktop_force_wake_up_fail_log";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_TASK_NAME = "rcdc_rco_desktop_force_wake_up_single_task_name";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_TASK_DESC = "rcdc_rco_desktop_force_wake_up_single_task_desc";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_FAIL = "rcdc_rco_desktop_force_wake_up_single_fail";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_SUC = "rcdc_rco_desktop_force_wake_up_single_suc";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_BATCH_TASK_NAME = "rcdc_rco_desktop_force_wake_up_batch_task_name";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_BATCH_TASK_DESC = "rcdc_rco_desktop_force_wake_up_batch_task_desc";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_BATCH_RESULT = "rcdc_rco_desktop_force_wake_up_batch_result";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_ITEM_NAME = "rcdc_rco_desktop_force_wake_up_item_name";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_ITEM_SUC_DESC = "rcdc_rco_desktop_force_wake_up_item_suc_desc";
    String RCDC_RCO_DESKTOP_FORCE_WAKE_UP_ITEM_FAIL_DESC = "rcdc_rco_desktop_force_wake_up_item_fail_desc";
    String RCDC_RCO_DESKTOP_WAKE_UP_FAIL_LOG = "rcdc_rco_desktop_wake_up_fail_log";
    String RCDC_RCO_DESKTOP_WAKE_UP_THIRD_PARTY = "23201474";


    // 重启云桌面
    String RCDC_RCO_DESKTOP_RESTART_SUC_LOG = "rcdc_rco_desktop_restart_suc_log";
    String RCDC_RCO_DESKTOP_RESTART_FAIL_LOG = "rcdc_rco_desktop_restart_fail_log";
    String RCDC_RCO_DESKTOP_RESTART_SINGLE_TASK_NAME = "rcdc_rco_desktop_restart_single_task_name";
    String RCDC_RCO_DESKTOP_RESTART_SINGLE_TASK_DESC = "rcdc_rco_desktop_restart_single_task_desc";
    String RCDC_RCO_DESKTOP_RESTART_ITEM_NAME = "rcdc_rco_desktop_restart_item_name";
    String RCDC_RCO_DESKTOP_RESTART_ITEM_SUC_DESC = "rcdc_rco_desktop_restart_item_suc_desc";
    String RCDC_RCO_DESKTOP_RESTART_ITEM_FAIL_DESC = "rcdc_rco_desktop_restart_item_fail_desc";
    String RCDC_RCO_DESKTOP_RESTART_SINGLE_SUC = "rcdc_rco_desktop_restart_single_suc";
    String RCDC_RCO_DESKTOP_RESTART_SINGLE_FAIL = "rcdc_rco_desktop_restart_single_fail";
    String RCDC_RCO_DESKTOP_RESTART_BATCH_TASK_NAME = "rcdc_rco_desktop_restart_batch_task_name";
    String RCDC_RCO_DESKTOP_RESTART_BATCH_TASK_DESC = "rcdc_rco_desktop_restart_batch_task_desc";
    String RCDC_RCO_DESKTOP_RESTART_BATCH_RESULT = "rcdc_rco_desktop_restart_batch_result";
    String RCDC_RCO_DESKTOP_RESTART_FAIL_NOT_SUPPORT_DESKTOP_TYPE_BATCH_TASK = "23201001";
    String RCDC_RCO_DESKTOP_RESTART_FAIL_NOT_SUPPORT_SLEEP_DESKTOP = "23201002";

    // 11.编辑云桌面的云桌面策略
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_ITEM_NAME = "rcdc_rco_desktop_edit_strategy_item_name";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_TASK_NAME = "rcdc_rco_desktop_edit_strategy_single_task_name";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_TASK_DESC = "rcdc_rco_desktop_edit_strategy_single_task_desc";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_TASK_NAME = "rcdc_rco_desktop_edit_strategy_batch_task_name";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_TASK_DESC = "rcdc_rco_desktop_edit_strategy_batch_task_desc";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_SUC_LOG = "rcdc_rco_desktop_edit_strategy_suc_log";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_FAIL_LOG = "rcdc_rco_desktop_edit_strategy_fail_log";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_ITEM_SUC_DESC = "rcdc_rco_desktop_edit_strategy_item_suc_desc";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_ITEM_FAIL_DESC = "rcdc_rco_desktop_edit_strategy_item_fail_desc";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_RESULT_SUC = "rcdc_rco_desktop_edit_strategy_single_result_suc";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_edit_strategy_single_result_fail";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_RESULT = "rcdc_rco_desktop_edit_strategy_batch_result";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_CONFLICT_WITH_UPM = "23200918";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT = "23200919";
    String RCDC_RCO_DESKTOP_EDIT_POOL_STRATEGY_SINGLE_RESULT_SUC = "rcdc_rco_desktop_edit_pool_strategy_single_result_suc";
    String RCDC_RCO_DESKTOP_EDIT_POOL_STRATEGY_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_edit_pool_strategy_single_result_fail";
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_CHECK_SESSION_FAIL = "rcdc_rco_desktop_edit_strategy_check_session_fail";

    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_ITEM_NAME = "rcdc_rco_desktop_refresh_strategy_item_name";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_BATCH_TASK_NAME = "rcdc_rco_desktop_refresh_strategy_batch_task_name";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_BATCH_TASK_DESC = "rcdc_rco_desktop_refresh_strategy_batch_task_desc";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_TASK_NAME = "rcdc_rco_desktop_refresh_strategy_single_task_name";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_TASK_DESC = "rcdc_rco_desktop_refresh_strategy_single_task_desc";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_ITEM_SUC_DESC = "rcdc_rco_desktop_refresh_strategy_item_suc_desc";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_ITEM_FAIL_DESC = "rcdc_rco_desktop_refresh_strategy_item_fail_desc";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_RESULT_SUC = "rcdc_rco_desktop_refresh_strategy_single_result_suc";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_refresh_strategy_single_result_fail";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SUC_LOG = "rcdc_rco_desktop_refresh_strategy_suc_log";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_FAIL_LOG = "rcdc_rco_desktop_refresh_strategy_fail_log";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_BATCH_RESULT = "rcdc_rco_desktop_refresh_strategy_batch_result";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SUC_NO_CHANGE_LOG = "rcdc_rco_desktop_refresh_strategy_suc_no_change_log";
    String RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SUC_CUSTOM_LOG = "rcdc_rco_desktop_refresh_strategy_suc_custom_log";
    String RCDC_RCO_ALONE_DESKTOP_APPLY_STRATEGY_SUC_LOG = "rcdc_rco_alone_desktop_apply_strategy_suc_log";

    // 12.编辑云桌面的网络策略
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_ITEM_NAME = "rcdc_rco_desktop_edit_network_item_name";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_TASK_NAME = "rcdc_rco_desktop_edit_network_single_task_name";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_TASK_DESC = "rcdc_rco_desktop_edit_network_single_task_desc";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_TASK_NAME = "rcdc_rco_desktop_edit_network_batch_task_name";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_TASK_DESC = "rcdc_rco_desktop_edit_network_batch_task_desc";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_SUC_LOG = "rcdc_rco_desktop_edit_network_suc_log";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_FAIL_LOG = "rcdc_rco_desktop_edit_network_fail_log";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_ITEM_SUC_DESC = "rcdc_rco_desktop_edit_network_item_suc_desc";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_ITEM_FAIL_DESC = "rcdc_rco_desktop_edit_network_item_fail_desc";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_RESULT_SUC = "rcdc_rco_desktop_edit_network_single_result_suc";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_edit_network_single_result_fail";
    String RCDC_RCO_DESKTOP_EDIT_POOL_NETWORK_SINGLE_RESULT_SUC = "rcdc_rco_desktop_edit_pool_network_single_result_suc";
    String RCDC_RCO_DESKTOP_EDIT_POOL_NETWORK_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_edit_pool_network_single_result_fail";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_RESULT = "rcdc_rco_desktop_edit_network_batch_result";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_DYNAMIC_POOL_NOT_SUPPORT = "23200914";
    String RCDC_RCO_DESKTOP_EDIT_NETWORK_THIRD_PARTY = "23201467";

    // 13.云桌面故障恢复
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_ITEM_NAME = "rcdc_rco_desktop_recover_from_error_item_name";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_TASK_NAME = "rcdc_rco_desktop_recover_from_error_single_task_name";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_TASK_DESC = "rcdc_rco_desktop_recover_from_error_single_task_desc";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_BATCH_TASK_NAME = "rcdc_rco_desktop_recover_from_error_batch_task_name";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_BATCH_TASK_DESC = "rcdc_rco_desktop_recover_from_error_batch_task_desc";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SUC_LOG = "rcdc_rco_desktop_recover_from_error_suc_log";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_FAIL_LOG = "rcdc_rco_desktop_recover_from_error_fail_log";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_ITEM_SUC_DESC = "rcdc_rco_desktop_recover_from_error_item_suc_desc";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_ITEM_FAIL_DESC = "rcdc_rco_desktop_recover_from_error_item_fail_desc";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_RESULT_SUC = "rcdc_rco_desktop_recover_from_error_single_result_suc";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_recover_from_error_single_result_fail";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_BATCH_RESULT = "rcdc_rco_desktop_recover_from_error_batch_result";
    String RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_THIRD_PARTY = "23201472";
    String RCDC_RCO_AD_SAVE_MAPPING_TASK_NAME = "rcdc_rco_ad_save_mapping_task_name";
    String RCDC_RCO_AD_SAVE_MAPPING_TASK_DESC = "rcdc_rco_ad_save_mapping_task_desc";
    String RCDC_RCO_AD_SAVE_MAPPING_SUCCESS = "rcdc_rco_ad_save_mapping_success";
    String RCDC_RCO_AD_SAVE_MAPPING_FAIL = "rcdc_rco_ad_save_mapping_fail";
    String RCDC_RCO_LDAP_SAVE_MAPPING_TASK_NAME = "rcdc_rco_ldap_save_mapping_task_name";
    String RCDC_RCO_LDAP_SAVE_MAPPING_TASK_DESC = "rcdc_rco_ldap_save_mapping_task_desc";
    String RCDC_RCO_LDAP_SAVE_MAPPING_SUCCESS = "rcdc_rco_ldap_save_mapping_success";
    String RCDC_RCO_LDAP_SAVE_MAPPING_FAIL = "rcdc_rco_ldap_save_mapping_fail";
    String RCDC_RCO_LDAP_SAVE_MAPPING_FAIL_RESULT = "rcdc_rco_ldap_save_mapping_result";
    String RCDC_RCO_AD_SAVE_MAPPING_FAIL_RESULT = "rcdc_rco_ad_save_mapping_result";
    String RCDC_RCO_AD_NOT_ALLOW_CHANGE_GROUP = "23200973";

    // 14.云桌面远程协助
    String RCDC_RCO_DESKTOP_ASSIST_REQUEST_SUC_LOG = "rcdc_rco_desktop_assist_request_suc_log";
    String RCDC_RCO_DESKTOP_ASSIST_REQUEST_FAIL_LOG = "rcdc_rco_desktop_assist_request_fail_log";
    String RCDC_RCO_DESKTOP_ASSIST_STOP_SUC_LOG = "rcdc_rco_desktop_assist_stop_suc_log";
    String RCDC_RCO_DESKTOP_ASSIST_STOP_FAIL_LOG = "rcdc_rco_desktop_assist_stop_fail_log";
    String RCDC_RCO_MULTI_DESKTOP_ASSIST = "23201485";
    String RCDC_RCO_MULTI_RCA_HOST_ASSIST = "rcdc_rco_multi_rca_host_assist";
    // 15.云桌面级别编辑
    String RCDC_RCO_DESKTOP_EDIT_ROLE_SUCCESS_LOG = "rcdc_rco_desktop_edit_role_success_log";
    String RCDC_RCO_DESKTOP_EDIT_ROLE_FAIL_LOG = "rcdc_rco_desktop_edit_role_fail_log";
    String RCDC_RCO_DESKTOP_EDIT_VIP_ROLE_TEXT = "rcdc_rco_desktop_edit_vip_role_text";
    String RCDC_RCO_DESKTOP_EDIT_NORMAL_ROLE_TEXT = "rcdc_rco_desktop_edit_normal_role_text";
    String RCDC_RCO_DESKTOP_EDIT_VIP_ROLE_TEXT_THIRD_PARTY = "23201468";
    String RCDC_RCO_DESKTOP_ROLE_DYNAMIC_POOL_NOT_SUPPORT = "23200921";

    String RCDC_RCO_DESKTOP_EDIT_NETWORK_IP_INVALID = "rcdc_rco_desktop_edit_network_ip_invalid";
    String RCDC_RCO_DESKTOP_NOT_ID_TO_FIND_IP_POOL = "rcdc_rco_desktop_not_id_to_find_ip_pool";
    String RCDC_RCO_VISITOR_NOT_ALLOW_EDIT_STRATEGY = "rcdc_rco_visitor_not_allow_edit_strategy";
    String RCDC_RCO_VISITOR_NOT_ALLOW_EDIT_NETWORK = "23200920";

    /* 应用云桌面标签 */
    String RCDC_RCO_DESKTOP_EDIT_REMARK_SINGLE_TASK_NAME = "rcdc_edit_desktop_remark_single_task_name";
    String RCDC_RCO_DESKTOP_EDIT_REMARK_SINGLE_TASK_DESC = "rcdc_edit_desktop_remark_single_task_desc";
    String RCO_DESKTOP_EDIT_REMARK_ITEM_NAME = "rco_edit_desktop_remark_item_name";
    /* 批量应用云桌面标签 */
    String RCO_DESKTOP_EDIT_REMARK_BATCH_TASK_NAME = "rco_edit_desktop_remark_batch_task_name";
    /* 正在批量应用云桌面标签 */
    String RCO_DESKTOP_EDIT_REMARK_BATCH_TASK_DESC = "rco_edit_desktop_remark_batch_task_desc";
    /* 编辑云桌面标签 */
    String RCO_DESKTOP_EDIT_REMARK_NAME = "rco_edit_desktop_remark_name";
    /**
     * 编辑云桌面标签成功
     */
    String RCO_EDIT_DESKTOP_REMARK_SUCCESS_LOG = "rco_edit_desktop_remark_success_log";
    /**
     * 编辑云桌面标签失败
     */
    String RCO_EDIT_DESKTOP_REMARK_FAIL_LOG = "rco_edit_desktop_remark_fail_log";
    String RCO_EDIT_DESKTOP_REMARK_FAIL_LOG_DESC = "rco_edit_desktop_remark_fail_log_desc";
    String RCO_EDIT_DESKTOP_REMARK_FAIL_THIRD_PARTY = "23201464";
    /* 批量编辑云桌面标签结果 */
    String RCO_EDIT_DESKTOP_REMARK_BATCH_RESULT = "rco_edit_desktop_remark_batch_result";
    String RCDC_RCO_DESKTOP_EDIT_REMARK_DYNAMIC_POOL_NOT_SUPPORT = "23200922";
    // 16.云桌面屏幕水印
    String RCDC_RCO_DESKTOP_EDIT_WATERMARK_SUCCESS = "rcdc_rco_desktop_edit_watermark_success";
    String RCDC_RCO_DESKTOP_EDIT_WATERMARK_FAIL = "rcdc_rco_desktop_edit_watermark_fail";
    String RCDC_RCO_DESKTOP_WATERMARK_SEND_SUCCESS = "rcdc_rco_desktop_watermark_send_success";
    String RCDC_RCO_DESKTOP_WATERMARK_SEND_FAIL = "rcdc_rco_desktop_watermark_send_fail";
    // 17.云桌面导出
    String RCDC_RCO_DESKTOP_EXPORT_SUCCESS_LOG = "rcdc_rco_desktop_export_success_log";
    String RCDC_RCO_DESKTOP_EXPORT_FAIL_LOG = "rcdc_rco_desktop_export_fail_log";
    // 18.云桌面日志收集
    String RCDC_RCO_DESKTOP_COLLECT_LOG_SUCCESS = "rcdc_rco_desktop_collect_log_success";
    String RCDC_RCO_DESKTOP_COLLECT_LOG_ERROR = "rcdc_rco_desktop_collect_log_error";
    // 19.导入用户VDI云桌面
    String RCDC_RCO_IMPORT_VDI_DESK_TEMPLATE_NOT_ALLOW = "23200924";
    String RCDC_RCO_IMPORT_VDI_DESK_TEMPLATE_IS_INVALIDATE = "23200925";
    String RCDC_RCO_VDI_DESK_IMPORT_FAIL = "23200926";
    String RCDC_RCO_VDI_DESK_IMPORT_NOT_DESK_DATA = "23200927";
    String RCO_IMPORT_VDI_DESK_PARSE_VALIDATE_FAIL = "23200928";
    String RCO_IMPORT_VDI_DESK_FAIL = "23201654";
    String RCDC_USER_NOT_FOUND = "23200929";
    String RCDC_RCO_IMPORT_VDI_DESK_TASK_NAME = "rcdc_rco_import_vdi_desk_task_name";
    String RCDC_RCO_IMPORT_VDI_DESK_TASK_DESC = "rcdc_rco_import_vdi_desk_task_desc";
    String RCDC_RCO_IMPORT_VDI_DESK_ITEM_NAME = "rcdc_rco_import_vdi_desk_item_name";
    String RCDC_RCO_IMPORT_VDI_DESK_ITEM_SUC_DESC = "rcdc_rco_import_vdi_desk_item_suc_desc";
    String RCDC_RCO_IMPORT_VDI_DESK_ITEM_FAIL_DESC = "rcdc_rco_import_vdi_desk_item_fail_desc";
    String RCDC_RCO_IMPORT_VDI_DESK_SINGLE_SUC = "rcdc_rco_import_vdi_desk_single_suc";
    String RCDC_RCO_IMPORT_VDI_DESK_SINGLE_FAIL = "rcdc_rco_import_vdi_desk_single_fail";
    String RCDC_RCO_IMPORT_VDI_DESK_BATCH_RESULT = "rcdc_rco_import_vdi_desk_batch_result";
    String RCDC_RCO_IMPORT_VDI_DESK_SUC_LOG = "rcdc_rco_import_vdi_desk_suc_log";
    String RCDC_RCO_IMPORT_VDI_DESK_FAIL_LOG = "rcdc_rco_import_vdi_desk_fail_log";
    String RCDC_RCO_IMPORT_VDI_DESK_INCOMPLETE_CONFIG_DESKTOP = "rcdc_rco_import_vdi_desk_incomplete_config_desktop";
    String RCDC_RCO_IMPORT_VDI_DESK_IMAGE_TEMPLATE_NAME_INCORRECT = "rcdc_rco_import_vdi_desk_image_template_name_incorrect";
    String RCDC_RCO_IMPORT_VDI_DESK_IMAGE_TEMPLATE_NAME_NOT_ALLOW_EMPTY = "rcdc_rco_import_vdi_desk_image_template_name_not_allow_empty";

    String RCDC_RCO_IMPORT_VDI_DESK_IMAGE_TEMPLATE_NAME_TOO_LENGTH = "rcdc_rco_import_vdi_desk_image_template_name_too_length";
    String RCDC_RCO_IMPORT_VDI_DESK_STRATEGY_NAME_INCORRECT = "rcdc_rco_import_vdi_desk_strategy_name_incorrect";
    String RCDC_RCO_IMPORT_VDI_DESK_STRATEGY_NAME_NOT_ALLOW_EMPTY = "rcdc_rco_import_vdi_desk_strategy_name_not_allow_empty";

    String RCDC_RCO_IMPORT_VDI_DESK_NETWORK_NAME_INCORRECT = "rcdc_rco_import_vdi_desk_network_name_incorrect";
    String RCDC_RCO_IMPORT_VDI_DESK_STRATEGY_NAME_TOO_LENGTH = "rcdc_rco_import_vdi_desk_strategy_name_too_length";
    String RCDC_RCO_IMPORT_VDI_DESK_NETWORK_NAME_TOO_LENGTH = "rcdc_rco_import_vdi_desk_network_name_too_length";
    String RCDC_RCO_IMPORT_VDI_DESK_NETWORK_NAME_NOT_ALLOW_EMPTY = "rcdc_rco_import_vdi_desk_network_name_not_allow_empty";
    /**
     * 回收站模块
     */
    // 1.删除回收站云桌面
    String RCDC_RCO_RECYCLEBIN_DELETE_SUC_LOG = "rcdc_rco_recyclebin_delete_suc_log";
    String RCDC_RCO_RECYCLEBIN_DELETE_FAIL_LOG = "rcdc_rco_recyclebin_delete_fail_log";
    String RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_TASK_NAME = "rcdc_rco_recyclebin_delete_single_task_name";
    String RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_TASK_DESC = "rcdc_rco_recyclebin_delete_single_task_desc";
    String RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_SUC = "rcdc_rco_recyclebin_delete_single_suc";
    String RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_FAIL = "rcdc_rco_recyclebin_delete_single_fail";
    String RCDC_RCO_RECYCLEBIN_DELETE_BATCH_TASK_NAME = "rcdc_rco_recyclebin_delete_batch_task_name";
    String RCDC_RCO_RECYCLEBIN_DELETE_BATCH_TASK_DESC = "rcdc_rco_recyclebin_delete_batch_task_desc";
    String RCDC_RCO_RECYCLEBIN_DELETE_BATCH_RESULT = "rcdc_rco_recyclebin_delete_batch_result";
    String RCDC_RCO_RECYCLEBIN_DELETE_ITEM_NAME = "rcdc_rco_recyclebin_delete_item_name";
    String RCDC_RCO_RECYCLEBIN_DELETE_ITEM_SUC_DESC = "rcdc_rco_recyclebin_delete_item_suc_desc";
    String RCDC_RCO_RECYCLEBIN_DELETE_ITEM_FAIL_DESC = "rcdc_rco_recyclebin_delete_item_fail_desc";
    // 2.恢复回收站云桌面
    String RCDC_RCO_RECYCLEBIN_RECOVER_SUC_LOG = "rcdc_rco_recyclebin_recover_suc_log";
    String RCDC_RCO_RECYCLEBIN_RECOVER_FAIL_LOG = "rcdc_rco_recyclebin_recover_fail_log";
    String RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_TASK_NAME = "rcdc_rco_recyclebin_recover_single_task_name";
    String RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_TASK_DESC = "rcdc_rco_recyclebin_recover_single_task_desc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_SUC = "rcdc_rco_recyclebin_recover_single_suc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_FAIL = "rcdc_rco_recyclebin_recover_single_fail";
    String RCDC_RCO_RECYCLEBIN_RECOVER_BATCH_TASK_NAME = "rcdc_rco_recyclebin_recover_batch_task_name";
    String RCDC_RCO_RECYCLEBIN_RECOVER_BATCH_TASK_DESC = "rcdc_rco_recyclebin_recover_batch_task_desc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_BATCH_RESULT = "rcdc_rco_recyclebin_recover_batch_result";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ITEM_NAME = "rcdc_rco_recyclebin_recover_item_name";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ITEM_SUC_DESC = "rcdc_rco_recyclebin_recover_item_suc_desc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ITEM_FAIL_DESC = "rcdc_rco_recyclebin_recover_item_fail_desc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_EXIST_USER_ONE_MORE_DESK = "23200923";
    // 3.指定用户恢复回收站云桌面
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SUC_LOG = "rcdc_rco_recyclebin_recover_assign_user_suc_log";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_FAIL_LOG = "rcdc_rco_recyclebin_recover_assign_user_fail_log";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_TASK_NAME = "rcdc_rco_recyclebin_recover_assign_user_single_task_name";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_TASK_DESC = "rcdc_rco_recyclebin_recover_assign_user_single_task_desc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_SUC = "rcdc_rco_recyclebin_recover_assign_user_single_suc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_FAIL = "rcdc_rco_recyclebin_recover_assign_user_single_fail";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_NAME = "rcdc_rco_recyclebin_recover_assign_user_item_name";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_SUC_DESC = "rcdc_rco_recyclebin_recover_assign_user_item_suc_desc";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_FAIL_DESC = "rcdc_rco_recyclebin_recover_assign_user_item_fail_desc";
    // 4.清空回收站
    String RCDC_RCO_RECYCLEBIN_CLEAR_TASK_NAME = "rcdc_rco_recyclebin_clear_task_name";
    String RCDC_RCO_RECYCLEBIN_CLEAR_TASK_DESC = "rcdc_rco_recyclebin_clear_task_desc";
    String RCDC_RCO_RECYCLEBIN_CLEAR_RESULT = "rcdc_rco_recyclebin_clear_result";
    // 指定桌面池恢复
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_DESKTOP_POOL_ITEM_NAME = "rcdc_rco_recyclebin_recover_assign_desktop_pool_item_name";
    String RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_DESKTOP_POOL_TASK_DESC = "rcdc_rco_recyclebin_recover_assign_desktop_pool_task_desc";

    /**
     * 终端管理
     */
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_ITEM_NAME = "rcdc_rco_terminal_delete_terminal_item_name";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_TASK_NAME = "rcdc_rco_terminal_delete_terminal_task_name";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_TASK_DESC = "rcdc_rco_terminal_delete_terminal_task_desc";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_RESULT_SUCCESS = "rcdc_rco_terminal_delete_terminal_result_success";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_RESULT_FAIL = "rcdc_rco_terminal_delete_terminal_result_fail";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_SUCCESS_LOG = "rcdc_rco_terminal_delete_terminal_success_log";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_FAIL_LOG = "rcdc_rco_terminal_delete_terminal_fail_log";
    String RCDC_RCO_TERMINAL_BATCH_DELETE_TERMINAL_RESULT = "rcdc_rco_terminal_batch_delete_terminal_result";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_SUCCESS = "rcdc_rco_terminal_delete_terminal_success";
    String RCDC_RCO_TERMINAL_DELETE_TERMINAL_FAIL = "rcdc_rco_terminal_delete_terminal_fail";

    String RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_ITEM_NAME = "rcdc_rco_terminal_update_terminal_group_item_name";
    String RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_TASK_NAME = "rcdc_rco_terminal_update_terminal_group_task_name";
    String RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_TASK_DESC = "rcdc_rco_terminal_update_terminal_group_task_desc";
    String RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_terminal_update_terminal_group_success_log";
    String RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_terminal_update_terminal_group_fail_log";
    String RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_RESULT_SUCCESS = "rcdc_rco_terminal_update_terminal_group_result_success";
    String RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_RESULT_FAIL = "rcdc_rco_terminal_update_terminal_group_result_fail";
    String RCDC_RCO_TERMINAL_BATCH_UPDATE_TERMINAL_GROUP_RESULT = "rcdc_rco_terminal_batch_update_terminal_group_result";

    String RCDC_RCO_TERMINAL_EDIT_TERMINAL_SUCCESS_LOG = "rcdc_rco_terminal_edit_terminal_success_log";
    String RCDC_RCO_TERMINAL_EDIT_TERMINAL_FAIL_LOG = "rcdc_rco_terminal_edit_terminal_fail_log";

    /**
     * 终端分组管理
     */
    String RCDC_RCO_TERMINAL_GROUP_EDIT_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_terminal_group_edit_terminal_group_success_log";
    String RCDC_RCO_TERMINAL_GROUP_EDIT_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_terminal_group_edit_terminal_group_fail_log";
    String RCDC_RCO_TERMINAL_GROUP_ADD_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_terminal_group_add_terminal_group_fail_log";
    String RCDC_RCO_TERMINAL_GROUP_ADD_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_terminal_group_add_terminal_group_success_log";

    String RCDC_RCO_TERMINAL_GROUP_DELETE_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_terminal_group_delete_terminal_group_success_log";
    String RCDC_RCO_TERMINAL_GROUP_DELETE_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_terminal_group_delete_terminal_group_fail_log";
    String RCDC_DELETE_TERMINAL_GROUP_RESULT_SUCCESS = "rcdc_delete_terminal_group_result_success";
    String RCDC_DELETE_TERMINAL_GROUP_RESULT_FAIL = "rcdc_delete_terminal_group_result_fail";
    String RCDC_DELETE_TERMINAL_GROUP_SUCCESS = "rcdc_delete_terminal_group_success";
    String RCDC_DELETE_TERMINAL_GROUP_FAIL = "rcdc_delete_terminal_group_fail";
    String RCDC_RCO_TERMINAL_GROUP_DELETE_ITEM_NAME = "rcdc_rco_terminal_group_delete_item_name";
    String RCDC_RCO_TERMINAL_GROUP_DELETE_TASK_NAME = "rcdc_rco_terminal_group_delete_task_name";
    String RCDC_RCO_TERMINAL_GROUP_DELETE_TASK_DESC = "rcdc_rco_terminal_group_delete_task_desc";

    String RCDC_RCO_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_terminal_group_add_idv_terminal_group_success_log";
    String RCDC_RCO_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_terminal_group_add_idv_terminal_group_fail_log";
    String RCDC_RCO_TERMINAL_GROUP_EDIT_IDV_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_terminal_group_edit_idv_terminal_group_success_log";
    String RCDC_RCO_TERMINAL_GROUP_EDIT_IDV_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_terminal_group_edit_idv_terminal_group_fail_log";
    String RCDC_RCO_TERMINAL_GROUP_DELETE_IDV_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_terminal_group_delete_idv_terminal_group_success_log";
    String RCDC_RCO_TERMINAL_GROUP_DELETE_IDV_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_terminal_group_delete_idv_terminal_group_fail_log";
    /**
     * 远程协助
     */
    String RCDC_RCO_DESKTOP_REMOTE_ASSIST_FAIL = "rcdc_rco_desktop_remote_assist_fail";
    String RCDC_RCO_DESKTOP_STOP_REMOTE_ASSIST_FAIL = "rcdc_rco_desktop_stop_remote_assist_fail";
    String RCDC_RCO_IMAGE_FINISH_EDIT = "rcdc_rco_image_finish_edit";
    String RCDC_RCO_IMAGE_STATE_ERROR = "rcdc_rco_iamge_state_error";

    /**
     * AD域用户管理
     **/
    String RCDC_RCO_AD_SYNC_USER_SUCCESS = "rcdc_rco_ad_sync_user_success";
    String RCDC_RCO_AD_SYNC_SINGLE_USER_SUCCESS = "rcdc_rco_ad_sync_single_user_success";
    String RCDC_RCO_AD_SYNC_SINGLE_USER_FAIL = "rcdc_rco_ad_sync_single_user_fail";
    String RCDC_RCO_AD_SYNC_USER_FAIL = "rcdc_rco_ad_sync_user_fail";
    String RCDC_RCO_AD_SYNC_USER_TASK_NAME = "rcdc_rco_ad_sync_user_task_name";
    String RCDC_RCO_AD_SYNC_USER_TASK_DESC = "rcdc_rco_ad_sync_user_task_desc";
    String RCDC_RCO_AD_SAVE_CONFIG_SUCCESS = "rcdc_rco_ad_save_config_success";
    String RCDC_RCO_AD_SAVE_CONFIG_FAIL = "23200972";
    String RCDC_RCO_AD_TEST_CONNECTION_SUCCESS = "rcdc_rco_ad_test_connection_success";
    String RCDC_RCO_AD_TEST_CONNECTION_FAIL = "rcdc_rco_ad_test_connection_fail";
    String RCDC_RCO_AD_SYNC_USER_RESULT = "rcdc_rco_ad_sync_user_result";
    String RCDC_RCO_AD_DISABLE_SUCCESS = "rcdc_rco_ad_disable_success";
    String RCDC_RCO_AD_ENABLE_SUCCESS = "rcdc_rco_ad_enable_success";
    String RCDC_RCO_AD_ENABLE_FAIL = "rcdc_rco_ad_enable_fail";
    String RCDC_RCO_AD_DISABLE_FAIL = "rcdc_rco_ad_disable_fail";
    String RCDC_RCO_AD_SAVE_MAPPING_CONFIG_FAIL = "rcdc_rco_ad_save_mapping_config_fail";
    String RCDC_RCO_AD_SAVE_MAPPING_CONFIG_SUCCESS = "rcdc_rco_ad_save_mapping_config_success";
    String RCDC_RCO_AD_SYNC_USER_NONE_USER_EXCEPTION = "rcdc_rco_ad_sync_user_none_user_exception";
    String RCDC_RCO_AD_SYNC_USER_FAIL_BY_EXCEPTION = "rcdc_rco_ad_sync_user_fail_by_exception";
    String RCDC_RCO_AD_SYNC_USER_AD_DISABLE_EXCEPTION = "23200981";

    /**
     * AD域用户批量更新权限 成功
     **/
    String RCDC_RCO_EDIT_AD_USER_AUTHORITY_SUCCESS = "rcdc_rco_edit_ad_user_authority_success";

    /**
     * AD域用户批量更新权限 失败
     **/
    String RCDC_RCO_EDIT_AD_USER_AUTHORITY_FAIL = "rcdc_rco_edit_ad_user_authority_fail";

    /**
     * AD域用户批量更新权限 任务名称
     **/
    String RCDC_RCO_EDIT_AD_USER_AUTHORITY_TASK_NAME = "rcdc_rco_edit_ad_user_authority_task_name";

    /**
     * AD域用户批量更新权限 任务名称
     **/
    String RCDC_RCO_EDIT_AD_USER_AUTHORITY_BATCH_TASK_NAME = "rcdc_rco_edit_ad_user_authority_batch_task_name";

    /**
     * AD域用户批量更新权限 任务结果
     **/
    String RCDC_RCO_EDIT_AD_USER_AUTHORITY_RESULT = "rcdc_rco_edit_ad_user_authority_result";

    /**
     * AD域用户批量更新权限 任务明细
     **/
    String RCDC_RCO_EDIT_AD_USER_AUTHORITY_TASK_DESC = "rcdc_rco_edit_ad_user_authority_task_desc";


    /**
     * AD域用户组批量更新权限 成功
     **/
    String RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_SUCCESS = "rcdc_rco_edit_ad_user_group_authority_success";

    /**
     * AD域用户组批量更新权限 失败
     **/
    String RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_FAIL = "rcdc_rco_edit_ad_user_group_authority_fail";

    /**
     * AD域用户组批量更新权限 任务名称
     **/
    String RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_TASK_NAME = "rcdc_rco_edit_ad_user_group_authority_task_name";

    /**
     * AD域用户组批量更新权限 任务名称
     **/
    String RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_BATCH_TASK_NAME = "rcdc_rco_edit_ad_user_group_authority_batch_task_name";

    /**
     * AD域用户组批量更新权限 任务结果
     **/
    String RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_RESULT = "rcdc_rco_edit_ad_user_group_authority_result";

    /**
     * AD域用户组批量更新权限 任务明细
     **/
    String RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_TASK_DESC = "rcdc_rco_edit_ad_user_group_authority_task_desc";

    /**
     * 回收站定时任务配置
     **/
    String RCDC_RCO_RECYCLE_BIN_CONFIG_SUCCESS = "rcdc_rco_recycle_bin_config_success";
    String RCDC_RCO_RECYCLE_BIN_CONFIG_REQUEST_ERROR = "23200980";

    /**
     * 用户未分组下不允许再创建子分组
     **/
    String RCDC_RCO_USER_GROUP_CAN_NOT_CREATE_IN_DEFAULT = "23200982";

    String RCDC_RCO_USER_NORMAL_USER_REAL_NAME_MUST_NOT_BE_NULL = "23200983";

    /**
     * AD域的覆盖类型不能以LDAP为准
     **/
    String RCDC_RCO_AD_COVER_ERROR = "23200988";

    /**
     * LDAP的覆盖类型不能以AD为准
     **/
    String RCDC_RCO_LDAP_COVER_ERROR = "23200989";

    /**
     * AD域自动加域选项不能为空
     **/
    String RCDC_RCO_AD_AUTO_JOIN_NULL = "23200990";

    // LDAP
    String RCDC_RCO_LDAP_SAVE_CONFIG_SUCCESS = "rcdc_rco_ldap_save_config_success";
    String RCDC_RCO_LDAP_SAVE_CONFIG_FAIL = "rcdc_rco_ldap_save_config_fail";
    String RCDC_RCO_LDAP_TEST_CONNECTION_SUCCESS = "rcdc_rco_ldap_test_connection_success";
    String RCDC_RCO_LDAP_TEST_CONNECTION_FAIL = "rcdc_rco_ldap_test_connection_fail";
    String RCDC_RCO_LDAP_DISABLE_SUCCESS = "rcdc_rco_ldap_disable_success";
    String RCDC_RCO_LDAP_ENABLE_SUCCESS = "rcdc_rco_ldap_enable_success";
    String RCDC_RCO_LDAP_ENABLE_FAIL = "rcdc_rco_ldap_enable_fail";
    String RCDC_RCO_LDAP_DISABLE_FAIL = "rcdc_rco_ldap_disable_fail";
    String RCDC_RCO_LDAP_SAVE_MAPPING_CONFIG_FAIL = "rcdc_rco_ldap_save_mapping_config_fail";
    String RCDC_RCO_LDAP_SAVE_MAPPING_CONFIG_SUCCESS = "rcdc_rco_ldap_save_mapping_config_success";
    String RCDC_RCO_LDAP_SYNC_USER_FAIL_BY_EXCEPTION = "rcdc_rco_ldap_sync_user_fail_by_exception";
    String RCDC_RCO_LDAP_SYNC_USER_LDAP_DISABLE_EXCEPTION = "23200974";

    String RCDC_RCO_LDAP_ATTR_NAME_CAN_NOT_CONTAIN_KEY_WORD = "23200975";

    String RCDC_RCO_LDAP_NON_ANONYMOUS_USER_AND_PASSWORD_MUST_NOT_BE_NULLABLE = "23200976";
    String RCDC_RCO_LDAP_NOT_ALLOW_CHANGE_GROUP = "23200977";
    String RCDC_RCO_THIRD_PARTY_NOT_ALLOW_CHANGE_GROUP = "23200978";
    String RCDC_RCO_AD_MANAGER_PASSWORD_LENGTH = "23200979";

    /**
     * 系统管理员用户组异常信息添加
     */
    String RCDC_USER_SUB_USERGROUP_NUM_OVER_FOR_SYSADMIN = "rcdc_user_sub_usergroup_num_over_for_sysadmin";

    String RCDC_USER_USERGROUP_HAS_DUPLICATION_NAME_FOR_SYSADMIN = "rcdc_user_usergroup_has_duplication_name_for_sysadmin";

    String RCDC_USER_DELETE_USER_GROUP_SUB_GROUP_NAME_DUPLICATION_WITH_MOVE_SUB_GROUP_FOR_SYSADMIN =
            "rcdc_user_delete_user_group_sub_group_name_duplication_with_move_sub_group_for_sysadmin";

    String RCDC_USER_USERGROUP_NUM_OVER_FOR_SYSADMIN = "rcdc_user_usergroup_num_over_for_sysadmin";

    /**
     * 编辑用户身份验证 任务名称
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_TASK_NAME = "rcdc_rco_edit_user_login_identity_task_name";

    /**
     * 编辑用户身份验证 任务结果
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_RESULT = "rcdc_rco_edit_user_login_identity_result";

    /**
     * 编辑用户身份验证 成功
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SUCCESS = "rcdc_rco_edit_user_login_identity_success";

    /**
     * 编辑用户身份验证 失败
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_FAIL = "rcdc_rco_edit_user_login_identity_fail";

    /**
     * 编辑用户身份验证 批量任务名称
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_BATCH_TASK_NAME = "rcdc_rco_edit_user_login_identity_batch_task_name";

    /**
     * 编辑用户身份验证 任务明细
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_BATCH_TASK_DESC = "rcdc_rco_edit_user_login_identity_batch_task_desc";

    /**
     * 编辑用户身份验证 单个任务名称
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_TASK_NAME = "rcdc_rco_edit_user_login_identity_single_task_name";

    /**
     * 编辑用户身份验证 单个任务明细
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_TASK_DESC = "rcdc_rco_edit_user_login_identity_single_task_desc";

    /**
     * 编辑用户身份验证 单个任务成功
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_SUCCESS_RESULT = "rcdc_rco_edit_user_login_identity_single_success_result";

    /**
     * 编辑用户身份验证 单个任务失败
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_FAIL_RESULT = "rcdc_rco_edit_user_login_identity_single_fail_result";

    /**
     * 编辑用户身份验证 失败原因访客用户不允许变更
     **/
    String RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_FAIL_REASON_VISITOR = "23200992";

    /**
     * 终端未绑定云桌面，不能还原云桌面
     **/
    String RCDC_USER_TERMINAL_UNBIND_CLOUDDESKTOP = "rcdc_user_terminal_unbind_clouddesktop";

    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_FOUND = "23200505";
    /* 无法找到IDV云桌面策略 */
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_IDV_NOT_FOUND = "23200503";
    /* 无法找到VOI云桌面策略 */
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_VOI_NOT_FOUND = "23200504";

    /**
     * 云桌面策略系统盘小于镜像系统盘
     **/
    String RCDC_USER_IMPORT_GROUP_STRATEGY_SYSTEM_SIZE_TOO_SMALL = "23200579";

    /**
     * 镜像模板不可用
     **/
    String RCDC_USER_CLOUDDESKTOP_IMAGE_DISABLED = "23200580";

    /**
     * 云桌面镜像模板[{0}]没有可用镜像版本
     **/
    String RCDC_USER_CLOUDDESKTOP_IMAGE_DISABLED_VERSION = "23200581";

    /**
     * 镜像模板类型不匹配
     **/
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TYPE_UNMATCH = "23200582";
    String RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_NOT_FOUND = "23200507";
    /**
     * 镜像或版本为应用镜像
     **/
    String RCDC_USER_CLOUDDESKTOP_IMAGE_USAGE_APP = "23201903";
    /**
     * 多版本-版本对应的源镜像为应用镜像
     **/
    String RCDC_USER_CLOUDDESKTOP_IMAGE_ROOT_USAGE_APP = "23201904";
    /**
     * 多版本-镜像最新版本为应用镜像
     **/
    String RCDC_USER_CLOUDDESKTOP_IMAGE_NEWEST_VERSION_USAGE_APP = "23201905";

    /**
     * VDI云桌面策略不存在
     **/
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_VDI_NOT_FOUND = "23200502";
    String RCO_IMPORT_USER_PARSE_VALIDATE_FAIL = "23201100";

    /**
     * 重置MAC地址
     */
    String RCDC_RCO_ILLEGAL_MAC = "23200597";
    String RCDC_RCO_RESET_CLOUD_DESK_MAC_FAIL = "rcdc_rco_reset_cloud_desk_mac_fail";
    String RCDC_RCO_RESET_CLOUD_DESK_MAC_SUCCESS = "rcdc_rco_reset_cloud_desk_mac_success";
    String RCDC_RCO_RESET_CLOUD_DESK_MAC_THIRD_PARTY = "23201463";
    String RCDC_RCO_CLOUD_DESK_NOT_CLOSED = "23201117";
    String RCDC_RCO_RESET_MAC_MUST_NOT_MULTICAST = "23200598";
    String RCDC_RCO_RESET_MAC_MUST_NOT_ALL_ZERO = "23200599";

    /**
     * 编辑用户身份认证 批量任务名称
     **/
    String RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_TASK_NAME = "rcdc_rco_edit_user_or_group_identity_certification_task_name";
    String RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_TASK_DESC = "rcdc_rco_edit_user_or_group_identity_certification_task_desc";
    String RCDC_RCO_EDIT_USER_IDENTITY_CERTIFICATION_SUCCESS = "rcdc_rco_edit_user_identity_certification_success";
    String RCDC_RCO_EDIT_USER_IDENTITY_CERTIFICATION_FAIL = "rcdc_rco_edit_user_identity_certification_fail";
    String RCDC_RCO_EDIT_USER_GROUP_IDENTITY_CERTIFICATION_SUCCESS = "rcdc_rco_edit_user_group_identity_certification_success";
    String RCDC_RCO_EDIT_USER_GROUP_IDENTITY_CERTIFICATION_FAIL = "rcdc_rco_edit_user_group_identity_certification_fail";
    String RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_RESULT = "rcdc_rco_edit_user_or_group_identity_certification_result";
    String RCDC_USER_OR_GROUP_ONLY_ONE_BATCH_TASK_SYNCHRONIZE = "23200709";
    String RCDC_USER_OR_GROUP_CREATE_BATCH_TASK_FAIL = "rcdc_user_or_group_create_batch_task_fail";

    /**
     * 管理员没有终端的权限
     */
    String RCDC_RCO_ADMIN_NOT_HAS_TERMINAL_GROUP_PERMISSION_FOR_MANAGE = "23200717";

    /**
     * 管理员没有终端组下子组的权限
     */
    String RCDC_RCO_ADMIN_NOT_HAS_BELONG_TERMINAL_GROUP_PERMISSION_FOR_MANAGE = "23200718";
    String RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_PROMPT_SUCCESS = "rcdc_rco_edit_user_or_group_identity_certification_prompt_success";
    String RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_PROMPT_FAIL = "rcdc_rco_edit_user_or_group_identity_certification_prompt_fail";

    /**
     * 管理员没有用户组的权限
     */
    String RCDC_RCO_ADMIN_NOT_HAS_USER_GROUP_PERMISSION_FOR_MANAGE = "23200719";

    /**
     * 管理员没有用户组下子组的权限
     */
    String RCDC_RCO_ADMIN_NOT_HAS_BELONG_USER_GROUP_PERMISSION_FOR_MANAGE = "23200720";

    /**
     * 打开关闭维护模式
     */
    String RCDC_USER_VDI_OPEN_MAINTENANCE_MODEL_ITEM_NAME = "rcdc_user_vdi_open_maintenance_model_item_name";
    String RCDC_USER_VDI_CLOSE_MAINTENANCE_MODEL_ITEM_NAME = "rcdc_user_vdi_close_maintenance_model_item_name";
    String RCDC_RCO_DESKTOP_OPEN_MAINTENANCE_BATCH_TASK_NAME = "rcdc_rco_desktop_open_maintenance_batch_task_name";
    String RCDC_RCO_DESKTOP_CLOSE_MAINTENANCE_BATCH_TASK_NAME = "rcdc_rco_desktop_close_maintenance_batch_task_name";
    String RCDC_RCO_DESKTOP_OPEN_MAINTENANCE_BATCH_TASK_DESC = "rcdc_rco_desktop_open_maintenance_batch_task_desc";
    String RCDC_RCO_DESKTOP_CLOSE_MAINTENANCE_BATCH_TASK_DESC = "rcdc_rco_desktop_close_maintenance_batch_task_desc";
    String RCDC_RCO_DESKTOP_OPEN_MAINTENANCE_SUC_LOG = "rcdc_rco_desktop_open_maintenance_suc_log";
    String RCDC_RCO_DESKTOP_CLOSE_MAINTENANCE_SUC_LOG = "rcdc_rco_desktop_close_maintenance_suc_log";
    String RCDC_RCO_DESKTOP_OPEN_MAINTENANCE_FAIL_LOG = "rcdc_rco_desktop_open_maintenance_fail_log";
    String RCDC_RCO_DESKTOP_CLOSE_MAINTENANCE_FAIL_LOG = "rcdc_rco_desktop_close_maintenance_fail_log";
    String RCDC_RCO_DESKTOP_OPEN_MAINTENANCE_SINGLE_RESULT_SUC = "rcdc_rco_desktop_open_maintenance_single_result_suc";
    String RCDC_RCO_DESKTOP_CLOSE_MAINTENANCE_SINGLE_RESULT_SUC = "rcdc_rco_desktop_close_maintenance_single_result_suc";
    String RCDC_RCO_DESKTOP_OPEN_MAINTENANCE_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_open_maintenance_single_result_fail";
    String RCDC_RCO_DESKTOP_CLOSE_MAINTENANCE_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_close_maintenance_single_result_fail";
    String RCDC_RCO_DESKTOP_OPEN_MAINTENANCE_BATCH_RESULT = "rcdc_rco_desktop_open_maintenance_batch_result";
    String RCDC_RCO_DESKTOP_CLOSE_MAINTENANCE_BATCH_RESULT = "rcdc_rco_desktop_close_maintenance_batch_result";

    /**
     * 远程修改云桌面网络配置
     */
    String RCDC_RCO_DESKTOP_EDIT_DNS_ITEM_NAME = "rcdc_rco_desktop_edit_dns_item_name";
    String RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_TASK_NAME = "rcdc_rco_desktop_edit_dns_single_task_name";
    String RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_TASK_DESC = "rcdc_rco_desktop_edit_dns_single_task_desc";
    String RCDC_RCO_DESKTOP_EDIT_DNS_BATCH_TASK_NAME = "rcdc_rco_desktop_edit_dns_batch_task_name";
    String RCDC_RCO_DESKTOP_EDIT_DNS_BATCH_TASK_DESC = "rcdc_rco_desktop_edit_dns_batch_task_desc";
    String RCDC_RCO_DESKTOP_EDIT_DNS_SUC_LOG = "rcdc_rco_desktop_edit_dns_suc_log";
    String RCDC_RCO_DESKTOP_EDIT_DNS_SUC_DESC = "rcdc_rco_desktop_edit_dns_suc_desc";
    String RCDC_RCO_DESKTOP_EDIT_DNS_FAIL_LOG = "rcdc_rco_desktop_edit_dns_fail_log";
    String RCDC_RCO_DESKTOP_EDIT_DNS_FAIL_DESC = "rcdc_rco_desktop_edit_dns_fail_desc";
    String RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_RESULT_SUC = "rcdc_rco_desktop_edit_dns_single_result_suc";
    String RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_RESULT_FAIL = "rcdc_rco_desktop_edit_dns_single_result_fail";
    String RCDC_RCO_DESKTOP_EDIT_DNS_BATCH_RESULT = "rcdc_rco_desktop_edit_dns_batch_result";

    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_ITEM_NAME = "rcdc_rco_desktop_edit_idv_network_item_name";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_SUC_LOG = "rcdc_rco_desktop_edit_idv_network_suc_log";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_SUC_DESC = "rcdc_rco_desktop_edit_idv_network_suc_desc";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_FAIL_LOG = "rcdc_rco_desktop_edit_idv_network_fail_log";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_FAIL_DESC = "rcdc_rco_desktop_edit_idv_network_fail_desc";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_RESULT_SUC = "rcdc_rco_desktop_edit_idv_network_result_suc";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_RESULT_FAIL = "rcdc_rco_desktop_edit_idv_network_result_fail";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_TASK_NAME = "rcdc_rco_desktop_edit_idv_network_task_name";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_TASK_DESC = "rcdc_rco_desktop_edit_idv_network_task_desc";
    String RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_THIRD_PARTY = "23201470";

    String RCDC_RCO_DESKTOP_NOT_IDV = "23200915";
    String RCDC_RCO_DESKTOP_BATCH_NOT_IDV = "23200916";
    String RCDC_RCO_NO_OPERATE_DESKTOP = "23200917";


    // 用户功能
    String RCDC_RCO_USER_IS_NOT_ADMIN_PERMISSION = "23200997";
    String RCDC_RCO_USER_ENABLE_PRESET_TASK_NAME = "rcdc_rco_user_enable_preset_task_name";
    String RCDC_RCO_USER_DISABLE_PRESET_TASK_NAME = "rcdc_rco_user_disable_preset_task_name";
    String RCDC_RCO_USER_ENABLE_PRESET_TASK_DESC = "rcdc_rco_user_enable_preset_task_desc";
    String RCDC_RCO_USER_DISABLE_PRESET_TASK_DESC = "rcdc_rco_user_disable_preset_task_desc";
    String RCDC_RCO_USER_ENABLE_PRESET_RESULT = "rcdc_rco_user_enable_preset_result";
    String RCDC_RCO_USER_DISABLE_PRESET_RESULT = "rcdc_rco_user_disable_preset_result";
    String RCDC_RCO_USER_ENABLE_PRESET_SUCCESS_RESULT = "rcdc_rco_user_enable_preset_success_result";
    String RCDC_RCO_USER_DISABLE_PRESET_SUCCESS_RESULT = "rcdc_rco_user_disable_preset_success_result";
    String RCDC_RCO_USER_ENABLE_PRESET_FAIL_RESULT = "rcdc_rco_user_enable_preset_fail_result";
    String RCDC_RCO_USER_DISABLE_PRESET_FAIL_RESULT = "23201127";
    String RCDC_RCO_USER_ENABLE_PRESET_SUC_LOG = "rcdc_rco_user_enable_preset_suc_log";
    String RCDC_RCO_USER_DISABLE_PRESET_SUC_LOG = "rcdc_rco_user_disable_preset_suc_log";
    String RCDC_RCO_USER_ENABLE_PRESET_FAIL_LOG = "rcdc_rco_user_enable_preset_fail_log";
    String RCDC_RCO_USER_ENABLE_DISABLE_NOT_SUPPORT_ENABLE_AD = "23201000";
    String RCDC_RCO_USER_ENABLE_DISABLE_NOT_SUPPORT_DISABLE_AD = "rcdc_rco_user_enable_disable_not_support_disable_ad";

    String RCDC_RCO_USER_DISABLE_PRESET_FAIL_LOG = "rcdc_rco_user_disable_preset_fail_log";

    String RCDC_RCO_USER_DISABLE_UPDATE_SUCCESS_LOG = "rcdc_rco_user_disable_update_success_log";
    String RCDC_RCO_USER_DISABLE_CLOSE_VM_SUCCESS_LOG = "rcdc_rco_user_disable_close_vm_success_log";
    String RCDC_RCO_USER_DISABLE_CLOSE_VM_FAIL_LOG = "rcdc_rco_user_disable_close_vm_fail_log";
    String RCDC_RCO_USER_DISABLE_CLOSE_VM_FAIL_RESULT = "23201126";
    String RCDC_RCO_USER_PASSWORD_DECRYPT_FAIL_LOG = "23201003";
    String RCDC_RCO_USER_DISABLE_FAIL_BY_USER_TYPE = "23200998";
    String RCDC_RCO_USER_ENABLE_FAIL_BY_USER_TYPE = "23200999";

    // 云桌面重启
    String RCDC_RCO_DESKSTOP_REBOOT_ITEM_NAME = "rcdc_rco_desktop_reboot_item_name";
    String RCDC_RCO_DESKSTOP_REBOOT_SINGLE_TASK_NAME = "rcdc_rco_desktop_reboot_single_task_name";
    String RCDC_RCO_DESKSTOP_REBOOT_SINGLE_TASK_DESC = "rcdc_rco_desktop_reboot_single_task_desc";
    String RCDC_RCO_DESKSTOP_REBOOT_SINGLE_SUC = "rcdc_rco_desktop_group_single_suc";
    String RCDC_RCO_DESKSTOP_REBOOT_SINGLE_FAIL = "rcdc_rco_desktop_reboot_single_fail";
    String RCDC_RCO_DESKSTOP_REBOOT_SUC_LOG = "rcdc_rco_desktop_reboot_suc_log";
    String RCDC_RCO_DESKSTOP_REBOOT_FAIL_LOG = "rcdc_rco_desktop_reboot_fail_log";
    String RCDC_RCO_DESKSTOP_REBOOT_BATCH_TASK_NAME = "rcdc_rco_desktop_reboot_batch_task_name";
    String RCDC_RCO_DESKSTOP_REBOOT_BATCH_TASK_DESC = "rcdc_rco_desktop_reboot_batch_task_desc";
    String RCDC_RCO_DESKSTOP_REBOOT_BATCH_RESULT = "rcdc_rco_desktop_reboot_batch_result";
    String RCDC_RCO_DESKSTOP_REBOOT_ITEM_SUC_DESC = "rcdc_rco_desktop_reboot_item_suc_desc";
    String RCDC_RCO_DESKSTOP_REBOOT_ITEM_FAIL_DESC = "rcdc_rco_desktop_reboot_item_fail_desc";

    // 邮箱模块
    String RCDC_RCO_EMAIL_SEND_TEST_SUCCESS = "rcdc_rco_email_send_test_success";
    String RCDC_RCO_EMAIL_UPDATE_CONFIG_SUCCESS = "rcdc_rco_email_update_config_success";
    String RCDC_RCO_EMAIL_RESET_CONFIG_SUCCESS = "rcdc_rco_email_reset_config_success";
    String RCDC_RCO_USER_EMAIL_NO_EXISTS = "23201004";
    String RCDC_RCO_USER_NAME_NO_EXISTS = "23201005";
    String RCDC_RCO_PASSWORD_IS_SAME = "rcdc_rco_password_is_same";
    String RCDC_RCO_USER_CREATE_SUCCESS_BUT_PSW_SEND_FAIL = "rcdc_rco_user_create_success_but_psw_send_fail";
    String RCDC_RCO_USER_PSW_SEND_SUCCESS_LOG = "rcdc_rco_user_psw_send_success_log";
    String RCDC_RCO_USER_INVALID_TIME_VALIDATE_FAIL = "rcdc_rco_user_invalid_time_validate_fail";
    String RCDC_RCO_USER_INVALID_RESOLVE_ERROR = "23201006";
    String RCDC_RCO_USER_IMPORT_EXPIRE_RESOLVE_FAIL = "23201007";
    String RCDC_RCO_USER_DESCRIPTION_VALIDATE_FAIL = "23201008";

    // 已删除云桌面磁盘备份恢复
    String RCDC_RCO_DESKTOP_DELETE_DISK_RESTORE_ITEM_NAME = "rcdc_rco_desktop_delete_disk_restore_item_name";
    String RCDC_RCO_DESKTOP_DELETE_DISK_SINGLE_TASK_NAME = "rcdc_rco_desktop_delete_disk_single_task_name";
    String RCDC_RCO_DESKTOP_DELETE_DISK_SINGLE_TASK_DESC = "rcdc_rco_desktop_delete_disk_single_task_desc";
    String RCDC_RCO_DESKTOP_DELETE_DISK_BATCH_TASK_NAME = "rcdc_rco_desktop_delete_disk_batch_task_name";
    String RCDC_RCO_DESKTOP_DELETE_DISK_BATCH_TASK_DESC = "rcdc_rco_desktop_delete_disk_batch_task_desc";

    String RCDC_RCO_DESKTOP_DELETE_DISK_RESTORE_DESC = "rcdc_rco_desktop_delete_disk_restore_desc";

    String RCDC_BATCH_IMPORT_STRATEGY_NAME_NOT_EXIST = "23201010";
    String RCDC_BATCH_IMPORT_NETWORK_NAME_NOT_EXIST = "23201011";
    String RCDC_IMPORT_USER_VALIDATE_EXPIRE_DATE_FAIL = "rcdc_import_user_validate_expire_time_fail";
    String RCDC_RCO_USER_INVALID_RECOVER_PRESET_TASK_NAME = "rcdc_rco_user_invalid_recover_preset_task_name";
    String RCDC_RCO_USER_INVALID_RECOVER_NOT_EXIT = "23201014";
    String RCDC_RCO_USER_INVALID_RECOVER_PRESET_SUCCESS = "rcdc_rco_user_invalid_recover_preset_success";
    String RCDC_RCO_USER_INVALID_RECOVER_PRESET_RESULT = "rcdc_rco_user_invalid_recover_preset_result";
    String RCDC_RCO_USER_INVALID_RECOVER_PRESET_SUCCESS_RESULT = "rcdc_rco_user_invalid_recover_preset_success_result";
    String RCDC_RCO_USER_INVALID_RECOVER_PRESET_FAIL_RESULT = "rcdc_rco_user_invalid_recover_preset_fail_result";
    String RCDC_RCO_USER_INVALID_RECOVER_PRESET_TASK_DESC = "rcdc_rco_user_invalid_recover_preset_task_desc";
    String RCDC_RCO_DESKTOP_CHECK_PORT_SUCCESS = "rcdc_rco_desktop_check_port_success";
    /**
     * 导出用户报表
     */
    String RCDC_RCO_EXPORT_USER_USE_INFO_SUCCESS_LOG = "rcdc_rco_export_user_use_info_success_log";
    String RCDC_RCO_EXPORT_USER_USE_INFO_FAIL_LOG = "rcdc_rco_export_user_use_info_fail_log";
    String RCDC_RCO_DOWNLOAD_USER_USE_INFO_SUCCESS_LOG = "rcdc_rco_download_user_use_info_success_log";
    String RCDC_RCO_DOWNLOAD_USER_USE_INFO_FAIL_LOG = "rcdc_rco_download_user_use_info_fail_log";

    String RCDC_RCO_CREATE_USER_PASSWORD_EQUALS_USER_NAME = "rcdc_rco_create_user_password_equals_user_name";

    /**
     * 导出桌面会话报表
     */
    String RCDC_RCO_DOWNLOAD_DESKTOP_SESSION_INFO_SUCCESS_LOG = "rcdc_rco_download_desktop_session_info_success_log";
    String RCDC_RCO_DOWNLOAD_DESKTOP_SESSION_INFO_FAIL_LOG = "rcdc_rco_download_desktop_session_info_fail_log";

    /**
     * 注销会话
     */
    String RCDC_RCO_USER_DESTROY_SESSION_ITEM_NAME = "rcdc_rco_user_destroy_session_item_name";
    String RCDC_RCO_USER_DESTROY_SESSION_BATCH_TASK_DESC = "rcdc_rco_user_destroy_session_batch_task_desc";
    String RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC = "rcdc_rco_user_destroy_session_task_desc_success";
    String RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC_FAIL = "rcdc_rco_user_destroy_session_task_desc_fail";
    String RCDC_RCO_DESTROY_SESSION_RESULT = "rcdc_rco_destroy_session_result";
    String RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC_LOG = "rcdc_rco_user_destroy_session_task_desc_log";
    String RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC_FAIL_LOG = "rcdc_rco_user_destroy_session_task_desc_fail_log";
    String RCDC_RCO_USER_DESTROY_SESSION_SHUTDOWN_FAIL = "rcdc_rco_user_destroy_session_shutdown_fail";
    String RCDC_RCO_USER_DESTROY_SESSION_FORCE_SHUTDOWN = "rcdc_rco_user_destroy_session_force_shutdown";


    // 云桌面密码显示配置
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_ITEM_NAME = "rcdc_rco_edit_root_pwd_config_item_name";
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_TASK_NAME = "rcdc_rco_edit_root_pwd_config_task_name";
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_TASK_DESC = "rcdc_rco_edit_root_pwd_config_task_desc";
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_SINGLE_TASK_NAME = "rcdc_rco_edit_root_pwd_config_single_task_name";
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_SINGLE_TASK_DESC = "rcdc_rco_edit_root_pwd_config_single_task_desc";
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_FAIL = "rcdc_rco_edit_root_pwd_config_fail";
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_SUCCESS = "rcdc_rco_edit_root_pwd_config_success";
    String RCDC_RCO_EDIT_ROOT_PWD_CONFIG_BATCH_TASK_RESULT = "rcdc_rco_edit_root_pwd_config_batch_task_result";

    String RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_NOT_ALLOW_EMPTY = "rcdc_rco_batch_import_cloud_platform_name_not_allow_empty";
    String RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_INCORRECT = "rcdc_rco_batch_import_cloud_platform_name_incorrect";
    String RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_TOO_LENGTH = "rcdc_rco_batch_import_cloud_platform_name_too_length";
    String RCDC_RCO_BATCH_IMPORT_CPU_INCORRECT = "rcdc_rco_batch_import_cpu_incorrect";
    String RCDC_RCO_BATCH_IMPORT_CPU_NOT_ALLOW_EMPTY = "rcdc_rco_batch_import_cpu_not_allow_empty";
    String RCDC_RCO_BATCH_IMPORT_MEMORY_INCORRECT = "rcdc_rco_batch_import_memory_incorrect";
    String RCDC_RCO_BATCH_IMPORT_MEMORY_NOT_ALLOW_EMPTY = "rcdc_rco_batch_import_memory_not_allow_empty";
    String RCDC_RCO_BATCH_IMPORT_SYSTEM_SIZE_INCORRECT = "rcdc_rco_batch_import_system_size_incorrect";
    String RCDC_RCO_BATCH_IMPORT_SYSTEM_SIZE_NOT_ALLOW_EMPTY = "rcdc_rco_batch_import_system_size_not_allow_empty";
    String RCDC_RCO_BATCH_IMPORT_PERSON_SIZE_INCORRECT = "rcdc_rco_batch_import_person_size_incorrect";
    String RCDC_RCO_BATCH_IMPORT_PERSON_SIZE_NOT_ALLOW_EMPTY = "rcdc_rco_batch_import_person_size_not_allow_empty";
    String RCDC_RCO_BATCH_IMPORT_PERSON_SIZE_MUST_STORAGE_POOL = "rcdc_rco_batch_import_person_size_must_storage_pool";
    String RCDC_RCO_BATCH_IMPORT_HAS_SYSTEM_SIZE_MUST_STORAGE_POOL = "rcdc_rco_batch_import_has_system_size_must_storage_pool";

    String RCDC_RCO_CREATE_USER_CONFIG_VDI_SPEC_SYSTEM_SIZE_ERROR = "23201527";
    String RCDC_RCO_USER_CONFIG_VDI_NO_PERSON_STORAGE_POOL = "23201528";
    String RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK = "23201531";

    String RCDC_RCO_DESKTOP_BIND_USER_ITEM_NAME = "rcdc_rco_desktop_bind_user_item_name";
    String RCDC_RCO_DESKTOP_BIND_USER_SINGLE_TASK_DESC = "rcdc_rco_desktop_bind_user_single_task_desc";
    String RCDC_RCO_DESKTOP_BIND_USER_SINGLE_TASK_NAME = "rcdc_rco_desktop_bind_user_single_task_name";
    String RCDC_RCO_DESKTOP_BIND_USER_BATCH_TASK_NAME = "rcdc_rco_desktop_bind_user_batch_task_name";
    String RCDC_RCO_DESKTOP_BIND_USER_BATCH_TASK_DESC = "rcdc_rco_desktop_bind_user_batch_task_desc";
    String RCDC_RCO_DESKTOP_BIND_USER_SINGLE_SUC = "rcdc_rco_desktop_bind_user_single_suc";
    String RCDC_RCO_DESKTOP_UN_BIND_USER_SINGLE_SUC = "rcdc_rco_desktop_un_bind_user_single_suc";
    String RCDC_RCO_DESKTOP_BIND_USER_SINGLE_FAIL = "rcdc_rco_desktop_bind_user_single_fail";
    String RCDC_RCO_DESKTOP_UN_BIND_USER_SINGLE_FAIL = "rcdc_rco_desktop_un_bind_user_single_fail";
    String RCDC_RCO_DESKTOP_BIND_USER_BATCH_RESULT = "rcdc_rco_desktop_bind_user_batch_result";
    String RCDC_RCO_DESKTOP_UN_BIND_USER_BATCH_RESULT = "rcdc_rco_desktop_un_bind_user_batch_result";
    String RCDC_RCO_DESKTOP_UN_BIND_USER_ITEM_NAME = "rcdc_rco_desktop_un_bind_user_item_name";
    String RCDC_RCO_DESKTOP_UN_BIND_USER_SINGLE_TASK_NAME = "rcdc_rco_desktop_un_bind_user_single_task_name";
    String RCDC_RCO_DESKTOP_UN_BIND_USER_SINGLE_TASK_DESC = "rcdc_rco_desktop_un_bind_user_single_task_desc";

    String RCDC_RCO_TARGET_PLATFORM_ID_EXIST_MIX_CPU = "rcdc_rco_target_platform_id_exist_mix_cpu";

    String RCDC_RCO_USER_MESSAGE_USER_GROUP_NO_USER = "rcdc_rco_user_message_user_group_no_user";

    // PC终端日志收集
    String RCDC_RCO_PC_COLLECT_LOG_SUCCESS = "rcdc_rco_pc_collect_log_success";
    String RCDC_RCO_PC_COLLECT_LOG_ERROR = "rcdc_rco_pc_collect_log_error";
}
