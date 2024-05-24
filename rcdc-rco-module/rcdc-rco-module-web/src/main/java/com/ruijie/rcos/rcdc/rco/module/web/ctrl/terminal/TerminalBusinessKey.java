package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

/**
 * Description: 国际化key
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/15
 *
 * @author Jarman
 */
public interface TerminalBusinessKey {

    /**
     * 操作成功
     */
    String RCDC_TERMINAL_MODULE_OPERATE_SUCCESS = "rcdc_terminal_module_operate_success";
    /**
     * 操作失败
     */
    String RCDC_TERMINAL_MODULE_OPERATE_FAIL = "rcdc_terminal_module_operate_fail";

    /**
     * 请求参数异常
     */
    String RCDC_COMMON_REQUEST_PARAM_ERROR = "23201049";

    /**
     * 上传终端系统升级包成功日志
     */
    String RCDC_TERMINAL_SYSTEM_UPGRADE_PACKAGE_UPLOAD_SUCCESS_LOG = "rcdc_terminal_system_upgrade_package_upload_success_log";

    /**
     * 上传终端系统升级包失败日志
     */
    String RCDC_TERMINAL_SYSTEM_UPGRADE_PACKAGE_UPLOAD_FAIL_LOG = "rcdc_terminal_system_upgrade_package_upload_fail_log";

    /**
     * 上传终端系统升级包文件名称长度超出限制
     */
    String RCDC_TERMINAL_SYSTEM_UPGRADE_PACKAGE_UPLOAD_FILE_NAME_LENGTH_EXCEED =
            "23201050";

    /**
     * 上传升级包校验未通过
     */
    String RCDC_PACKAGE_UPLOAD_NOT_ALLOWED = "rcdc_terminal_package_upload_not_allowed";

    /**
     * 创建终端升级任务参数检验异常
     */
    String RCDC_CREATE_UPGRADE_TERMINAL_TASK_TERMINAL_AND_GROUP_BOTH_EMPTY_ERROR =
            "23201051";

    /**
     * 编辑终端升级任务终端分组成功
     */
    String RCDC_EDIT_UPGRADE_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_edit_upgrade_terminal_group_success_log";

    /**
     * 编辑终端升级任务终端分组失败
     */
    String RCDC_EDIT_UPGRADE_TERMINAL_GROUP_FAIL_LOG = "rcdc_edit_upgrade_terminal_group_fail_log";

    /**
     * 创建终端刷机任务成功日志
     */
    String RCDC_CREATE_UPGRADE_TERMINAL_TASK_SUCCESS_LOG = "rcdc_terminal_create_upgrade_terminal_task_success_log";

    /**
     * 创建终端刷机任务失败日志
     */
    String RCDC_CREATE_UPGRADE_TERMINAL_TASK_FAIL_LOG = "rcdc_terminal_create_upgrade_terminal_task_fail_log";

    /**
     * 批量追加终端升级任务项名称
     */
    String RCDC_ADD_UPGRADE_TERMINAL_ITEM_NAME = "rcdc_terminal_add_upgrade_terminal_item_name";

    /**
     * 批量追加终端升级任务名称
     */
    String RCDC_ADD_UPGRADE_TERMINAL_TASK_NAME = "rcdc_terminal_add_upgrade_terminal_task_name";

    /**
     * 批量追加终端升级任务描述
     */
    String RCDC_ADD_UPGRADE_TERMINAL_TASK_DESC = "rcdc_terminal_add_upgrade_terminal_task_desc";

    /**
     * 批量追加终端系统升级任务成功日志
     */
    String RCDC_ADD_UPGRADE_TERMINAL_SUCCESS_LOG = "rcdc_terminal_add_upgrade_terminal_success_log";

    /**
     * 批量追加终端系统升级任务失败日志
     */
    String RCDC_ADD_UPGRADE_TERMINAL_FAIL_LOG = "rcdc_terminal_add_upgrade_terminal_fail_log";

    /**
     * 批量追加终端系统升级任务结果
     */
    String RCDC_ADD_UPGRADE_TERMINAL_RESULT = "rcdc_terminal_add_upgrade_terminal_result";

    /**
     * 批量追加终端系统升级任务成功
     */
    String RCDC_ADD_UPGRADE_TERMINAL_RESULT_SUCCESS = "rcdc_terminal_add_upgrade_terminal_result_success";

    /**
     * 批量追加终端系统升级任务项失败
     */
    String RCDC_ADD_UPGRADE_TERMINAL_RESULT_FAIL = "rcdc_terminal_add_upgrade_terminal_result_fail";

    /**
     * 追加单条终端升级成功
     */
    String RCDC_ADD_UPGRADE_TERMINAL_SUCCESS = "rcdc_terminal_add_upgrade_terminal_success";

    /**
     * 追加单条终端升级失败
     */
    String RCDC_ADD_UPGRADE_TERMINAL_FAIL = "rcdc_terminal_add_upgrade_terminal_fail";


