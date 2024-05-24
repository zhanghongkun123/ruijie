package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:34
 *
 * @author coderLee23
 */
public interface UamAppBusinessKey {

    /**
     * 批量应用删除key
     */
    String RCDC_UAM_APP_DELETE_ITEM_NAME = "rcdc_uam_app_delete_item_name";


    /**
     * 删除应用
     */
    String RCDC_UAM_APP_DELETE_SINGLE_TASK_NAME = "rcdc_uam_app_delete_single_task_name";


    /**
     * 正在删除应用[{0}]
     */
    String RCDC_UAM_APP_DELETE_SINGLE_TASK_DESC = "rcdc_uam_app_delete_single_task_desc";

    /**
     * 批量删除应用
     */
    String RCDC_UAM_APP_DELETE_TASK_NAME = "rcdc_uam_app_delete_task_name";

    /**
     * 正在批量删除应用
     */
    String RCDC_UAM_APP_DELETE_TASK_DESC = "rcdc_uam_app_delete_task_desc";

    /**
     * 批量删除应用，成功[{0}]条，失败[{1}]条
     */
    String RCDC_UAM_APP_DELETE_SUCCESS_RESULT = "rcdc_uam_app_delete_success_result";


    /**
     * 删除应用[{0}]成功
     */
    String RCDC_UAM_APP_DELETE_SINGLE_SUCCESS_RESULT = "rcdc_uam_app_delete_single_success_result";


    /**
     * 删除应用[{0}]失败
     */
    String RCDC_UAM_APP_DELETE_SINGLE_FAIL_RESULT = "rcdc_uam_app_delete_single_fail_result";

    /**
     * 删除应用[{0}]成功
     */
    String RCDC_UAM_APP_DELETE_SUCCESS_LOG = "rcdc_uam_app_delete_success_log";

    /**
     * 删除应用[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_APP_DELETE_FAIL_LOG = "rcdc_uam_app_delete_fail_log";

    /**
     * 删除应用失败，失败原因：{0}
     */
    String RCDC_UAM_APP_NOT_EXISTS_DELETE_FAIL_LOG = "rcdc_uam_app_not_exists_delete_fail_log";

    /**
     * 非待发布状态，不允许放弃编辑
     */
    String RCDC_UAM_GIVE_UP_STATUS_ERROR = "rcdc_uam_give_up_status_error";

    /**
     * 非待发布状态，不允许发布
     */
    String RCDC_UAM_PUBLISH_STATUS_ERROR = "rcdc_uam_publish_status_error";

    /**
     * 应用[{0}]被交付组使用，不允许删除
     */
    String RCDC_UAM_APP_EXISTS_DELIVERY_GROUP_USED = "23201070";

    /**
     * 应用[{0}]被测试组使用，不允许删除
     */
    String RCDC_UAM_APP_EXISTS_DELIVERY_TEST_USED = "23201071";
}
