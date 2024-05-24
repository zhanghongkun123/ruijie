package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

/**
 * Description:国际化key
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 15:11
 *
 * @author ketb
 */
public interface ComputerBusinessKey {

    /**关闭PC成功日志*/
    String RCDC_RCO_COMPUTER_CLOSE_SUCCESS_LOG = "rcdc_rco_computer_close_success_log";

    /**关闭PC失败日志*/
    String RCDC_RCO_COMPUTER_CLOSE_FAIL_LOG = "rcdc_rco_computer_close_fail_log";

    /**发送关闭PC命令*/
    String RCDC_RCO_COMPUTER_CLOSE_ITEM_NAME = "rcdc_rco_computer_close_item_name";

    /**关闭pc任务名称*/
    String RCDC_RCO_COMPUTER_CLOSE_SINGLE_TASK_NAME = "rcdc_rco_computer_close_single_task_name";

    /**关闭pc任务描述*/
    String RCDC_RCO_COMPUTER_CLOSE_SINGLE_TASK_DESC = "rcdc_rco_computer_close_single_task_desc";

    /**批量关闭pc任务名称*/
    String RCDC_RCO_COMPUTER_CLOSE_TASK_NAME = "rcdc_rco_computer_close_task_name";

    /**批量关闭pc任务描述*/
    String RCDC_RCO_COMPUTER_CLOSE_TASK_DESC = "rcdc_rco_computer_close_task_desc";

    /**当前PC处于离线状态，无法关机*/
    String RCDC_RCO_COMPUTER_CLOSE_OFFLINE = "rcdc_rco_computer_close_offline";

    /**批量关闭pc结果*/
    String RCDC_RCO_COMPUTER_CLOSE_RESULT = "rcdc_rco_computer_close_result";

    /**取消pc包障*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT = "rcdc_rco_computer_relieve_fault";

    /**取消pc包障(批量取消PC报障任务名称)*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_TASK_NAME = "rcdc_rco_computer_relieve_fault_task_name";

    /**正在取消PC报障*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_TASK_DESC = "rcdc_rco_computer_relieve_fault_task_desc";

    /**正在取消PC[{0}]报障*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_ONE_TASK_DESC = "rcdc_rco_computer_relieve_fault_one_task_desc";

    /**取消pc包障成功*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_SUCCESS = "rcdc_rco_computer_relieve_fault_success";

    /**取消pc报障失败*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL = "rcdc_rco_computer_relieve_fault_fail";

    /**取消PC[{0}]报障失败*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL_RESULT = "rcdc_rco_computer_relieve_fault_fail_result";

    /**取消PC[{0}]报障成功*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_SUCCESS_RESULT = "rcdc_rco_computer_relieve_fault_success_result";

    /**批量取消PC报障，成功[{0}]条，失败[{1}]条*/
    String RCDC_RCO_COMPUTER_RELIEVE_FAULT_RESULT = "rcdc_rco_computer_relieve_fault_result";

    /**修改pc备注成功*/
    String RCDC_RCO_COMPUTER_MODIFY_ALIAS_SUCCESS = "rcdc_rco_computer_modify_alias_success";

    /**修改pc备注失败*/
    String RCDC_RCO_COMPUTER_MODIFY_ALIAS_FAIL = "rcdc_rco_computer_modify_alias_fail";

    /**删除pc成功*/
    String RCDC_RCO_COMPUTER_DELETE_COMPUTER_SUCCESS_LOG = "rcdc_rco_computer_delete_computer_success_log";

    /**删除pc失败*/
    String RCDC_RCO_COMPUTER_DELETE_COMPUTER_FAIL_LOG = "rcdc_rco_computer_delete_computer_fail_log";

    /**删除pc*/
    String RCDC_RCO_COMPUTER_DELETE_ITEM_NAME = "rcdc_rco_computer_delete_item_name";

    /**删除pc任务名称*/
    String RCDC_RCO_COMPUTER_DELETE_SINGLE_TASK_NAME = "rcdc_rco_computer_delete_single_task_name";

    /**删除pc任务描述*/
    String RCDC_RCO_COMPUTER_DELETE_SINGLE_TASK_DESC = "rcdc_rco_computer_delete_single_task_desc";

    /**批量删除pc任务名称*/
    String RCDC_RCO_COMPUTER_DELETE_TASK_NAME = "rcdc_rco_computer_delete_task_name";

    /**批量删除pc任务描述*/
    String RCDC_RCO_COMPUTER_DELETE_TASK_DESC = "rcdc_rco_computer_delete_task_desc";

    /**删除pc成功*/
    String RCDC_RCO_COMPUTER_DELETE_SINGLE_SUCCESS_RESULT = "rcdc_rco_computer_delete_single_success_result";

    /**删除pc失败*/
    String RCDC_RCO_COMPUTER_DELETE_SINGLE_FAIL_RESULT = "rcdc_rco_computer_delete_single_fail_result";

    /**批量删除pc结果*/
    String RCDC_RCO_COMPUTER_DELETE_RESULT = "rcdc_rco_computer_delete_result";

