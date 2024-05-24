package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月25日
 *
 * @author Ghang
 */
public interface CloudDesktopBusinessKey {


    /** 操作成功 */
    String RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS = "rcdc_clouddesktop_module_operate_success";
    /** 操作失败 */
    String RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL = "rcdc_clouddesktop_module_operate_fail";

    // 安装包
    // 操作日志
    /** 上传软件安装包成功（操作日志） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_SUCCESS_LOG = "rcdc_clouddesktop_desksoft_upload_success_log";
    /** 上传软件安装包失败（操作日志） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_FAIL_LOG = "rcdc_clouddesktop_desksoft_upload_fail_log";
    /** 编辑软件安装包成功（操作日志） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPDATE_SUCCESS_LOG = "rcdc_clouddesktop_desksoft_update_success_log";
    /** 编辑软件安装包失败（操作日志） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPDATE_FAIL_LOG = "rcdc_clouddesktop_desksoft_update_fail_log";
    /** 删除软件安装包成功（操作日志） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_SUCCESS_LOG = "rcdc_clouddesktop_desksoft_delete_success_log";
    /** 删除软件安装包成功（操作日志） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_FAIL_LOG = "rcdc_clouddesktop_desksoft_delete_fail_log";
    // 上传
    /** 上传软件安装包异步任务名称 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_TASK_NAME = "rcdc_clouddesktop_desksoft_upload_task_name";
    /** 上传软件安装包异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_DESC = "rcdc_clouddesktop_desksoft_upload_desc";
    /** 上传软件安装包任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_TASK_SUCCESS = "rcdc_clouddesktop_desksoft_upload_task_success";
    /** 上传软件安装包任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_TASK_FAIL = "rcdc_clouddesktop_desksoft_upload_task_fail";
    /** 上传软件安装包任务子项名称 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_ITEM_NAME = "rcdc_clouddesktop_desksoft_upload_item_name";
    /** 上传软件安装包任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_desksoft_upload_item_success_desc";
    /** 上传软件安装包任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_ITEM_FAIL_DESC = "rcdc_clouddesktop_desksoft_upload_item_fail_desc";
    // 删除单条记录
    /** 删除软件安装包异步任务名称（单条） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_SINGLE_DELETE_TASK_NAME = "rcdc_clouddesktop_desksoft_single_delete_task_name";
    /** 删除软件安装包异步任务过程提示（单条） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_SINGLE_DELETE_TASK_DESC = "rcdc_clouddesktop_desksoft_single_delete_task_desc";
    /** 删除软件安装包任务执行结果成功提示（单条） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_SINGLE_DELETE_TASK_SUCCESS = "rcdc_clouddesktop_desksoft_single_delete_task_success";
    /** 删除软件安装包任务执行结果失败提示（单条） */
    String RCDC_CLOUDDESKTOP_DESKSOFT_SINGLE_DELETE_TASK_FAIL = "rcdc_clouddesktop_desksoft_single_delete_task_fail";
    // 批量删除
    /** 批量删除软件安装包异步任务名称 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_desksoft_batch_delete_task_name";
    /** 批量删除软件安装包异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_desksoft_batch_delete_task_desc";
    /** 批量删除软件安装包异步任务结果 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_BATCH_DELETE_RESULT = "rcdc_clouddesktop_desksoft_batch_delete_result";
    /** 删除软件安装包任务子项名称 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_NAME = "rcdc_clouddesktop_desksoft_delete_item_name";
    /** 删除软件安装包任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_desksoft_delete_item_success_desc";
    /** 删除软件安装包任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_FAIL_DESC = "rcdc_clouddesktop_desksoft_delete_item_fail_desc";

    /** 删除单条软件安装包异步任务名称 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_SINGLE_TASK_NAME = "rcdc_clouddesktop_desksoft_delete_single_task_name";
    /** 删除单条软件安装包异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_SINGLE_TASK_DESC = "rcdc_clouddesktop_desksoft_delete_single_task_desc";
    /** 删除单条软件安装包任务子项名称 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_SINGLE_ITEM_NAME = "rcdc_clouddesktop_desksoft_delete_single_item_name";
    /** 删除单条软件安装包任务结果-成功 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_SUCCESS_RESULT = "rcdc_clouddesktop_desksoft_delete_item_success_result";
    /** 删除单条软件安装包任务结果-失败 */
    String RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_FAIL_RESULT = "rcdc_clouddesktop_desksoft_delete_item_fail_result";


    /** 全局策略配置成功 */
    String RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_CONFIG_SUCCESS = "rcdc_clouddesktop_global_strategy_config_success";
    /** 全局策略恢复默认配置成功 */
    String RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_RESET_SUCCESS = "rcdc_clouddesktop_global_strategy_reset_success";
    /** 全局策略恢复默认配置失败 */
    String RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_RESET_FAILED = "rcdc_clouddesktop_global_strategy_reset_failed";
    /** 内置第三方策略，不允许删除*/
    String RCO_DESK_STRATEGY_THIRD_PARTY_BUILT_IN = "23201478";

    /** 全局策略配置失败 */
    String RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_CONFIG_FAIL = "rcdc_clouddesktop_global_strategy_config_fail";
    /** 局部显示策略配置成功 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_CONFIG_SUCCESS = "rcdc_clouddesktop_part_display_strategy_config_success";
    /** 局部显示策略配置失败 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_CONFIG_FAIL = "rcdc_clouddesktop_part_display_strategy_config_fail";
    /** 局部显示策略编辑成功 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_UPDATE_SUCCESS = "rcdc_clouddesktop_part_display_strategy_update_success";
    /** 局部显示策略编辑失败 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_UPDATE_FAIL = "rcdc_clouddesktop_part_display_strategy_update_fail";
    /** 局部显示策略删除成功 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_SUCCESS = "rcdc_clouddesktop_part_display_strategy_delete_success";
    /** 局部显示策略删除失败 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_FAIL = "rcdc_clouddesktop_part_display_strategy_delete_fail";
    /** 局部显示策略批量删除成功 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_BATCH_DELETE_RESULT = "rcdc_clouddesktop_part_display_strategy_batch_delete_success";
    /** 局部显示策略批量删除失败 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_BATCH_DELETE_FAIL = "rcdc_clouddesktop_part_display_strategy_batch_delete_fail";
    /** 删除局部显示策略 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_ITEM_NAME = "rcdc_clouddesktop_part_display_strategy_delete_item_name";
    /** 局部显示批量任务名 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_TASK_NAME = "rcdc_clouddesktop_part_display_strategy_delete_name";
    /** 局部显示批量任务描述 */
    String RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_TASK_DESC = "rcdc_clouddesktop_part_display_strategy_delete_desc";

    /** 自动休眠策略编辑失败 */
    String RCDC_CLOUDDESKTOP_AUTO_SLEEP_STRATEGY_CONFIG_FAIL = "rcdc_clouddesktop_auto_sleep_strategy_config_fail";
    /** 自动休眠策略编辑成功 */
    String RCDC_CLOUDDESKTOP_AUTO_SLEEP_STRATEGY_CONFIG_SUC = "rcdc_clouddesktop_auto_sleep_strategy_config_suc";

    /** USB类型创建成功 */
    String RCDC_CLOUDDESKTOP_USBTYPE_CREATE_SUCCESS = "rcdc_clouddesktop_usbtype_create_success";
    /** USB类型创建失败 */
    String RCDC_CLOUDDESKTOP_USBTYPE_CREATE_FAIL = "rcdc_clouddesktop_usbtype_create_fail";
    /** USB类型编辑成功 */
    String RCDC_CLOUDDESKTOP_USBTYPE_UPDATE_SUCCESS = "rcdc_clouddesktop_usbtype_update_success";
    /** USB类型编辑失败 */
    String RCDC_CLOUDDESKTOP_USBTYPE_UPDATE_FAIL = "rcdc_clouddesktop_usbtype_update_fail";
    /** USB类型删除成功 */
    String RCDC_CLOUDDESKTOP_USBTYPE_DELETE_SUCCESS = "rcdc_clouddesktop_usbtype_delete_success";
    /** USB类型删除失败 */
    String RCDC_CLOUDDESKTOP_USBTYPE_DELETE_FAIL = "rcdc_clouddesktop_usbtype_delete_fail";
    /** USB类型批量删除任务 */
    String RCDC_CLOUDDESKTOP_USBTYPE_DELETE_BATCH_TASK_NAME = "rcdc_clouddesktop_usbtype_delete_batch_task_name";
    /** USB类型批量删除任务描述 */
    String RCDC_CLOUDDESKTOP_USBTYPE_DELETE_BATCH_TASK_DESC = "rcdc_clouddesktop_usbtype_delete_batch_task_desc";
    /** USB类型删除项 */
    String RCDC_CLOUDDESKTOP_USBTYPE_DELETE_ITEM_NAME = "rcdc_clouddesktop_usbtype_delete_item_name";
    /** USB类型批量删除任务 */
    String RCDC_CLOUDDESKTOP_USBTYPE_DELETE_BATCH_TASK_RESULT = "rcdc_clouddesktop_usbtype_delete_batch_task_result";


