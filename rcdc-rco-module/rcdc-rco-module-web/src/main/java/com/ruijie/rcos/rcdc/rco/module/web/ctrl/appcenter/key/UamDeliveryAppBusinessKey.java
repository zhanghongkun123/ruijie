package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/16 15:25
 *
 * @author coderLee23
 */
public interface UamDeliveryAppBusinessKey {


    /**
     * 批量删除交互应用key
     */
    String RCDC_DELIVERY_APP_DELETE_ITEM_NAME = "rcdc_delivery_app_delete_item_name";


    /**
     * 删除交付应用
     */
    String RCDC_DELIVERY_APP_DELETE_SINGLE_TASK_NAME = "rcdc_delivery_app_delete_single_task_name";


    /**
     * 正在删除交付应用[{0}]
     */
    String RCDC_DELIVERY_APP_DELETE_SINGLE_TASK_DESC = "rcdc_delivery_app_delete_single_task_desc";

    /**
     * 批量删除应用
     */
    String RCDC_UAM_DELIVERY_APP_DELETE_TASK_NAME = "rcdc_uam_delivery_app_delete_task_name";

    /**
     * 正在批量删除交付应用
     */
    String RCDC_UAM_DELIVERY_APP_DELETE_TASK_DESC = "rcdc_uam_delivery_app_delete_task_desc";


    /**
     * 删除交付应用[{0}]成功
     */
    String RCDC_UAM_DELIVERY_APP_DELETE_SUCCESS_LOG = "rcdc_uam_delivery_app_delete_success_log";

    /**
     * 删除交付应用[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_DELIVERY_APP_DELETE_FAIL_LOG = "rcdc_uam_delivery_app_delete_fail_log";

    /**
     * 删除交付应用失败，失败原因：{0}
     */
    String RCDC_UAM_DELIVERY_APP_NOT_EXISTS_DELETE_FAIL_LOG = "rcdc_uam_delivery_app_not_exists_delete_fail_log";


    /**
     * 批量删除交付应用，成功[{0}]条，失败[{1}]条
     */
    String RCDC_UAM_DELIVERY_APP_DELETE_SUCCESS_RESULT = "rcdc_uam_delivery_app_delete_success_result";


    /**
     * 删除交付应用[{0}]成功
     */
    String RCDC_UAM_DELIVERY_APP_DELETE_SINGLE_SUCCESS_RESULT = "rcdc_uam_delivery_app_delete_single_success_result";


    /**
     * 删除交付应用[{0}]失败
     */
    String RCDC_UAM_DELIVERY_APP_DELETE_SINGLE_FAIL_RESULT = "rcdc_uam_delivery_app_delete_single_fail_result";


    /**
     * 非待发布或已发布的应用[{0}]，不允许添加到交付组[{1}]
     */
    String RCDC_PUBLISH_DELIVERY_APP_NOT_ALLOWED_ADD = "23201089";

    /**
     * 批量添加交付应用key
     */
    String RCDC_DELIVERY_APP_ADD_ITEM_NAME = "rcdc_delivery_app_add_item_name";

    /**
     * 添加交付应用
     */
    String RCDC_DELIVERY_APP_ADD_SINGLE_TASK_NAME = "rcdc_delivery_app_add_single_task_name";

    /**
     * 正在添加交付应用[{0}]
     */
    String RCDC_DELIVERY_APP_ADD_SINGLE_TASK_DESC = "rcdc_delivery_app_add_single_task_desc";

    /**
     * 批量添加交付应用
     */
    String RCDC_UAM_DELIVERY_APP_ADD_TASK_NAME = "rcdc_uam_delivery_app_add_task_name";

    /**
     * 正在批量添加交付应用
     */
    String RCDC_UAM_DELIVERY_APP_ADD_TASK_DESC = "rcdc_uam_delivery_app_add_task_desc";

