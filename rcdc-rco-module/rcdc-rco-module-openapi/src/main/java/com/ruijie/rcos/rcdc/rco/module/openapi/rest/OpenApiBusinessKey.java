package com.ruijie.rcos.rcdc.rco.module.openapi.rest;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/21
 *
 * @author xiao'yong'deng
 */
public interface OpenApiBusinessKey {

    /**
     * 新增用户组成功
     */
    String RCDC_OPENAPI_CREATE_USER_GROUP_SUCCESS_LOG = "rcdc_openapi_create_user_group_success_log";

    /**
     * 新增用户组失败
     */
    String RCDC_OPENAPI_CREATE_USER_GROUP_FAIL_LOG = "rcdc_openapi_create_user_group_fail_log";

    /**
     * 删除用户组成功
     */
    String RCDC_OPENAPI_DELETE_USER_GROUP_SUCCESS_LOG = "rcdc_openapi_delete_user_group_success_log";

    /**
     * 删除用户组失败
     */
    String RCDC_OPENAPI_DELETE_USER_GROUP_FAIL_LOG = "rcdc_openapi_delete_user_group_fail_log";

    /**
     * 用户组名称校验
     */
    String RCDC_OPENAPI_GROUP_NAME_CHECK_FAIL = "rcdc_openapi_group_name_check_fail";

    String RCDC_OPENAPI_GROUP_EXPIRE_TIME_CHECK_FAIL = "23201105";

    /**
     * 云桌面策略相关
     */
    String RCDC_OPENAPI_DESK_STRATEGY_CREATE_SUCCESS_LOG = "rcdc_openapi_desk_strategy_create_success_log";
    String RCDC_OPENAPI_DESK_STRATEGY_CREATE_FAIL_LOG = "rcdc_openapi_desk_strategy_create_fail_log";
    String RCDC_OPENAPI_DESK_STRATEGY_EDIT_SUCCESS_LOG = "rcdc_openapi_desk_strategy_edit_success_log";
    String RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG = "rcdc_openapi_desk_strategy_edit_fail_log";
    String RCDC_OPENAPI_DESK_STRATEGY_DELETE_SUCCESS_LOG = "rcdc_openapi_desk_strategy_delete_success_log";
    String RCDC_OPENAPI_DESK_STRATEGY_DELETE_FAIL_LOG = "rcdc_openapi_desk_strategy_delete_fail_log";
    String RCDC_OPENAPI_DESK_STRATEGY_SYSTEM_ERROR = "rcdc_openapi_desk_strategy_system_error";
    String RCDC_OPENAPI_DESK_STRATEGY_USB_TYPE_NOT_EXIST = "23201104";

    /**
     * 统一管理对象相关
     */
    String RCDC_OPENAPI_UNIFIED_DATA_NOT_EXISTS = "23201106";

    String RCO_DESK_STRATEGY_THIRD_PARTY_BUILT_IN = "23201478";
}
