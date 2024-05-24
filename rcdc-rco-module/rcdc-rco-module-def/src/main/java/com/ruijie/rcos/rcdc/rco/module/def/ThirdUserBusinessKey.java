package com.ruijie.rcos.rcdc.rco.module.def;

/**
 * Description: 抽取部分提示语，上层需要根据提示语进行加工。例如分级分权等不同提示。
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月25日
 *
 * @author xiejian
 */
public interface ThirdUserBusinessKey {

    String RCDC_USER_USERGROUP_NUM_OVER = "rcdc_user_usergroup_num_over";

    String RCDC_USER_SUB_USERGROUP_NUM_OVER = "rcdc_user_sub_usergroup_num_over";

    String RCDC_USER_USERGROUP_HAS_DUPLICATION_NAME = "rcdc_user_usergroup_has_duplication_name";

    String RCDC_USER_DELETE_USER_GROUP_SUB_GROUP_NAME_DUPLICATION_WITH_MOVE_SUB_GROUP =
        "rcdc_user_delete_user_group_sub_group_name_duplication_with_move_sub_group";

    String DEFAULT_EMPTY_USERNAME = "--";



    /**
     * 第三方用户相关错误码
     */
    /**
     * 第三方认证平台配置未开启
     */
    String RCDC_USER_THIRD_PARTY_CONFIG_UNABLE = "rcdc_user_third_party_config_unable";
    /**
     * 第三方认证用户同步未开启
     */
    String RCDC_USER_THIRD_PARTY_CONFIG_SYNC_USER_UNABLE = "rcdc_user_third_party_config_sync_user_unable";
    /**
     * 第三方用户同步任务名称
     */
    String RCDC_USER_THIRD_PARTY_USER_SYNC_TASK_NAME = "rcdc_user_third_party_user_sync_task_name";

    /**
     * 第三方用户同步任务描述
     */
    String RCDC_USER_THIRD_PARTY_USER_SYNC_TASK_DESCRIPTION = "rcdc_user_third_party_user_sync_task_description";

    /**
     * 第三方用户同步任务告警
     */
    String RCDC_USER_THIRD_PARTY_USER_SYNC_ALARM_NAME = "rcdc_user_third_party_user_sync_alarm_name";

    /**
     * 第三方用户同步任务告警内容
     */
    String RCDC_USER_THIRD_PARTY_USER_SYNC_ALARM_CONTENT = "rcdc_user_third_party_user_sync_alarm_content";
    /**
     * 定时同步第三方用户成功
     */
    String RCDC_RCO_THIRD_PARTY_SCHEDULE_SYNC_SUCCESS = "rcdc_user_third_party_schedule_sync_success";
    /**
     * 定时同步第三方用户失败
     */
    String RCDC_RCO_THIRD_PARTY_SCHEDULE_SYNC_FAIL = "rcdc_user_third_party_schedule_sync_fail";
    /**
     * 第三方用户同步任务已经存在
     */
    String RCDC_USER_THIRD_PARTY_USER_SYNC_TASK_HAS_EXIST = "rcdc_user_third_party_user_sync_task_has_exist";

    /**
     * 第三方用户登录测试失败
     */
    String RCDC_USER_THIRD_PARTY_USER_TEST_LOGIN_FAIL = "rcdc_user_third_party_user_test_login_fail";

    /**
     * 第三方用户列表为空
     */
    String RCDC_USER_THIRD_PARTY_USER_IS_EMPTY = "rcdc_user_third_party_user_is_empty";

    /**
     * 第三方用户同步失败
     */
    String RCDC_USER_THIRD_PARTY_SYNC_USER_ERROR = "rcdc_user_third_party_sync_user_error";

    /**
     * 批量同步第三方用户任务名称
     */
    String RCDC_USER_THIRD_PARTY_SYNC_BATCH_TASK_NAME = "rcdc_user_third_party_sync_batch_task_name";

    /**
     * 批量同步第三方用户任务描述
     */
    String RCDC_USER_THIRD_PARTY_SYNC_BATCH_TASK_DESC = "rcdc_user_third_party_sync_batch_task_desc";

    /**
     * 批任务同步成功
     */
    String RCDC_USER_THIRD_PARTY_SYNC_USER_SUCCESS = "rcdc_user_third_party_sync_user_success";
    /**
     * 批任务同步失败
     */
    String RCDC_USER_THIRD_PARTY_SYNC_USER_FAIL = "rcdc_user_third_party_sync_user_fail";
    /**
     * 批任务同步单个用户失败
     */
    String RCDC_USER_THIRD_PARTY_SYNC_USER_RESULT = "rcdc_user_third_party_sync_user_result";
    /**
     * 第三方用户同步错误
     */
    String RCDC_USER_THIRD_PARTY_USER_SYNC_ERROR = "rcdc_user_third_party_user_sync_error";
    /**
     * 第三方平台连接失败
     */
    String RCDC_USER_THIRD_PARTY_TEST_CONNECT_ERROR = "rcdc_user_third_party_test_connect_error";
    /**
     * 第三方平台的用户名为空
     */
    String RCDC_USER_THIRD_PARTY_USER_NAME_EMPTY = "rcdc_user_third_party_user_name_empty";
    /**
     * 第三方平台的用户名过长
     */
    String RCDC_USER_THIRD_PARTY_USER_NAME_TOO_LONG = "rcdc_user_third_party_user_name_too_long";
    /**
     * 本地存在用户，跳过同步
     */
    String RCDC_USER_THIRD_PARTY_LOCAL_USER_HAS_EXIST = "rcdc_user_third_party_local_user_has_exist";
    /**
     * 访客用户不可同步为第三方用户
     */
    String RCDC_USER_THIRD_PARTY_VISITOR_AD_LDAP_CANT_SYNC = "rcdc_user_third_party_visitor_ad_ldap_cant_sync";
    /**
     * 第三方用户，获取用户地址有误
     */
    String RCDC_USER_THIRD_PARTY_GET_USER_URL_INVALID = "rcdc_user_third_party_get_user_url_invalid";

    /**
     * 第三方用户，获取用户请求体有误
     */
    String RCDC_USER_THIRD_PARTY_GET_USER_BODY_INVALID = "rcdc_user_third_party_get_user_body_invalid";
    /**
     * 第三方用户，获取用户发生异常
     */
    String RCDC_USER_THIRD_PARTY_GET_USER_ERROR = "rcdc_user_third_party_get_user_error";

    /**
     * 第三方认证平台配置未开启
     */
    String RCDC_USER_THIRD_PARTY_CONNECTED_UNABLE = "rcdc_user_third_party_connected_unable";

    /**
     * 第三方认证同步任务名
     */
    String RCDC_USER_THIRD_PARTY_SYNC_USER_TASK_NAME = "rcdc_user_third_party_sync_user_task_name";

    String RCDC_USER_THIRD_PARTY_ROOT_USER_GROUP_NAME = "rcdc_user_third_party_root_user_group_name";
}
