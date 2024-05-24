package com.ruijie.rcos.rcdc.rco.module.impl;

/**
 * 国际化key
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/26
 *
 * @author Jarman
 */
public interface BusinessKey {

    /**
     * 公用字段
     */
    String RCDC_USER_QUARTZ_CLUSTER_SERVER_TREND = "rcdc_user_quartz_cluster_server_trend";

    String RCDC_RCO_ALARM_RESOURCE_NAME_PHYSICAL_SERVER = "rcdc_rco_alarm_resource_name_physical_server";
    String RCDC_RCO_ALARM_RESOURCE_NAME_SERVICE = "rcdc_rco_alarm_resource_name_service";
    String RCDC_RCO_ALARM_RESOURCE_NAME_STORAGE = "rcdc_rco_alarm_resource_name_storage";
    String RCDC_RCO_ALARM_RESOURCE_NAME_DESKTOP = "rcdc_rco_alarm_resource_name_desktop";

    /**
     * 大屏监控相关
     */
    String RCDC_RCO_CLUSTER_BASIC_INFO_FAIL = "rcdc_rco_cluster_basic_info_fail";
    String RCDC_RCO_CLUSTER_MONITOR_INFO_FAIL = "rcdc_rco_cluster_monitor_info_fail";
    String RCDC_RCO_CABINET_NOT_EXIST = "23200447";
    String RCDC_RCO_CABINET_SERVER_INFO_NOT_EXIST = "23200448";
    String RCDC_RCO_CABINET_VALIDATE_EDIT_CABINET_NAME_EXIST = "23200449";
    String RCDC_RCO_CABINET_VALIDATE_CREATE_CABINET_NAME_EXIST = "23200450";
    String RCDC_RCO_CABINET_VALIDATE_CONFIG_SERVER_NUM_OVER = "23200451";
    String RCDC_RCO_CABINET_VALIDATE_CONFIG_SERVER_ONE_OVER = "23200452";
    String RCDC_RCO_CABINET_CONFIG_SERVER_LOCATION_USED = "23200453";
    String RCDC_RCO_CABINET_CONFIG_SERVER_LOCATION_SET_ERROR = "23200458";
    String RCDC_RCO_CABINET_CONFIG_SERVER_LOCATION_SET_OVER_LIMIT = "23200459";
    String RCDC_RCO_CABINET_SERVER_HAS_BEEN_DELETED = "23200460";
    String RCDC_RCO_CABINET_SERVER_NOT_BELONG_THIS_CABINET = "23200461";
    String RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_GROUP_NOT_FOUND = "23200454";
    String RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_GROUP_ID_NOT_EXIST = "23200455";
    String RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_DESKTOP_NOT_FOUND = "23200456";
    String RCDC_RCO_BIGSCREEN_RESOURCETYPE_NORMALDISTRIBUTION_NOT_SUPPORT = "rcdc_rco_bigscreen_resourcetype_normaldistribution_not_support";
    String RCDC_RCO_BIGSCREEN_GET_DESKTOP_INFO_FAIL = "23200457";

    /**
     * 配置管理
     **/
    String RCDC_RCO_BIGSCREEN_COMMON_CONFIG_NOT_FOUND = "23200446";

    /**
     * 定时任务
     **/
    String RCDC_RCO_QUARTZ_TERMINAL_SHUT_DOWN = "rcdc_rco_quartz_terminal_shut_down";
    String RCDC_RCO_QUARTZ_TERMINAL_WAKE_UP = "rcdc_rco_quartz_terminal_wake_up";
    String RCDC_RCO_QUARTZ_CLOUD_DESK_START = "rcdc_rco_quartz_cloud_desk_start";
    String RCDC_RCO_QUARTZ_CLOUD_DESK_SHUT_DOWN = "rcdc_rco_quartz_cloud_desk_shut_down";
    String RCDC_RCO_QUARTZ_USER_UNLOCK = "rcdc_rco_quartz_user_unlock";
    String RCDC_RCO_QUARTZ_SEND_SYSLOG = "rcdc_rco_quartz_send_syslog";
    String RCDC_RCO_QUARTZ_SET_NEED_SEND_SYSLOG = "rcdc_rco_quartz_set_need_send_syslog";
    String RCDC_RCO_QUARTZ_CLOUD_DESK_RESTART = "rcdc_rco_quartz_cloud_desk_restart";

    String RCDC_RCO_QUARTZ_TERMINAL_POWER_OFF_SUCCESS_SYSTEM_LOG = "rcdc_rco_quartz_terminal_power_off_success_system_log";
    String RCDC_RCO_QUARTZ_TERMINAL_POWER_OFF_FAIL_SYSTEM_LOG = "rcdc_rco_quartz_terminal_power_off_fail_system_log";
    String RCDC_RCO_QUARTZ_TERMINAL_WAKE_UP_SUCCESS_SYSTEM_LOG = "rcdc_rco_quartz_terminal_wake_up_success_system_log";
    String RCDC_RCO_QUARTZ_TERMINAL_WAKE_UP_FAIL_SYSTEM_LOG = "rcdc_rco_quartz_terminal_wake_up_fail_system_log";
    String RCDC_RCO_QUARTZ_START_CLOUD_DESK_SUCCESS_SYSTEM_LOG = "rcdc_rco_quartz_start_cloud_desk_success_system_log";
    String RCDC_RCO_QUARTZ_START_CLOUD_DESK_FAIL_SYSTEM_LOG = "rcdc_rco_quartz_start_cloud_desk_fail_system_log";
    String RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG = "rcdc_rco_quartz_shut_down_cloud_desk_success_system_log";
    String RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_FAIL_SYSTEM_LOG = "rcdc_rco_quartz_shut_down_cloud_desk_fail_system_log";
    String RCDC_RCO_QUARTZ_RESTART_CLOUD_DESK_SUCCESS_SYSTEM_LOG = "rcdc_rco_quartz_restart_cloud_desk_success_system_log";
    String RCDC_RCO_QUARTZ_RESTART_CLOUD_DESK_FAIL_SYSTEM_LOG = "rcdc_rco_quartz_restart_cloud_desk_fail_system_log";
    String RCDC_RCO_DESKTOP_RESTART_FAIL_NOT_SUPPORT_DESKTOP_TYPE = "rcdc_rco_desktop_restart_fail_not_support_desktop_type";

    String RCDC_RCO_QUARTZ_DESKTOP_ONLINE_LOG_CLEAN = "rcdc_rco_quartz_desktop_online_log_clean";
    String RCDC_RCO_QUARTZ_DESKTOP_ONLINE_LOG_CLEAN_FINISH = "rcdc_rco_quartz_desktop_online_log_clean_finish";


    String RCDC_CLOUDDESKTOP_ACTIVE_DESKTOP_COUNT_SYNC = "rcdc_clouddesktop_active_desktop_count_sync";
    String RCDC_RCO_EXPORT_DATA_CLEAN = "rcdc_rco_export_data_clean";

    /**
     * 大屏信息收集
     */
    String RCDC_RCO_BIGSCREEN_ABNORMAL_DESKTOP_LIST_TYPE_ILLEGAL = "23200462";
    String RCDC_RCO_BIGSCREEN_GET_FROM_API_PAGE_QUERY_FAIL = "23200463";

    /**
     * 屏幕水印
     */
    String RCDC_RCO_WATERMARK_DESKTOP_NOT_EXIST = "23200465";
    String RCDC_RCO_WATERMARK_GET_RUNNING_DESKTOP_NOT_EXIST = "23200464";
    String RCDC_RCO_WATERMARK_DESKTOP_STATE_IS_NOT_RUNNING = "23200466";

    /**
     * rcdc主题
     */
    String RCDC_RCO_UPLOAD_PICTURE_FAIL = "23200445";


    /**
     * 管理员终端登录
     */
    String RCDC_RCO_ADMIN_LOGIN_ON_TERMINAL_LOG_KEY = "rcdc_rco_admin_login_on_terminal_log_key";
    String RCDC_RCO_ADMIN_LOGOUT_ON_TERMINAL_LOG_KEY = "rcdc_rco_admin_logout_on_terminal_log_key";
    String RCDC_RCO_ADMIN_LOGIN_ON_TERMINAL_OVERWRITE_LOG_KEY = "rcdc_rco_admin_login_on_terminal_overwrite_log_key";
    String RCDC_RCO_ADMIN_LOGIN_ON_TERMINAL_TIME_OUT_LOG_KEY = "rcdc_rco_admin_login_on_terminal_time_out_log_key";

