package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/16 15:25
 *
 * @author coderLee23
 */
public interface UamDeliveryObjectBusinessKey {


    /**
     * 批量删除交互对象key
     */
    String RCDC_DELIVERY_OBJECT_DELETE_ITEM_NAME = "rcdc_delivery_object_delete_item_name";

    /**
     * 删除交付对象
     */
    String RCDC_DELIVERY_OBJECT_DELETE_SINGLE_TASK_NAME = "rcdc_delivery_object_delete_single_task_name";

    /**
     * 正在删除交付对象[{0}]
     */
    String RCDC_DELIVERY_OBJECT_DELETE_SINGLE_TASK_DESC = "rcdc_delivery_object_delete_single_task_desc";

    /**
     * 批量删除对象
     */
    String RCDC_UAM_DELIVERY_OBJECT_DELETE_TASK_NAME = "rcdc_uam_delivery_object_delete_task_name";

    /**
     * 正在批量删除交付对象
     */
    String RCDC_UAM_DELIVERY_OBJECT_DELETE_TASK_DESC = "rcdc_uam_delivery_object_delete_task_desc";

    /**
     * 删除交付对象[{0}]成功
     */
    String RCDC_UAM_DELIVERY_OBJECT_DELETE_SUCCESS_LOG = "rcdc_uam_delivery_object_delete_success_log";

    /**
     * 删除交付对象[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_DELIVERY_OBJECT_DELETE_FAIL_LOG = "rcdc_uam_delivery_object_delete_fail_log";

    /**
     * 删除交付对象失败，失败原因：{0}
     */
    String RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_DELETE_FAIL_LOG = "rcdc_uam_delivery_object_not_exists_delete_fail_log";

    /**
     * 批量删除交付对象，成功[{0}]条，失败[{1}]条
     */
    String RCDC_UAM_DELIVERY_OBJECT_DELETE_SUCCESS_RESULT = "rcdc_uam_delivery_object_delete_success_result";


    /**
     * 删除交付对象[{0}]成功
     */
    String RCDC_UAM_DELIVERY_OBJECT_DELETE_SINGLE_SUCCESS_RESULT = "rcdc_uam_delivery_object_delete_single_success_result";


    /**
     * 删除交付对象[{0}]失败
     */
    String RCDC_UAM_DELIVERY_OBJECT_DELETE_SINGLE_FAIL_RESULT = "rcdc_uam_delivery_object_delete_single_fail_result";

    /**
     * 批量添加交付对象key
     */
    String RCDC_DELIVERY_OBJECT_ADD_ITEM_NAME = "rcdc_delivery_object_add_item_name";

    /**
     * 添加交付对象
     */
    String RCDC_DELIVERY_OBJECT_ADD_SINGLE_TASK_NAME = "rcdc_delivery_object_add_single_task_name";

    /**
     * 正在添加交付对象[{0}]
     */
    String RCDC_DELIVERY_OBJECT_ADD_SINGLE_TASK_DESC = "rcdc_delivery_object_add_single_task_desc";

    /**
     * 批量添加交付对象
     */
    String RCDC_UAM_DELIVERY_OBJECT_ADD_TASK_NAME = "rcdc_uam_delivery_object_add_task_name";

    /**
     * 正在批添加交付对象
     */
    String RCDC_UAM_DELIVERY_OBJECT_ADD_TASK_DESC = "rcdc_uam_delivery_object_add_task_desc";

    /**
     * 交付组[{0}]添加交付对象[{1}]成功
     */
    String RCDC_UAM_DELIVERY_OBJECT_ADD_SUCCESS_LOG = "rcdc_uam_delivery_object_add_success_log";

    /**
     * 交付组[{0}]添加交付对象[{1}]失败，失败原因：{2}
     */
    String RCDC_UAM_DELIVERY_OBJECT_ADD_FAIL_LOG = "rcdc_uam_delivery_object_add_fail_log";

    /**
     * 交付组[{0}]添加交付对象失败，失败原因：{1}
     */
    String RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_ADD_FAIL_LOG = "rcdc_uam_delivery_object_not_exists_add_fail_log";

    /**
     * 批量添加交付对象，成功[{0}]条，失败[{1}]条
     */
    String RCDC_UAM_DELIVERY_OBJECT_ADD_SUCCESS_RESULT = "rcdc_uam_delivery_object_add_success_result";

    /**
     * 添加交付对象[{0}]成功
     */
    String RCDC_UAM_DELIVERY_OBJECT_ADD_SINGLE_SUCCESS_RESULT = "rcdc_uam_delivery_object_add_single_success_result";

    /**
     * 添加交付对象[{0}]失败
     */
    String RCDC_UAM_DELIVERY_OBJECT_ADD_SINGLE_FAIL_RESULT = "rcdc_uam_delivery_object_add_single_fail_result";

    /**
     * 云桌面[{0}]为非个性桌面，不允许添加到交付组[{1}]
     */
    String RCDC_UAM_DELIVERY_OBJECT_NOT_PERSONAL = "23201082";

    /**
     * 云桌面[{0}]为非windows桌面，不允许添加到windows交付组[{1}]
     */
    String RCDC_UAM_DELIVERY_OBJECT_NOT_WINDOWS = "23201083";

    /**
     * 云桌面[{0}]为非windows7或windows10桌面，不允许添加到交付组[{1}]
     */
    String RCDC_UAM_DELIVERY_OBJECT_NOT_WINDOWS_7_OR_10 = "23201084";

    /**
     * 添加的云桌面[{0}]的虚机类型为[{1}]、云桌面类型[{2}],不匹配交付组[{3}]的虚机类型[{4}]和云桌面类型[{5}],添加失败
     */
    String RCDC_UAM_DELIVERY_OBJECT_MISMATCH = "23201085";

