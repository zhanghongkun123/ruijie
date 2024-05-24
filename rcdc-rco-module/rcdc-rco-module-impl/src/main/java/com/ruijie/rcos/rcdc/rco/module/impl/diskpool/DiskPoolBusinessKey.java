package com.ruijie.rcos.rcdc.rco.module.impl.diskpool;

/**
 * Description: 国际化KEY
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
public interface DiskPoolBusinessKey {

    String RCDC_RCO_DISK_NO_EXIST = "23200314";

    String RCDC_RCO_DISK_POOL_AUTO_CREATE_DISK_SUCCESS = "rcdc_rco_disk_pool_auto_create_disk_success";

    String RCDC_RCO_DISK_POOL_AUTO_CREATE_DISK_FAIL = "rcdc_rco_disk_pool_auto_create_disk_fail";

    String RCDC_RCO_DISK_FAULT_AUTO_RECOVERY_TASK = "rcdc_rco_disk_fault_auto_recovery_task";

    String RCDC_RCO_USER_DESK_ATTACH_DISK_FAIL = "rcdc_rco_user_desk_attach_disk_fail";

    String RCDC_RCO_USER_DESK_ATTACH_DISK_SUCCESS = "rcdc_rco_user_desk_attach_disk_success";

    String RCDC_RCO_USER_DESK_DETACH_DISK_FAIL = "rcdc_rco_user_desk_detach_disk_fail";

    String RCDC_RCO_USER_DESK_DETACH_DISK_SUCCESS = "rcdc_rco_user_desk_detach_disk_success";

    String RCDC_RCO_DISK_EXPAND_DELAY_SUCCESS = "rcdc_rco_disk_expand_delay_success";

    String RCDC_RCO_USER_DISK_STATE_ERROR = "rcdc_rco_user_disk_state_error";

    String RCDC_RCO_SYSTEM_ERROR = "rcdc_rco_system_error";

    /**
     * 磁盘池相关异常提示
     */
    String RCDC_RCO_DISK_POOL_UNAVAILABLE_DELETE_UNALLOWED = "23200860";
    String RCDC_RCO_DISK_POOL_IN_DISK_DELETE_UNALLOWED = "23200861";

    String RCDC_RCO_DISK_POOL_UPDATE_BIND_TASK_REPEAT = "23200315";
    String RCDC_RCO_DISK_POOL_GROUP_BIND_REPEAT = "23200316";
    String RCDC_RCO_DISK_POOL_USER_BIND_REPEAT = "23200317";
    String RCDC_RCO_DISK_POOL_USER_BIND_DISK_DELETE_ERROR = "23200318";
    String RCDC_RCO_DISK_POOL_ADD_BIND_USER_LOG = "rcdc_rco_disk_pool_add_bind_user_log";
    String RCDC_RCO_DISK_POOL_DELETE_BIND_USER_LOG = "rcdc_rco_disk_pool_delete_bind_user_log";
    String RCDC_RCO_DISK_POOL_ADD_BIND_GROUP_LOG = "rcdc_rco_disk_pool_add_bind_group_log";
    String RCDC_RCO_DISK_POOL_DELETE_BIND_GROUP_LOG = "rcdc_rco_disk_pool_delete_bind_group_log";

    String RCDC_RCO_QUARTZ_TASK_DISK_FAULT_RECOVERY_SUCCESS = "rcdc_rco_quartz_task_disk_fault_recovery_success";

    String RCDC_RCO_DISK_POOL_UPDATE_BIND_USER_NULL = "23200319";
    String RCDC_RCO_DISK_POOL_UPDATE_BIND_GROUP_NULL = "23200321";
    String RCDC_RCO_DISK_POOL_UPDATE_BIND_VISITOR_FAIL = "23200320";

    String RCDC_RCO_DISK_POOL_NOT_EXIST_PERSONAL_DISK = "rcdc_rco_disk_pool_not_exist_personal_disk";

    String RCDC_RCO_DISK_DEFAULT_NAME = "rcdc_rco_disk_default_name";

    String RCDC_RCO_DISK_POOL_ASSIGN_FAIL_NAME = "rcdc_rco_disk_pool_assign_fail_name";

    String RCDC_RCO_DISK_POOL_ASSIGN_FAIL = "rcdc_rco_disk_pool_assign_fail";

    String RCDC_RCO_DISK_TEMPORARY_UNAVAILABLE = "rcdc_rco_disk_temporary_unavailable";

    String RCDC_RCO_USER_DISK_UNAVAILABLE_DESK_ATTACH = "rcdc_rco_user_disk_unavailable_desk_attach";

    String RCDC_RCO_USER_DISK_IS_RELATION_DESK = "rcdc_rco_user_disk_is_relation_desk";

    String RCDC_RCO_DISK_POOL_AD_GROUP_BIND_REPEAT = "23200322";

    String RCDC_RCO_DISK_POOL_ADD_BIND_AD_GROUP_LOG = "rcdc_rco_disk_pool_add_bind_ad_group_log";

    String RCDC_RCO_DISK_POOL_DELETE_BIND_AD_GROUP_LOG = "rcdc_rco_disk_pool_delete_bind_ad_group_log";

    String RCDC_RCO_DISK_POOL_ASSIGN_AD_GROUP_ERROR = "23200323";

    String RCDC_RCO_DESKTOP_USER_DISK_NOT_READY_ERROR = "rcdc_rco_desktop_user_disk_not_ready_error";

    String RCDC_RCO_USER_NOT_ASSIGN_DISK_POOL = "rcdc_rco_user_not_assign_disk_pool_error";
    String RCDC_RCO_DISK_POOL_STATUS_ERROR = "rcdc_rco_disk_pool_status_error";
    String RCDC_RCO_DISK_POOL_ASSIGN_DISK_ERROR = "rcdc_rco_disk_pool_assign_disk_error";
    String RCDC_RCO_DESKTOP_LOCK_USER_DISK_ERROR = "rcdc_rco_desktop_lock_user_disk_error";
    String RCDC_RCO_DISK_OTHER_DESKTOP_USE_ERROR = "rcdc_rco_disk_other_desktop_use_error";
    String RCDC_RCO_SYSTEM_AUTO_ASSIGN_DISK_USER_SUCCESS = "rcdc_rco_system_auto_assign_disk_user_success";
    String RCDC_RCO_USER_DISK_DESKTOP_COMPUTE_CLUSTER_ERROR = "rcdc_rco_user_disk_desktop_compute_cluster_error";
    String RCDC_RCO_DISK_DESKTOP_COMPUTE_CLUSTER_ERROR = "rcdc_rco_disk_desktop_compute_cluster_error";
    String RCDC_RCO_USER_DISK_DESKTOP_MULTI_SESSION = "23201490";
}