    /**
     * 批量取消终端升级任务项名称
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_ITEM_NAME = "rcdc_terminal_cancel_upgrade_terminal_item_name";

    /**
     * 批量取消终端升级任务名称
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_TASK_NAME = "rcdc_terminal_cancel_upgrade_terminal_task_name";

    /**
     * 批量取消终端升级任务描述
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_TASK_DESC = "rcdc_terminal_cancel_upgrade_terminal_task_desc";

    /**
     * 批量取消终端系统升级任务成功日志
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_SUCCESS_LOG = "rcdc_terminal_cancel_upgrade_terminal_success_log";

    /**
     * 批量取消终端系统升级任务失败日志
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_FAIL_LOG = "rcdc_terminal_cancel_upgrade_terminal_fail_log";

    /**
     * 批量取消终端系统升级任务结果
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_RESULT = "rcdc_terminal_cancel_upgrade_terminal_result";

    /**
     * 批量取消终端系统升级任务项成功
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_RESULT_SUCCESS = "rcdc_terminal_cancel_upgrade_terminal_result_success";

    /**
     * 批量取消终端系统升级任务项失败
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_RESULT_FAIL = "rcdc_terminal_cancel_upgrade_terminal_result_fail";

    /**
     * 取消终端系统升级成功（单条）
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_SUCCESS = "rcdc_terminal_cancel_upgrade_terminal_success";

    /**
     * 取消终端系统升级失败（单条）
     */
    String RCDC_CANCEL_UPGRADE_TERMINAL_FAIL = "rcdc_terminal_cancel_upgrade_terminal_fail";

    /**
     * 批量重试终端升级任务项名称
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_ITEM_NAME = "rcdc_terminal_retry_upgrade_terminal_item_name";

    /**
     * 批量重试终端升级任务名称
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_TASK_NAME = "rcdc_terminal_retry_upgrade_terminal_task_name";

    /**
     * 批量重试终端升级任务描述
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_TASK_DESC = "rcdc_terminal_retry_upgrade_terminal_task_desc";

    /**
     * 批量重试终端系统升级任务成功日志
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_SUCCESS_LOG = "rcdc_terminal_retry_upgrade_terminal_success_log";

    /**
     * 批量重试终端系统升级任务失败日志
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_FAIL_LOG = "rcdc_terminal_retry_upgrade_terminal_fail_log";

    /**
     * 批量重试终端系统升级任务结果
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_RESULT = "rcdc_terminal_retry_upgrade_terminal_result";

    /**
     * 批量重试终端系统升级任务项成功
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_RESULT_SUCCESS = "rcdc_terminal_retry_upgrade_terminal_result_success";

    /**
     * 批量重试终端系统升级任务项失败
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_RESULT_FAIL = "rcdc_terminal_retry_upgrade_terminal_result_fail";


    /**
     * 重试终端系统升级成功（单条）
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_SUCCESS = "rcdc_terminal_retry_upgrade_terminal_success";

    /**
     * 重试终端系统升级失败（单条）
     */
    String RCDC_RETRY_UPGRADE_TERMINAL_FAIL = "rcdc_terminal_retry_upgrade_terminal_fail";

    /**
     * 关闭终端升级包[{0}]升级任务成功
     */
    String RCDC_UPGRADE_TERMINAL_TASK_CLOSE_SUCCESS_LOG = "rcdc_terminal_upgrade_terminal_task_close_success_log";

    /**
     * 关闭终端升级包[{0}]升级任务失败，失败原因：{1}
     */
    String RCDC_UPGRADE_TERMINAL_TASK_CLOSE_FAIL_LOG = "rcdc_terminal_upgrade_terminal_task_close_fail_log";

    /**
     * 批量删除终端升级包任务项名称
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_ITEM_NAME = "rcdc_terminal_delete_terminal_upgrade_package_item_name";

    /**
     * 批量删除终端升级包任务名称
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_TASK_NAME = "rcdc_terminal_delete_terminal_upgrade_package_task_name";

    /**
     * 批量删除终端升级包任务描述
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_TASK_DESC = "rcdc_terminal_delete_terminal_upgrade_package_task_desc";

    /**
     * 批量删除终端系统升级包任务成功日志
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_SUCCESS_LOG = "rcdc_terminal_delete_terminal_upgrade_package_success_log";

    /**
     * 批量删除终端系统升级包任务失败日志
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_FAIL_LOG = "rcdc_terminal_delete_terminal_upgrade_package_fail_log";

    /**
     * 批量删除终端系统升级包任务结果
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT = "rcdc_terminal_delete_terminal_upgrade_package_result";

    /**
     * 批量删除终端系统升级包任务项成功
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT_SUCCESS = "rcdc_terminal_delete_terminal_upgrade_package_result_success";

    /**
     * 批量删除终端系统升级包任务项失败
     */
    String RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT_FAIL = "rcdc_terminal_delete_terminal_upgrade_package_result_fail";


    /**
     * 删除单条终端系统升级包结果 - 成功
     */
    String RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_SUCCESS = "rcdc_delete_single_terminal_upgrade_package_task_success";

    /**
     * 删除单条终端系统升级包结果 - 失败
     */
    String RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_FAIL = "rcdc_delete_single_terminal_upgrade_package_task_fail";

    /**
     * 删除单条终端系统升级包itemName
     */
    String RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_ITEM_NAME = "rcdc_delete_single_terminal_upgrade_package_item_name";

    /**
     * 删除单条终端系统升级包taskName
     */
    String RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_NAME = "rcdc_delete_single_terminal_upgrade_package_task_name";

    /**
     * 删除单条终端系统升级包taskDesc
     */
    String RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_DESC = "rcdc_delete_single_terminal_upgrade_package_task_desc";

    /**
     * 上传升级包itemName
     */
    String RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_ITEM_NAME = "rcdc_upload_terminal_upgrade_package_item_name";

    /**
     * 上传升级包taskName
     */
    String RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_NAME = "rcdc_upload_terminal_upgrade_package_task_name";

    /**
     * 上传升级包taskDesc
     */
    String RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_DESC = "rcdc_upload_terminal_upgrade_package_task_desc";

