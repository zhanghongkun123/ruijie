package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.06
 *
 * @author linhj
 */
public interface ImageTemplateBusinessKey {

    /** 编辑镜像发布任务验证是否属于 VDI 镜像 */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_VERIFICATION_VDI_LOG = "23200742";
    /** 编辑镜像发布任务验证是否属于待发布状态 */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_VERIFICATION_PRE_LOG = "23200743";
    /** 编辑镜像发布任务操作成功（操作日志） */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_SUCCESS_LOG = "rcdc_rco_update_image_publish_task_success_log";
    /** 编辑镜像发布任务操作失败（操作日志） */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_ERROR_LOG = "rcdc_rco_update_image_publish_task_error_log";
    /** 编辑镜像发布任务时间需要传入 */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIME_EMPTY_LOG = "23200744";
    /** 输入的通知内容不允许全为空格 */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIP_MSG_EMPTY_LOG = "23200746";
    /** 编辑镜像发布任务时间不能早于服务器时间（操作日志） */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIME_CHECK_ERROR_LOG = "23200745";
    /** 编辑镜像发布通知时间不能早于服务器时间（操作日志） */
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_TIME_CHECK_ERROR_LOG = "23200747";
    /** 编辑镜像发布需要通知用户则通知消息需要填写 **/
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_MESSAGE_EMPTY_LOG = "23200748";
    /** 需要通知用户则通知时间需要设置 **/
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_TIME_EMPTY_LOG = "23200749";
    /** 编辑镜像发布通知消息长度过长 **/
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_MESSAGE_TOO_LONG_LOG = "23200750";
    /** 通知时间需要与发布时间提前五分钟以上 **/
    String RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_TIME_ERROR_LOG = "23200751";

    /** 取消镜像发布任务操作成功（操作日志） */
    String RCDC_RCO_CANCEL_IMAGE_PUBLISH_TASK_SUCCESS_LOG = "rcdc_rco_cancel_image_publish_task_success_log";
    /** 取消镜像发布任务操作失败（操作日志） */
    String RCDC_RCO_CANCEL_IMAGE_PUBLISH_TASK_ERROR_LOG = "rcdc_rco_cancel_image_publish_task_error_log";
    /* 镜像未找到 */
    String RCDC_IMAGE_TEMPLATE_NOT_FOUND = "23200773";
    /* 镜像快照不存在 */
    String RCDC_RCO_IMAGE_SNAPSHOT_NOT_FOUND = "23200776";
    /* 创建恢复镜像模板[{0}]到快照[{1}]定时任务操作成功 */
    String RCDC_RCO_IMAGE_SNAPSHOT_TASK_SUCCESS_LOG = "rcdc_rco_image_snapshot_task_success_log";
    /* =创建恢复镜像模板 到快照 定时任务操作失败，失败原因： */
    String RCDC_RCO_IMAGE_SNAPSHOT_TASK_ERROR_LOG = "rcdc_rco_image_snapshot_task_error_log";
    /* 恢复的任务 定时时间需要传入 */
    String RCDC_RCO_IMAGE_SNAPSHOT_TASK_TIME_EMPTY_LOG = "23200774";
    /* 恢复的任务 定时时间不能早于当前时间 */
    String RCDC_RCO_IMAGE_SNAPSHOT_TASK_TIME_CHECK_ERROR_LOG = "23200775";
    /* 取消恢复镜像模板 到快照定时任务操作成功 */
    String RCDC_RCO_CANCEL_IMAGE_SNAPSHOT_TASK_SUCCESS_LOG = "rcdc_rco_cancel_image_snapshot_task_success_log";
    /* 取消恢复镜像模板 到快照定时任务操作失败，失败原因： */
    String RCDC_RCO_CANCEL_IMAGE_SNAPSHOT_TASK_ERROR_LOG = "rcdc_rco_cancel_image_snapshot_task_error_log";
    /* 锁定镜像模板 快照 成功 */
    String RCDC_RCO_IMAGE_SNAPSHOT_LOCK_SUCCESS = "rcdc_rco_image_snapshot_lock_success";
    /* 锁定镜像模板 快照 失败，失败原因： */
    String RCDC_RCO_IMAGE_SNAPSHOT_LOCK_FAIL = "rcdc_rco_image_snapshot_lock_fail";
    /* 解锁镜像模板 快照 成功 */
    String RCDC_RCO_IMAGE_SNAPSHOT_UNLOCK_SUCCESS = "rcdc_rco_image_snapshot_unlock_success";
    /* 解锁镜像模板 快照 失败，失败原因： */
    String RCDC_RCO_IMAGE_SNAPSHOT_UNLOCK_FAIL = "rcdc_rco_image_snapshot_unlock_fail";
    /* 镜像模板重命名快照成功 */
    String RCDC_RCO_IMAGE_SNAPSHOT_RENAME_SUCCESS_LOG = "rcdc_rco_image_snapshot_rename_success_log";
    /* 镜像模板 重命名快照 失败，失败原因： */
    String RCDC_RCO_IMAGE_SNAPSHOT_RENAME_FAIL_LOG = "rcdc_rco_image_snapshot_rename_fail_log";