    /**
     * 用户身份验证配置
     */
    String RCDC_RCO_USER_IDENTITY_CREATE_USER_IDENTITY_IS_EXIST = "23200467";
    String RCDC_RCO_USER_IDENTITY_CONFIG_NOT_EXIT = "23200468";

    String RCDC_RCO_GET_ADMIN_PASSWORD_FAIL = "23200469";

    /**
     * 管理员管理相关
     */
    String RCDC_RCO_AAA_CREATE_ADMIN_IS_EXIST = "23200470";
    String RCDC_RCO_AAA_DELETE_ADMIN_IS_NOT_EXIST = "23200471";

    /* 8======================PC纳管业务========= */
    String RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER = "23200472";
    String RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER_NAME = "23200473";
    String RCDC_RCO_COMPUTER_DELETE_COMPUTER_FAIL = "23200474";
    String RCDC_RCO_COMPUTER_ONLINE_NOT_ALLOW_DELETE = "23200475";
    String RCDC_RCO_COMPUTER_ASSISTANTAPP_UPGRADE_PACKAGE_NOT_EXIST = "23200476";
    String RCDC_RCO_COMPUTER_HALO_UPGRADE_PACKAGE_NOT_EXIST = "23200477";
    String RCDC_RCO_COMPUTER_OFFLINE_NOT_ALLOW_REMOTE_ASSIST = "23200479";
    String RCDC_RCO_COMPUTER_NO_FAULT = "23200478";


    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL_REFRESH = "23200496";

    String RCDC_RCO_GROUP_NOT_EXIST = "23200497";

    String RCDC_RCO_COMPUTER_REPORT_FAULT_FAIL_BY_DESK_NOT_EXIST = "rcdc_rco_computer_report_fault_fail_by_desk_not_exist";

    /**
     * 远程协助
     */
    String RCDC_RCO_REMOTE_ASSIST_INQUIRE_FAIL = "23200480";
    String RCDC_RCO_REMOTE_ASSIST_QUERY_NO_INFO = "23200482";
    String RCDC_RCO_REMOTE_ASSIST_BY_OTHER_ADMIN = "23200483";
    String RCDC_RCO_REMOTE_ASSIST_OBTAIN_CONNECT_INFO_FAIL = "23200484";
    String RCDC_RCO_REMOTE_ASSIST_IS_USED = "23200485";
    /** 当前PC远程协助信息为空 */
    String RCDC_RCO_COMPUTER_GET_REMOTE_ASSIST_IS_NULL = "23200486";

    String RCDC_RCO_COMPUTER_ASSISTANT_MESSAGE_PC_SEND_FAIL = "23200487";
    String RCDC_RCO_COMPUTER_ASSISTANT_MESSAGE_PC_RESPONSE_FAIL = "23200488";
    String RCDC_RCO_COMPUTER_ASSISTANT_MESSAGE_PC_DESK_NOT_EXIST = "23200489";
    String RCDC_RCO_COMPUTER_CREATE_OR_MODIFY_TOKEN_FILE_FAIL = "23200490";
    String RCDC_RCO_COMPUTER_VNC_CONNECT_UNREADY = "23200481";

    /** 终端不是在线状态 */
    String RCDC_RCO_TERMINAL_NOT_ONLINE = "23200491";

    String RCDC_RCO_TERMINAL_NOT_WIRED = "23200492";

    /** 管理员不是系统管理员 */
    String RCDC_RCO_NOT_SYS_ADMIN = "23200493";

    /** 管理员没有终端的权限 */
    String RCDC_RCO_ADMIN_NOT_HAS_TERMINAL_GROUP_PERMISSION = "23200494";

    String RCDC_RCO_ADMIN_NOT_HAS_IMAGE_TEMPLATE_MENU_PERMISSION = "23200495";

    /**
     * 终端编辑镜像操作日志
     */
    String RCDC_RCO_VDI_EDIT_IMAGE_ABORT_SUCCESS = "rcdc_rco_vdi_edit_image_abort_success";
    String RCDC_RCO_VDI_EDIT_IMAGE_ABORT_FAIL = "rcdc_rco_vdi_edit_image_abort_fail";
    String RCDC_RCO_VDI_EDIT_IMAGE_CONFIG_VM = "rcdc_rco_vdi_edit_image_config_vm";
    String RCDC_RCO_VDI_EDIT_IMAGE_PUBLISH_SUCCESS = "rcdc_rco_vdi_edit_image_publish_success";
    String RCDC_RCO_VDI_EDIT_IMAGE_PUBLISH_FAIL = "rcdc_rco_vdi_edit_image_publish_fail";
    String RCDC_RCO_VDI_EDIT_IMAGE_START_VM_SUCCESS = "rcdc_rco_vdi_edit_image_start_vm_success";
    String RCDC_RCO_VDI_EDIT_IMAGE_START_VM_FAIL = "rcdc_rco_vdi_edit_image_start_vm_fail";
    String RCDC_RCO_VDI_EDIT_IMAGE_STOP_VM_SUCCESS = "rcdc_rco_vdi_edit_image_stop_vm_success";
    String RCDC_RCO_VDI_EDIT_IMAGE_STOP_VM_FAIL = "rcdc_rco_vdi_edit_image_stop_vm_fail";


    /**
     * 证书模块
     */
    String RCDC_RCO_VDI_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_vdi_license_temp_license_expired_success";
    String RCDC_RCO_IDV_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_idv_license_temp_license_expired_success";
    String RCDC_RCO_VOI_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_voi_license_temp_license_expired_success";
    String RCDC_RCO_IDV_UP_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_idv_up_license_temp_license_expired_success";

    String RCDC_RCO_EXCESS_VDI_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_excess_vdi_license_temp_license_expired_success";
    String RCDC_RCO_EXCESS_IDV_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_excess_idv_license_temp_license_expired_success";
    String RCDC_RCO_EXCESS_VOI_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_excess_voi_license_temp_license_expired_success";
    String RCDC_RCO_EXCESS_IDV_UP_LICENSE_TEMP_LICENSE_EXPIRED_SUCCESS = "rcdc_rco_excess_idv_up_license_temp_license_expired_success";

    /** 镜像关联的云桌面未全部关闭 */
    String RCDC_CLOUDDESKTOP_START_VM_DESKTOP_NOT_CLOSE_ALL_FAIL = "rcdc_clouddesktop_start_vm_desktop_not_close_all_fail";

    /**
     * 用户模块
     */
    String RCDC_USER_USER_MESSAGE_USER_NOT_EXIST = "23200534";
    String RCDC_USER_USER_MESSAGE_NOT_EXIST = "23200535";
    String RCDC_USER_MESSAGE_USER_GROUP_NO_USER = "23200536";
    String RCDC_USER_VISITOR_NOT_ALLOW_CREATE_DIFF_DESKTOP = "rcdc_user_visitor_not_allow_create_diff_desktop";
    String RCDC_USER_USER_ENTITY_IS_NOT_EXIST = "23200539";
    String RCDC_USER_GROUP_SYNC_NETWORK_DELETE_DESK_CONFIG = "rcdc_user_group_sync_network_delete_desk_config";
    String RCDC_USER_DELETE_IDV_RELATIVE_DATA_ERROR = "23200542";
    String RCDC_USER_EDIT_SUCCESS_LOG = "rcdc_user_edit_success_log";
    String RCDC_USER_LOGIN_FAIL_LOG = "rcdc_user_login_fail_log";

    /**
     * 云桌面模块
     */
    String RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID = "23200009";
    String RCDC_USER_IS_NOT_VISITOR_NOT_ALLOW_GET_DESKTOP_CONFIG = "23200529";
    String RCDC_USER_CLOUDDESKTOP_CREATE_SUC_LOG = "rcdc_user_clouddesktop_create_suc_log";
    /**
     * 云桌面状态前缀
     */
    String RCDC_USER_CLOUDDESKTOP_STATE_PRE = "rcdc_user_clouddesktop_state_";

