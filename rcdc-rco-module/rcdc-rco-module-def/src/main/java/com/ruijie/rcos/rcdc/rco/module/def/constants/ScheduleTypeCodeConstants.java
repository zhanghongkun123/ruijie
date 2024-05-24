package com.ruijie.rcos.rcdc.rco.module.def.constants;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月17日
 *
 * @author xgx
 */
public interface ScheduleTypeCodeConstants {

    /**
     * 关闭云桌面，与前端约定好的，修改是要前后端一起修改
     */
    String CLOUD_DESK_SHUT_DOWN_SCHEDULE_TYPE_CODE = "cloud_desk_shut_down";

    /**
     * 启动云桌面，与前端约定好的，修改是要前后端一起修改
     */
    String CLOUD_DESK_START_SCHEDULE_TYPE_CODE = "cloud_desk_start";

    /**
     * 终端关机，与前端约定好的，修改是要前后端一起修改
     */
    String TERMINAL_SHUT_DOWN_SCHEDULE_TYPE_CODE = "terminal_shut_down";

    /**
     * 远程唤醒，与前端约定好的，修改是要前后端一起修改
     */
    String TERMINAL_WAKE_UP_SCHEDULE_TYPE_CODE = "terminal_wake_up";

    /**
     * 创建桌面快照，与前端约定好的，修改是要前后端一起修改
     */
    String CLOUD_DESK_CREATE_SNAPSHOT_TYPR_CODE = "cloud_desk_create_snapshot";

    /**
     * 创建桌面快照，与前端约定好的，修改是要前后端一起修改
     */
    String DISK_CREATE_SNAPSHOT_TYPE_CODE = "disk_create_snapshot";

    /**
     * 创建桌面备份，与前端约定好的，修改是要前后端一起修改
     */
    String CLOUD_DESK_CREATE_BACKUP_TYPR_CODE = "cloud_desk_create_backup";

    /**
     * 创建池桌面预启动，与前端约定好的，修改是要前后端一起修改
     */
    String DESKTOP_POOL_START_TYPR_CODE = "desktop_pool_start";

    /**
     * 终端每小时在线情况收集
     */
    String TERMINAL_ONLINE_SITUATION_COLLECT_EVERY_HOUR = "terminal_online_situation_collect_every_hour";
    String TERMINAL_ONLINE_SITUATION_COLLECT_EVERY_DAY = "terminal_online_situation_collect_every_day";

    /**
     * 用户解锁扫描任务
     */
    String USER_AUTO_UNLOCK_SCHEDULE_TYPE_CODE = "user_auto_unlock";

    /**
     * 用户登入登出日志自动清理任务
     */
    String DESKTOP_ONLINE_LOG_CLEAN = "desktop_online_log_clean";

    /**
     * syslog定时发送任务
     */
    String SYSLOG_SCHEDULE_TASK_TYPE_CODE = "send_syslog";

    /**
     * rcdc与rccm 健康检查任务
     */
    String RCCM_MANAGE_HEART_BEAT_TYPE_CODE = "rccm_manage_heart_beat_type_code";

    /**
     * 服务器迁移纪录定时清除
     */
    String SYSTEM_UPGRADE_RECORD_CLEAN_TYPE_CODE = "system_upgrade_record_clean_type_code";



    /**
     * 重启云桌面，与前端约定好的，修改时要前后端一起修改
     */
    String CLOUD_DESK_RESTART_SCHEDULE_TYPE_CODE = "cloud_desk_restart";

    String DESKTOP_ONLINE_SITUATION_COLLECT_EVERY_HOUR = "desktop_online_situation_collect_every_hour";

    String DESKTOP_ONLINE_SITUATION_COLLECT_EVERY_DAY = "desktop_online_situation_collect_every_day";

    /**
     * 终端在线总时长定时器
     */
    String TERMINAL_ONLINE_TIME_RECORD = "terminal_online_time_record";

    /**
     * 用户导出数据清理定时器
     */
    String RCDC_RCO_EXPORT_DATA_CLEAN = "rcdc_rco_export_data_clean";

    /**
     * 定时采集云桌面授权使用情况
     */
    String DESKTOP_LICENSE_USED_INFO_STAT = "desktop_license_used_info_stat";

    /**
     * 定时清理云桌面授权使用情况过期数据
     */
    String DESKTOP_LICENSE_USED_INFO_STAT_CLEAN = "desktop_license_used_info_stat_clean";

    /**
     * 外部消息记录自动清理任务
     */
    String EXTERNAL_MESSAGE_LOG_CLEAN = "external_message_log_clean";

    /**
     * 桌面GT 状态检测
     */
    String DESKTOP_GUEST_TOOL_CHECK = "DESKTOP_GUEST_TOOL_CHECK";

    String CHECK_DESKTOP_PORT = "check_desktop_port";

    /**
     * 静态池支持的d定时任务
     */
    Set<String> STATIC_POOL_SUPPORT_TASK_SET = Sets.newHashSet(CLOUD_DESK_CREATE_BACKUP_TYPR_CODE, CLOUD_DESK_START_SCHEDULE_TYPE_CODE,
            CLOUD_DESK_SHUT_DOWN_SCHEDULE_TYPE_CODE, CLOUD_DESK_RESTART_SCHEDULE_TYPE_CODE, CLOUD_DESK_CREATE_SNAPSHOT_TYPR_CODE);
}