    /** USB设备创建成功 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_CREATE_SUCCESS = "rcdc_clouddesktop_usbdevice_create_success";
    /** USB设备创建失败 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_CREATE_FAIL = "rcdc_clouddesktop_usbtdevice_create_fail";
    /** USB设备编辑成功 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_UPDATE_SUCCESS = "rcdc_clouddesktop_usbdevice_update_success";
    /** USB设备编辑失败 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_UPDATE_FAIL = "rcdc_clouddesktop_usbtdevice_update_fail";
    /** USB设备编辑失败（USB设备不存在） */
    String RCDC_CLOUDDESKTOP_USBDEVICE_NOT_EXIST_UPDATE_FAIL = "rcdc_clouddesktop_usbtdevice_not_exist_update_fail";
    /** USB设备批量删除任务 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_usbtdevice_batch_delete_task_name";
    /** USB设备批量删除任务描述 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_usbtdevice_batch_delete_task_desc";
    /** USB外设删除 */
    String RCDC_CLOUDDESKTOP_USBTDEVICE_BATCH_DELETE_ITEM_NAME = "rcdc_clouddesktop_usbtdevice_batch_delete_item_name";
    /** USB设备删除成功 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_SUCCESS = "rcdc_clouddesktop_usbdevice_delete_success";
    /** USB设备删除失败 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_FAIL = "rcdc_clouddesktop_usbdevice_delete_fail";
    /** USB设备删除失败（USB设备不存在） */
    String RCDC_CLOUDDESKTOP_USBDEVICE_NOT_EXIST_DELETE_FAIL = "rcdc_clouddesktop_usbdevice_not_exist_delete_fail";
    /** USB设备批量删除任务 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_BATCH_DELETE_TASK_RESULT = "rcdc_clouddesktop_usbtdevice_batch_delete_task_result";
    /** USB设备批量删除任务描述 */
    String RCDC_CLOUDDESKTOP_USBDEVICE_BATCH_DELETE_TASK_FAIL = "rcdc_clouddesktop_usbtdevice_batch_delete_task_fail";

    // USB高级配置
    // 摄像头配置
    /** 编辑摄像头配置策略成功 */
    String RCDC_CLOUDDESKTOP_CAMERA_CONFIG_STRATEGY_UPDATE_SUCCESS = "rcdc_clouddesktop_camera_config_strategy_update_success";
    /** 编辑摄像头配置策略失败 */
    String RCDC_CLOUDDESKTOP_CAMERA_CONFIG_STRATEGY_UPDATE_FAIL = "rcdc_clouddesktop_camera_config_strategy_update_fail";
    // 特定设备驱动配置
    /** 编辑特定设备驱动配置策略成功 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_STRATEGY_UPDATE_SUCCESS =
            "rcdc_clouddesktop_special_device_driver_config_strategy_update_success";
    /** 编辑特定设备驱动配置策略失败 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_STRATEGY_UPDATE_FAIL =
            "rcdc_clouddesktop_special_device_driver_config_strategy_update_fail";
    /** 创建特定设备驱动配置成功 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_CREATE_SUCCESS = "rcdc_clouddesktop_special_device_driver_config_create_success";
    /** 创建特定设备驱动配置失败 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_CREATE_FAIL = "rcdc_clouddesktop_special_device_driver_config_create_fail";
    /** 删除特定设备驱动配置成功 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_SUCCESS = "rcdc_clouddesktop_special_device_driver_config_delete_success";
    /** 删除特定设备驱动配置失败 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_FAIL = "rcdc_clouddesktop_special_device_driver_config_delete_fail";
    /** 删除特定设备驱动配置失败（记录不存在） */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_NOT_EXIST_DELETE_FAIL =
            "rcdc_clouddesktop_special_device_driver_config_not_exist_delete_fail";
    /** 特定设备驱动配置批量删除任务项名称 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_ITEM_NAME =
            "rcdc_clouddesktop_special_device_driver_config_batch_delete_item_name";
    /** 特定设备驱动配置批量删除任务名称 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_TASK_NAME =
            "rcdc_clouddesktop_special_device_driver_config_batch_delete_task_name";
    /** 特定设备驱动配置批量删除任务描述 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_TASK_DESC =
            "rcdc_clouddesktop_special_device_driver_config_batch_delete_task_desc";
    /** 特定设备驱动配置批量删除任务结果 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_RESULT = "rcdc_clouddesktop_special_device_driver_config_batch_delete_result";
    // 特定设备配置
    /** 创建特定设备配置成功 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_CREATE_SUCCESS = "rcdc_clouddesktop_special_device_config_create_success";
    /** 创建特定设备配置失败 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_CREATE_FAIL = "rcdc_clouddesktop_special_device_config_create_fail";
    /** 删除特定设备配置成功 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_DELETE_SUCCESS = "rcdc_clouddesktop_special_device_config_delete_success";
    /** 删除特定设备配置失败 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_DELETE_FAIL = "rcdc_clouddesktop_special_device_config_delete_fail";
    /** 删除特定设备配置失败（记录不存在） */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_NOT_EXIST_DELETE_FAIL = "rcdc_clouddesktop_special_device_config_not_exist_delete_fail";
    /** 特定设备配置批量删除任务项名称 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_BATCH_DELETE_ITEM_NAME = "rcdc_clouddesktop_special_device_config_batch_delete_item_name";
    /** 特定设备配置批量删除任务名称 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_special_device_config_batch_delete_task_name";
    /** 特定设备配置批量删除任务描述 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_special_device_config_batch_delete_task_desc";
    /** 特定设备配置批量删除任务结果 */
    String RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_BATCH_DELETE_RESULT = "rcdc_clouddesktop_special_device_config_batch_delete_result";
    // 自动重定向配置
    /** 编辑自动重定向配置策略成功 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_STRATEGY_UPDATE_SUCCESS = "rcdc_clouddesktop_auto_redirect_config_strategy_update_success";
    /** 编辑自动重定向配置策略失败 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_STRATEGY_UPDATE_FAIL = "rcdc_clouddesktop_auto_redirect_config_strategy_update_fail";
    /** 创建自动重定向配置成功 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_CREATE_SUCCESS = "rcdc_clouddesktop_auto_redirect_config_create_success";
    /** 创建自动重定向配置失败 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_CREATE_FAIL = "rcdc_clouddesktop_auto_redirect_config_create_fail";
    /** 删除自动重定向配置成功 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_SUCCESS = "rcdc_clouddesktop_auto_redirect_config_delete_success";
    /** 删除自动重定向配置失败 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_FAIL = "rcdc_clouddesktop_auto_redirect_config_delete_fail";
    /** 删除自动重定向配置失败（记录不存在） */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_NOT_EXIST_DELETE_FAIL = "rcdc_clouddesktop_auto_redirect_config_not_exist_delete_fail";
    /** 自动重定向配置批量删除任务项名称 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_ITEM_NAME = "rcdc_clouddesktop_auto_redirect_config_batch_delete_item_name";
    /** 自动重定向配置批量删除任务名称 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_auto_redirect_config_batch_delete_task_name";
    /** 自动重定向配置批量删除任务描述 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_auto_redirect_config_batch_delete_task_desc";
    /** 自动重定向配置批量删除任务结果 */
    String RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_RESULT = "rcdc_clouddesktop_auto_redirect_config_batch_delete_result";