    String RCDC_USER_CLOUDDESKTOP_RESUME_STATE_ERR = "23200498";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_HAS_VM = "23200500";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_HAS_RELATIVE_VISITOR = "rcdc_user_clouddesktop_strategy_has_relative_visitor";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_GROUP = "23200511";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_MANY_GROUP = "23200512";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_IDV_GROUP = "23200513";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_MANY_IDV_GROUP = "23200514";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_VDI_NOT_FOUND = "23200502";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_IDV_NOT_FOUND = "23200503";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_VOI_NOT_FOUND = "23200504";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_FOUND = "23200505";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_TYPE_UNMATCH = "23200506";
    String RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_NOT_FOUND = "23200507";
    String RCDC_USER_CLOUDDESKTOP_IDV_STRATEGY_NOT_FOUND = "23200508";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_SYSTEM_DISK_SIZE_OUT_RANGE = "23200509";
    String RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_HAS_VM = "23200501";
    String RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_RELATIVE_GROUP = "23200515";
    String RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_HAS_RELATIVE_VISITOR = "23200516";
    String RCDC_USER_CLOUDDESKTOP_NETWORK_AD_AUTO_JOIN_MUST_HAS_DNS = "23200510";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_RELATIVE_GROUP = "23200517";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_RELATIVE_GROUP = "23200518";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_RELATIVE_USER = "23200519";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_RELATIVE_USER = "23200520";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_RELATIVE_IDV_GROUP = "23200521";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_HAS_RELATIVE_VISITOR = "23200522";
    String RCDC_USER_CLOUDDESKTOP_RECOVER_DESK_FROM_ERROR_STATE_ERR = "23200499";
    String RCDC_USER_CLOUDDESKTOP_VISITOR_CAN_NOT_BIND_VIP_DESKTOP = "rcdc-user_clouddesktop_visitor_can_not_bind_vip_desktop";
    String RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_USER = "23200525";

    String RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_MANY_USER = "23200526";
    String RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_HAS_RELATIVE_TERMINAL = "23200523";

    String RCDC_RCO_DESKTOP_EDIT_IMAGE_NOT_EXIST = "23200837";
    String RCDC_RCO_DESKTOP_EDIT_IMAGE_STATE_NOT_ALLOW = "23200527";
    String RCDC_RCO_DESKTOP_EDIT_IMAGE_VALID_NO_PASS = "23200528";
    String RCDC_RCO_DESKTOP_IMAGE_CHANGE_TASK_LOG_FAIL = "rcdc_rco_desktop_image_change_task_log_fail";
    String RCDC_RCO_DESKTOP_IMAGE_CHANGE_TASK_LOG_SUC = "rcdc_rco_desktop_image_change_task_log_suc";
    String RCDC_RCO_DESKTOP_IMAGE_CHANGE_DESK_STATE_NOT_ALLOW = "rcdc_rco_desktop_image_change_desk_state_not_allow";
    String I18N_DESK_STATE_PREFIX = "rcdc-clouddesktop_desk_state_";

    String RCD_RCO_SHINE_START_VM_VM_BUSY = "rcdc_user_shine_start_vm_vm_busy";
    String RCD_RCO_SHINE_START_VM_VM_DELETE = "rcdc_user_shine_start_vm_vm_delete";
    String RCD_RCO_SHINE_START_VM_VM_MOVING = "rcd_user_shine_start_vm_vm_moving";
    String RCD_RCO_SHINE_START_VM_VM_ERR = "rcdc_user_shine_start_vm_vm_err";
    String RCD_RCO_SHINE_START_VM_SYSTEM_ERR = "rcdc_user_shine_start_vm_system_err";

    String RCD_RCO_SHINE_START_VM_AGREEMENT_AGENCY_ERR = "rcd_rco_shine_start_vm_agreement_agency_err";
    String RCD_RCO_SHINE_START_VM_AGREEMENT_AGENCY_FORCE_ERR = "rcd_rco_shine_start_vm_agreement_agency_force_err";
    String RCDC_RCO_SHINE_START_VM_IP_LIMIT = "rcdc_rco_shine_start_vm_ip_limit";


    String RCDC_USER_CLOUDDESKTOP_USER_VM_MAX = "rcdc_user_clouddesktop_user_vm_max";
    String RCDC_USER_CLOUDDESKTOP_VISITOR_USER_VM_MAX = "23200524";


    /**
     * 云桌面回收站
     */
    String RCDC_USER_RECYCLEBIN_RECOVER_USER_DELETE = "23200530";
    String RCDC_USER_RECYCLEBIN_RECOVER_STATE_ERR = "23200531";
    String RCDC_USER_RECYCLEBIN_DELETE_STATE_ERR = "23200532";
    String RCDC_USER_RECYCLEBIN_RECOVER_TO_VISITOR_HAS_DESKTOP_FORBID = "23200533";
    String RCDC_RCO_RECYCLEBIN_RECOVER_EXIST_USER_ONE_MORE_DESK = "23200923";

    /**
     * 终端模块
     */
    String RCDC_USER_TERMINAL_TERMINAL_NOT_EXIST = "23200543";
    String RCDC_USER_TERMINAL_OFFLINE_NOT_ALLOW_INIT = "23200544";
    String RCDC_USER_TERMINAL_UPGRADING_NOT_ALLOW_INIT = "23200545";
    String RCDC_RCO_TERMINAL_ONLINE_NOT_ALLOW_DELETE = "23200546";
    String RCDC_RCO_TERMINAL_BIND_USER_SUCCESS_LOG = "rcdc_rco_terminal_bind_user_success_log";
    String RCDC_USER_TERMINAL_NOT_PERSONAL_NOT_BIND_USER = "23200566";
    String RCDC_USER_TERMINAL_NOT_BIND_VISITOR_USER = "23200567";

    String RCDC_USER_QUARTZ_DESK_CLEAN = "rcdc_user_quartz_desk_clean";
    String RCDC_USER_QUARTZ_USER_MESSAGE = "rcdc_user_quartz_user_message";
    /**
     * rcdc与rccm 健康检查任务
     **/
    String RCCM_MANAGE_HEART_BEAT_QUARTZ = "rccm_manage_heart_beat_quartz";
    String RCDC_NO_AUTH_QUARTZ_INVALID_TOKEN_CLEAN = "rcdc_no_auth_quartz_invalid_token_clean";

    String RCDC_USER_QUARTZ_FETCH_DESKTOP_RESOURCE_USE_RATE = "rcdc_user_quartz_fetch_desktop_resource_use_rate";