    String RCDC_RCO_IMAGE_SNAPSHOT_RENAME_FAIL_REASON = "rcdc_rco_image_snapshot_rename_fail_reason";
    /* 镜像模板 设置快照最大数量 成功 */
    String RCDC_RCO_IMAGE_SNAPSHOT_SET_MAX_NUM_SUCCESS_LOG = "rcdc_rco_image_snapshot_set_max_num_success_log";
    /* 镜像模板 设置快照最大数量 失败，失败原因： */
    String RCDC_RCO_IMAGE_SNAPSHOT_SET_MAX_NUM_FAIL_LOG = "rcdc_rco_image_snapshot_set_max_num_fail_log";
    /* 镜像模板 快照数量 超过最大数量，请删除多余快照 */
    String RCDC_RCO_IMAGE_SNAPSHOT_SET_MAX_NUM_FAIL = "23200777";
    /* 删除镜像模板快照 */
    String RCDC_RCO_IMAGE_SNAPSHOT_DELETE_TASK_NAME = "rcdc_rco_image_snapshot_delete_task_name";
    /* 正在删除镜像模板 快照 */
    String RCDC_RCO_IMAGE_SNAPSHOT_DELETE_DESC = "rcdc_rco_image_snapshot_delete_desc";
    /* 删除镜像模板快照成功 */
    String RCDC_RCO_IMAGE_SNAPSHOT_DELETE_SUCCESS_LOG = "rcdc_rco_image_snapshot_delete_success_log";
    /* 删除镜像模板快照失败，失败原因： */
    String RCDC_RCO_IMAGE_SNAPSHOT_DELETE_FAIL_LOG = "rcdc_rco_image_snapshot_delete_fail_log";
    /* 删除镜像模板快照成功 */
    String RCDC_RCO_IMAGE_SNAPSHOT_DELETE_TASK_SUCCESS = "rcdc_rco_image_snapshot_delete_task_success";
    /* 删除镜像模板 快照失败 */
    String RCDC_RCO_IMAGE_SNAPSHOT_DELETE_TASK_FAIL = "rcdc_rco_image_snapshot_delete_task_fail";
    /** 快照恢复任务 需要通知用户则通知时间需要设置 **/
    String RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_TIME_EMPTY_LOG = "23200778";
    /** 快照恢复任务 需要通知用户则通知消息需要填写 **/
    String RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_MESSAGE_EMPTY_LOG = "23200779";
    /** 快照恢复任务 通知消息长度过长 **/
    String RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_MESSAGE_TOO_LONG_LOG = "23200780";
    /** 快照恢复任务 通知时间不能早于服务器时间（操作日志） */
    String RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_TIME_CHECK_ERROR_LOG = "23200781";
    /** 快照恢复任务 通知时间需要与恢复时间提前五分钟以上 **/
    String RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_TIME_ERROR_LOG = "23200782";
    /** 镜像发布中不允许锁定快照 **/
    String RCDC_RCO_NOT_ALLOW_LOCK_SNAPSHOT_WHEN_IMAGE_PUBLISHING = "23200783";
    /** 镜像发布中不允许解锁快照 **/
    String RCDC_RCO_NOT_ALLOW_UNLOCK_SNAPSHOT_WHEN_IMAGE_PUBLISHING = "23200784";
    /* 镜像模板 镜像版本 超过最大数量，请删除多余版本 */
    String RCDC_RCO_IMAGE_VERSION_SET_MAX_NUM_FAIL = "23200785";
    /* 镜像模板 设置镜像版本最大数量 成功 */
    String RCDC_RCO_IMAGE_VERSION_SET_MAX_NUM_SUCCESS_LOG = "rcdc_rco_image_version_set_max_num_success_log";

    /* 镜像模板 设置快照最大数量 失败，失败原因： */
    String RCDC_RCO_IMAGE_VERSION_SET_MAX_NUM_FAIL_LOG = "rcdc_rco_image_version_set_max_num_fail_log";

    String RCDC_RCC_REMOTE_TERMINAL_EDIT_IMAGE_OPERATE = "rcdc_rcc_terminal_edit_image_operate";

    String RCDC_RCC_REMOTE_TERMINAL_IMAGE_INSTALL_DRIVER_OPERATE = "rcdc_rcc_terminal_image_install_driver_operate";

    String RCDC_RCO_IMAGE_TERMINAL_EDIT_HAS_NO_PERMISSION = "23200799";

    String RCDC_RCO_TERMINAL_HAS_NO_PERMISSION = "rcdc_rco_terminal_has_no_permission";

    String RCDC_RCO_IMAGE_TERMINAL_EDIT_HAS_NO_SUPPORT = "23200800";

    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_TASK_NAME = "rcdc_rco_remote_terminal_edit_idv_image_task_name";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_DESC = "rcdc_rco_remote_terminal_edit_idv_image_desc";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_TASK_SUCCESS = "rcdc_rco_remote_terminal_edit_idv_image_task_success";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_TASK_FAIL = "rcdc_rco_remote_terminal_edit_idv_image_task_fail";