    // 云桌面策略
    // 操作日志
    /** 创建云桌面策略成功(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_CREATE_SUCCESS_LOG = "rcdc_clouddesktop_strategy_create_success_log";
    /** 创建云桌面策略失败(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_CREATE_FAIL_LOG = "rcdc_clouddesktop_strategy_create_fail_log";
    /** 编辑云桌面策略成功(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_SUCCESS_LOG = "rcdc_clouddesktop_strategy_update_success_log";
    /** 编辑云桌面策略成功，但需要手动生效 */
    String RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_SUCCESS_NEED_REFRESH = "rcdc_clouddesktop_strategy_update_success_need_refresh";
    /** 编辑云桌面策略成功，但启动批量生效失败(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_SUCCESS_REFRESH_ERR_LOG = "rcdc_clouddesktop_strategy_update_success_refresh_err_log";
    /** 编辑云桌面策略失败(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_FAIL_LOG = "rcdc_clouddesktop_strategy_update_fail_log";
    /** 删除云桌面策略成功(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_DELETE_SUCCESS_LOG = "rcdc_clouddesktop_strategy_delete_success_log";
    /** 删除云桌面策略失败(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_DELETE_FAIL_LOG = "rcdc_clouddesktop_strategy_delete_fail_log";
    /** 策略不可用 */
    String RCDC_CLOUDDESKTOP_STRATEGY_NOT_AVAILABLE = "23200752";
    /** 策略类型不存在 */
    String RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST = "23200727";
    /** 用户自助快照数量不能超过全局配置的数量 */
    String RCDC_CLOUDDESKTOP_STRATEGY_USER_SNAPHOST_NUM_LIMIT = "23200728";
    /** 策略类型不存在 */
    String RCDC_CLOUDDESKTOP_STRATEGY_DISK_MAPPING_TYPE_NOT_EXIST = "23200729";
    String RCDC_CLOUDDESKTOP_STRATEGY_NET_DISK_MAPPING_TYPE_NOT_EXIST =
            "rcdc_clouddesktop_strategy_net_disk_mapping_type_not_exist";
    String RCDC_CLOUDDESKTOP_STRATEGY_CDROM_MAPPING_TYPE_NOT_EXIST = "rcdc_clouddesktop_strategy_CDROM_mapping_type_not_exist";
    /** 策略类型为空 */
    String RCDC_CLOUDDESKTOP_STRATEGY_TYPE_IS_NULL = "23200730";
    /** 策略的系统盘和个人盘不能小于原来的值 */
    String RCDC_CLOUDDESKTOP_STRATEGY_SYSTEM_PERSON_SIZE_LESS = "23200731";
    /** 策略关联的云桌面未全部关闭 */
    String RCDC_CLOUDDESKTOP_STRATEGY_BIND_NOT_ALL_CLOSE = "rcdc_clouddesktop_strategy_bind_not_all_close";
    // 删除单条记录
    /** 删除云桌面策略任务名称（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_TASK_NAME = "rcdc_clouddesktop_strategy_single_delete_task_name";
    /** 删除云桌面策略任务描述（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_TASK_DESC = "rcdc_clouddesktop_strategy_single_delete_task_desc";
    /** 删除云桌面策略成功结果（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_SUCCESS = "rcdc_clouddesktop_strategy_single_delete_success";
    /** 删除云桌面策略失败结果（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_FAIL = "rcdc_clouddesktop_strategy_single_delete_fail";
    // 批量删除
    /** 批量删除云桌面策略任务名称 */
    String RCDC_CLOUDDESKTOP_STRATEGY_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_strategy_batch_delete_task_name";
    /** 批量删除云桌面策略任务过程提示 */
    String RCDC_CLOUDDESKTOP_STRATEGY_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_strategy_batch_delete_task_desc";
    /** 批量删除云桌面策略任务执行结果 */
    String RCDC_CLOUDDESKTOP_STRATEGY_BATCH_DELETE_RESULT = "rcdc_clouddesktop_strategy_batch_delete_result";
    /** 删除云桌面策略任务子项名称 */
    String RCDC_CLOUDDESKTOP_STRATEGY_DELETE_ITEM_NAME = "rcdc_clouddesktop_strategy_delete_item_name";
    /** 删除云桌面策略任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_STRATEGY_DELETE_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_strategy_delete_item_success_desc";
    /** 删除云桌面策略任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_STRATEGY_DELETE_ITEM_FAIL_DESC = "rcdc_clouddesktop_strategy_delete_item_fail_desc";
    /** VOI云桌面策略云桌面类型枚举选择异常 */
    String RCDC_CLOUDDESKTOP_STRATEGY_CREATE_VOI_PATTERN_ERR = "rcdc_clouddesktop_strategy_create_voi_pattern_err";

    /** 配置云平台IP成功 */
    String RCDC_CLOUDDESKTOP_CLOUDPLATFORMIP_UPDATE_SUCCESS = "rcdc_clouddesktop_cloudplatformip_update_success";
    /** 配置云平台IP失败 */
    String RCDC_CLOUDDESKTOP_CLOUDPLATFORMIP_UPDATE_FAIL = "rcdc_clouddesktop_cloudplatformip_update_fail";
    /** 配置云平台IP成功操作日志 */
    String RCDC_CLOUDDESKTOP_CLOUDPLATFORMIP_UPDATE_SUCCESS_LOG = "rcdc_clouddesktop_cloudplatformip_update_success_log";
    /** 配置云平台IP失败操作日志 */
    String RCDC_CLOUDDESKTOP_CLOUDPLATFORMIP_UPDATE_FAIL_LOG = "rcdc_clouddesktop_cloudplatformip_update_fail_log";
    /** 云平台ip可连通 */
    String RCDC_CLOUDDESKTOP_CLOUDPLATFORMIP_TEST_CONNNECT_SUCCESS = "rcdc_clouddesktop_cloudplatformip_test_connnect_success";
    /** 云平台ip无法连通 */
    String RCDC_CLOUDDESKTOP_CLOUDPLATFORMIP_TEST_CONNNECT_FAIL = "rcdc_clouddesktop_cloudplatformip_test_connnect_fail";

    /** 镜像文件上传成功日志 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_SUCCESS_LOG = "rcdc_clouddesktop_os_upload_success_log";
    /** 镜像文件上传失败日志 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_FAIL_LOG = "rcdc_clouddesktop_os_upload_fail_log";
    /** 上传镜像文件异步任务名称 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_NAME = "rcdc_clouddesktop_os_upload_task_name";
    /** 上传镜像文件异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_DESC = "rcdc_clouddesktop_os_upload_desc";
    /** 上传镜像文件任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_SUCCESS = "rcdc_clouddesktop_os_upload_task_success";
    /** 上传镜像文件任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_FAIL = "rcdc_clouddesktop_os_upload_task_fail";
    /** 上传镜像文件任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_os_upload_item_success_desc";
    /** 上传镜像文件任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_OS_UPLOAD_ITEM_FAIL_DESC = "rcdc_clouddesktop_os_upload_item_fail_desc";
    /** 镜像文件类型不是ISO */
    String RCDC_CLOUDDESKTOP_OS_FILETYPE_NOT_ISO = "23200721";
    /** 镜像文件类型不是QCOW2 */
    String RCDC_CLOUDDESKTOP_OS_FILETYPE_NOT_QCOW2 = "23200722";

    /** 删除镜像文件任务子项名称 */
    String RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_NAME = "rcdc_clouddesktop_osfile_delete_item_name";
    /** 删除镜像文件任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_osfile_delete_item_success_desc";
    /** 删除镜像文件任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_FAIL_DESC = "rcdc_clouddesktop_osfile_delete_item_fail_desc";
    /** 批量删除镜像文件任务名称 */
    String RCDC_CLOUDDESKTOP_OSFILE_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_osfile_batch_delete_task_name";
    /** 批量删除镜像文件任务过程提示 */
    String RCDC_CLOUDDESKTOP_OSFILE_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_osfile_batch_delete_task_desc";
    /** 批量删除镜像文件任务执行结果 */
    String RCDC_CLOUDDESKTOP_OSFILE_BATCH_DELETE_RESULT = "rcdc_clouddesktop_osfile_batch_delete_result";

    /** 镜像文件上传失败日志 */
    String RCDC_CLOUDDESKTOP_OSFILE_DETAIL_FAIL_LOG = "rcdc_clouddesktop_osfile_detail_fail_log";
    /** 镜像文件修改基本信息成功日志 */
    String RCDC_CLOUDDESKTOP_OS_EDIT_INFO_SUCCESS_LOG = "rcdc_clouddesktop_os_edit_info_success_log";
    /** 镜像文件修改基本信息失败日志 */
    String RCDC_CLOUDDESKTOP_OS_EDIT_INFO_FAIL_LOG = "rcdc_clouddesktop_os_edit_info_fail_log";

    /** 镜像文件获取集群信息失败 */
    String RCDC_CLOUDDESKTOP_OSFILE_GET_CLUSTER_FAIL_LOG = "rcdc_clouddesktop_osfile_get_cluster_fail_log";