    String RCDC_USER_TERMINAL_NOT_ONLINE_NOT_ALLOW_SETTING = "23200547";
    String RCDC_USER_TERMINAL_NOT_IDV_NOT_ALLOW_SETTING = "23200548";
    String RCDC_USER_TERMINAL_NOT_VOI_NOT_ALLOW_SETTING = "23200549";
    String RCDC_USER_TERMINAL_NOT_PUBLIC_NOT_ALLOW_SETTING = "23200550";
    String RCDC_USER_TERMINAL_OPERATE_MSG_SEND_FAIL = "23200551";
    String RCDC_USER_TERMINAL_IDV_VISITOR_SETTING_FAIL = "23200552";
    String RCDC_USER_TERMINAL_IDV_VISITOR_SETTING_CONFIG_WRONG = "23200553";
    String RCDC_USER_TERMINAL_CLOUD_DESKTOP_ON_RUNNING = "23200554";
    String RCDC_USER_TERMINAL_CANCEL_INIT = "rcdc_user_terminal_cancel_init";
    String RCDC_USER_TERMINAL_CONFIRM_INIT = "rcdc_user_terminal_confirm_init";
    String RCDC_USER_TERMINAL_NOTIFY_SHINE_WEB_FAIL = "23200555";
    String RCDC_USER_TERMINAL_INIT_FAIL = "rcdc_user_terminal_init_fail";
    String RCDC_USER_TERMINAL_INIT_SUCCESS = "rcdc_user_terminal_init_success";
    String RCDC_USER_TERMINAL_ON_CLEARING_DATA_DISK = "23200556";
    String RCDC_USER_TERMINAL_ON_INIT = "23200557";
    String RCDC_USER_TERMINAL_ON_RESTORE_DESKTOP = "23200558";
    String RCDC_USER_TERMINAL_ON_UPGRADE = "23200559";
    String RCDC_USER_TERMINAL_ON_REPAIR_IMAGE = "23200560";
    String RCDC_USER_TERMINAL_ON_IMAGE_REPLACEMENT = "23200561";
    String RCDC_USER_TERMINAL_REQUEST_TERMINAL_TIME_OUT = "23200562";
    String RCDC_TERMINAL_APP_UPDATELIST_CACHE_NOT_READY = "23200563";
    String RCDC_TERMINAL_COMPONENT_UPDATELIST_CACHE_INCORRECT = "23200564";
    String RCDC_USER_TERMINAL_LOCAL_EDIT_IMAGE = "23200565";
    /**
     ** 云桌面审计日志相关
     */
    String RCDC_USER_DESKTOP_OPLOG_SYSTEM_CLOSE = "rcdc_user_desktop_oplog_system_close";
    String RCDC_USER_DESKTOP_OPLOG_SYSTEM_SUSPEND = "rcdc_user_desktop_oplog_system_suspend";
    String RCDC_USER_DESKTOP_OPLOG_USER_START_SUC = "rcdc_user_desktop_oplog_user_start_suc";
    String RCDC_USER_DESKTOP_OPLOG_USER_START_FAIL = "rcdc_user_desktop_oplog_user_start_fail";
    String RCDC_USER_DESKTOP_OPLOG_USER_LOGIN = "rcdc_user_desktop_oplog_user_login";
    String RCDC_USER_DESKTOP_OPLOG_USER_RESUME_SUC = "rcdc_user_desktop_oplog_user_resume_suc";
    String RCDC_USER_DESKTOP_OPLOG_USER_RESUME_FAIL = "rcdc_user_desktop_oplog_user_resume_fail";
    String RCDC_USER_DESKTOP_OPLOG_USER_STOP_SUC = "rcdc_user_desktop_oplog_user_stop_suc";
    String RCDC_USER_DESKTOP_OPLOG_USER_STOP_FAIL = "rcdc_user_desktop_oplog_user_stop_fail";
    String RCDC_USER_DESKTOP_OPLOG_USER_REBOOT_SUC = "rcdc_user_desktop_oplog_user_reboot_suc";
    String RCDC_USER_DESKTOP_OPLOG_USER_REBOOT_FAIL = "rcdc_user_desktop_oplog_user_reboot_fail";
    String RCDC_USER_DESKTOP_OPLOG_USER_LOCK_SCREEN = "rcdc_user_desktop_oplog_user_lock_screen";
    String RCDC_USER_DESKTOP_OPLOG_USER_LOGOUT = "rcdc_user_desktop_oplog_user_logout";
    String RCDC_USER_DESKTOP_OPLOG_DELETE_DESKTOP_PRE = "rcdc_user_desktop_oplog_delete_desktop_when_";
    String RCDC_USER_DESKTOP_OPLOG_SYNC_USER_ERROR = "rcdc_user_desktop_oplog_sync_user_error";
    String RCDC_USER_DESKTOP_OPLOG_SYNC_PWD_ERROR = "rcdc_user_desktop_oplog_sync_pwd_error";

    /**
     * 维护模式
     */
    String RCDC_USER_SHINE_START_VM_UNDER_MAINTENANCE_ERR = "rcdc_user_shine_start_vm_under_maintenance_err";
    String RCDC_USER_SHINE_START_VM_UNDER_DESKTOP_MAINTENANCE_ERR = "23200568";

    String RCDC_USER_REMOTE_ASSIST_VM_NORUN = "23200569";
    String RCDC_USER_REMOTE_ASSIST_VM_LIMIT = "23200570";
    String RCDC_USER_REMOTE_ASSIST_INQUIRE_FAIL = "23200571";
    String RCDC_USER_REMOTE_ASSIST_QUERY_NO_INFO = "23200572";
    String RCDC_USER_REMOTE_ASSIST_IS_WAITTING_STATE = "23200573";
    String RCDC_USER_REMOTE_ASSIST_BY_OTHER_ADMIN = "23200574";
    String RCDC_USER_REMOTE_ASSIST_OBTAIN_CONNECT_INFO_FAIL = "23200575";

    /**
     * 虚拟托盘
     */
    String RCO_VIRTUAL_DISK_ENABLE = "rco_virtual_disk_enable";


    /** 镜像关联的云桌面未全部关闭 */
    String RCDC_RCO_CLOUDDESKTOP_START_VM_DESKTOP_NOT_CLOSE_ALL_FAIL = "23200576";

    /**
     * 导出云桌面日志
     */
    String RCDC_RCO_EXPORT_DATA_NOT_EXIST = "23200577";

    /**
     * 刪除文件失敗
     */
    String RCDC_RCO_DELETE_FILE_ERROR = "23200578";

    /** 向终端[{0}]下发消息[{1}]超时 **/
    String RCDC_RCO_REQUEST_TERMINAL_TIME_OUT = "23200585";

    /** IDV终端[{0}正在本地编辑镜像中，请稍后重试或选择其他IDV终端 **/
    String IDV_LOCAL_EDITING_IMAGE_TEMPLATE = "23200586";

    /**
     * 云桌面状态不支持变更镜像
     */
    String RCDC_RCO_DESKTOP_CHANGE_IMAGE_FAIL = "rcdc_rco_desktop_change_image_fail";

    /**
     * 向IDV,VOI终端发送同状态用户禁用状态信息失败
     */
    String RCDC_RCO_USER_DISABLED = "rcdc_rco_user_disabled";

    /**
     * 镜像相关
     */
    String RCDC_NOT_FIND_IMAGE_FILE = "23200588";
    String RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_EXIST = "23250236";
    String RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_CAN_NOT_LOCK = "rcdc-clouddesktop_image_template_can_not_lock";
    String RCDC_CLOUDDESKTOP_IMAGE_LOCAL_EDIT_STATUS_ERROR = "23250015";
    String RCDC_CLOUDDESKTOP_IMAGE_NOT_EXISTS = "rcdc_clouddesktop_image_not_exists";
    String RCDC_IMAGE_TEMPLATE_STATE_DISABLED = "rcdc_image_template_state_disabled";
    String RCDC_IMAGE_NOT_SUPPORT_TC = "23200589";
    String RCDC_IMAGE_DRIVER_NOT_INSTALL = "23200590";
    String RCDC_IMAGE_NOT_SUPPORT_TERMINAL = "23200591";


    /**
     * vm时间通知
     */
    String RCDC_RCO_EVENT_NOTICE_DESKTOP_NOT_EXIST = "23200596";

    /**
     * 云桌面磁盘容量小于镜像的磁盘容量
     */
    String RCDC_RCO_DESK_DISK_ADD_CAPACITY_UNDER_IMAGE = "rcdc_rco_desk_disk_add_capacity_under_image";

    /**
     * 认证策略
     */
    String RCDC_CERTIFICATION_USER_LOCKED = "rcdc_certification_user_locked";
    String RCDC_UNLOCK_TERMINAL_MNG_PWD_FAIL = "23200592";
    String RCDC_LOCK_TERMINAL_MNG_PWD = "rcdc_lock_terminal_mng_pwd";


    /**
     * IP相关
     */
    String RCDC_RCO_START_IP_GREATER_THAN_END_IP = "23200600";
    String RCDC_RCO_IP_POOL_COVERAGE = "23200601";
    String RCDC_RCO_IP_POOL_MUST_NOT_EMPTY = "23200602";
    /**
     * 相关对象查找不到
     **/
    String RCDC_ENTITY_NOT_FIND = "23200603";

    /**
     * syslog
     */
    String RCDC_RCO_SEND_SYSLOG_SCHEDULE_RUN_SUCCESS = "rcdc_send_syslog_schedule_run_success";
    String RCDC_RCO_SEND_OPERATE_LOG_EXISTS_FAIL = "rcdc_rco_send_operate_log_exists_fail";
    String RCDC_RCO_SEND_SYSTEM_LOG_EXISTS_FAIL = "rcdc_rco_send_system_log_exists_fail";
    String RCDC_RCO_SEND_CLOUD_DESK_LOG_EXISTS_FAIL = "rcdc_rco_send_cloud_desk_log_exists_fail";

    /** 网关授权错误 */
    String GATEWAY_AUTH_ERROR = "gateway_auth_error";

    /** 网关请求错误 */
    String GATEWAY_REQUEST_ERROR = "gateway_request_error";
    /** 没有找到对应接口 */
    String GATEWAY_REQUEST_NOT_FOUND_ROUTE = "23200624";

    /** rcenter宕机异常 */
    String RCENTER_SERVICE_UNAVAILABLE = "23200625";

