package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月21日
 * 
 * @author zhuangchenwu
 */
public interface AaaBusinessKey {
    /** 管理员操作 **/
    String RCDC_AAA_ADMIN_CREATE_ADMIN_SUCCESS = "rcdc_aaa_admin_create_admin_success";
    /**
     * 创建管理员[{0}]失败，失败原因：{1}
     */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_FAIL = "rcdc_aaa_admin_create_admin_fail";

    /**
     * 升级管理员
     */
    String RCDC_AAA_ADMIN_UPGRADE_ADMIN_SUCCESS = "rcdc_aaa_admin_upgrade_admin_success";
    /**
     * 设置为管理员[{0}]失败，失败原因：{1}
     */
    String RCDC_AAA_ADMIN_UPGRADE_ADMIN_FAIL = "rcdc_aaa_admin_upgrade_admin_fail";

    /**
     * 管理员名称不可重名，且不能是：（超级管理员、系统管理员、安全管理员、审计管理员）
     */
    String RCDC_AAA_ADMIN_NAME_IS_DEFAULT_ADMIN_NAME = "23200670";

    /**
     * 不可编辑内置管理员
     */
    String RCDC_AAA_ADMIN_NOT_ALLOW_EDIT_DEFAULT_ADMIN = "23200671";

    /**
     * 仅内置超级管理员admin允许创建超级管理员
     */
    String RCDC_AAA_NOT_DEFAULT_ADMIN_NOT_ALLOW_CREATE_SUPERADMIN = "23200672";

    /**
     * AD域或者LDAP域用户升级为管理员，不允许修改状态
     */
    String RCDC_AAA_AD_USER_UPGRADE_ADMIN_CAN_NOT_EDIT_STATUS = "23200673";

    String RCDC_AAA_ADMIN_EDIT_ADMIN_SUCCESS = "rcdc_aaa_admin_edit_admin_success";
    String RCDC_AAA_ADMIN_EDIT_ADMIN_DATA_PERMISSION_SUCCESS = "rcdc_aaa_admin_edit_admin_data_permission_success";
    String RCDC_AAA_ADMIN_EDIT_ADMIN_FAIL = "rcdc_aaa_admin_edit_admin_fail";
    String RCDC_AAA_ADMIN_EDIT_STATE_TASK_EXCLUDE_EXECUTE_EXCEPTION_TIP = "rcdc_aaa_admin_edit_state_task_exclude_execute_exception_tip";

    String RCDC_AAA_ADMIN_DELETE_ADMIN_SUCCESS = "rcdc_aaa_admin_delete_admin_success";
    String RCDC_AAA_ADMIN_DELETE_ADMIN_FAIL = "rcdc_aaa_admin_delete_admin_fail";

    String RCDC_AAA_ADMIN_DELETE_ADMIN_DO_SUCCESS = "rcdc_aaa_admin_delete_admin_do_success";
    String RCDC_AAA_ADMIN_DELETE_ADMIN_DO_FAIL = "rcdc_aaa_admin_delete_admin_do_fail";

    String RCDC_AAA_ADMIN_BATCH_DELETE_ADMIN_TASK_NAME = "rcdc_aaa_admin_batch_delete_admin_task_name";
    String RCDC_AAA_ADMIN_BATCH_DELETE_ADMIN_TASK_DESC = "rcdc_aaa_admin_batch_delete_admin_task_desc";
    String RCDC_AAA_ADMIN_BATCH_DELETE_ADMIN_SINGLE_TASK_NAME = "rcdc_aaa_admin_batch_delete_admin_single_task_name";
    String RCDC_AAA_ADMIN_BATCH_DELETE_ADMIN_SINGLE_TASK_DESC = "rcdc_aaa_admin_batch_delete_admin_single_task_desc";
    String RCDC_AAA_ADMIN_BATCH_DELETE_ADMIN_TASK_RESULT = "rcdc_aaa_admin_batch_delete_admin_task_result";

