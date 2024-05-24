package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy;

/**
 * Description: 临时权限BusinessKey
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-05-06
 *
 * @author linke
 */
public interface DesktopTempPermissionBusinessKey {

    String RCO_DESK_TEMP_PERMISSION_BIND_USER_NULL = "23200285";

    String RCO_DESK_TEMP_PERMISSION_BIND_USER_REPEAT = "23200286";

    String RCO_DESK_TEMP_PERMISSION_BIND_DESKTOP_NULL = "23200287";

    String RCO_DESK_TEMP_PERMISSION_BIND_DESKTOP_REPEAT = "23200288";

    String RCO_DESK_TEMP_PERMISSION_ADD_USER_LOG = "rco_desk_temp_permission_add_user_log";

    String RCO_DESK_TEMP_PERMISSION_DELETE_USER_LOG = "rco_desk_temp_permission_delete_user_log";

    String RCO_DESK_TEMP_PERMISSION_ADD_DESKTOP_LOG = "rco_desk_temp_permission_add_desktop_log";

    String RCO_DESK_TEMP_PERMISSION_DELETE_DESKTOP_LOG = "rco_desk_temp_permission_delete_desktop_log";

    String RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_ADD = "rco_desk_temp_permission_add_user_msg_title_add";

    /**
     * 临时权限变更通知
     */
    String RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_UPDATE = "rco_desk_temp_permission_add_user_msg_title_update";

    /**
     * 临时权限变更  USB 外设变化增补通知
     */
    String RCO_DESK_TEMP_PERMISSION_USER_MSG_UPDATE_USB_TIP = "rco_desk_temp_permission_user_msg_update_usb_tip";


    String RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_DELETE = "rco_desk_temp_permission_add_user_msg_title_delete";

    String RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_EXPIRE = "rco_desk_temp_permission_add_user_msg_title_expire";

    String RCO_DESK_TEMP_PERMISSION_USER_MSG_OPEN = "rco_desk_temp_permission_user_msg_open";
    String RCO_DESK_TEMP_PERMISSION_USER_MSG_STOP = "rco_desk_temp_permission_user_msg_stop";
    String RCO_DESK_TEMP_PERMISSION_USER_MSG_WILL_STOP = "rco_desk_temp_permission_user_msg_will_stop";
    String RCO_DESK_TEMP_PERMISSION_USER_MSG_WILL_STOP_USB_TIP = "rco_desk_temp_permission_user_msg_will_stop_usb_tip";

    String RCO_DESK_TEMP_PERMISSION_USER_MSG_USB_TIP = "rco_desk_temp_permission_user_msg_usb_tip";


    String RCO_DESK_TEMP_PERMISSION_CONTENT = "rco_desk_temp_permission_content";

    String RCO_DESK_TEMP_PERMISSION_CONTENT_EFFECT_TIME = "rco_desk_temp_permission_content_effect_time";

    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD = "rco_desk_temp_permission_content_clip_board";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_FORBIDDEN = "rco_desk_temp_permission_content_clip_board_forbidden";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_VM_TO_HOST = "rco_desk_temp_permission_content_clip_board_vm_to_host";

    /**
     *允许拷贝文件
     */
    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_VM_TO_HOST_LIMIT_FILE = "rco_desk_temp_permission_content_clip_board_vm_to_host_limit_file";

    /**
     * 允许拷贝字符
     */
    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_VM_TO_HOST_LIMIT_TEXT = "rco_desk_temp_permission_content_clip_board_vm_to_host_limit_text";

    /**
     * 拷贝字符数量限制
     */
    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_TEXT_CHAR_LIMIT = "rco_desk_temp_permission_content_clip_board_text_char_limit";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_TEXT_CHAR_LIMIT_NOLIMIT = "rco_desk_temp_permission_content_clip_board_text_char_limit_nolimit";

    String RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_HOST_TO_VM = "rco_desk_temp_permission_content_clip_board_host_to_vm";

    String RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING = "rco_desk_temp_permission_content_disk_mapping";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING_CLOSE = "rco_desk_temp_permission_content_disk_mapping_close";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING_READ = "rco_desk_temp_permission_content_disk_mapping_read";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING_READ_WRITE = "rco_desk_temp_permission_content_disk_mapping_read_write";

    //禁用打印水印
    String RCO_DESK_TEMP_PERMISSION_CONTENT_DISABLE_WATE_RMARK =  "rco_desk_temp_permission_content_disable_wate_rmark";
    //禁用打印水印 关闭
    String RCO_DESK_TEMP_PERMISSION_CONTENT_DISABLE_WATE_RMARK_CLOSE = "rco_desk_temp_permission_content_disable_wate_rmark_close";
    //禁用打印水印 开启
    String RCO_DESK_TEMP_PERMISSION_CONTENT_DISABLE_WATE_RMARK_OPEN =  "rco_desk_temp_permission_content_disable_wate_rmark_open";

    /**
     * USB存储设备映射相关
     */
    String RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING = "rco_desk_temp_permission_content_usb_storage_device_mapping";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING_CLOSE = "rco_desk_temp_permission_content_usb_storage_device_mapping_close";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING_READ = "rco_desk_temp_permission_content_usb_storage_device_mapping_read";
    String RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING_READ_WRITE 
            = "rco_desk_temp_permission_content_usb_storage_device_mapping_read_write";

    String RCO_DESK_TEMP_PERMISSION_CONTENT_USB = "rco_desk_temp_permission_content_usb";

    String RCO_DESK_TEMP_PERMISSION_CREATE_AUDIT_LOG = "rco_desk_temp_permission_create_audit_log";
    String RCO_DESK_TEMP_PERMISSION_UPDATE_AUDIT_LOG = "rco_desk_temp_permission_update_audit_log";
    String RCO_DESK_TEMP_PERMISSION_DELETE_AUDIT_LOG = "rco_desk_temp_permission_delete_audit_log";
    String RCO_DESK_TEMP_PERMISSION_DELETE_BY_SYSTEM_AUDIT_LOG = "rco_desk_temp_permission_delete_by_system_audit_log";

    String RCO_QUARTZ_DESKTOP_TEMP_PERMISSION_EFFECT = "rco_quartz_desktop_temp_permission_effect";
}