    /**
     * 上传升级包任务成功
     */
    String RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_SUCCESS = "rcdc_upload_terminal_upgrade_package_task_success";

    /**
     * 上传升级包任务失败
     */
    String RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_FAIL = "rcdc_upload_terminal_upgrade_package_task_fail";

    /**
     * 上传升级包结果 - 成功
     */
    String RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_RESULT_SUCCESS = "rcdc_upload_terminal_upgrade_package_result_success";

    /**
     * 上传升级包结果 - 失败
     */
    String RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_RESULT_FAIL = "rcdc_upload_terminal_upgrade_package_result_fail";

    /**
     * 开启终端检测批量任务
     */
    String RCDC_TERMINAL_DETECT_BATCH_TASK_RESULT = "rcdc_terminal_detect_batch_task_result";
    String RCDC_TERMINAL_START_DETECT_SUCCESS_LOG = "rcdc_terminal_start_detect_success_log";
    String RCDC_TERMINAL_START_DETECT_FAIL_LOG = "rcdc_terminal_start_detect_fail_log";
    String RCDC_START_TERMINAL_DETECT_TASK_NAME = "rcdc_start_terminal_detect_task_name";
    String RCDC_START_TERMINAL_DETECT_TASK_DESC = "rcdc_start_terminal_detect_task_desc";
    String RCDC_START_TERMINAL_DETECT_ITEM_NAME = "rcdc_start_terminal_detect_item_name";
    String RCDC_TERMINAL_START_DETECT_RESULT_SUCCESS = "rcdc_terminal_start_detect_result_success";
    String RCDC_TERMINAL_START_DETECT_RESULT_FAIL = "rcdc_terminal_start_detect_result_fail";

    /**
     * 修改终端管理密码成功日志
     */
    String RCDC_TERMINAL_CHANGE_PWD_SUCCESS_LOG = "rcdc_terminal_change_pwd_success_log";
    /**
     * 修改终端管理密码失败日志
     */
    String RCDC_TERMINAL_CHANGE_PWD_FAIL_LOG = "rcdc_terminal_change_pwd_fail_log";


    /**
     * 关闭终端失败日志
     */
    String RCDC_TERMINAL_CLOSE_FAIL_LOG = "rcdc_terminal_close_fail_log";

    /**
     * 批量关闭终端结果
     */
    String RCDC_TERMINAL_CLOSE_RESULT = "rcdc_terminal_close_result";

    /**
     * 关闭终端成功日志
     */
    String RCDC_TERMINAL_CLOSE_SUCCESS_LOG = "rcdc_terminal_close_success_log";

    /**
     * 批量关闭终端任务名称
     */
    String RCDC_TERMINAL_CLOSE_TASK_NAME = "rcdc_terminal_close_task_name";

    /**
     * 批量关闭终端任务描述
     */
    String RCDC_TERMINAL_CLOSE_TASK_DESC = "rcdc_terminal_close_task_desc";

    /**
     * 发送关闭终端命令
     */
    String RCDC_TERMINAL_CLOSE_ITEM_NAME = "rcdc_terminal_close_item_name";

    /**
     * 关闭命令发送成功
     */
    String RCDC_TERMINAL_CLOSE_SEND_SUCCESS = "rcdc_terminal_close_send_success";

    /**
     * 关闭命令发送失败
     */
    String RCDC_TERMINAL_CLOSE_SEND_FAIL = "rcdc_terminal_close_send_fail";

    /**
     * 批量重启终端任务结果
     */
    String RCDC_TERMINAL_RESTART_RESULT = "rcdc_terminal_restart_result";

    /**
     * 重启终端失败日志
     */
    String RCDC_TERMINAL_RESTART_FAIL_LOG = "rcdc_terminal_restart_fail_log";

    /**
     * 重启终端成功日志
     */
    String RCDC_TERMINAL_RESTART_SUCCESS_LOG = "rcdc_terminal_restart_success_log";

    /**
     * 批量重启终端任务名称
     */
    String RCDC_TERMINAL_RESTART_TASK_NAME = "rcdc_terminal_restart_task_name";

    /**
     * 批量重启终端任务描述
     */
    String RCDC_TERMINAL_RESTART_TASK_DESC = "rcdc_terminal_restart_task_desc";

    /**
     * 发送重启命令给终端
     */
    String RCDC_TERMINAL_RESTART_ITEM_NAME = "rcdc_terminal_restart_item_name";

    /**
     * 重启命令发送成功
     */
    String RCDC_TERMINAL_RESTART_SEND_SUCCESS = "rcdc_terminal_restart_send_success";

    /**
     * 重启命令发送失败
     */
    String RCDC_TERMINAL_RESTART_SEND_FAIL = "rcdc_terminal_restart_send_fail";

    /******************************* 终端LogoKEY **********************************/
    String RCDC_TERMINAL_UPLOAD_TERMINAL_LOGO_SUCCESS_LOG = "rcdc_terminal_upload_terminal_logo_success_log";

    String RCDC_TERMINAL_UPLOAD_TERMINAL_LOGO_FAIL_LOG = "rcdc_terminal_upload_terminal_logo_fail_log";

