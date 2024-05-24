package com.ruijie.rcos.rcdc.rco.module.openapi.rest;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public interface MtoolBusinessKey {

    /**
     * 镜像文件上传成功日志
     */
    String RCDC_OPENAPI_OS_UPLOAD_SUCCESS_LOG = "rcdc_openapi_os_upload_success_log";

    /**
     * 镜像文件上传失败日志
     */
    String RCDC_OPENAPI_OS_UPLOAD_FAIL_LOG = "rcdc_openapi_os_upload_fail_log";

    /**
     * 终端导入成功
     */
    String RCDC_OPENAPI_TERMINAL_ADD_IDV_TERMINAL_SUCCESS_LOG = "rcdc_openapi_terminal_add_idv_terminal_success_log";

    /**
     * 终端导入失败
     */
    String RCDC_OPENAPI_TERMINAL_ADD_IDV_TERMINAL_FAILED_LOG = "rcdc_openapi_terminal_add_idv_terminal_failed_log";

    /**
     * 终端组导入成功
     */
    String RCDC_OPENAPI_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_SUCCESS_LOG = "rcdc_openapi_terminal_group_add_idv_terminal_group_success_log";

    /**
     * 终端组导入失败
     */
    String RCDC_OPENAPI_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_FAIL_LOG = "rcdc_openapi_terminal_group_add_idv_terminal_group_fail_log";

    /**
     * 用户组导入成功日志
     */
    String RCDC_OPENAPI_USER_GROUP_SUCCESS_LOG = "rcdc_openapi_user_group_success_log";

    /**
     * 用户组导入失败日志
     */
    String RCDC_OPENAPI_USER_GROUP_FAIL_LOG = "rcdc_openapi_user_group_fail_log";

    /**
     * 用户导入成功日志
     */
    String RCDC_OPENAPI_USER_SUCCESS_LOG = "rcdc_openapi_user_success_log";

    /**
     * 用户导入失败日志
     */
    String RCDC_OPENAPI_USER_FAIL_LOG = "rcdc_openapi_user_fail_log";

    /**
     * 用户镜像关联成功日志
     */
    String RCDC_OPENAPI_USER_IMAGE_RELATION_SUCCESS_LOG = "rcdc_openapi_user_image_relation_success_log";

    /**
     * 用户镜像关联失败日志
     */
    String RCDC_OPENAPI_USER_IMAGE_RELATION_FAIL_LOG = "rcdc_openapi_user_image_relation_fail_log";

    /**
     * 用户组镜像关联成功日志
     */
    String RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_LOG = "rcdc_openapi_user_group_image_relation_log";


    /**
     * 用户组镜像关联失败日志
     */
    String RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_FAIL_LOG = "rcdc_openapi_user_group_image_relation_fail_log";

    /**
     * 桌面策略创建成功日志
     */
    String RCDC_OPENAPI_DESK_STRATEGY_RELATION_LOG = "rcdc_openapi_desk_strategy_relation_log";


    /**
     * 创建云桌面成功
     */
    String RCDC_OPENAPI_CLOUDDESKTOP_CREATE_SUC_LOG = "rcdc_openapi_clouddesktop_create_suc_log";

    /**
     * 创建云桌面策略成功
     */
    String RCDC_OPENAPI_STRATEGY_CREATE_SUCCESS_LOG = "rcdc_openapi_strategy_create_success_log";

    /**
     * 创建云桌面策略成功
     */
    String RCDC_OPENAPI_STRATEGY_CREATE_FAIL_LOG = "rcdc_openapi_strategy_create_fail_log";

    /**
     * 用户组导入开始
     */
    String RCDC_OPENAPI_USER_GROUP_BEGIN = "rcdc_openapi_user_group_begin";

    /**
     * 用户组导入结束
     */
    String RCDC_OPENAPI_USER_GROUP_END = "rcdc_openapi_user_group_end";

    /**
     * 用户导入开始
     */
    String RCDC_OPENAPI_USER_BEGIN = "rcdc_openapi_user_begin";

    /**
     * 用户导入结束
     */
    String RCDC_OPENAPI_USER_END = "rcdc_openapi_user_end";

    /**
     * 用户组镜像关联导入开始
     */
    String RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_BEGIN = "rcdc_openapi_user_group_image_relation_begin";

    /**
     * 用户组镜像关联导入结束
     */
    String RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_END = "rcdc_openapi_user_group_image_relation_end";

    /**
     * 用户镜像关联导入开始
     */
    String RCDC_OPENAPI_USER_IMAGE_RELATION_BEGIN = "rcdc_openapi_user_image_relation_begin";

    /**
     * 用户镜像关联导入结束
     */
    String RCDC_OPENAPI_USER_IMAGE_RELATION_END = "rcdc_openapi_user_image_relation_end";


    /**
     * 创建镜像模版成功
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_CREATE_SUCCESS_LOG = "rcdc_openapi_image_template_create_success_log";

    /**
     * 创建镜像模版失败
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_CREATE_FAIL_LOG = "rcdc_openapi_image_template_create_fail_log";

    /**
     * 编辑镜像模版成功
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_EDIT_SUCCESS_LOG = "rcdc_openapi_image_template_edit_success_log";

    /**
     * 编辑镜像模版失败
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_EDIT_FAIL_LOG = "rcdc_openapi_image_template_edit_fail_log";

    /**
     * 发布镜像模版成功
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_PUBLISH_SUCCESS_LOG = "rcdc_openapi_image_template_publish_success_log";
    /**
     * 发布镜像模版失败
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_PUBLISH_FAIL_LOG = "rcdc_openapi_image_template_publish_fail_log";
    /**
     * 安装镜像模版驱动成功
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_INSTALL_DRIVER_SUCCESS_LOG = "rcdc_openapi_image_template_install_driver_success_log";
    /**
     * 安装镜像模版驱动失败
     */
    String RCDC_OPENAPI_IMAGE_TEMPLATE_INSTALL_DRIVER_FAIL_LOG = "rcdc_openapi_image_template_install_driver_fail_log";

    /**
     * 离线时长配置成功
     */
    String RCDC_OPENAPI_CONF_OFFLINE_SUCCESS_LOG = "rcdc_openapi_conf_offline_success_log";

    /**
     * 离线时长配置失败
     */
    String RCDC_OPENAPI_CONF_OFFLINE_FAIL_LOG = "rcdc_openapi_conf_offline_fail_log";

    /**
     * 导入打印机配置成功
     */
    String RCDC_OPENAPI_CONF_PRINTER_SUCCESS_LOG = "rcdc_openapi_conf_printer_success_log";

    /**
     * 导入打印机开关配置成功
     */
    String RCDC_OPENAPI_CONF_PRINTER_TOGGLE_SUCCESS_LOG = "rcdc_openapi_conf_printer_toggle_success_log";

    /**
     * 导入打印机配置失败
     */
    String RCDC_OPENAPI_CONF_PRINTER_FAIL_LOG = "rcdc_openapi_conf_printer_fail_log";

    /**
     * 特殊设备驱动配置成功
     */
    String RCDC_OPENAPI_CONF_SPECIAL_DEVICE_DRIVER_SUCCESS_LOG = "rcdc_openapi_conf_special_device_driver_success_log";

    /**
     * 特殊设备驱动配置成功
     */
    String RCDC_OPENAPI_CONF_SPECIAL_DEVICE_DRIVER_UPDATE_SUCCESS_LOG = "rcdc_openapi_conf_special_device_driver_update_success_log";

    /**
     * 特殊设备驱动配置失败
     */
    String RCDC_OPENAPI_CONF_SPECIAL_DEVICE_DRIVER_FAIL_LOG = "rcdc_openapi_conf_special_device_driver_fail_log";

    /**
     * 特殊设备配置成功
     */
    String RCDC_OPENAPI_CONF_SPECIAL_DEVICE_SUCCESS_LOG = "rcdc_openapi_conf_special_device_success_log";

    /**
     * 特殊设备配置失败
     */
    String RCDC_OPENAPI_CONF_SPECIAL_DEVICE_FAIL_LOG = "rcdc_openapi_conf_special_device_fail_log";

    /**
     * 自动重定向成功
     */
    String RCDC_OPENAPI_CONF_AUTO_REDIRECT_SUCCESS_LOG = "rcdc_openapi_conf_auto_redirect_success_log";

    /**
     * 自动重定向失败
     */
    String RCDC_OPENAPI_CONF_AUTO_REDIRECT_FAIL_LOG = "rcdc_openapi_conf_auto_redirect_fail_log";

    /**
     * 摄像头成功
     */
    String RCDC_OPENAPI_CONF_STRATEGY_CAMERA_SUCCESS_LOG = "rcdc_openapi_conf_strategy_camera_success_log";

    /**
     * 摄像头失败
     */
    String RCDC_OPENAPI_CONF_STRATEGY_CAMERA_FAIL_LOG = "rcdc_openapi_conf_strategy_camera_fail_log";

    /**
     * 特殊设备驱动配置策略成功
     */
    String RCDC_OPENAPI_CONF_STRATEGY_SPECIAL_DEVICE_DRIVER_SUCCESS_LOG = "rcdc_openapi_conf_strategy_special_device_driver_success_log";

    /**
     * 特殊设备驱动配置策略失败
     */
    String RCDC_OPENAPI_CONF_STRATEGY_SPECIAL_DEVICE_DRIVER_FAIL_LOG = "rcdc_openapi_conf_strategy_special_device_driver_fail_log";

    /**
     * 自动重定向策略成功
     */
    String RCDC_OPENAPI_CONF_STRATEGY_AUTO_REDIRECT_SUCCESS_LOG = "rcdc_openapi_conf_strategy_auto_redirect_success_log";

    /**
     * 自动重定向策略失败
     */
    String RCDC_OPENAPI_CONF_STRATEGY_AUTO_REDIRECT_FAIL_LOG = "rcdc_openapi_conf_strategy_auto_redirect_fail_log";

    /**
     * USB设备成功
     */
    String RCDC_OPENAPI_CONF_USB_DEVICE_SUCCESS_LOG = "rcdc_openapi_conf_usb_device_success_log";

    /**
     * USB设备更新成功
     */
    String RCDC_OPENAPI_CONF_USB_DEVICE_UPDATE_SUCCESS_LOG = "rcdc_openapi_conf_usb_device_update_success_log";

    /**
     * USB设备失败
     */
    String RCDC_OPENAPI_CONF_USB_DEVICE_FAIL_LOG = "rcdc_openapi_conf_usb_device_fail_log";

    /**
     * 终端导入成功
     */
    String RCDC_OPENAPI_TERMINAL_UPDATE_GLOBAL_VM_MODE_FOR_G3_SUCCESS_LOG = "rcdc_openapi_terminal_update_global_vm_mode_for_g3_success_log";
}
