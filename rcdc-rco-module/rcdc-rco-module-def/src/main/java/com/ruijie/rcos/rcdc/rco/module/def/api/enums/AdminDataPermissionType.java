package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * <p>Title: RoleGroupPermissionType</p>
 * <p>Description: Function Description</p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2019</p>
 * <p>@Author: linrenjian</p>
 * <p>@Date: 2021/7/21 13:50</p>
 */
public enum AdminDataPermissionType {
    /**
     * 用户组
     */
    USER_GROUP,
    /**
     * 终端组
     */
    TERMINAL_GROUP,
    /**
     * 镜像
     */
    IMAGE,
    /**
     * 桌面池组
     */
    DESKTOP_POOL,
    /**
     * 磁盘池组
     */
    DISK_POOL,

    /**
     * 交付应用
     */
    UAM_APP,

    /**
     * 交付组
     */
    DELIVERY_GROUP,

    /**
     * 测试组
     */
    DELIVERY_TEST,

    /**
     * 文件分发
     */
    FILE_DISTRIBUTION,

    /**
     * 云桌面策略
     */
    DESKTOP_STRATEGY,
    /**
     * 应用池
     */
    APP_POOL,
    /**
     * 云应用策略
     */
    APP_MAIN_STRATEGY,
    /**
     * 云应用外设策略
     */
    APP_PERIPHERAL_STRATEGY;
}