    /** rcenter内部错误 */
    String RCENTER_SERVICE_INNER_ERROR = "23200626";

    /** 连接不上网关服务器，请检查网关服务器是否正常，由于目前rcenter没有网关暂时以rcenter命名 */
    String GATEWAY_REQUEST_SERVER_CONNECT_ERROR = "23200627";

    /** 网关token失效 */
    String GATEWAY_TOKEN_INVALID = "gateway_token_invalid";

    /** 发送RCCM心跳失败 */
    String RCCM_SEND_HEART_BEAT_FAIL = "rccm_send_heart_beat_fail";

    /** 用户所在集群不正常 */
    String CLUSTER_NO_EXISTS = "23200432";


    /**
     * 管理登录
     */
    String RCDC_RCO_AAA_ADMIN_IS_DISABLED = "rcdc-aaa_admin_is_disabled";
    String RCDC_RCO_AAA_ADMIN_NOT_ALLOW_LOGIN_THIS_TIME = "23200606";
    String RCDC_RCO_AAA_ADMIN_EXPIRED = "23200607";
    String RCDC_RCO_AAA_ADMIN_UNKNOWN_EXCEPTION = "23200608";

    String DESK_SESSION_OVERTIME_SHUTDOWN_QUARTZ = "desk_session_overtime_shutdown_quartz";

    /**
     * 云桌面组
     */
    String RCDC_RCO_CLOUD_DESKTOP_GROUP_NOT_FOUND = "rcdc_rco_cloud_desktop_group_not_found";
    String RCDC_RCO_CLOUD_DESKTOP_GROUP_NAME_EXIST = "rcdc_rco_cloud_desktop_group_name_exist";
    String RCDC_RCO_CLOUD_DESKTOP_GROUP_NO_DESKTOP = "rcdc_rco_cloud_desktop_group_no_desktop";

    /**
     * 云桌面修复
     */
    String RCDC_CLOUDDESKTOP_NOT_IN_REPAIR_STATE = "rcdc_clouddesktop_not_in_repair_state";

    /**
     * 虚拟显卡
     */
    String RCDC_RCO_IMAGE_NOT_EXIST_GPU_DRIVER = "23200610";
    String RCDC_RCO_IMAGE_NOT_SUPPORT_GPU = "23200611";
    String RCDC_RCO_IMAGE_NOT_SUPPORT_GPU_MODEL = "23200612";
    String RCDC_RCO_IMAGE_GPU_CACHE_ERROR = "23200613";
    String RCDC_RCO_OS_TYPE_NOT_SUPPORT_GPU = "23200614";
    String RCDC_RCO_OS_TYPE_WIN7_X64_NOT_SUPPORT_AMD_GPU = "23200615";
    String RCDC_RCO_OS_TYPE_WIN7_X64_NOT_SUPPORT_N_GPU = "23200616";

    String RCDC_RCO_COMPRESS_FILE_FAIL = "23200617";
    String RCDC_RCO_UPDATE_CONFIG_FILE_FAIL = "rcdc_rco_update_config_file_fail";

    /**
     * 删除用户相关
     */
    String RCDC_RCO_USER_IS_AD_USER_NOT_ALLOW_DELETE = "23200962";
    String RCDC_RCO_USER_IS_THIRD_PARTY_USER_NOT_ALLOW_DELETE = "23200963";
    String RCDC_RCO_USER_IS_ADMIN_NOT_ALLOW_DELETE = "23200965";
    String RCDC_RCO_USER_CREATING_DESKTOP_NOT_ALLOW_DELETE = "23200961";
    String RCDC_RCO_USERGROUP_HIERARCHY_OVER = "23200935";
    String RCDC_RCO_USER_GROUP_PARENT_GROUP_NOT_ALLOWED_EMPTY = "23200619";
    String RCDC_RCO_USER_GROUP_NAME_INCORRECT = "23200620";
    String RCDC_RCO_USER_GROUP_NAME_LENGTH_OVER = "23200621";

    String RCDC_RCO_RESERVED_GROUP_NAME_UNGROUPED = "rcdc_rco_reserved_group_name_ungrouped";
    String RCDC_RCO_RESERVED_GROUP_NAME_OVERVIEW = "rcdc_rco_reserved_group_name_overview";
    String RCDCC_RCO_USER_ROOT_LDAP_USER_GROUP_NAME = "rcdc_rco_user_root_ldap_user_group_name";
    String RCDC_RCO_RESERVED_AD_GROUP_NAME = "rcdc_rco_reserved_ad_group_name";
    String RCDC_RCO_USER_GROUP_NOT_ALLOW_RESERVE_NAME = "23200618";

    String RCDC_RCO_CREATE_USER_NAME_CONTAIN_DEFAULT = "23200951";
    String RCDC_RCO_USER_NORMAL_USER_REAL_NAME_MUST_NOT_BE_NULL = "23200983";
    String RCDC_RCO_CREATE_USER_TYPE_NOT_AD_LDAP_FAIL = "23200622";
    String RCDC_RCO_CREATE_USER_SAME_NAME_EXIST_ERROR = "23200623";

    /**
     * 云桌面操作
     */
    String RCDC_RCO_DESK_START_FAIL_REASON_USER_NOT_LOGIN = "23200628";

    /**
     * 无法获取自动加域消息内容
     */
    String RCDC_RCO_NO_AUTO_JOIN_DOMAIN_INFO = "23200609";

    /**
     * 修改网卡工作模式
     */
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_ONLY_SUPPORT_ONLINE = "23200629";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_ONLY_SUPPORT_IDV = "23200630";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SHINE_OPERATE_FAIL = "23200631";

    /**
     * 系统盘自动扩容
     */
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_ONLY_SUPPORT_IDV_OR_TCI = "23200632";
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_NOT_ALLOW_LOCAL_DISK = "23200633";
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_ONLY_SUPPORT_WIN7_AND_WIN10 = "23200634";

    /**
     * 错误提示语的前缀
     */
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_ERROR_MESSAGE_PREFIX = "rcdc_terminal_config_full_system_disk_";
    /**
     * 存在本地磁盘，不允许修改
     */
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_EXISTS_LOCAL_DISK = "rcdc_terminal_config_full_system_disk_exists_local_disk";

    /**
     * 不存在云桌面无法修改
     */
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_NOT_EXISTS_CLOUD_DESK = "23200635";

    /**
     * 终端上桌面ID和服务器上桌面ID不一致
     */
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_NOT_SAME_DESK = "rcdc_terminal_config_full_system_disk_not_same_desk";

    /**
     * 只支持WIN7、WIN10操作系统
     */
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_NOT_SUPPORT_OS = "rcdc_terminal_config_full_system_disk_not_support_os";

    /**
     * 未知异常
     */
    String RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_UNKNOWN = "rcdc_terminal_config_full_system_disk_unknown";

    String SUNNY_REPORT_IMAGE_NOT_FOUND_ERROR = "23200636";

    String RCDC_CLOUDDESKTOP_DESKTOP_SYSTEM_SIZE_MUST_NOT_SMALLER_THAN_IMAGE_TEMPLATE =
            "rcdc_clouddesktop_desktop_system_size_must_not_smaller_than_image_template";


    /**
     * 统计桌面相关信息定时任务
     */
    String RCDC_RCO_QUARTZ_STATISTIC_LAST_HOUR_CONNECT_FAIL_DASHBOARD_DATA = "rcdc_rco_quartz_statistic_last_hour_connect_fail_dashboard_data";
    String RCDC_RCO_QUARTZ_STATISTIC_DESK_POOL_DASHBOARD_DATA_BY_DAY = "rcdc_rco_quartz_statistic_desk_pool_dashboard_data_by_day";
    String RCDC_RCO_QUARTZ_STATISTIC_DESK_USE_RATE_DASHBOARD_DATA = "rcdc_rco_quartz_statistic_desk_use_rate_dashboard_data";
    String RCDC_RCO_QUARTZ_COMPENSATE_DESKTOP_SESSION_LOG = "rcdc_rco_quartz_compensate_desktop_session_log";

    String RCDC_RCO_DESKTOP_QEMU_INFO_INCOMPLETE = "23200637";

    String RCDC_RCO_AD_GROUP_NOT_EXIST = "rcdc_rco_ad_group_not_exist";