    String RCDC_UPLOAD_TERMINAL_LOGO_RESULT_SUCCESS = "rcdc_upload_terminal_logo_result_success";
    String RCDC_UPLOAD_TERMINAL_LOGO_RESULT_FAIL = "rcdc_upload_terminal_logo_result_fail";
    String RCDC_UPLOAD_TERMINAL_LOGO_TASK_SUCCESS = "rcdc_upload_terminal_logo_task_success";
    String RCDC_UPLOAD_TERMINAL_LOGO_TASK_FAIL = "rcdc_upload_terminal_logo_task_fail";
    String RCDC_UPLOAD_TERMINAL_LOGO_ITEM_NAME = "rcdc_upload_terminal_logo_item_name";
    String RCDC_UPLOAD_TERMINAL_LOGO_TASK_NAME = "rcdc_upload_terminal_logo_task_name";
    String RCDC_UPLOAD_TERMINAL_LOGO_TASK_DESC = "rcdc_upload_terminal_logo_task_desc";

    /**
     * 开启终端OTA升级任务成功日志
     **/
    String RCDC_TERMINAL_START_OTA_UPGRADE_TASK_SUCCESS_LOG = "rcdc_terminal_start_ota_upgrade_task_success_log";

    /**
     * 开启终端OTA升级任务失败日志
     **/
    String RCDC_TERMINAL_START_OTA_UPGRADE_TASK_FAIL_LOG = "rcdc_terminal_start_ota_upgrade_task_fail_log";

    /**
     * 关闭终端OTA升级任务成功日志
     **/
    String RCDC_TERMINAL_CLOSE_OTA_UPGRADE_TASK_SUCCESS_LOG = "rcdc_terminal_close_ota_upgrade_task_success_log";

    /**
     * 关闭终端OTA升级任务失败日志
     **/
    String RCDC_TERMINAL_CLOSE_OTA_UPGRADE_TASK_FAIL_LOG = "rcdc_terminal_close_ota_upgrade_task_fail_log";

    String RCDC_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT_FOR_SYSADMIN = "rcdc_terminalgroup_group_num_exceed_limit_for_sysadmin";
    String RCDC_TERMINALGROUP_SUB_GROUP_NUM_EXCEED_LIMIT_FOR_SYSADMIN = "rcdc_terminalgroup_sub_group_num_exceed_limit_for_sysadmin";
    String RCDC_TERMINALGROUP_GROUP_NAME_DUPLICATE_FOR_SYSADMIN = "rcdc_terminalgroup_group_name_duplicate_for_sysadmin";
    String RCDC_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP_FOR_SYSADMIN =
            "rcdc_delete_terminal_group_sub_group_has_duplication_with_move_group_for_sysadmin";

    String RCDC_TERMINAL_PASSWORD_UPDATE_FOR_SYSADMIN = "23200991";

    /******************************* 离线登录设置KEY **********************************/
    String RCDC_TERMINAL_CHANGE_OFFLINE_LOGIN_SETTING_SUCCESS_LOG = "rcdc_terminal_change_offline_login_setting_success_log";
    String RCDC_TERMINAL_CHANGE_OFFLINE_LOGIN_SETTING_FAIL_LOG = "rcdc_terminal_change_offline_login_setting_fail_log";
    String RCDC_TERMINAL_CHANGE_OFFLINE_AUTO_LOCKED_ENUM_NOT_EXIST = "23201052";

    /***************** 批量修改终端访客设置 **********************/

    /**
     * 修改终端访客设置失败日志
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_FAIL_LOG = "rcdc_terminal_edit_visitor_setting_fail_log";

    /**
     * 修改终端访客设置结果
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_RESULT = "rcdc_terminal_edit_visitor_setting_result";

    /**
     * 修改终端访客设置成功日志
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_SUCCESS_LOG = "rcdc_terminal_edit_visitor_setting_success_log";

    /**
     * 修改终端访客设置任务名称
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_TASK_NAME = "rcdc_terminal_edit_visitor_setting_task_name";

    /**
     * 修改终端访客设置任务描述
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_TASK_DESC = "rcdc_terminal_edit_visitor_setting_task_desc";

    /**
     * 修改终端访客设置
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_ITEM_NAME = "rcdc_terminal_edit_visitor_setting_item_name";

    /**
     * 修改终端访客设置成功
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_SUCCESS = "rcdc_terminal_edit_visitor_setting_success";

    /**
     * 修改终端访客设置失败
     */
    String RCDC_TERMINAL_EDIT_VISITOR_SETTING_FAIL = "rcdc_terminal_edit_visitor_setting_fail";

    /*******************************************************/

    /******************************* 数据盘清空KEY **********************************/
    String RCDC_TERMINAL_CLEAR_DISK_SUCCESS_LOG = "rcdc_terminal_clear_disk_success_log";
    String RCDC_TERMINAL_CLEAR_DISK_FAIL_LOG = "rcdc_terminal_clear_disk_fail_log";
    String RCDC_TERMINAL_CLEAR_DISK_RESULT = "rcdc_terminal_clear_disk_result";
    String RCDC_TERMINAL_CLEAR_DISK_ITEM_NAME = "rcdc_terminal_clear_disk_item_name";
    String RCDC_TERMINAL_CLEAR_DISK_ITEM_DESC = "rcdc_terminal_clear_disk_item_desc";

    /** 恢复终端主题默认配置成功 */
    String RCDC_TERMINAL_INIT_TERMINAL_THEME_SUCCESS = "rcdc_terminal_init_terminal_theme_success";

    /** 恢复背景图默认配置失败 */
    String RCDC_TERMINAL_INIT_TERMINAL_THEME_FAIL = "rcdc_terminal_init_terminal_theme_fail";

    /*************************************************** 终端背景KEY *****************************************/
    String RCDC_TERMINAL_UPLOAD_TERMINAL_BACKGROUND_SUCCESS_LOG = "rcdc_terminal_upload_terminal_background_success_log";