    /** 虚拟网络批量删除任务 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_virtual_network_batch_delete_task_name";
    /** 虚拟网络批量删除任务描述 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_virtual_network_batch_delete_task_desc";
    /** 虚拟网络批量删除任务结果 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_TASK_RESULT = "rcdc_clouddesktop_virtual_network_batch_delete_task_result";
    /** 虚拟网络删除任务名称 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_ITEM_NAME = "rcdc_clouddesktop_virtual_network_batch_delete_item_name";
    /** 虚拟网络创建成功 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_CREATE_SUCCESS = "rcdc_clouddesktop_virtual_network_create_success";
    /** 虚拟网络创建失败 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_CREATE_FAIL = "rcdc_clouddesktop_virtual_network_create_fail";
    /** 虚拟网络修改成功 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_SUCCESS = "rcdc_clouddesktop_virtual_network_edit_success";
    /** 虚拟网络修改失败 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_FAIL = "rcdc_clouddesktop_virtual_network_edit_fail";
    /** 修改虚拟网络 - itemName */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_ITEM_NAME = "rcdc_clouddesktop_virtual_network_edit_item_name";
    /** 修改虚拟网络 - taskName */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_NAME = "rcdc_clouddesktop_virtual_network_edit_task_name";
    /** 修改虚拟网络 - taskDesc */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_DESC = "rcdc_clouddesktop_virtual_network_edit_task_desc";
    /** 修改虚拟网络结果 - 成功 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_RESULT_SUCCESS = "rcdc_clouddesktop_virtual_network_edit_result_success";
    /** 修改虚拟网络结果 - 失败 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_RESULT_FAIL = "rcdc_clouddesktop_virtual_network_edit_result_fail";
    /** 修改虚拟网络任务 - 成功 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_SUCCESS = "rcdc_clouddesktop_virtual_network_edit_task_success";
    /** 修改虚拟网络任务 - 失败 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_FAIL = "rcdc_clouddesktop_virtual_network_edit_task_fail";

    /** 虚拟网络删除成功 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SUCCESS = "rcdc_clouddesktop_virtual_network_delete_success";
    /** 虚拟网络删除失败 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_FAIL = "rcdc_clouddesktop_virtual_network_delete_fail";
    /** 虚拟网络删除单条结果 - 成功 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_RESULT_SUCCESS = "rcdc_clouddesktop_virtual_network_delete_single_result_success";
    /** 虚拟网络删除单条结果 - 失败 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_RESULT_FAIL = "rcdc_clouddesktop_virtual_network_delete_single_result_fail";
    /** 虚拟网络删除单条 - itemName */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_ITEM_NAME = "rcdc_clouddesktop_virtual_network_delete_single_item_name";
    /** 虚拟网络删除单条- taskName */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_NAME = "rcdc_clouddesktop_virtual_network_delete_single_task_name";
    /** 虚拟网络删除单条- taskDesc */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_DESC = "rcdc_clouddesktop_virtual_network_delete_single_task_desc";
    /** 虚拟网络删除单条任务结果 - 成功 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_SUCCESS = "rcdc_clouddesktop_virtual_network_delete_single_task_success";
    /** 虚拟网络删除单条任务结果 - 失败 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_FAIL = "rcdc_clouddesktop_virtual_network_delete_single_task_fail";

    /** 虚拟网络删除失败，由于不存在 */
    String RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_NOT_EXIST_FAIL = "rcdc_clouddesktop_virtual_network_delete_not_exist_fail";
    /** 批量删除镜像模板异步任务结果 */
    String RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_BATCH_DELETE_RESULT = "rcdc_clouddesktop_image_template_delete_result";

    // 镜像编辑相关

    /** 发布镜像异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_TASK_NAME = "rcdc_clouddesktop_image_publish_task_name";
    /** 发布镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_DESC = "rcdc_clouddesktop_image_publish_desc";
    /** 发布镜像任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_TASK_SUCCESS = "rcdc_clouddesktop_image_publish_task_success";
    /** 发布镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_TASK_FAIL = "rcdc_clouddesktop_image_publish_task_fail";
    /** 发布任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_image_publish_item_success_desc";
    /** 发布镜像任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_ITEM_FAIL_DESC = "rcdc_clouddesktop_image_publish_item_fail_desc";
    /** 发布镜像成功（操作日志） */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_SUCCESS_LOG = "rcdc_clouddesktop_image_publish_success_log";
    /** 发布镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_FAIL_LOG = "rcdc_clouddesktop_image_publish_fail_log";
    /** 镜像创建成功（操作日志） */
    String RCDC_CLOUDDESKTOP_IMAGE_CREATE_SUCCESS_LOG = "rcdc_clouddesktop_image_create_success_log";
    /** 镜像创建失败（操作日志） */
    String RCDC_CLOUDDESKTOP_IMAGE_CREATE_FAIL_LOG = "rcdc_clouddesktop_image_create_fail_log";
    /** 镜像创建成功（提示信息） */
    String RCDC_CLOUDDESKTOP_IMAGE_CREATE_SUCCESS = "rcdc_clouddesktop_image_create_success";
    /** 镜像创建失败（提示信息） */
    String RCDC_CLOUDDESKTOP_IMAGE_CREATE_FAIL = "rcdc_clouddesktop_image_create_fail";
    /** 镜像创建异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_CREATE_TASK_NAME = "rcdc_clouddesktop_image_create_task_name";
    /** 创建镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_CREATE_IMAGE_DESC = "rcdc_clouddesktop_create_image_desc";
    /** 创建镜像任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_CREATE_TASK_SUCCESS = "rcdc_clouddesktop_image_create_task_success";
    /** 创建镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_CREATE_TASK_FAIL = "rcdc_clouddesktop_image_create_task_fail";

    String RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_CREATE_TCI_LINUX_NOT_SUPPORT_DISK = "23200725";

    String RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_CREATE_BAN_OS = "23200726";

    /** 镜像删除批量任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_image_batch_delete_task_name";
    /** 镜像版本删除批量任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_VERSION_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_image_version_batch_delete_task_name";
    /** 镜像删除批量任务描述 */
    String RCDC_CLOUDDESKTOP_IMAGE_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_image_batch_delete_task_desc";
    /**镜像模板版本删除任务描述*/
    String RCDC_CLOUDDESKTOP_IMAGE_VERSION_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_image_version_batch_delete_task_desc";
    /** 镜像删除批量任务子项名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_DELETE_ITEM_NAME = "rcdc_clouddesktop_image_delete_item_name";
    /** 镜像删除批量任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_DELETE_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_image_delete_item_success_desc";
    /** 镜像删除批量任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_DELETE_ITEM_FAIL_DESC = "rcdc_clouddesktop_image_delete_item_fail_desc";
    String RCDC_CLOUDDESKTOP_IMAGE_DELETE_FAIL_LOG = "rcdc_clouddesktop_image_delete_fail_log";

    String RCDC_CLOUDDESKTOP_IMAGE_VERSION_DELETE_ITEM_FAIL_DESC = "rcdc_clouddesktop_image_version_delete_item_fail_desc";
    String RCDC_CLOUDDESKTOP_IMAGE_VERSION_DELETE_FAIL_LOG = "rcdc_clouddesktop_image_version_delete_fail_log";

    String RCDC_CLOUDDESKTOP_FORCE_DELETE = "rcdc_clouddesktop_force_delete";
    String RCDC_CLOUDDESKTOP_BATCH_OPERATE = "rcdc_clouddesktop_batch_operate";


    /** 准备虚机参数成功（操作日志） */
    String RCDC_CLOUDDESKTOP_CONFIG_VM_FOR_EDIT_IMAGETEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_config_vm_for_edit_imagetemplate_success_log";
    /** 准备虚机参数失败（操作日志） */
    String RCDC_CLOUDDESKTOP_CONFIG_VM_FOR_EDIT_IMAGETEMPLATE_FAIL_LOG = "rcdc_clouddesktop_config_vm_for_edit_imagetemplate_fail_log";
    /** 准备虚机参数成功（提示信息） */
    String RCDC_CLOUDDESKTOP_CONFIG_VM_FOR_EDIT_IMAGETEMPLATE_SUCCESS = "rcdc_clouddesktop_config_vm_for_edit_imagetemplate_success";
    /** 准备虚机参数失败（提示信息） */
    String RCDC_CLOUDDESKTOP_CONFIG_VM_FOR_EDIT_IMAGETEMPLATE_FAIL = "rcdc_clouddesktop_config_vm_for_edit_imagetemplate_fail";