    /**
     * 云桌面登录时间限制
     */
    String RCDC_USER_CLOUDDESKTOP_LOGIN_TIME_LIMIT = "rcdc_user_clouddesktop_login_time_limit";

    /**
     * 大屏云桌面状态统计数量异常
     **/
    String RCDC_RCO_BIGSCREEN_SERVER_HOST_COUNT_ERROR = "rcdc_rco_bigscreen_server_host_count_error";

    /**
     * VDI云桌面指定存储池不存在
     */
    String RCDC_RCO_ASSIGNED_STORAGE_POOL_NOT_EXSIT = "23200646";

    /**
     * VDI云桌面指定存储池不可用
     */
    String RCDC_RCO_ASSIGNED_STORAGE_POOL_NOT_AVAILABLE = "23200647";

    /**
     * VDI云桌面指定计算集群不存在
     */
    String RCDC_RCO_ASSIGNED_COMPUTER_CLUSTER_NOT_EXSIT = "23200648";

    /**
     * VDI云桌面指定计算集群不可用
     */
    String RCDC_RCO_ASSIGNED_COMPUTER_CLUSTER_NOT_AVAILABLE = "23200649";

    /**
     * VDI云桌面指定云平台不可用
     */
    String RCDC_RCO_ASSIGNED_CLOUD_PLATFORM_NOT_AVAILABLE = "23201205";



    /**
     * 文件分发
     */
    String RCDC_RCO_FILE_DISTRIBUTION_UPLOADED_FILE_NOT_EXIST = "23200327";
    String RCDC_RCO_FILE_DISTRIBUTION_TARGET_FILE_EXIST = "23200328";
    String RCDC_RCO_FILE_DISTRIBUTION_FILE_MOVE_EXISTS = "23200329";
    String RCDC_RCO_FILE_DISTRIBUTION_FILE_MOVE_SECURITY = "23200330";
    String RCDC_RCO_FILE_DISTRIBUTION_FILE_MOVE_ERROR = "23200331";
    String RCDC_RCO_FILE_DISTRIBUTION_FILE_SIZE_UNEQUAL = "23200332";
    String RCDC_RCO_FILE_DISTRIBUTION_FILE_NOT_EXIST = "23200333";

    String RCDC_RCO_FILE_DISTRIBUTION_FILE_DELETE_FAIL = "23200334";
    String RCDC_RCO_FILE_DISTRIBUTION_FILE_NOT_ALLOW_DELETE = "23200335";

    String RCDC_RCO_FILE_DISTRIBUTION_TASK_PARAMETER_NOT_EXIST = "23200336";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_NOT_EXIST = "23200337";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_NAME_REPEATED = "23200886";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_HAS_INVALIDED = "rcdc_rco_file_distribution_task_has_invalided";
    String RCDC_RCO_FILE_DISTRIBUTION_SUB_TASK_IS_CANCELING = "23200338";
    String RCDC_RCO_FILE_DISTRIBUTION_SUB_TASK_HAS_CANCELED = "23200339";

    String RCDC_RCO_FILE_DISTRIBUTION_TARGET_DESKTOP_IS_DELETED = "23200340";
    String RCDC_RCO_FILE_DISTRIBUTION_APP_TERMINAL_NOT_EXIST = "23200341";

    String RCDC_RCO_FILE_DISTRIBUTION_TASK_FAIL_MSG_TIMEOUT = "rcdc_rco_file_distribution_task_fail_msg_timeout";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_FAIL_MSG_OTHER = "rcdc_rco_file_distribution_task_fail_msg_other";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_FAIL_MSG_SAMBA_UNMOUNT = "23250396";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_FAIL_MSG_STOPPED = "rcdc_rco_file_distribution_task_fail_msg_stopped";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_FAIL_REBUILD_ERROR = "rcdc_rco_file_distribution_task_fail_rebuild_error";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_REPORT_RESOLVE_FAIL = "23200342";
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_REPORT_RESULT_UNKNOWN = "23200343";

    String RCDC_RCO_FILE_DISTRIBUTION_TASK_DELETE_FAIL_SUBTASK_RUNNING = "23200346";
    String RCDC_RCO_TERMINAL_DESKTOP_NOT_EXIST = "rcdc_rco_terminal_desktop_not_exist";
    String RCDC_TERMINAL_SYNC_USER_INFO_FAIL = "23200344";

    String RCDC_RCO_EXPORT_USER_DATA_NOT_EXIST = "23200345";
    /**
     * 文件分发任务中存做种失败的文件，任务分发失败
     */
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_HAS_MAKE_SEED_FAIL = "rcdc_rco_file_distribution_task_has_make_seed_fail";

    /**
     * 文件分发任务中文件缺失
     */
    String RCDC_RCO_FILE_DISTRIBUTION_TASK_FILE_HIATUS = "rcdc_rco_file_distribution_task_file_hiatus";

    /**
     * 终端在线总时长定时器
     */
    String TERMINAL_ONLINE_TIME_RECORD_TASK = "terminal_online_time_record_task";

    String FILE_DISTRIBUTE_STASHED_TASK = "file_distribute_stashed_task";

    /**
     * 镜像D盘:
     *
     */
    String RCDC_RCO_NOT_ALLOW_UPDATE_DATA_DISK_WITH_OLD_NOT_EXIST = "23200313";

    /**
     * 已被交付组[0]使用
     */
    String RCDC_RCO_UAM_USED_BY_DELIVERY_GROUP = "rcdc_rco_uam_used_by_delivery_group";

    /**
     * 已被测试组[0]使用
     */
    String RCDC_RCO_UAM_USED_BY_TEST_GROUP = "rcdc_rco_uam_used_by_test_group";

    /**
     * 桌面[0]当前是关闭关闭状态
     */
    String RCDC_RCO_UAM_TEST_GROUP_CLOUD_DESKTOP_NOT_RUNNING = "rcdc_rco_uam_test_group_cloud_desktop_not_running";
    String RCDC_RCO_UAM_TEST_GROUP_TERMINAL_NO_ONLINE = "rcdc_rco_uam_test_group_terminal_no_online";

    String RCDC_RCO_UAM_DELETE_DELIVERY_DESKTOP = "rcdc_rco_uam_delete_delivery_desktop";

    String RCDC_RCO_BUSINESS_COMMON_MESSAGE_KEY = "rcdc_rco_business_common_message_key";

    String RCDC_RCO_UAM_DELETE_TEST_TARGET = "rcdc_rco_uam_delete_test_target";

    /**
     * 操作系统版本号不存在
     */
    String RCDC_RCO_UAM_CLOUD_DESKTOP_OS_VERSION_NOT_EXISTS = "rcdc_rco_uam_cloud_desktop_os_version_not_exists";

    /**
     * 操作系统版本号不一致
     */
    String RCDC_RCO_UAM_CLOUD_DESKTOP_OS_VERSION_INCONSISTENT = "rcdc_rco_uam_cloud_desktop_os_version_inconsistent";


    String RCDC_CLOUDDESKTOP_STRATEGY_NOT_AVAILABLE = "23200752";
    String RCDC_CLOUDDESKTOP_STRATEGY_USER_SNAPHOST_NUM_LIMIT = "23200728";
    /** 策略的系统盘和个人盘不能小于原来的值 */
    String RCDC_CLOUDDESKTOP_STRATEGY_SYSTEM_PERSON_SIZE_LESS = "23200731";
    String RCO_DESK_STRATEGY_HAD_DATA_DISK_CANNOT_CLOSE = "23200830";
    String RCO_DESK_STRATEGY_DYNAMIC_POOL_CANNOT_OPEN_DISK = "23200831";
    String RCDC_DESK_STRATEGY_UPDATE_BIND_IMAGE_GPU_NOT_SUPPORT = "23200832";
    String RCO_DESK_STRATEGY_VGPU_OPTIONS_NOT_EXIST = "23200827";
    String RCO_DESK_STRATEGY_SYNC_NOT_SUPPORTED = "23200833";
    String RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST = "23200727";
    String RCDC_CLOUDDESKTOP_STRATEGY_ENABLE_AUDIT_FILE_AND_ENABLE_STORAGE = "23200733";
    String RCDC_CLOUDDESKTOP_STRATEGY_ENABLE_AUDIT_PRINTER_AND_ENABLE_STORAGE = "23200734";
    String RCDC_CLOUDDESKTOP_STRATEGY_CLIP_BOARD_SUPPORT_TYPE_NOT_EMPTY = "23200732";
    String RCDC_CLOUDDESKTOP_STRATEGY_NOT_SUPPORT_SERVER_MODEL = "23200735";
    String RCDC_CLOUDDESKTOP_STRATEGY_UNOPENED_AD_AUTO_JOIN = "23200736";
    String RCDC_CLOUDDESKTOP_STRATEGY_NOT_SUPPORT_CREATE_APP_LAYER = "23200737";