    String RCDC_TERMINAL_UPLOAD_TERMINAL_BACKGROUND_FAIL_LOG = "rcdc_terminal_upload_terminal_background_fail_log";

    String RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_SUCCESS = "rcdc_upload_terminal_background_task_success";

    String RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_FAIL = "rcdc_upload_terminal_background_task_fail";

    String RCDC_UPLOAD_TERMINAL_BACKGROUND_ITEM_NAME = "rcdc_upload_terminal_background_item_name";

    String RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_NAME = "rcdc_upload_terminal_background_task_name";

    String RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_DESC = "rcdc_upload_terminal_background_task_desc";

    /** 预览背景图片失败 */
    /**
     * 预览背景图片失败
     */
    String RCDC_TERMINAL_PREVIEW_BACKGROUND_FILE_FAIL = "23201053";

    /*************************************************** 终端快捷方式图KEY *****************************************/

    String RCDC_UPLOAD_TERMINAL_SHORTCUT_ITEM_NAME = "rcdc_upload_terminal_shortcut_item_name";

    String RCDC_TERMINAL_UPLOAD_TERMINAL_SHORTCUT_SUCCESS_LOG = "rcdc_terminal_upload_terminal_shortcut_success_log";

    String RCDC_TERMINAL_UPLOAD_TERMINAL_SHORTCUT_FAIL_LOG = "rcdc_terminal_upload_terminal_shortcut_fail_log";

    String RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_SUCCESS = "rcdc_upload_terminal_shortcut_task_success";

    String RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_FAIL = "rcdc_upload_terminal_shortcut_task_fail";

    String RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_NAME = "rcdc_upload_terminal_shortcut_task_name";

    String RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_DESC = "rcdc_upload_terminal_shortcut_task_desc";

    /**
     * 快捷方式图设置失败
     */
    String RCDC_TERMINAL_UPLOAD_TERMINAL_SHORTCUT_FAIL = ""; //fixme 设置错误码【终端快捷方式图[{0}]设置失败,失败原因：{1}】

    /****************************** 终端初始化KEY *************************************/
    String RCDC_TERMINAL_INIT_TERMINAL_SUCCESS = "rcdc_terminal_init_terminal_success";
    String RCDC_TERMINAL_INIT_TERMINAL_SUCCESS_HAS_WARN = "rcdc_terminal_init_terminal_success_has_warn";
    String RCDC_TERMINAL_INIT_TERMINAL_FAIL = "rcdc_terminal_init_terminal_fail";
    String RCDC_TERMINAL_INIT_TERMINAL_RESULT = "rcdc_terminal_init_terminal_result";
    String RCDC_TERMINAL_INIT_TERMINAL_ITEM_NAME = "rcdc_terminal_init_terminal_item_name";
    String RCDC_TERMINAL_INIT_TERMINAL_ITEM_DESC = "rcdc_terminal_init_terminal_item_desc";

    /**
     * shine搜集日志异常，无法获取日志信息
     */
    /** shine搜集日志异常，无法获取日志信息 */
    String RCDC_TERMINAL_SHINE_ERROR_GET_SHINE_LOG_FAIL = "23201054";

    /**
     * 收集终端日志成功
     */
    String RCDC_TERMINAL_COLLECT_LOG_SUCCESS_LOG = "rcdc_terminal_collect_log_success_log";

    /**
     * 收集终端日志失败
     */
    String RCDC_TERMINAL_COLLECT_LOG_RESULT_FAIL_LOG = "rcdc_terminal_collect_log_result_fail_log";

    /**
     * 收集终端日志失败
     */
    String RCDC_TERMINAL_COLLECT_LOG_FAIL_LOG = "rcdc_terminal_collect_log_fail_log";

    String RCDC_SOFT_TERMINAL_COLLECT_LOG_FAIL_LOG = "rcdc_soft_terminal_collect_log_fail_log";
    String RCDC_SOFT_TERMINAL_COLLECT_LOG_SUCCESS_LOG = "rcdc_soft_terminal_collect_log_success_log";
    String RCDC_SOFT_TERMINAL_COLLECT_LOG_RESULT_FAIL_LOG = "rcdc_soft_terminal_collect_log_result_fail_log";
    String RCDC_IDV_TERMINAL_COLLECT_LOG_FAIL_LOG = "rcdc_idv_terminal_collect_log_fail_log";
    String RCDC_IDV_TERMINAL_COLLECT_LOG_SUCCESS_LOG = "rcdc_idv_terminal_collect_log_success_log";
    String RCDC_IDV_TERMINAL_COLLECT_LOG_RESULT_FAIL_LOG = "rcdc_idv_terminal_collect_log_result_fail_log";

    /** 终端更新白名单成功 */
    String RCDC_TERMINAL_UPDATE_WHITELIST_SUCCESS_LOG = "rcdc_terminal_update_whitelist_success_log";
    /** 终端更新白名单失败 */
    String RCDC_TERMINAL_UPDATE_WHITELIST_ERROR_LOG = "rcdc_terminal_update_whitelist_error_log";
    String RCDC_TERMINAL_UPDATE_WHITELIST_RESULT = "rcdc_terminal_update_whitelist_result";
    /** 终端更新白名单任务名称 */
    String RCDC_TERMINAL_UPDATE_WHITELIST_TASK_NAME = "rcdc_terminal_update_whitelist_task_name";
    String RCDC_TERMINAL_UPDATE_WHITELIST_TASK_DESC = "rcdc_terminal_update_whitelist_task_desc";