    /** 准备编辑镜像成功（操作日志） */
    String RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_prepare_edit_imagetemplate_success_log";
    /** 准备编辑镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_FAIL_LOG = "rcdc_clouddesktop_prepare_edit_imagetemplate_fail_log";
    /** 准备编辑镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_VNX_LIMIT = "23200741";


    /** 克隆镜像成功（操作日志） */
    String RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_clone_imagetemplate_success_log";
    /** 克隆镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_FAIL_LOG = "rcdc_clouddesktop_clone_imagetemplate_fail_log";

    /** 克隆镜像成功（提示信息） */
    String RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_SUCCESS = "rcdc_clouddesktop_clone_imagetemplate_success";
    /** 克隆镜像失败（提示信息） */
    String RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_FAIL = "rcdc_clouddesktop_clone_imagetemplate_fail";

    /** 上传镜像异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_UPLOAD_TASK_NAME = "rcdc_clouddesktop_image_upload_task_name";
    /** 上传镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_UPLOAD_DESC = "rcdc_clouddesktop_image_upload_desc";
    /** 上传镜像成功（操作日志） */
    String RCDC_CLOUDDESKTOP_UPLOAD_IMAGETEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_upload_imagetemplate_success_log";
    /** 上传镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_UPLOAD_IMAGETEMPLATE_FAIL_LOG = "rcdc_clouddesktop_upload_imagetemplate_fail_log";
    /** 上传镜像任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_UPLOAD_TASK_SUCCESS = "rcdc_clouddesktop_image_upload_task_success";
    /** 上传镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_UPLOAD_TASK_FAIL = "rcdc_clouddesktop_image_upload_task_fail";
    /** 上传镜像任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_UPLOAD_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_image_upload_item_success_desc";
    /** 上传镜像任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_UPLOAD_ITEM_FAIL_DESC = "rcdc_clouddesktop_image_upload_item_fail_desc";

    /** 修改镜像基本信息（操作日志） */
    String RCDC_CLOUDDESKTOP_EDIT_BASE_IMAGETEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_edit_base_imagetemplate_success_log";

    /** 修改镜像基本信息失败提示 */
    String RCDC_CLOUDDESKTOP_EDIT_BASE_IMAGETEMPLATE_FAIL_LOG = "rcdc_clouddesktop_edit_base_imagetemplate_fail_log";

    /** 修改镜像版本基本信息（操作日志） */
    String RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_edit_version_imagetemplate_success_log";
    String RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_SUCCESS = "rcdc_clouddesktop_edit_version_imagetemplate_success";
    String RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_NAME_SUCCESS_LOG = "rcdc_clouddesktop_edit_version_imagetemplate_name_success_log";
    String RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_DESC_SUCCESS_LOG = "rcdc_clouddesktop_edit_version_imagetemplate_desc_success_log";

    /** 修改镜像基本信息失败提示 */
    String RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_FAIL_LOG = "rcdc_clouddesktop_edit_version_imagetemplate_fail_log";

    String RCDC_CLOUDDESKTOP_START_VM_DESKTOP_NOT_CLOSE_ALL = "rcdc_clouddesktop_start_vm_desktop_not_close_all";

    /** 启动虚机（操作日志） */
    String RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_start_vm_imagetemplate_success_log";

    /** 启动虚机（操作日志） */
    String RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_FAIL_LOG = "rcdc_clouddesktop_start_vm_imagetemplate_fail_log";

    /** 启动虚机（提示信息） */
    String RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_SUCCESS = "rcdc_clouddesktop_start_vm_imagetemplate_success";

    /** 启动虚机（提示信息） */
    String RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_FAIL = "rcdc_clouddesktop_start_vm_imagetemplate_fail";

    /** 查询vnc条件失败（操作日志） */
    String RCDC_CLOUDDESKTOP_QUERY_VNC_CONDITION_FAIL_LOG = "rcdc_clouddesktop_query_vnc_condition_fail_log";

    /** 放弃编辑镜像成功（操作日志） */
    String RCDC_CLOUDDESKTOP_ABORT_EDIT_SUCCESS_LOG = "rcdc_clouddesktop_abort_edit_success_log";

    /** 放弃编辑镜像成功（提示信息） */
    String RCDC_CLOUDDESKTOP_ABORT_EDIT_SUCCESS = "rcdc_clouddesktop_abort_edit_success";
    /** 放弃编辑镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_ABORT_EDIT_FAIL_LOG = "rcdc_clouddesktop_abort_edit_fail_log";

    /** 放弃编辑镜像失败（提示信息） */
    String RCDC_CLOUDDESKTOP_ABORT_EDIT_FAIL = "rcdc_clouddesktop_abort_edit_fail";

    /** 查询镜像状态（操作日志） */
    String RCDC_CLOUDDESKTOP_QUERY_STATE_SUCCESS_LOG = "rcdc_clouddesktop_query_state_success_log";

    /** 克隆镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_CLONE_TASK_FAIL = "rcdc_clouddesktop_image_clone_task_fail";

    /** 克隆镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_CLONE_TASK_SUCCESS = "rcdc_clouddesktop_image_clone_task_success";

    /** 克隆镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_FAIL = "rcdc_clouddesktop_image_run_task_fail";

    /** 克隆镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_DELETE_IMAGE_TASK_SUCCESS = "rcdc_clouddesktop_delete_image_task_success";

    /** 克隆镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_DELETE_IMAGE_TASK_FAIL = "rcdc_clouddesktop_delete_image_task_fail";


    /** 克隆镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_CLONE_NAME_DUPLICATION = "23200723";

    /** 克隆镜像名称和版本名称重复 */
    String RCDC_CLOUDDESKTOP_IMAGE_CLONE_VERSION_NAME_DUPLICATION = "23200724";

    /** 克隆镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_SUCCESS = "rcdc_clouddesktop_image_run_task_success";
    /** 克隆镜像异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_CLONE_TASK_NAME = "rcdc_clouddesktop_image_clone_task_name";

    /** 克隆镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_CLONE_DESC = "rcdc_clouddesktop_image_clone_desc";

    /** 克隆镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_VERSION_CLONE_DESC = "rcdc_clouddesktop_image_version_clone_desc";

    /** 关闭镜像成功（操作日志） */
    String RCDC_CLOUDDESKTOP_STOP_VM_SUCCESS_LOG = "rcdc_clouddesktop_stop_vm_success_log";

    /** 关闭镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_STOP_VM_FAIL_LOG = "rcdc_clouddesktop_stop_vm_fail_log";

    /** 开启镜像成功（操作日志） */
    String RCDC_CLOUDDESKTOP_START_VM_SUCCESS_LOG = "rcdc_clouddesktop_start_vm_success_log";

    /** 开启镜像失败（操作日志） */
    String RCDC_CLOUDDESKTOP_START_VM_FAIL_LOG = "rcdc_clouddesktop_start_vm_fail_log";

    /** 发布镜像失败：镜像关联的云桌面未全部关闭 */
    String RCDC_CLOUDDESKTOP_PUBLISH_IMAGE_DESKTOP_NOT_CLOSE_ALL_FAIL = "rcdc_clouddesktop_publish_image_desktop_not_close_all_fail";

    /** 启动镜像编辑失败：镜像空间不足 */
    String RCDC_CLOUDDESKTOP_CHECK_IMAGE_SPACE_NOT_ENOUGH = "rcdc_clouddesktop_check_image_space_not_enough";

    /** 查询镜像信息成功（操作日志） */
    String RCDC_CLOUDDESKTOP_GET_IMAGE_INFO_SUCCESS_LOG = "rcdc_clouddesktop_get_image_info_success_log";

    /** 开启编辑镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_CREATE_VM_TASK_SUCCESS = "rcdc_clouddesktop_create_vm_task_success";
    /** 开启编辑镜像 异步任务名称 */
    String RCDC_CLOUDDESKTOP_CREATE_VM_TASK_NAME = "rcdc_clouddesktop_create_vm_task_name";