    String RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_TASK_NAME = "rcdc_rco_remote_terminal_edit_tci_image_task_name";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_DESC = "rcdc_rco_remote_terminal_edit_tci_image_desc";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_TASK_SUCCESS = "rcdc_rco_remote_terminal_edit_tci_image_task_success";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_TASK_FAIL = "rcdc_rco_remote_terminal_edit_tci_image_task_fail";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_FAIL_LOG = "rcdc_rco_remote_terminal_edit_tci_image_fail_log";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_FAIL_LOG = "rcdc_rco_remote_terminal_edit_idv_image_fail_log";


    String RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_TASK_NAME = "rcdc_rco_remote_terminal_finish_edit_image_task_name";
    String RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_DESC = "rcdc_rco_remote_terminal_finish_edit_image_desc";
    String RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_TASK_SUCCESS = "rcdc_rco_remote_terminal_finish_edit_image_task_success";
    String RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_TASK_FAIL = "rcdc_rco_remote_terminal_finish_edit_image_task_fail";
    String RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_FAIL_LOG = "rcdc_rco_remote_terminal_finish_edit_image_fail_log";

    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_NAME = "rcdc_rco_remote_terminal_re_edit_image_task_name";
    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_DESC = "rcdc_rco_remote_terminal_re_edit_image_desc";
    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_SUCCESS = "rcdc_rco_remote_terminal_re_edit_image_task_success";
    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_FAIL = "rcdc_rco_remote_terminal_re_edit_image_task_fail";
    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_ITEM_SUCCESS_DESC = "rcdc_rco_remote_terminal_re_edit_image_item_success_desc";
    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_ITEM_FAIL_DESC = "rcdc_rco_remote_terminal_re_edit_image_item_fail_desc";
    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_SUCCESS_LOG = "rcdc_rco_remote_terminal_re_edit_image_success_log";
    String RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_FAIL_LOG = "rcdc_rco_remote_terminal_re_edit_image_fail_log";


    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_TASK_NAME = "rcdc_rco_remote_terminal_edit_cancel_upload_image_task_name";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_DESC = "rcdc_rco_remote_terminal_edit_cancel_upload_image_desc";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_TASK_SUCCESS = "rcdc_rco_remote_terminal_edit_cancel_upload_image_task_success";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_TASK_FAIL = "rcdc_rco_remote_terminal_edit_cancel_upload_image_task_fail";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_ITEM_SUCCESS_DESC =
            "rcdc_rco_remote_terminal_edit_cancel_upload_image_item_success_desc";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_ITEM_FAIL_DESC = "rcdc_rco_remote_terminal_edit_cancel_upload_image_item_fail_desc";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_SUCCESS_LOG = "rcdc_rco_remote_terminal_edit_cancel_upload_image_success_log";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_FAIL_LOG = "rcdc_rco_remote_terminal_edit_cancel_upload_image_fail_log";
    String  RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_FAIL = "rcdc_rco_remote_terminal_edit_cancel_upload_image_fail";
    String  RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_FAIL_TYPE_ERROR = "23200801";

    String  RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_FAIL_TERMINAL_NOT_EXIT = "23200802";

    String RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_SUCCESS = "rcdc_rco_abort_remote_edit_image_template_success";
    String RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_FAIL = "rcdc_rco_abort_remote_edit_image_template_task_fail";
    String RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_FAIL_LOG = "rcdc_rco_abort_remote_edit_image_template_task_fail_log";
    String RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_DESC = "rcdc_rco_abort_remote_edit_image_template_task_desc";
    String RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_NAME = "rcdc_rco_abort_remote_edit_image_template_task_name";

    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_TASK_NAME = "rcdc_rco_remote_terminal_edit_image_upload_task_name";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_DESC = "rcdc_rco_remote_terminal_edit_image_upload_desc";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_TASK_SUCCESS = "rcdc_rco_remote_terminal_edit_image_upload_task_success";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_TASK_FAIL = "rcdc_rco_remote_terminal_edit_image_upload_task_fail";
    String RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_FAIL_LOG = "rcdc_rco_remote_terminal_edit_image_upload_fail_log";

    String RCDC_RCO_ARGS_MATCH_ARR_CAN_NOT_NULL = "rcdc_rco_args_match_arr_can_not_null";
    String RCDC_RCO_ARGS_IMAGE_ID_CAN_NOT_NULL = "rcdc_rco_args_image_id_can_not_null";

    String RCDC_RCC_REMOTE_TERMINAL_EDIT_IMAGE_LOG_NO_FIND = "23200803";

    String RCDC_RCC_REMOTE_TERMINAL_EDIT_IMAGE_OPERATE_TYPE_NO_FIND = "23200804";

    String RCDC_RCC_ABORT_LOCAL_EDIT_STATUS_ERROR = "rcdc_rcc_abort_local_edit_status_error";

    String RCDC_RCO_ABORT_LOCAL_EDIT_IMAGE_TYPE_NO_SUPPORT = "rcdc_rco_abort_local_edit_image_type_no_support";

}