    /**
     * 交付组[{0}]添加交付应用[{1}]成功
     */
    String RCDC_UAM_DELIVERY_APP_ADD_SUCCESS_LOG = "rcdc_uam_delivery_app_add_success_log";

    /**
     * 交付组[{0}]添加交付应用[{1}]失败，失败原因：{2}
     */
    String RCDC_UAM_DELIVERY_APP_ADD_FAIL_LOG = "rcdc_uam_delivery_app_add_fail_log";

    /**
     * 交付组[{0}]添加交付应用失败，失败原因：{1}
     */
    String RCDC_UAM_DELIVERY_APP_NOT_EXISTS_ADD_FAIL_LOG = "rcdc_uam_delivery_app_not_exists_add_fail_log";

    /**
     * 批量添加交付应用，成功[{0}]条，失败[{1}]条
     */
    String RCDC_UAM_DELIVERY_APP_ADD_SUCCESS_RESULT = "rcdc_uam_delivery_app_add_success_result";

    /**
     * 添加交付应用[{0}]成功
     */
    String RCDC_UAM_DELIVERY_APP_ADD_SINGLE_SUCCESS_RESULT = "rcdc_uam_delivery_app_add_single_success_result";

    /**
     * 添加交付应用[{0}]失败
     */
    String RCDC_UAM_DELIVERY_APP_ADD_SINGLE_FAIL_RESULT = "rcdc_uam_delivery_app_add_single_fail_result";

    /**
     * 应用软件包只允许添加一个
     */
    String RCDC_UAM_APP_SOFTWARE_PACKAGE_ALLOW_ADD_ONE = "23201081";


    /**
     * 交付应用[{0}]重新交付成功
     */
    String RCDC_UAM_DELIVERY_APP_REDELIVERY_SUCCESS_LOG = "rcdc_uam_delivery_app_redelivery_success_log";


    /**
     * 交付应用[{0}]的交付状态为交付中，无需执行重新交付
     */
    String RCDC_UAM_DELIVERY_APP_REDELIVERY_FAIL_LOG = "rcdc_uam_delivery_app_redelivery_fail_log";

    String RCDC_UAM_DISK_DELIVERY_APP_DEALCANUSED_THIRD_DESKTOP = "rcdc_uam_disk_delivery_app_dealcanused_third_desktop";

    /**
     * 交付应用[{0}]的交付状态非交付失败，无需执行重新交付
     */
    String RCDC_UAM_DISK_DELIVERY_APP_REDELIVERY_FAIL_LOG = "rcdc_uam_disk_delivery_app_redelivery_fail_log";

    /**
     * 交付应用[{0}]中交付对象[{1}]重新交付成功
     */
    String RCDC_UAM_DELIVERY_APP_REDELIVERY_OBJECT_SUCCESS_LOG = "rcdc_uam_delivery_app_redelivery_object_success_log";

    /**
     * 交付应用[{0}]中交付对象[{1}]为交付中，无需执行重新交付
     */
    String RCDC_UAM_DELIVERY_APP_REDELIVERY_OBJECT_FAIL_LOG = "rcdc_uam_delivery_app_redelivery_object_fail_log";

    /**
     * 交付应用[{0}]中交付对象[{1}]不是交付失败状态，无需执行重新交付
     */
    String RCDC_UAM_DISK_DELIVERY_APP_REDELIVERY_OBJECT_FAIL_LOG = "rcdc_uam_disk_delivery_app_redelivery_object_fail_log";

    /**
     * 应用[{}]与交付组[{}]操作系统类型不匹配，不允许添加到交付组
     */
    String RCDC_PUBLISH_DELIVERY_APP_NOT_MATCH_DELIVERY_GROUP_OS_TYPE = "23201156";

    /**
     * 应用[{}]对应推送安装包已不存在
     */
    String RCDC_PUBLISH_DELIVERY_APP_PUSH_PACKAGE_NOT_EXIST = "23201155";

}