    /**
     * 修改终端网卡工作模式
     */
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_RESULT = "rcdc_terminal_modify_nic_work_mode_result";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_FAIL_LOG = "rcdc_terminal_modify_nic_work_mode_fail_log";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SUCCESS_LOG = "rcdc_terminal_modify_nic_work_mode_success_log";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_TASK_NAME = "rcdc_terminal_modify_nic_work_mode_task_name";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_TASK_DESC = "rcdc_terminal_modify_nic_work_mode_task_desc";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SINGLE_TASK_DESC = "rcdc_terminal_modify_nic_work_mode_single_task_desc";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_ITEM_NAME = "rcdc_terminal_modify_nic_work_mode_item_name";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_SUCCESS = "rcdc_terminal_modify_nic_work_mode_send_success";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_SUCCESS_WITH_TERMINAL = "rcdc_terminal_modify_nic_work_mode_send_success_with_terminal";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_FAIL = "rcdc_terminal_modify_nic_work_mode_send_fail";
    String RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_FAIL_WITH_REASON = "rcdc_terminal_modify_nic_work_mode_send_fail_with_reason";

    /**
     * 系统盘自动扩容
     */
    String RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_SUCCESS = "rcdc_terminal_edit_full_system_disk_success";
    String RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_FAIL = "rcdc_terminal_edit_full_system_disk_fail";
    String RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_RESULT = "rcdc_terminal_edit_full_system_result";
    String RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_TASK_NAME = "rcdc_terminal_edit_full_system_task_name";
    String RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_ITEM_DESC = "rcdc_terminal_edit_full_system_item_desc";
    String RCDC_TERMINAL_EDIT_FULL_SYSTEM_ITEM_NAME = "rcdc_terminal_edit_full_system_item_name";

    /**
     * 设置IDV终端网络配置
     */
    String RCDC_TERMINAL_NOT_IDV_OR_TCI = "23201055";
    String RCDC_TERMINAL_BATCH_NOT_IDV_OR_TCI = "23201056";
    String RCDC_TERMINAL_NO_OPERATE_TERMINAL = "23201057";
    /** 批量设置终端IP */
    String RCDC_TERMINAL_EDIT_IP_ITEM_NAME = "rcdc_terminal_edit_ip_item_name";
    String RCDC_TERMINAL_EDIT_IP_ITEM_DESC = "rcdc_terminal_edit_ip_item_desc";
    String RCDC_TERMINAL_EDIT_IP_SUC_LOG = "rcdc_terminal_edit_ip_suc_log";
    String RCDC_TERMINAL_EDIT_IP_FAIL_LOG = "rcdc_terminal_edit_ip_fail_log";
    String RCDC_TERMINAL_EDIT_IP_BATCH_RESULT = "rcdc_terminal_edit_ip_batch_result";
    /** 单台设置终端网络配置 */
    String RCDC_TERMINAL_EDIT_NETWORK_ITEM_NAME = "rcdc_terminal_edit_network_item_name";
    String RCDC_TERMINAL_EDIT_NETWORK_ITEM_DESC = "rcdc_terminal_edit_network_item_desc";
    String RCDC_TERMINAL_EDIT_NETWORK_SUC_LOG = "rcdc_terminal_edit_network_suc_log";
    String RCDC_TERMINAL_EDIT_NETWORK_FAIL_LOG = "rcdc_terminal_edit_network_fail_log";
    String RCDC_TERMINAL_EDIT_NETWORK_RESULT_SUC = "rcdc_terminal_edit_network_result_suc";
    String RCDC_TERMINAL_EDIT_NETWORK_RESULT_FAIL = "rcdc_terminal_edit_network_result_fail";

    /*****************************  修改终端云服务器地址 *************************************/
    String RCDC_TERMINAL_CHANGE_SERVER_IP_SUCCESS = "rcdc_terminal_change_server_ip_success";
    String RCDC_TERMINAL_CHANGE_SERVER_IP_FAIL = "rcdc_terminal_change_server_ip_fail";
    String RCDC_TERMINAL_CHANGE_SERVER_IP_RESULT = "rcdc_terminal_change_server_ip_result";
    String RCDC_TERMINAL_CHANGE_SERVER_IP_ITEM_NAME = "rcdc_terminal_change_server_ip_item_name";
    String RCDC_TERMINAL_CHANGE_SERVER_IP_ITEM_DESC = "rcdc_terminal_change_server_ip_item_desc";
    String RCDC_TERMINAL_CHANGE_SERVER_IP_NO_ONLINE = "23201058";
    String RCDC_TERMINAL_CHANGE_SERVER_IP_NOT_SAME = "rcdc_terminal_change_server_ip_not_same";


    /**
     * 远程唤醒
     */
    String RCDC_TERMINAL_WAKE_UP_SUCCESS = "rcdc_terminal_wake_up_success";
    String RCDC_TERMINAL_WAKE_UP_FAIL = "rcdc_terminal_wake_up_fail";
    String RCDC_TERMINAL_WAKE_UP_RESULT = "rcdc_terminal_wake_up_result";
    String RCDC_TERMINAL_WAKE_UP_TASK_NAME = "rcdc_terminal_wake_up_task_name";
    String RCDC_TERMINAL_WAKE_UP_SINGLE_ITEM_DESC = "rcdc_terminal_wake_up_single_item_desc";
    String RCDC_TERMINAL_WAKE_UP_BATCH_ITEM_DESC = "rcdc_terminal_wake_up_batch_item_desc";
    String RCDC_TERMINAL_WAKE_UP_ITEM_NAME = "rcdc_terminal_wake_up_item_name";