    /**
     * 交付对象[{0}]重新交付成功
     */
    String RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_SUCCESS_LOG = "rcdc_uam_delivery_object_redelivery_success_log";


    /**
     * 交付对象[{0}]的交付状态交付中，无需执行重新交付
     */
    String RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_FAIL_LOG = "rcdc_uam_delivery_object_redelivery_fail_log";

    /**
     * 交付对象[{0}]的交付状态非交付失败，无需执行重新交付
     */
    String RCDC_UAM_DISK_DELIVERY_OBJECT_REDELIVERY_FAIL_LOG = "rcdc_uam_disk_delivery_object_redelivery_fail_log";

    /**
     * 交付对象[{0}]中应用[{1}]重新交付成功
     */
    String RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_APP_SUCCESS_LOG = "rcdc_uam_delivery_object_redelivery_app_success_log";

    /**
     * 交付对象[{0}]中应用[{1}]为交付中，无需执行重新交付
     */
    String RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_APP_FAIL_LOG = "rcdc_uam_delivery_object_redelivery_app_fail_log";

    /**
     * 交付对象[{0}]中应用[{1}]为非交付失败，无需执行重新交付
     */
    String RCDC_UAM_DISK_DELIVERY_OBJECT_REDELIVERY_APP_FAIL_LOG = "rcdc_uam_disk_delivery_object_redelivery_app_fail_log";


    /**
     * 添加的应用软件包[{0}]的虚机类型为[{1}]、云桌面类型[{2}],不匹配交付组[{3}]的虚机类型[{4}]和云桌面类型[{5}],添加失败
     */
    String RCDC_UAM_DELIVERY_APP_MISMATCH = "23201088";


    /**
     * 交付组[{0}]存在多种规格类型,添加应用软件包[{1}]失败
     */
    String RCDC_UAM_DELIVERY_GROUP_OBJECT_MULTIPLE_SPECS = "rcdc_uam_delivery_group_object_multiple_specs";


    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_ITEM_NAME = "rcdc_uam_unbind_app_disk_relate_desk_item_name";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SINGLE_TASK_NAME = "rcdc_uam_unbind_app_disk_relate_desk_single_task_name";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SINGLE_TASK_DESC = "rcdc_uam_unbind_app_disk_relate_desk_single_task_desc";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_TASK_NAME = "rcdc_uam_unbind_app_disk_relate_desk_task_name";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_TASK_DESC = "rcdc_uam_unbind_app_disk_relate_desk_task_desc";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SUCCESS_RESULT = "rcdc_uam_unbind_app_disk_relate_desk_success_result";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SINGLE_SUCCESS_RESULT = "rcdc_uam_unbind_app_disk_relate_desk_single_success_result";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SINGLE_FAIL_RESULT = "rcdc_uam_unbind_app_disk_relate_desk_single_fail_result";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SUCCESS_LOG = "rcdc_uam_unbind_app_disk_relate_desk_success_log";

    String RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_FAIL_LOG = "rcdc_uam_unbind_app_disk_relate_desk_fail_log";

    /**
     * 重新交付应用测试
     */

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_ITEM_NAME = "rcdc_uam_app_test_redistribute_item_name";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_SINGLE_TASK_NAME = "rcdc_uam_app_test_redistribute_single_task_name";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_SINGLE_TASK_DESC = "rcdc_uam_app_test_redistribute_single_task_desc";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_TASK_NAME = "rcdc_uam_app_test_redistribute_task_name";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_TASK_DESC = "rcdc_uam_app_test_redistribute_task_desc";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_SUCCESS_RESULT = "rcdc_uam_app_test_redistribute_success_result";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_SINGLE_SUCCESS_RESULT = "rcdc_uam_app_test_redistribute_single_success_result";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_SINGLE_FAIL_RESULT = "rcdc_uam_app_test_redistribute_single_fail_result";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_SUCCESS_LOG = "rcdc_uam_app_test_redistribute_success_log";

    String RCDC_UAM_APP_TEST_REDISTRIBUTE_FAIL_LOG = "rcdc_uam_app_test_redistribute_fail_log";

    String RCDC_UAM_APP_TEST_TASK_REDISTRIBUTE_SUCCESS_LOG = "rcdc_uam_app_test_task_redistribute_success_log";

    String RCDC_RCO_APPCENTER_TEST_APP_DISK_NAME_NOT_EXITS = "23201090";

    /**
     *  云桌面[{0}]的镜像模板与交付组[{1}]的镜像模板不匹配
     */
    String RCDC_UAM_DELIVERY_IMAGE_TEMPLATE_MISMATCH = "23201091";

    /**
     *  云桌面[{0}]的操作系统版本号[{1}]与交付组[{2}]的操作系统版本号[{3}]不匹配
     */
    String RCDC_UAM_DELIVERY_OS_VERSION_MISMATCH = "23201092";

    /**
     * 云桌面[{}]为非linux桌面，不允许添加到linux交付组[{}]
     */
    String RCDC_UAM_DELIVERY_OBJECT_NOT_LINUX = "23201137";
    /**
     * 应用磁盘交付组[{0}]不支持第三方桌面
     */
    String RCDC_UAM_CREATE_THIRD_PARTY_DELIVERY_GROUP_FAIL = "23201476";

    /**
     * 应用磁盘交付组不支持第三方桌面[{0}]
     */
    String RCDC_UAM_CREATE_THIRD_PARTY_DESKTOP_DELIVERY_GROUP_FAIL = "23201491";


}