    /** 开启编辑镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_CREATE_VM_TASK_FAIL = "rcdc_clouddesktop_create_vm_task_fail";
    /** 创建虚机异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_CREATE_VM_DESC = "rcdc_clouddesktop_create_vm_desc";

    /** 启动镜像异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_NAME = "rcdc_clouddesktop_image_run_task_name";

    /** 启动镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_RUN_DESC = "rcdc_clouddesktop_image_run_desc";

    /** 关闭虚机任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_STOP_TASK_SUCCESS = "rcdc_clouddesktop_image_stop_task_success";

    /** 关闭虚机任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_STOP_TASK_FAIL = "rcdc_clouddesktop_image_stop_task_fail";

    /** 启动镜像异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_STOP_TASK_NAME = "rcdc_clouddesktop_image_stop_task_name";

    /** 关闭镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_STOP_DESC = "rcdc_rcdc_clouddesktop_image_stop_desc";

    /** 放弃镜像任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_ABORT_TASK_SUCCESS = "rcdc_clouddesktop_image_abort_task_success";
    /** 放弃镜像任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_ABORT_TASK_FAIL = "rcdc_clouddesktop_image_abort_task_fail";

    /** 开启编辑镜像 异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_ABORT_TASK_NAME = "rcdc_clouddesktop_image_abort_task_name";
    /** 创建虚机异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_ABORT_DESC = "rcdc_clouddesktop_image_abort_desc";

    // 驱动管理
    /** 上传驱动成功（操作日志） */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_SUCCESS_LOG = "rcdc_clouddesktop_image_driver_upload_success_log";
    /** 上传驱动失败（操作日志） */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_FAIL_LOG = "rcdc_clouddesktop_image_driver_upload_fail_log";
    /** 上传驱动结果 - 成功 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_RESULT_SUCCESS = "rcdc_clouddesktop_image_driver_upload_result_success";
    /** 上传驱动结果 - 失败 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_RESULT_FAIL = "rcdc_clouddesktop_image_driver_upload_result_fail";
    /** 上传驱动异步任务成功 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_SUCCESS = "rcdc_clouddesktop_image_driver_upload_task_success";
    /** 上传驱动异步任务失败 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_FAIL = "rcdc_clouddesktop_image_driver_upload_task_fail";
    /** 删除驱动itemName */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_ITEM_NAME = "rcdc_clouddesktop_image_driver_upload_item_name";
    /** 删除驱动taskName */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_NAME = "rcdc_clouddesktop_image_driver_upload_task_name";
    /** 删除驱动taskDesc */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_DESC = "rcdc_clouddesktop_image_driver_upload_task_desc";

    /** 上传驱动成功 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_SUCCESS = "rcdc_clouddesktop_image_driver_upload_success";
    /** 上传驱动失败 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_FAIL = "rcdc_clouddesktop_image_driver_upload_fail";
    /** 删除驱动成功 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_SUCCESS = "rcdc_clouddesktop_image_driver_delete_result_success";
    /** 删除驱动失败 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_FAIL = "rcdc_clouddesktop_image_driver_delete_result_fail";
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT = "rcdc_clouddesktop_image_driver_delete_result";

    /** 校验驱动上传失败 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_VAILD_FAIL = "rcdc_clouddesktop_image_driver_vaild_fail";
    /** 驱动文件名超过长度限制 */
    String RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_EXCEED_MAX_LENGTH = "23200753";
    /** 驱动删除传入的数组长度必须小于1 */
    String RCDC_CLOUDDESKTOP_DRIVER_IMAGE_DELETE_ID_ARR_MUST_BE_ONE = "rcdc_clouddesktop_driver_image_delete_id_arr_must_be_one";

    String RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_DUPLICATION_ERROR_MSG = "23200754";

    /** 删除驱动结果-成功 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SUCCESS_RESULT = "rcdc_clouddesktop_image_driver_delete_success_result";
    /** 删除驱动结果-失败 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_FAIL_RESULT = "rcdc_clouddesktop_image_driver_delete_fail_result";
    /** 删除驱动itemName */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_ITEM_NAME = "rcdc_clouddesktop_image_driver_delete_item_name";
    /** 删除驱动taskName */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_TASK_NAME = "rcdc_clouddesktop_image_driver_delete_task_name";
    /** 删除单个驱动taskName */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SINGLE_TASK_NAME = "rcdc_clouddesktop_image_driver_delete_single_task_name";
    /** 删除驱动taskDesc */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_TASK_DESC = "rcdc_clouddesktop_image_driver_delete_task_desc";

    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SINGLE_TASK_DESC = "rcdc_clouddesktop_image_driver_delete_single_task_desc";
    /** 编辑驱动 */
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_SUC = "rcdc_clouddesktop_image_driver_edit_suc";
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_FAIL = "rcdc_clouddesktop_image_driver_edit_fail";
    String RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_RELEASE_TIME_ERROR = "23200738";
    /** 上传驱动数量超过限制 */
    String RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_EXCEED_MAX_NUM = "23200739";
    /** 上传驱动数量超过限制 */
    String RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_EXCEED_MAX_NUM_CHECK = "23200740";
    // IDV云桌面策略*
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_CREATE_SUCCESS_LOG = "rcdc_clouddesktop_strategy_idv_create_success_log";
    /** 创建云桌面策略失败(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_CREATE_FAIL_LOG = "rcdc_clouddesktop_strategy_idv_create_fail_log";
    /** 编辑云桌面策略成功(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_UPDATE_SUCCESS_LOG = "rcdc_clouddesktop_strategy_idv_update_success_log";
    /** 编辑云桌面策略失败(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_UPDATE_FAIL_LOG = "rcdc_clouddesktop_strategy_idv_update_fail_log";
    /** 删除云桌面策略成功(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_DELETE_SUCCESS_LOG = "rcdc_clouddesktop_strategy_idv_delete_success_log";
    /** 删除云桌面策略失败(操作日志) */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_DELETE_FAIL_LOG = "rcdc_clouddesktop_strategy_idv_delete_fail_log";
    // 删除单条记录
    /** 删除云桌面策略任务名称（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_SINGLE_DELETE_TASK_NAME = "rcdc_clouddesktop_strategy_idv_single_delete_task_name";
    /** 删除云桌面策略任务描述（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_SINGLE_DELETE_TASK_DESC = "rcdc_clouddesktop_strategy_idv_single_delete_task_desc";
    /** 删除云桌面策略成功结果（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_SINGLE_DELETE_SUCCESS = "rcdc_clouddesktop_strategy_idv_single_delete_success";
    /** 删除云桌面策略失败结果（单条） */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_SINGLE_DELETE_FAIL = "rcdc_clouddesktop_strategy_idv_single_delete_fail";
    // 批量删除
    /** 批量删除云桌面策略任务名称 */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_BATCH_DELETE_TASK_NAME = "rcdc_clouddesktop_strategy_idv_batch_delete_task_name";
    /** 批量删除云桌面策略任务过程提示 */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_BATCH_DELETE_TASK_DESC = "rcdc_clouddesktop_strategy_idv_batch_delete_task_desc";
    /** 批量删除云桌面策略任务执行结果 */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_BATCH_DELETE_RESULT = "rcdc_clouddesktop_strategy_idv_batch_delete_result";
    /** 删除云桌面策略任务子项名称 */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_DELETE_ITEM_NAME = "rcdc_clouddesktop_strategy_idv_delete_item_name";
    /** 删除云桌面策略任务子项成功提示 */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_DELETE_ITEM_SUCCESS_DESC = "rcdc_clouddesktop_strategy_idv_delete_item_success_desc";
    /** 删除云桌面策略任务子项失败提示 */
    String RCDC_CLOUDDESKTOP_STRATEGY_IDV_DELETE_ITEM_FAIL_DESC = "rcdc_clouddesktop_strategy_idv_delete_item_fail_desc";

    // 终端编辑镜像：
    /** 启动终端虚机成功（提示信息） */
    String RCDC_CLOUDDESKTOP_START_TERMINAL_VM_IMAGETEMPLATE_SUCCESS = "rcdc_clouddesktop_start_terminal_vm_imagetemplate_success";
    /** 启动终端虚机成功（提示信息） */
    String RCDC_CLOUDDESKTOP_START_TERMINAL_VM_IMAGETEMPLATE_FAIL = "rcdc_clouddesktop_start_terminal_vm_imagetemplate_fail";
    /** 关闭终端虚机成功（提示信息） */
    String RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_SUCCESS = "rcdc_clouddesktop_close_terminal_vm_imagetemplate_success";
    /** 关闭终端虚机成功（提示信息） */
    String RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_FAIL = "rcdc_clouddesktop_close_terminal_vm_imagetemplate_fail";
    /** 开启编辑镜像，异步任务名称 */
    String RCDC_CLOUDDESKTOP_CREATE_TERMINAL_IMAGE_TASK_NAME = "rcdc_clouddesktop_create_terminal_image_task_name";
    /** 创建终端镜像虚机异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_CREATE_TERMINAL_IMAGE_DESC = "rcdc_clouddesktop_create_terminal_image_desc";
    /** 关闭编辑镜像，异步任务名称 */
    String RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_IMAGE_TASK_NAME = "rcdc_clouddesktop_close_terminal_image_task_name";
    /** 关闭终端镜像虚机异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_IMAGE_DESC = "rcdc_clouddesktop_close_terminal_image_desc";
    /**
     * rcdc发起通知IDV终端放弃本地编辑成功
     */
    String RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_SUCCESS = "rcdc_clouddesktop_abort_local_edit_image_template_success";
    /**
     * rcdc发起通知IDV终端放弃本地编辑失败
     */
    String RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_FAIL = "rcdc_clouddesktop_abort_local_edit_image_template_fail";
    /**
     * rcdc发起通知IDV终端放弃本地编辑成功提示
     */
    String RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_SUCCESS_LOG = "rcdc_clouddesktop_abort_local_edit_image_template_success_log";
    /**
     * rcdc发起通知IDV终端放弃本地编辑成功失败
     */
    String RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_FAIL_LOG = "rcdc_clouddesktop_abort_local_edit_image_template_fail_log";