    String RCDC_AAA_ADMIN_BATCH_DELETE_TASK_ITEM_NAME = "rcdc_aaa_admin_batch_delete_task_item_name";
    String RCDC_AAA_ADMIN_BATCH_DELETE_TASK_ITEM_SUCCESS = "rcdc_aaa_admin_batch_delete_task_item_success";
    String RCDC_AAA_ADMIN_BATCH_DELETE_TASK_ITEM_FAIL = "rcdc_aaa_admin_batch_delete_task_item_fail";
    String RCDC_AAA_ADMIN_NOT_EXIST_UWS = "23200674";

    /** 管理员密码 **/
    String RCDC_AAA_ADMIN_RESET_ADMIN_PASSWORD_SUCCESS = "rcdc_aaa_admin_reset_admin_password_success";
    String RCDC_AAA_ADMIN_RESET_ADMIN_PASSWORD_FAIL = "rcdc_aaa_admin_reset_admin_password_fail";

    String RCDC_AAA_ADMIN_EDIT_ADMIN_PASSWORD_SUCCESS = "rcdc_aaa_admin_edit_admin_password_success";
    String RCDC_AAA_ADMIN_EDIT_ADMIN_PASSWORD_FAIL = "rcdc_aaa_admin_edit_admin_password_fail";

    String RCDC_AAA_ADMIN_INVALID_PASSWORD = "23200675";
    String RCDC_AAA_ADMIN_NAME_SHOULD_NOT_EQUAL_PASSWORD = "23200676";
    /** 管理登陆退出 **/
    String RCDC_AAA_ADMIN_LOGIN_SUCCESS = "rcdc_aaa_admin_login_success";
    String RCDC_AAA_ADMIN_LOGIN_FAIL = "rcdc_aaa_admin_login_fail";
    String RCDC_AAA_ADMIN_LOGIN_FAIL_BY_ADMIN_NAME = "rcdc_aaa_admin_login_fail_by_admin_name";
    String RCDC_AAA_ADMIN_MODIFY_PWD_FAIL_BY_ADMIN_NAME = "rcdc_aaa_admin_modify_pwd_fail_by_admin_name";

    String RCDC_AAA_ADMIN_LOGOUT_SUCCESS = "rcdc_aaa_admin_logout_success";
    String RCDC_AAA_ADMIN_LOGOUT_FAIL = "rcdc_aaa_admin_logout_fail";

    /****/
    String RCDC_AAA_OPERATOR_SUCCESS = "rcdc_aaa_operator_success";
    String RCDC_AAA_OPERATOR_FAIL = "rcdc_aaa_operator_fail";

    // 前端交互提示语国际化
    /** 管理员未登录 */
    String RCDC_AAA_ADMIN_NOT_LOGIN = "23200677";

    /** 管理员密码错误 */
    String RCDC_AAA_ADMIN_PWD_NOT_CORRECT = "rcdc_aaa_admin_pwd_not_correct";

    /** 管理员已登录 */
    String RCDC_AAA_ADMIN_ALREADY_LOGIN_CURRENT_TIME = "23200678";

    /** 禁止删除当前登录的管理员 */
    String RCDC_AAA_ADMIN_DELETE_CURRENT_LOGIN_FORBID = "23200679";

    /** 管理员退出系统成功（操作日志） */
    String RCDC_AAA_ADMIN_LOGOUT_KICKOUT_LOG = "rcdc_aaa_admin_logout_kickout_log";

    /** 删除管理员失败（操作日志） */
    String RCDC_AAA_ADMIN_DELETE_ADMIN_FAIL_LOG = "rcdc_aaa_admin_delete_admin_fail_log";

    /** 启用管理员成功（操作日志） */
    String RCDC_AAA_ADMIN_ENABLE_ADMIN_LOG = "rcdc_aaa_admin_enable_admin_log";

