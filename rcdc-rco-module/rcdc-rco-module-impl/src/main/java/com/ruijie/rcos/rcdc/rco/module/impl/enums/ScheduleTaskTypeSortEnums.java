package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;

import com.ruijie.rcos.rcdc.rco.module.impl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desksnapshot.DeskSnapshotBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.DiskSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/02
 *
 * @author lifeng
 */
public enum ScheduleTaskTypeSortEnums {

    /**
     * 关闭终端
     */
    TERMINAL_SHUT_DOWN(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_QUARTZ_TERMINAL_SHUT_DOWN), 0),

    /**
     * 唤醒终端
     */
    TERMINAL_WAKE_UP(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_QUARTZ_TERMINAL_WAKE_UP) ,1),

    /**
     * 关闭云桌面
     */
    CLOUD_DESK_SHUT_DOWN(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_SHUT_DOWN) ,2),

    /**
     * 启动云桌面
     */
    CLOUD_DESK_START(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_START), 3),

    /**
     * 重启云桌面
     */
    CLOUD_DESK_RESTART(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_RESTART), 4),

    /**
     * 桌面池预启动
     */
    DESKTOP_POOL_START(LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESKTOP_POOL_START) ,5),

    /**
     * 创建桌面快照
     */
    CLOUD_DESK_CREATE_SNAPSHOT(LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT), 6),

    /**
     * 创建磁盘快照
     */
    DISK_CREATE_SNAPSHOT(LocaleI18nResolver.resolve(DiskSnapshotBusinessKey.RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT) ,7),

    /**
     * 创建桌面备份
     */
    CLOUD_DESK_CREATE_BACKUP(LocaleI18nResolver.resolve(DeskBackupBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_BACKUP), 8);

    /**
     * 默认排序
     */
    public static final Integer DEFAULT_SORT = ScheduleTaskTypeSortEnums.values().length - 1;

    private String scheduleTaskName;

    private Integer sort;

    ScheduleTaskTypeSortEnums(String scheduleTaskName, Integer sort) {
        this.scheduleTaskName = scheduleTaskName;
        this.sort = sort;
    }

    public String getScheduleTaskName() {
        return scheduleTaskName;
    }

    public Integer getSort() {
        return sort;
    }

    /**
     * 通过定时任务名称获取排序值
     * @param scheduleTaskName 任务名称
     * @return 排序值
     */
    public static Integer getSortByScheduleTaskName(String scheduleTaskName) {
        Assert.hasText(scheduleTaskName, "scheduleTaskName must not be empty");
        for (ScheduleTaskTypeSortEnums scheduleTaskTypeEnums : ScheduleTaskTypeSortEnums.values()) {
            if (scheduleTaskTypeEnums.getScheduleTaskName().equals(scheduleTaskName)) {
                return scheduleTaskTypeEnums.getSort();
            }
        }
        return DEFAULT_SORT;
    }
}