    /**
     * 不存在小助手安装包
     */
    String RCDC_RCO_ASSISTANTAPP_UPGRADE_PACKAGE_NOT_EXIST = "rcdc_rco_assistantapp_upgrade_package_not_exist";

    String RCDC_RCO_COMPUTER_UPDATE_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_rco_computer_update_terminal_group_success_log";
    String RCDC_RCO_COMPUTER_UPDATE_TERMINAL_GROUP_FAIL_LOG = "rcdc_rco_computer_update_terminal_group_fail_log";
    String RCDC_RCO_COMPUTER_UPDATE_TERMINAL_GROUP_RESULT = "rcdc_rco_computer_update_terminal_group_result";
    String RCDC_RCO_COMPUTER_CREATE_TERMINAL_SUCCESS_LOG = "rcdc_rco_computer_create_terminal_success_log";
    String RCDC_RCO_COMPUTER_CREATE_TERMINAL_FAIL_LOG = "rcdc_rco_computer_create_terminal_fail_log";
    String RCDC_RCO_COMPUTER_CREATE_TERMINAL_IP_EXIST = "rcdc_rco_computer_create_terminal_ip_exist";

    /**
     * 远程协助
     */
    String RCDC_RCO_COMPUTER_ASSIST_REQUEST_SUC_LOG = "rcdc_rco_computer_assist_request_suc_log";
    String RCDC_RCO_COMPUTER_ASSIST_REQUEST_FAIL_LOG = "rcdc_rco_computer_assist_request_fail_log";
    String RCDC_RCO_COMPUTER_ASSIST_STOP_SUC_LOG = "rcdc_rco_computer_assist_stop_suc_log";
    String RCDC_RCO_COMPUTER_ASSIST_STOP_FAIL_LOG = "rcdc_rco_computer_assist_stop_fail_log";
    String RCDC_RCO_COMPUTER_REMOTE_ASSIST_FAIL = "rcdc_rco_computer_remote_assist_fail";

    String RCDC_RCO_COMPUTER_IMPORT_FAIL = "23201400";
    String RCDC_RCO_COMPUTER_IMPORT_ANALYSIS_FAIL = "23201658";
    String RCDC_RCO_IMPORT_COMPUTER_TEMPLATE_IS_INVALIDATE = "23201401";
    String RCDC_RCO_USER_IMPORT_NOT_COMPUTER_DATA = "23201402";
    String RCDC_RCO_COMPUTER_IMPORT_IP_NOT_EMPTY = "23201403";
    String RCDC_RCO_COMPUTER_IMPORT_IP_INCORRECT = "23201404";
    String RCO_IMPORT_COMPUTER_PARSE_VALIDATE_FAIL = "23201405";
    String RCDC_RCO_COMPUTER_IMPORT_USERGROUP_NOT_EMPTY = "23201406";
    String RCDC_RCO_COMPUTER_IMPORT_GROUPNAME_INCORRECT = "23201407";
    String RCDC_RCO_COMPUTER_IMPORT_GROUPNAME_TOO_LENGTH = "23201408";
    String RCDC_RCO_COMPUTER_IMPORT_GROUPNAME_NOT_ALLOW_RESERVED = "23201409";
    String RCDC_RCO_IMPORT_COMPUTER_SUCCESS = "rcdc_rco_import_computer_success";
    String RCDC_RCO_IMPORT_COMPUTER_FAIL = "23201411";
    String RCDC_RCO_COMPUTER_IMPORT_THIRD_PARTY_NOT_EXIT = "23201412";
    String RCDC_RCO_IMPORT_COMPUTER_ITEM_NAME = "rcdc_rco_import_computer_item_name";
    String RCDC_RCO_IMPORT_COMPUTER_TASK_NAME = "rcdc_rco_import_computer_task_name";
    String RCDC_RCO_IMPORT_COMPUTER_TASK_DESC = "rcdc_rco_import_computer_task_desc";
    String RCDC_RCO_IMPORT_COMPUTER_BATCH_TASK_SUCCESS = "rcdc_rco_import_computer_batch_task_success";
    String RCDC_RCO_EXPORT_COMPUTER_EMPTY_DATA_ERROR = "23201414";
    String RCDC_RCO_EXPORT_COMPUTER_SUCCESS_LOG = "rcdc_rco_export_computer_success_log";
    String RCDC_COMPUTER_WAKE_UP_ITEM_NAME = "rcdc_computer_wake_up_item_name";
    String RCDC_COMPUTER_WAKE_UP_BATCH_ITEM_DESC = "rcdc_computer_wake_up_batch_item_desc";
    String RCDC_COMPUTER_WAKE_UP_SINGLE_ITEM_DESC = "rcdc_computer_wake_up_single_item_desc";
    String RCDC_COMPUTER_WAKE_UP_TASK_NAME = "23201435";
    String RCDC_COMPUTER_WAKE_UP_SUCCESS = "rcdc_computer_wake_up_success";
    String RCDC_COMPUTER_WAKE_UP_FAIL = "rcdc_computer_wake_up_fail";
    String RCDC_COMPUTER_WAKE_UP_RESULT = "rcdc_computer_wake_up_result";
    String RCDC_RCO_COMPUTER_NOT_SUPPORT_THIRD_PARTY_OP = "23201441";
}