    /** 启用管理员失败（操作日志） */
    String RCDC_AAA_ADMIN_ENABLE_ADMIN_FAIL_LOG = "rcdc_aaa_admin_enable_admin_fail_log";

    /** 禁用管理员成功（操作日志） */
    String RCDC_AAA_ADMIN_DISABLE_ADMIN_LOG = "rcdc_aaa_admin_disable_admin_log";

    /** 禁用管理员失败（操作日志） */
    String RCDC_AAA_ADMIN_DISABLE_ADMIN_FAIL_LOG = "rcdc_aaa_admin_disable_admin_fail_log";

    /** 初始化失败 **/
    String RCDC_AAA_ADMIN_INIT_ERROR = "23200680";

    /** 管理员已启用 */
    String RCDC_AAA_ADMIN_ALREAD_ENABLE_ADMIN_ERROR = "23200681";

    /** 管理员已禁用 */
    String RCDC_AAA_ADMIN_ALREAD_DISABLE_ADMIN_ERROR = "23200682";


    /** 管理员不能操作自身 */
    String RCDC_AAA_ADMIN_CAN_NOT_OPRATE_YOUSELF = "23200683";

    /**
     * 管理员不能对同级别管理员进行操作
     */
    String RCDC_AAA_ADMIN_CAN_NOT_OPRATE_SAME_LEVEL_ADMIN = "23200684";

    /** 当前管理员不能操作 */
    String RCDC_AAA_ADMIN_CAN_NOT_OPRATE = "23200685";

    /** 当前接口不能重复操作 */
    String RCDC_AAA_ADMIN_CAN_NOT_OPRATE_TWICE = "23200686";

    /** 角色信息异常 */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_ROLE_ERROR = "23200687";

    /** 菜单信息异常 */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_MENU_ERROR = "23200688";

    /**
     * 菜单必须包含必选菜单
     */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_DEFAULT_MENU_ERROR = "rcdc_aaa_admin_create_admin_valid_default_menu_error";

    /**
     * 用户组ID信息异常
     */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_USER_GROUP_ID_ERROR = "23200689";

    /** 用户组ID必须包含未分组 */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_DEFAULT_USER_GROUP_ID_ERROR = "23200690";

    /** 终端组ID信息异常 */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_TERMINAL_GROUP_ID_ERROR = "23200691";

    /** 终端组ID必须包含未分组 */
    String RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_DEFAULT_TERMINAL_GROUP_ID_ERROR = "23200692";
    /**
     * 设置日志周期打印操作日志
     */
    String RCDC_RCO_SET_LOG_INTERVAL_SUCCESS_LOG = "rcdc_rco_set_log_interval_success_log";

    String RCDC_RCO_SET_LOG_INTERVAL_FAIL_LOG = "rcdc_rco_set_log_interval_fail_log";

    /**
     * 邮箱服务器配置错误信息
     */
    String RCDC_RCO_MAIL_CONFIG_ERROR = "rcdc_rco_mail_config_error";
    String RCDC_RCO_MAIL_SEND_ERROR = "rcdc_rco_mail_send_error";


    /** 角色模块 */
    //角色创建
    String RCDC_AAA_ROLE_CREATE_SUCCESS = "rcdc_aaa_role_create_success";
    String RCDC_AAA_ROLE_CREATE_FAIL = "rcdc_aaa_role_create_fail";
    String RCDC_AAA_ROLE_NAME_IS_DEFAULT_NAME = "23200693";
    String RCDC_AAA_ROLE_EDIT_SUCCESS = "rcdc_aaa_role_edit_success";
    String RCDC_AAA_ROLE_EDIT_FAIL = "rcdc_aaa_role_edit_fail";
    // 编码角色，角色不存在或已被删除
    String RCDC_AAA_ROLE_EDIT_ROLE_NOT_EXIST = "rcdc-aaa_role_edit_role_not_exist";
    String RCDC_AAA_ROLE_EDIT_STATE_TASK_EXCLUDE_EXECUTE_EXCEPTION_TIP = "23200694";
    /** 禁止删除内置角色 */
    String RCDC_AAA_ROLE_DELETE_DEFAULT_FORBID = "rcdc_aaa_role_delete_default_forbid";

