package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/16 15:26
 *
 * @author coderLee23
 */
public interface UamDeliveryGroupBusinessKey {


    /**
     * 不允许添加多个应用软件包
     */
    String RCDC_UAM_NOT_ALLOW_ADD_MOST_THAN_ONE_APP_SOFTWARE_PACKAGE = "23201075";


    /**
     * 添加的应用软件包[{0}]的规格不匹配
     */
    String RCDC_UAM_APP_SOFTWARE_PACKAGE_SPEC_MISMATCH = "23201086";


    /**
     * 部分应用已不存在
     */
    String RCDC_UAM_SOME_APP_NOT_EXISTS = "23201076";
    /**
     *包含不是windows的桌面
     */
    String RCDC_UAM_CONTAINS_DESK_NO_WINDOWS = "23201077";

    /**
     * 已经被使用的云桌面[{0}]，不允许再添加到该交付组[{1}]
     */
    String RCDC_UAM_SOME_CLOUD_DESKTOPS_EXISTS_USED = "23201079";

    /**
     * 部分云桌面已被使用
     */
    String RCDC_UAM_CLOUD_DESKTOP_EXISTS_USED = "23201078";


    /**
     * 部分云桌面不合规
     */
    String RCDC_UAM_SOME_CLOUD_DESKTOPS_NOT_CONFORM_USED = "23201080";


    /**
     * 交付组名称[{0}]已存在
     */
    String RCDC_DELIVERY_GROUP_NAME_EXISTS = "23201073";

    /**
     * 推送包交付组操作系统类型不能为空
     */
    String RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_OS_PLATFORM_NOT_NULL = "23201152";

    /**
     * 交付应用与交付组所选操作系统不匹配
     */
    String RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_AND_APP_OS_PLATFORM_DIFFERENT = "23201153";

    /**
     * 交付桌面与交付组操作系统不匹配
     */
    String RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_AND_DESKTOP_OS_PLATFORM_DIFFERENT = "23201154";

    /**
     * 仅支持下发个性桌面
     */
    String RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_ONLY_SUPPORT_PERSONAL = "23201157";

    /**
     * 编辑交付组[{0}]成功
     */
    String RCDC_UAM_EDIT_DELIVERY_GROUP_SUCCESS_LOG = "rcdc_uam_edit_delivery_group_success_log";


    /**
     * 编辑推交付组[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_EDIT_DELIVERY_GROUP_FAIL_LOG = "rcdc_uam_edit_delivery_group_fail_log";

    /**
     * 编辑交付组失败，失败原因：{0}
     */
    String RCDC_UAM_EDIT_DELIVERY_GROUP_NOT_EXISTS_LOG = "rcdc_uam_edit_delivery_group_not_exists_log";


    /**
     * 创建交付组[{0}]成功
     */
    String RCDC_UAM_CREATE_DELIVERY_GROUP_SUCCESS_LOG = "rcdc_uam_create_delivery_group_success_log";

    /**
     * 创建交付组[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_CREATE_DELIVERY_GROUP_FAIL_LOG = "rcdc_uam_create_delivery_group_fail_log";

    /**
     * 创建交付组[{0}]失败，推送对象包含多会话桌面池桌面，不支持应用交付
     */
    String RCDC_UAM_CREATE_DELIVERY_GROUP_FAIL_MULTI_SESSIONTYPE = "23201481";



    /**
     * 批量删除交付组key
     */
    String RCDC_DELIVERY_GROUP_DELETE_ITEM_NAME = "rcdc_delivery_group_delete_item_name";


    /**
     * 删除交付组
     */
    String RCDC_DELIVERY_GROUP_DELETE_SINGLE_TASK_NAME = "rcdc_delivery_group_delete_single_task_name";


    /**
     * 正在删除交付组[{0}]
     */
    String RCDC_DELIVERY_GROUP_DELETE_SINGLE_TASK_DESC = "rcdc_delivery_group_delete_single_task_desc";

    /**
     * 批量删除交付组
     */
    String RCDC_UAM_DELIVERY_GROUP_DELETE_TASK_NAME = "rcdc_uam_delivery_group_delete_task_name";

    /**
     * 正在批量删除交付组
     */
    String RCDC_UAM_DELIVERY_GROUP_DELETE_TASK_DESC = "rcdc_uam_delivery_group_delete_task_desc";


    /**
     * 删除删除交付组[{0}]成功
     */
    String RCDC_UAM_DELIVERY_GROUP_DELETE_SUCCESS_LOG = "rcdc_uam_delivery_group_delete_success_log";

    /**
     * 删除删除交付组[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_DELIVERY_GROUP_DELETE_FAIL_LOG = "rcdc_uam_delivery_group_delete_fail_log";

    /**
     * 删除删除交付组失败，失败原因：{1}
     */
    String RCDC_UAM_DELIVERY_GROUP_NOT_EXIST_DELETE_FAIL_LOG = "rcdc_uam_delivery_group_not_exist_delete_fail_log";

    /**
     * 批量删除交付组，成功[{0}]条，失败[{1}]条
     */
    String RCDC_UAM_DELIVERY_GROUP_DELETE_SUCCESS_RESULT = "rcdc_uam_delivery_group_delete_success_result";


    /**
     * 删除交付组[{0}]成功
     */
    String RCDC_UAM_DELIVERY_GROUP_DELETE_SINGLE_SUCCESS_RESULT = "rcdc_uam_delivery_group_delete_single_success_result";


    /**
     * 删除交付组[{0}]失败
     */
    String RCDC_UAM_DELIVERY_GROUP_DELETE_SINGLE_FAIL_RESULT = "rcdc_uam_delivery_group_delete_single_fail_result";


}