    String RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_FAIL_LOG = "rcdc_clouddesktop_abort_local_edit_image_template_task_fail_log";
    String RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_FAIL = "rcdc_clouddesktop_abort_local_edit_image_template_task_fail";

    /**
     * rcdc不允许放弃
     */
    String RCDC_CLOUDDESKTOP_NOT_ALLOW_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE = "23200034";

    String RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_DESC = "rcdc_rco_abort_remote_edit_image_template_task_desc";
    String RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_NAME = "rcdc_rco_abort_remote_edit_image_template_task_name";

    String RCDC_RCO_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_DESC = "rcdc_rco_abort_local_edit_image_template_task_desc";
    String RCDC_RCO_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_NAME = "rcdc_rco_abort_local_edit_image_template_task_name";

    /**
     * 软件安装包名称长度非法
     */
    String RCDC_CLOUDDESKTOP_DESKSOFT_SOFT_NAME_LENGTH_ILLEGAL = "rcdc-clouddesktop_desksoft_soft_name_length_illegal";

    /**
     * 软件安装包名称重复
     */
    String RCDC_CLOUDDESKTOP_DESKSOFT_SOFT_NAME_REPETION = "rcdc-clouddesktop_desksoft_soft_name_repetition";

    String RCDC_CLOUDDESKTOP_EXACTMATCH_ARR_NOT_EMPTY = "23200757";
    String RCDC_CLOUDDESKTOP_USBTYPE_ID_NOT_EMPTY = "23200758";

    String RCDC_CLOUDDESKTOP_LIST_EXIST_EMPTY = "rcdc_clouddesktop_list_exist_empty";
    String RCDC_RCO_IMAGE_FILE_NAME_CHECK_FAIL = "rcdc_rco_iamge_file_name_check_fail";
    String RCDC_RCO_IMAGE_FILE_COUNT_LIMIT = "rcdc_rco_image_file_count_limit";

    /**
     * rcdc 只允许同时只有一个镜像模板创建中，请稍后重试
     */
    String RCDC_CLOUDDESKTOP_ONLY_ONE_IMAGE_TEMPLATE_ALLOW_CREATING = "23200755";
    /**
     * rcdc 其他管理员目前也在创建镜像模板中，请稍后重试
     */
    String RCDC_CLOUDDESKTOP_OTHER_ADMIN_CREATE_IMAGE_TEMPLATE = "23200756";
    /**
     * 云桌面切换镜像
     */
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_NAME = "rcdc_rco_desktop_image_edit_task_name";
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_DESC = "rcdc_rco_desktop_image_edit_task_desc";
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_RESULT_FAIL = "rcdc_rco_desktop_image_edit_task_result_fail";
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_RESULT_SUC = "rcdc_rco_desktop_image_edit_task_result_suc";
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_WAIT_RESULT_SUC = "rcdc_rco_desktop_image_edit_task_wait_result_suc";
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_LOG_FAIL = "rcdc_rco_desktop_image_edit_task_log_fail";
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_LOG_SUC = "rcdc_rco_desktop_image_edit_task_log_suc";
    String RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_WAIT_LOG_SUC = "rcdc_rco_desktop_image_edit_task_wait_log_suc";
    String RCDC_RCO_DESKTOP_EDIT_IMAGE_NOT_EXIST = "23200837";

    String RCDC_RCO_IMAGE_TEMPLATE_IDV_NOT_SUPPORT_GPU = "23200767";
    String RCDC_RCO_DESKTOP_EDIT_IMAGE_DYNAMIC_POOL_NOT_SUPPORT = "23200838";

    /**
     * IP限制相关
     */
    String RCDC_RCO_IP_LIMIT_EDIT_FAIL = "rcdc_rco_ip_limit_edit_fail";
    String RCDC_RCO_IP_LIMIT_EDIT_SUCCESS = "rcdc_rco_ip_limit_edit_success";
    String RCDC_RCO_IP_LIMIT_TOO_MANY = "23200769";

    /**
     * 高级策略
     */
    String RCDC_RCO_EDIT_ADVANCE_CONFIGURATION_SUCCESS = "rcdc_rco_edit_advance_configuration_success";
    String RCDC_RCO_EDIT_ADVANCE_CONFIGURATION_FAIL = "rcdc_rco_edit_advance_configuration_fail";
    String RCDC_CLOUDDESKTOP_SERVER_BACKUP_NOT_ALLOW_CREATING = "23200770";
    String RCDC_RCO_EDIT_ADVANCE_VIRTUAL_APPLICATION_SUCCESS = "rcdc_rco_edit_advance_virtual_application_success";

    /**
     *
     */
    String RCDC_RCO_EDIT_GLOBAL_CONFIGURATION_SUCCESS = "rcdc_rco_edit_global_configuration_success";
    String RCDC_RCO_EDIT_GLOBAL_CONFIGURATION_LOG = "rcdc_rco_edit_global_configuration_log";

    /**
     * TC引导
     */
    String RCDC_RCO_TCI_TERMINAL_NOT_SUPPORT_CAN_NOT_USE_WIN7_32 = "23200771";
    /**
     * CMC 配置
     */
    String RCDC_RCO_EDIT_CMC_CONFIGURATION_SUCCESS = "rcdc_rco_edit_cmc_configuration_success";
    String RCDC_RCO_EDIT_CMC_CONFIGURATION_FAIL = "rcdc_rco_edit_cmc_configuration_fail";
    String RCDC_RCO_USER_EXPERIENCE_PLAN_CLOSE_CAN_NOT_EDIT_CMC = "23200768";

    /**
     * 终端极简部署模式
     */
    String RCDC_RCO_TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG_SUCCESS = "rcdc_rco_terminal_simplify_deployment_config_success";
    String RCDC_RCO_TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG_FAIL = "rcdc_rco_terminal_simplify_deployment_config_fail";

    /**
     * EST配置
     */
    String RCDC_RCO_EST_WAN_CONFIG_EDIT_SUCCESS = "rcdc_rco_est_wan_config_edit_success";
    String RCDC_RCO_EST_LAN_CONFIG_EDIT_SUCCESS = "rcdc_rco_est_lan_config_edit_success";
    String RCDC_RCO_EST_WAN_CONFIG_EDIT_FAIL = "rcdc_rco_est_wan_config_edit_fail";
    String RCDC_RCO_EST_LAN_CONFIG_EDIT_FAIL = "rcdc_rco_est_lan_config_edit_fail";
    String RCDC_RCO_EST_WAN_CONFIG_RESET_SUCCESS = "rcdc_rco_est_wan_config_reset_success";
    String RCDC_RCO_EST_LAN_CONFIG_RESET_SUCCESS = "rcdc_rco_est_lan_config_reset_success";
    String RCDC_RCO_EST_WAN_CONFIG_RESET_FAIL = "rcdc_rco_est_wan_config_reset_fail";
    String RCDC_RCO_EST_LAN_CONFIG_RESET_FAIL = "rcdc_rco_est_lan_config_reset_fail";
    String RCDC_RCO_EST_CONFIG_TYPE_NOT_EXISTS = "23200772";

    /** 发布镜像异步任务名称 */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_TASK_NAME = "rcdc_image_snapshot_restore_task_name";
    /** 发布镜像异步任务过程提示 */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_DESC = "rcdc_image_snapshot_restore_desc";
    /** 发布镜像任务执行结果成功提示 */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_TASK_SUCCESS = "rcdc_image_snapshot_restore_task_success";
    /** 发布镜像任务执行结果失败提示 */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_TASK_FAIL = "rcdc_image_snapshot_restore_task_fail";
    /** 发布任务子项成功提示 */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_ITEM_SUCCESS_DESC = "rcdc_image_snapshot_restore_item_success_desc";
    /** 发布镜像任务子项失败提示 */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_ITEM_FAIL_DESC = "rcdc_image_snapshot_restore_item_fail_desc";
    /** 发布镜像成功（操作日志） */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_SUCCESS_LOG = "rcdc_image_snapshot_restore_success_log";
    /** 发布镜像失败（操作日志） */
    String RCDC_IMAGE_SNAPSHOT_RESTORE_FAIL_LOG = "rcdc_image_snapshot_restore_fail_log";