    /** 终端组件独立升级 **/
    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_FILE_NAME_LENGTH_EXCEED =
            "23201059";
    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_SUCCESS_LOG =
            "rcdc_terminal_component_independent_upgrade_package_upload_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_FAIL_LOG =
            "rcdc_terminal_component_independent_upgrade_package_upload_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_RESULT_SUCCESS =
            "rcdc_terminal_component_independent_upgrade_upload_package_result_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_RESULT_FAIL =
            "rcdc_terminal_component_independent_upgrade_upload_package_result_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_SUCCESS =
            "rcdc_terminal_component_independent_upgrade_upload_package_task_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_FAIL =
            "rcdc_terminal_component_independent_upgrade_upload_package_task_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_ITEM_NAME =
            "rcdc_terminal_component_independent_upgrade_upload_package_item_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_NAME =
            "rcdc_terminal_component_independent_upgrade_upload_package_task_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_DESC =
            "rcdc_terminal_component_independent_upgrade_upload_package_task_desc";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_SUCCESS =
            "rcdc_terminal_component_independent_upgrade_delete_single_package_task_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_FAIL =
            "rcdc_terminal_component_independent_upgrade_delete_single_package_task_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_ITEM_NAME =
            "rcdc_terminal_component_independent_upgrade_delete_single_package_item_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_NAME =
            "rcdc_terminal_component_independent_upgrade_delete_single_package_task_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_DESC =
            "rcdc_terminal_component_independent_upgrade_delete_single_package_task_desc";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_NOT_ALLOWED =
            "rcdc_terminal_component_independent_upgrade_package_upload_not_allowed";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT = "rcdc_terminal_component_independent_upgrade_delete_package_result";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT_SUCCESS =
            "rcdc_terminal_component_independent_upgrade_delete_package_result_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT_FAIL =
            "rcdc_terminal_component_independent_upgrade_delete_package_result_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_TASK_DESC =
            "rcdc_terminal_component_independent_upgrade_delete_package_task_desc";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_ITEM_NAME =
            "rcdc_terminal_component_independent_upgrade_delete_package_item_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_TASK_NAME =
            "rcdc_terminal_component_independent_upgrade_delete_package_task_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_SUCCESS_LOG =
            "rcdc_terminal_component_independent_upgrade_delete_package_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_FAIL_LOG =
            "rcdc_terminal_component_independent_upgrade_delete_package_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CREATE_TASK_SUCCESS_LOG =
            "rcdc_terminal_component_independent_upgrade_create_task_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CREATE_TASK_FAIL_LOG = "rcdc_terminal_component_independent_upgrade_create_task_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CREATE_TASK_TERMINAL_AND_GROUP_BOTH_EMPTY_ERROR =
            "23201060";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_EDIT_TERMINAL_GROUP_SUCCESS_LOG =
            "rcdc_terminal_component_independent_upgrade_edit_terminal_group_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_EDIT_TERMINAL_GROUP_FAIL_LOG =
            "rcdc_terminal_component_independent_upgrade_edit_terminal_group_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_RESULT = "rcdc_terminal_component_independent_upgrade_cancel_result";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_RESULT_SUCCESS = "rcdc_terminal_component_independent_upgrade_cancel_result_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_RESULT_FAIL = "rcdc_terminal_component_independent_upgrade_cancel_result_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_TASK_DESC = "rcdc_terminal_component_independent_upgrade_cancel_task_desc";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_ITEM_NAME = "rcdc_terminal_component_independent_upgrade_cancel_item_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_TASK_NAME = "rcdc_terminal_component_independent_upgrade_cancel_task_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_SUCCESS_LOG = "rcdc_terminal_component_independent_upgrade_cancel_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_FAIL_LOG = "rcdc_terminal_component_independent_upgrade_cancel_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_SUCCESS = "rcdc_terminal_component_independent_upgrade_cancel_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_FAIL = "rcdc_terminal_component_independent_upgrade_cancel_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_RESULT = "rcdc_terminal_component_independent_upgrade_retry_result";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_RESULT_SUCCESS = "rcdc_terminal_component_independent_upgrade_retry_result_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_RESULT_FAIL = "rcdc_terminal_component_independent_upgrade_retry_result_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_TASK_DESC = "rcdc_terminal_component_independent_upgrade_retry_task_desc";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_ITEM_NAME = "rcdc_terminal_component_independent_upgrade_retry_item_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_TASK_NAME = "rcdc_terminal_component_independent_upgrade_retry_task_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_SUCCESS_LOG = "rcdc_terminal_component_independent_upgrade_retry_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_FAIL_LOG = "rcdc_terminal_component_independent_upgrade_retry_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_SUCCESS = "rcdc_terminal_component_independent_upgrade_retry_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_FAIL = "rcdc_terminal_component_independent_upgrade_retry_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_RESULT = "rcdc_terminal_component_independent_upgrade_add_result";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_RESULT_SUCCESS = "rcdc_terminal_component_independent_upgrade_add_result_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_RESULT_FAIL = "rcdc_terminal_component_independent_upgrade_add_result_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_TASK_DESC = "rcdc_terminal_component_independent_upgrade_add_task_desc";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_ITEM_NAME = "rcdc_terminal_component_independent_upgrade_add_item_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_TASK_NAME = "rcdc_terminal_component_independent_upgrade_add_task_name";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_SUCCESS_LOG = "rcdc_terminal_component_independent_upgrade_add_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL_LOG = "rcdc_terminal_component_independent_upgrade_add_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_SUCCESS = "rcdc_terminal_component_independent_upgrade_add_success";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL = "rcdc_terminal_component_independent_upgrade_add_fail";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_TASK_CLOSE_SUCCESS_LOG = "rcdc_terminal_component_independent_upgrade_task_close_success_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_TASK_CLOSE_FAIL_LOG = "rcdc_terminal_component_independent_upgrade_task_close_fail_log";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_IS_EMPTY = "23201061";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_IS_INVALID = "23201062";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_HAS_EMPTY_VALUE = "23201063";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_HAS_DUPLICATE_VALUE =
            "23201064";