    String RCDC_RCO_QUARTZ_DESKTOP_LICENSE_USED_INFO_STAT = "rcdc_rco_quartz_desktop_license_used_info_stat";
    String RCDC_RCO_QUARTZ_DESKTOP_LICENSE_USED_INFO_STAT_CLEAN = "rcdc_rco_quartz_desktop_license_used_info_stat_clean";


    /**
     * 云桌面GT状态异常
     */
    String RCDC_CLOUDDESKTOP_GUESTTOOL_STATUS_EXCEPT_ALARM_NAME = "rcdc_clouddesktop_guesttool_status_except_alarm_name";

    /**
     * 云桌面GT状态异常 告警内容
     */
    String RCDC_CLOUDDESKTOP_GUESTTOOL_STATUS_EXCEPT_ALARM_CONTENT = "rcdc_clouddesktop_guesttool_status_except_alarm_content";

    String RCDC_CLOUDDESKTOP_GUESTTOOL_MODULE_STATUS_ALARM_NAME = "rcdc_clouddesktop_guesttool_module_status_alarm_name";
    String RCDC_CLOUDDESKTOP_GUESTTOOL_MODULE_STATUS_ERROR = "rcdc_clouddesktop_guesttool_module_status_error";
    String RCDC_CLOUDDESKTOP_GUESTTOOL_MODULE_STATUS_ALARM_CONTENT = "rcdc_clouddesktop_guesttool_module_status_alarm_content";

    /**
     * 定时检查 在线VDI 桌面GT在线情况
     */
    String RCDC_RCO_QUARTZ_DESKTOP_GUEST_TOOL_CHECK = "rcdc_rco_quartz_desktop_guest_tool_check";

    /**
     * 关联用户
     */
    String RCDC_RCO_DESKTOP_BIND_USER_ERROR_NOT_STATIC_POOL_DESK = "23200651";
    String RCDC_RCO_DESKTOP_BIND_USER_DESKTOP_MUST_CLOSE = "23200652";

    /**
     * 取消关联
     */
    String RCDC_RCO_DESKTOP_UNBIND_USER_DESKTOP_MUST_CLOSE = "23200653";
    String RCDC_RCO_DESKTOP_UNBIND_USER_ERROR_NOT_STATIC_POOL_DESK = "23200654";


    String RCDC_RCO_CERTIFICATION_STRATEGY_NOT_FOUND = "23200650";

    String RCDC_RCO_RADIUS_CERTIFICATION_NOT_OPEN = "rcdc_rco_radius_certification_not_open";

    String RCDC_RCO_RADIUS_CERTIFICATION_CONFIG_ERROR = "rcdc_rco_radius_certification_config_error";

    String RCDC_RCO_RADIUS_CERTIFICATION_CODE_ERROR = "rcdc_rco_radius_certification_code_error";

    String RCDC_RCO_RADIUS_CERTIFICATION_SERVER_ERROR = "rcdc_rco_radius_certification_server_error";
    /**
     * 指定桌面池恢复回收站桌面
     */
    String RCDC_RCO_RECYCLE_BIN_DESKTOP_POOL_ID_NULL = "23200655";
    String RCDC_RCO_RECYCLE_BIN_DESKTOP_POOL_NOT_EXISTS = "23200656";
    String RCDC_RCO_RECYCLE_BIN_DESKTOP_POOL_DELETING = "23200657";
    String RCDC_RCO_RECYCLE_BIN_ASSIGN_USER_ERROR_POOL_DESKTOP = "23200658";
    String RCDC_RCO_RECYCLE_BIN_USER_HAD_DESK_IN_POOL = "23200659";

    String RCDC_RCO_EXPORT_TASK_NOT_EMPTY = "23200660";

    /**
     * 数据导出不存在
     */
    String RCDC_RCO_EXPORT_EXCEL_DATA_NOT_EXIST = "23200661";

    /**
     * 数据导出文件不存在
     */
    String RCDC_RCO_EXPORT_EXCEL_FILE_NOT_EXIST = "23200662";


    String RCDC_QUARTZ_CLEAR_USER_LOGIN_RECORD_LOG = "rcdc_quartz_clear_user_login_record_log";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_PWD_ERROR = "rcdc_user_self_operation_pwd_confirm_pwd_error";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_AD_ERROR = "rcdc_user_self_operation_pwd_confirm_ad_error";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_USER_FORBIDDEN = "rcdc_user_self_operation_pwd_confirm_user_forbidden";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_LOGIN_LIMIT = "rcdc_user_self_operation_pwd_confirm_login_limit";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_AD_ACCOUNT_EXPIRED = "rcdc_user_self_operation_pwd_confirm_ad_account_expired";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_LDAP_ERROR = "rcdc_user_self_operation_pwd_confirm_ldap_error";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_USER_LOCKED = "rcdc_user_self_operation_pwd_confirm_user_locked";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_PWD_ERROR_WITH_REMIND_TIMES = "rcdc_user_self_operation_pwd_confirm_pwd_error_with_remind_times";

    String RCDC_USER_SELF_OPERATION_PWD_CONFIRM_OTHER_ERROR = "rcdc_user_self_operation_pwd_confirm_other_error";




    String RCDC_RCO_NOTIFY_USER_UPDATE_PWD_FAIL = "rcdc_rco_notify_user_update_pwd_fail";

    String RCDC_RCO_NOT_CONNECT_RCCM = "rcdc_rco_not_connect_rccm";

    String RCDC_RCO_NOTIFY_USER_UPDATE_PWD_SUCCESS = "rcdc_rco_notify_user_update_pwd_success";

    String RCDC_RCO_SYNC_USER_IDENTITY_SUCCESS = "rcdc_rco_sync_user_identity_success";

    String RCDC_RCO_SYNC_USER_IDENTITY_FAIL = "rcdc_rco_sync_user_identity_fail";

    String RCDC_RCO_UNIFIED_LOGIN_RESULT = "rcdc_rco_unified_login_result";

    /**
     * 第三方管理员登录
     */
    String RCDC_RCO_AAA_ADMIN_THIRD_PARTY_CONFIG_UNABLE = "23200663";
    String RCDC_RCO_AAA_ADMIN_THIRD_PARTY_USER_IDCONFIG_UNABLE = "23200664";
    String RCDC_RCO_AAA_ADMIN_THIRD_PARTY_LOGIN_ERROR = "rcdc_rco_aaa_admin_third_party_login_error";

    String CHECK_THIRD_PARTY_CODE_FAIL = "check_third_party_code_fail";

    String RCDC_USER_TERMINAL_OFFLINE_NOT_ALLOW_CHANGE_SERVER_IP = "23200794";

    String RCDC_USER_TERMINAL_CHANGE_SERVER_IP_SETTING_FAIL = "23200795";
    String RCDC_USER_TERMINAL_CHANGE_SERVER_IP_FAIL = "23200796";

    // 修改服务器IP后配置一键安装
    String RCDC_RCO_AUTO_CONFIG_ONE_CLICK_INSTALL_SUCCESS_LOG = "rcdc_rco_auto_config_one_click_install_success_log";
    String RCDC_RCO_AUTO_CONFIG_ONE_CLICK_INSTALL_NAME = "rcdc_rco_auto_config_one_click_install_name";
    String RCDC_RCO_AUTO_CONFIG_ONE_CLICK_INSTALL_TIP = "rcdc_rco_auto_config_one_click_install_tip";

    String RCDC_RCO_CHECK_DESKTOP_PORT_EXCEPTION = "rcdc_rco_check_desktop_port_exception";

    // UAM应用软件冲突告警
    String RCDC_CLOUDDESKTOP_DESK_SOFT_CONFLICT_ALARM_CODE = "rcdc_clouddesktop_desk_soft_conflict_alarm_code";
    String RCDC_CLOUDDESKTOP_DESK_SOFT_CONFLICT = "rcdc_clouddesktop_desk_soft_conflict";
    String RCDC_CLOUDDESKTOP_DESK_SOFT_CONFLICT_ALARM_NAME = "rcdc_clouddesktop_desk_soft_conflict_alarm_name";