    String RCDC_AAA_ROLE_ERROR = "23200687";

    /** 存在关联的管理员不允许删除 */
    String RCDC_AAA_ROLE_DELETE_RELATION_MANAGE_FORBID = "rcdc_aaa_role_delete_relation_manage_forbid";

    String RCDC_AAA_ROLE_DELETE_ROLE_SUCCESS = "rcdc_aaa_role_delete_role_success";
    String RCDC_AAA_ROLE_DELETE_ROLE_FAIL = "rcdc_aaa_role_delete_role_fail";
    /** 删除角色 */
    String RCDC_AAA_ROLE_DELETE_ROLE_DO_SUCCESS = "rcdc_aaa_role_delete_role_do_success";
    String RCDC_AAA_ROLE_DELETE_ROLE_DO_FAIL = "rcdc_aaa_role_delete_role_do_fail";

    String RCDC_AAA_ROLE_BATCH_DELETE_ROLE_TASK_NAME = "rcdc_aaa_role_batch_delete_role_task_name";
    String RCDC_AAA_ROLE_BATCH_DELETE_ROLE_TASK_DESC = "rcdc_aaa_role_batch_delete_role_task_desc";
    String RCDC_AAA_ROLE_BATCH_DELETE_ROLE_TASK_RESULT = "rcdc_aaa_role_batch_delete_role_task_result";

    String RCDC_AAA_ROLE_BATCH_DELETE_TASK_ITEM_NAME = "rcdc_aaa_role_batch_delete_task_item_name";
    String RCDC_AAA_ROLE_BATCH_DELETE_TASK_ITEM_SUCCESS = "rcdc_aaa_role_batch_delete_task_item_success";
    String RCDC_AAA_ROLE_BATCH_DELETE_TASK_ITEM_FAIL = "rcdc_aaa_role_batch_delete_task_item_fail";

    // 单个删除
    String RCDC_AAA_ROLE_BATCH_DELETE_ROLE_SINGLE_TASK_NAME = "rcdc_aaa_role_batch_delete_role_single_task_name";
    String RCDC_AAA_ROLE_BATCH_DELETE_ROLE_SINGLE_TASK_DESC = "rcdc_aaa_role_batch_delete_role_single_task_desc";
    String RCDC_AAA_ROLE_DELETE_SINGLE_ROLE_SUCCESS = "rcdc_aaa_role_delete_single_role_success";
    String RCDC_AAA_ROLE_DELETE_SINGLE_ROLE_FAIL = "rcdc_aaa_role_delete_single_role_fail";

    /**
     * admin不能被禁用
     */
    String RCDC_AAA_ADMIN_CAN_NOT_DISABLE = "23200695";

    /**
     * 域用户升级的管理员
     */
    String RCDC_AAA_AD_ADMIN_CAN_NOT_ENABLE_OR_DISABLE = "23200696";
    String RCDC_AAA_AD_ADMIN_CAN_NOT_MODIFY_PASSWORD = "23200697";

    /**
     *
     */
    String RCDC_AAA_ONLY_ADMIN_CAN_JUMP_TO_CMS  = "23200698";

    /**
     * 只有管理员允许跳转到uws
     */
    String RCDC_AAA_ONLY_ADMIN_CAN_JUMP_TO_UWS = "23200699";

    /**
     * 管理员第三方扫码认证失败
     */
    String RCDC_AAA_ADMIN_THIRD_PARTY_QR_AUTH_LOGIN_FAIL = "rcdc_aaa_admin_third_party_qr_auth_login_fail";

    String RCDC_AAA_ADMIN_THIRD_PARTY_QR_AUTH_LOGIN_SUCCESS = "rcdc_aaa_admin_third_party_qr_auth_login_success";
}