    String RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_HAS_VDI_TERMINAL = "23201065";

    /** 上传终端配置任务子项名称 */
    String RCDC_TERMINAL_CONFIG_UPLOAD_ITEM_NAME = "rcdc_terminal_config_upload_item_name";
    /** 上传终端配置异步任务名称 */
    String RCDC_TERMINAL_CONFIG_UPLOAD_TASK_NAME = "rcdc_terminal_config_upload_task_name";
    /** 上传终端配置异步任务过程提示 */
    String RCDC_TERMINAL_CONFIG_UPLOAD_DESC = "rcdc_terminal_config_upload_desc";
    /** 上传终端配置成功日志*/
    String RCDC_TERMINAL_CONFIG_UPLOAD_SUCCESS_LOG = "rcdc_terminal_config_upload_success_log";
    /** 上传终端配置结果 - 成功*/
    String RCDC_TERMINAL_CONFIG_UPLOAD_RESULT_SUCCESS = "rcdc_terminal_config_upload_result_success";
    /** 上传终端配置失败日志*/
    String RCDC_TERMINAL_CONFIG_UPLOAD_FAIL_LOG = "rcdc_terminal_config_upload_fail_log";
    /** 上传终端配置结果 - 失败*/
    String RCDC_TERMINAL_CONFIG_UPLOAD_RESULT_FAIL = "rcdc_terminal_config_upload_result_fail";
    /** 上传终端配置任务成功*/
    String RCDC_TERMINAL_CONFIG_UPLOAD_TASK_SUCCESS = "rcdc_terminal_config_upload_task_success";
    /** 上传终端配置任务失败*/
    String RCDC_TERMINAL_CONFIG_UPLOAD_TASK_FAIL = "rcdc_terminal_config_upload_task_fail";
    /**
     * 配置文件没有操作权限
     */



    /** 导入终端配置任务子项名称 */
    String RCDC_TERMINAL_CONFIG_IMPORT_ITEM_NAME = "rcdc_terminal_config_import_item_name";
    /** 导入终端配置异步任务名称 */
    String RCDC_TERMINAL_CONFIG_IMPORT_TASK_NAME = "rcdc_terminal_config_import_task_name";
    /** 导入终端配置异步任务过程提示 */
    String RCDC_TERMINAL_CONFIG_IMPORT_DESC = "rcdc_terminal_config_import_desc";
    /** 导入终端配置成功日志*/
    String RCDC_TERMINAL_CONFIG_IMPORT_SUCCESS_LOG = "rcdc_terminal_config_import_success_log";
    /** 导入终端配置结果 - 成功*/
    String RCDC_TERMINAL_CONFIG_IMPORT_RESULT_SUCCESS = "rcdc_terminal_config_import_result_success";
    /** 导入终端配置失败日志*/
    String RCDC_TERMINAL_CONFIG_IMPORT_FAIL_LOG = "rcdc_terminal_config_import_fail_log";
    /** 导入终端配置结果 - 失败*/
    String RCDC_TERMINAL_CONFIG_IMPORT_RESULT_FAIL = "rcdc_terminal_config_import_result_fail";
    /** 导入终端配置任务成功*/
    String RCDC_TERMINAL_CONFIG_IMPORT_TASK_SUCCESS = "rcdc_terminal_config_import_task_success";
    /** 导入终端配置任务失败*/
    String RCDC_TERMINAL_CONFIG_IMPORT_TASK_FAIL = "rcdc_terminal_config_import_task_fail";

    String RCDC_TERMINAL_INIT_HAS_EXIST_BE_DELETE = "23201066";

    String RCDC_TERMINAL_INIT_HAS_EXIST_TERMINAL_NOT_SUPPORT = "23201067";

    String RCDC_TERMINAL_INIT_HAS_EXIST_TERMINAL_NOT_ONLINE = "23201068";

    String RCDC_TERMINAL_CONFIG_START_MODE_SUCCESS = "rcdc_terminal_config_start_mode_success";

    String RCDC_TERMINAL_CONFIG_START_MODE_FAIL = "rcdc_terminal_config_start_mode_fail";

    String RCDC_TERMINAL_CONFIG_START_MODE_ALL_SUCCESS = "rcdc_terminal_config_start_mode_all_success";

    String RCDC_TERMINAL_CONFIG_START_MODE_PART_SUCCESS = "rcdc_terminal_config_start_mode_part_success";

    String RCDC_TERMINAL_CONFIG_TERMINAL_FAST_MODE = "rcdc_terminal_config_terminal_fast_mode";

    String RCDC_TERMINAL_CONFIG_TERMINAL_COMPATIBLE_MODE = "rcdc_terminal_config_terminal_compatible_mode";

    String RCDC_TERMINAL_CONFIG_START_MODE = "rcdc_terminal_config_start_mode";

}