    /**
     * 固定mac提示
     */
    String RCDC_CLOUDDESKTOP_DESK_INFO_MAC_EXIST = "rcdc_clouddesktop_desk_info_mac_exist";
    String RCDC_CLOUDDESKTOP_IMAGE_INFO_MAC_EXIST = "rcdc_clouddesktop_image_info_mac_exist";

    /**
     * 开启扩容时，不允许选择系统盘大于桌面的镜像模板
     */
    String RCDC_RCO_IMAGE_SYSTEM_SIZE_SHOULD_NOT_GREATER_CURRENT_IMAGE_SYSTEM_DISK =
            "rcdc_rco_image_system_size_should_not_greater_current_image_system_disk";

    String RCDC_RCO_IMAGE_VERSION_SYSTEM_SIZE_SHOULD_NOT_GREATER_CURRENT_DESK_DISK =
        "rcdc_rco_image_version_system_size_should_not_greater_current_desk_disk";

    String RCDC_RCO_MULTIPLE_IMAGE_SYSTEM_SIZE_SHOULD_NOT_GREATER_CURRENT_DESK_DISK =
            "rcdc_rco_multiple_image_system_size_should_not_greater_current_desk_disk";

    String RCDC_RCO_IMAGE_DATA_SIZE_SHOULD_EQUALS_OR_LESS_CURRENT_IMAGE_DATA_DISK =
            "rcdc_rco_image_data_size_should_equals_or_less_current_image_data_disk";


    String RCDC_RCO_IMAGE_PERSONAL_CAN_NOT_PUBLISH = "rcdc_rco_image_personal_can_not_publish";

    /**
     * 软件安装包名称长度非法
     */
    String RCDC_CLOUDDESKTOP_BATCH_DESKSOFT_SOFT_NAME_LENGTH_ILLEGAL = "rcdc-clouddesktop_batch_desksoft_soft_name_length_illegal";

    /**
     * 软件安装包名称重复
     */
    String RCDC_CLOUDDESKTOP_BATCH_DESKSOFT_SOFT_NAME_REPETION = "rcdc-clouddesktop_batch_desksoft_soft_name_repetition";

    /**
     * 软件安装包[{0}]被推送安装包使用无法删除
     */
    String RCDC_CLOUDDESKTOP_BATCH_DESKSOFT_SOFT_EXISTS_USED_BY_UAM_APP = "23200786";

    /**
     * 应用软件包类型不支持当前操作
     */
    String RCDC_CLOUDDESKTOP_APPSOFTWARE_TYPE_PACKAGE_NOT_SUPPORT = "rcdc_clouddesktop_appsoftware_type_package_not_support";

    String RCDC_CLOUDDESKTOP_FILE_DOWNLOAD_NOT_EXIST = "23200787";

    String RCDC_CLOUDDESKTOP_FILE_NOT_EXIST = "23200788";

    String RCDC_CLOUDDESKTOP_DOWNLOAD_FILE_NAME_ERROR = "23200789";

    String RCDC_CLOUDDESKTOP_DOWNLOAD_FILE_TYPE_ERROR = "23200790";

    String RCDC_CLOUDDESKTOP_DOWNLOAD_FILE_FORMAT_ERROR = "23200791";

    String RCDC_CLOUDDESKTOP_DIRECT_DELETE_LATEST_IMAGE_VERSION_NOT_SUPPORTED = "23200792";

    String RCDC_CLOUDDESKTOP_IMAGE_DELETE_NOT_EXIST = "23200793";

    String RCDC_CLOUDDESKTOP_IMAGE_VERSION_ROLLBACK_TASK_NAME = "rcdc_clouddesktop_image_version_rollback_task_name";

    String RCDC_CLOUDDESKTOP_IMAGE_VERSION_ROLLBACK_TASK_ITEM_NAME = "rcdc_clouddesktop_image_version_rollback_task_item_name";


    /** 修改windows密码类型（云桌面） */
    String RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK = "rcdc_rco_reset_windows_pwd_type_desk";
    /** 修改windows密码类型（镜像模板） */
    String RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE = "rcdc_rco_reset_windows_pwd_type_image";
    /** 开启重置镜像windows密码 异步任务名称 */
    String RCDC_RCO_RESET_WINDOWS_PWD_TASK_NAME = "rcdc_rco_reset_windows_pwd_task_name";
    /** 开启重置镜像windows密码 异步任务描述 */
    String RCDC_RCO_RESET_WINDOWS_PWD_TASK_DESC = "rcdc_rco_reset_windows_pwd_task_desc";
    /** 开启重置镜像windows密码 异步任务成功 */
    String RCDC_RCO_RESET_WINDOWS_PWD_TASK_SUCCESS = "rcdc_rco_reset_windows_pwd_task_success";
    /** 开启重置镜像windows密码 异步任务失败 */
    String RCDC_RCO_RESET_WINDOWS_PWD_TASK_FAIL = "rcdc_rco_reset_windows_pwd_task_fail";
    /** 修改windows密码成功（操作日志） */
    String RCDC_RCO_RESET_WINDOWS_PWD_SUCCESS_LOG = "rcdc_rco_reset_windows_pwd_success_log";
    /** 修改windows密码失败（操作日志） */
    String RCDC_RCO_RESET_WINDOWS_PWD_FAIL_LOG = "rcdc_rco_reset_windows_pwd_fail_log";
    /** 修改windows密码失败原因；账号有误 */
    String RCDC_RCO_RESET_WINDOWS_PWD_FAIL_ACCOUNT_ERROR = "rcdc_rco_reset_windows_pwd_fail_account_error";
    /** 修改windows密码失败: 密码不合规*/
    String RCDC_RCO_RESET_WINDOWS_PWD_FAIL_PASSWORD_ERROR = "rcdc_rco_reset_windows_pwd_fail_password_error";
    /** 修改windows密码失败: 密码太短*/
    String RCDC_RCO_RESET_WINDOWS_PWD_FAIL_PASSWORD_TOO_SHORT = "rcdc_rco_reset_windows_pwd_fail_password_too_short";

    /** 修改windows密码失败： 其他异常 */
    String RCDC_RCO_RESET_WINDOWS_PWD_FAIL_OTHER_ERROR = "rcdc_rco_reset_windows_pwd_fail_other_error";
    /** 发送桌面windows重置密码成功邮件异常 */
    String RCDC_RCO_RESET_WINDOWS_PWD_SEND_EMAIL_ERROR = "rcdc_rco_reset_windows_pwd_send_email_error";

    String RCDC_RCO_IMAGE_OS_TYPE_CAN_NOT_CHANGE_TO_LINUX = "rcdc_rco_image_os_type_can_not_change_to_linux";

    String RCDC_RCO_IMAGE_OS_TYPE_CAN_NOT_CHANGE_TO_WINDOWS = "rcdc_rco_image_os_type_can_not_change_to_windows";

    String RCDC_RCO_IMAGE_TYPE_NOT_SUPPORT_OS_VERSION = "rcdc_rco_image_type_not_support_os_version";

    String RCDC_RCO_CLOUD_PLATFORM_IS_UN_AVAILABLE = "rcdc_rco_cloud_platform_is_un_available";

    /**
     * 云应用 - 镜像相关
     */
    String RCDC_CLOUDDESKTOP_APP_IMAGE_NO_TCI = "rcdc_clouddesktop_app_image_no_tci";

    /** 切换镜像异步任务名称 */
    String RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_TASK_NAME = "rcdc_clouddesktop_image_transfer_usage_task_name";

    /** 切换镜像异步任务过程提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_DESC = "rcdc_clouddesktop_image_transfer_usage_desc";

    /** 切换镜像用途任务执行结果成功提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_TASK_SUCCESS = "rcdc_clouddesktop_image_transfer_usage_task_success";

    /** 切换镜像用途任务执行结果失败提示 */
    String RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_TASK_FAIL = "rcdc_clouddesktop_image_transfer_usage_task_fail";

    /** 切换镜像用途失败（操作日志） */
    String RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_FAIL_LOG = "rcdc_clouddesktop_image_transfer_usage_fail_log";

    /** 镜像创建成功（提示信息） */
    String RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_SUCCESS = "rcdc_clouddesktop_image_transfer_usage_success";
    /** 镜像创建失败（提示信息） */
    String RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_FAIL = "rcdc_clouddesktop_image_transfer_usage_fail";

    String RCDC_IMAGE_TEMPLATE_NOT_VDI_WITH_STORAGE_CLUSTRE_ERROR = "23201519";
}