    String RCDC_RCO_DESK_SOFT_HAS_BE_DELETE = "23200665";

    String RCDC_RCO_DESK_SOFT_MESSAGE_TO_GT_TOO_LONG = "23200666";

    // 终端软硬件解耦合
    String RCDC_RCO_TERMINAL_CONFIG_IMPORT_WITH_DESK_RUNNING = "rcdc_rco_terminal_config_import_with_desk_running";

    String RCDC_RCO_DESK_NOT_ALLOW_EDIT_ROOT_PWD_CONFIG = "23200668";
    String RCDC_RCO_RECOVER_POOL_DESK_ADD_USER = "rcdc_rco_recover_pool_desk_add_user";
    String RCDC_RCO_USER_RECORD_EXPORT_TASK_FAIL = "23200797";

    String RCDC_RCO_NOTIFY_LOGIN_TERMINAL_CHANGE_CAN_NOT_OPEN_BY_ISUNIFIEDLOGIN =
            "23200798";

    String RCDC_RCO_TERMINAL_HAS_DOWNLOAD_IMAGE = "23200805";

    String RCDC_RCO_EDIT_IMAGE_TERMINAL_REMOTE_EDIT_IMAGE = "23200806";
    String RCDC_RCO_EDIT_IMAGE_TERMINAL_DESK_RUNNING = "23200807";
    String RCDC_RCO_EDIT_IMAGE_TERMINAL_LOCAL_EDIT_IMAGE = "23200808";
    String RCDC_RCO_EDIT_IMAGE_TERMINAL_ERROR = "23200809";


    String RCDC_RCO_USER_SNAPSHOT_LOGIN_ERROR = "23200667";

    String TERMINAL_IP_ILLEGAL = "23200669";

    String RCDC_TERMINAL_CONFIG_START_MODE_FAIL_PLATFORM_ERROR = "rcdc_terminal_config_start_mode_fail_platform_error";

    String RCDC_TERMINAL_CONFIG_START_MODE_FAIL_TERMINAL_NOT_EXIST = "rcdc_terminal_config_start_mode_fail_terminal_not_exist";

    String RCDC_NOTIFY_TERMINAL_RESPONSE_FAIL = "rcdc_notify_terminal_response_fail";

    String RCDC_TERMINAL_CONFIG_START_MODE_FAIL_NOT_SUPPORT_TC = "rcdc_terminal_config_start_mode_fail_not_support_tc";

    String RCDC_TERMINAL_CODE_SUCCESS = "rcdc_terminal_code_success";

    String RCDC_RCO_USER_CHANGE_PWD_FAIL = "rcdc_rco_user_change_pwd_fail";
    String RCDC_TERMINAL_CODE_NOT_TCI = "rcdc_terminal_code_not_tci";

    String RCDC_TERMINAL_CODE_NOT_SUPPORT_TC = "rcdc_terminal_code_not_support_tc";

    String RCDC_TERMINAL_CODE_EDITING = "rcdc_terminal_code_not_editing";

    String RCDC_TERMINAL_CODE_IMAGE_FIXING = "rcdc_terminal_code_image_fixing";

    String RCDC_TERMINAL_CODE_OTHER = "rcdc_terminal_code_other";

    String RCDC_CONFIG_START_CODE_NOT_FIND = "rcdc_config_start_code_not_find";

    String RCDC_TERMINAL_CODE_WIN732_NOT_SUPPORT_TC = "rcdc_terminal_code_win732_not_support_tc";

    String RCDC_TERMINAL_CODE_INITIALING = "rcdc_terminal_code_initialing";

    String RCDC_TERMINAL_CODE_SWITCHING_START_MODE = "rcdc_terminal_code_switching_start_mode";

    String RCO_DESK_STRATEGY_AGREEMENT_PARAMETER_ERROR = "23201171";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_PARAMETER_ERROR = "23201172";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_ERROR = "23201173";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_SOFTWARE_ERROR = "23201174";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_BROWSER_ERROR = "23201175";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_SUPPORT_PATTERN = "23201177";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_REPEAT = "23201179";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ENCRYPTION_NOT_EXIST = "23201180";
    String RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_SUPPORT_SESSION = "23201188";


    String RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR = "23201181";
    String RCO_DESK_STRATEGY_USB_BANDWIDTH_INFO_NOT_NULL = "23201183";

    String RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK = "23201531";
    String RCDC_CLOUDDESKTOP_STRATEGY_NOT_VDI_MUST_SYSTEM_DISK = "23201536";

    String RCDC_RCO_COMPUTER_NOT_ALLOW_DELETE = "23201413";

    String RCDC_RCO_COMPUTER_EXISTS_WAKE_UP_TASK = "23201415";

    String RCDC_RCO_COMPUTER_WAKE_UP_TIMEOUT = "23201416";

    String RCDC_RCO_COMPUTER_CONNECT_MOT_VDI_SERVER = "23201438";

    String RCDC_RCO_EXIST_SESSION_NOT_ALLOW_DELETE = "23201439";

    String RCDC_RCO_DESKTOP_HAS_SESSION_NOT_CANCEL = "23201445";


    String RCD_RCO_SHINE_START_VM_HEST_ERR = "23201447";

    String RCDC_COMPUTER_MOVE_GROUP_BIND_DESKTOP_POOL_FAIL = "rcdc_computer_move_group_bind_desktop_pool_fail";

    String RCDC_COMPUTER_MOVE_GROUP_BIND_DESKTOP_POOL_SUCCESS = "rcdc_computer_move_group_bind_desktop_pool_success";

    String RCDC_RCO_USER_LOGIN_USER_LOCKED = "rcdc_rco_user_login_user_locked";

    String RCDC_RCO_USER_LOGIN_AD_ACCOUNT_DISABLE = "rcdc_rco_user_login_ad_account_disable";


    String RCDC_RCO_HARDWARE_OVER_MAX = "rcdc_rco_hardware_over_max";

    String RCDC_RCO_HARDWARE_PENDING_APPROVE = "rcdc_rco_hardware_pending_approve";

    String RCDC_RCO_HARDWARE_REJECTED = "rcdc_rco_hardware_rejected";

    /**
     * 执行重启云桌面[{0}]失败,不支持的云桌面类型[{1}]
     */
    String RCDC_RCO_DESKTOP_REBOOT_FAIL_NOT_SUPPORT_DESKTOP_TYPE = "23201651";

    /**
     * 执行重启云桌面[{0}]失败,不支持的多会话类型云桌面
     */
    String RCDC_RCO_DESKTOP_REBOOT_FAIL_NOT_SUPPORT_MULTIPLE_SESSION_DESKTOP = "23201652";

    String RCDC_RCO_USER_LOGIN_AD_PASSWORD_ERROR = "23201141";

    /**
     * 应用池预启动告警
     */
    String RCDC_RCO_APP_POOL_QUARTZ_ALARM_NAME = "rcdc_rco_app_pool_quartz_alarm_name";
    String RCDC_RCO_APP_POOL_QUARTZ_ALARM_CONTENT = "rcdc_rco_app_pool_quartz_alarm_content";

    String RCDC_RCO_CLIENT_QUARTZ_CLEAR_CLIENT_OPT_LOG = "rcdc_rco_client_quartz_clear_client_opt_log";
    String RCDC_RCO_USER_NAME_IS_TOO_LONG = "rcdc_rco_user_name_is_too_long";
    String RCDC_RCO_USER_NAME_IS_TOO_LONG_NOT_ALLOW_USED_MULTI_DESKTOP = "rcdc_rco_user_name_is_too_long_not_allow_user_multi_desktop";

    String RCDC_RCO_GLOBAL_WATERMARK_CONFIG_NOT_EXIST = "rcdc_rco_global_watermark_config_not_exist";

    String RCDC_RCO_COMPUTER_BIND_USER_POOL_DESK_ADD_USER = "rcdc_rco_computer_bind_user_pool_desk_add_user";
    String RCDC_RCO_COMPUTER_GT_LOG_COMPUTER_NOT_RUNNING = "23201450";
    String RCDC_RCO_COMPUTER_IS_THIRD = "23201451";
}
