package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants;

/**
 * Description: 桌面池常量类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-03-02
 *
 * @author linke
 */
public interface DesktopPoolConstants {

    String DATA_PERMISSION_KEY = "id";

    /**
     * 单个池最大云桌面数量
     */
    int SINGLE_POOL_MAX_DESK_NUM = 1000;

    /**
     * 池中云桌面名称分割下划线
     */
    String DESKTOP_NAME_SEPARATOR = "_";

    /**
     * 池中无虚机错误码
     */
    int NO_AVAILABLE_DESKTOP = -12;

    /**
     * 池桌面分配出现异常
     */
    int DESKTOP_POOL_ASSIGN_ERROR = -13;

    /**
     * 处于维护模式
     */
    int DESKTOP_UNDER_MAINTENANCE = -15;

    /**
     * 用户未分配池
     */
    int USER_NOT_ASSIGN_POOL = -16;

    /**
     * 桌面池状态不可以
     */
    int DESKTOP_POOL_STATUS_ERROR = -17;

    /**
     * 桌面池关联的镜像模板状态不可以
     */
    int DESKTOP_POOL_IMAGE_STATUS_ERROR = -18;

    /**
     * 桌面磁盘未就绪
     */
    int DESKTOP_USER_DISK_STATUS_ERROR = -19;

    /**
     * 云桌面使用时间限制
     */
    int DESKTOP_LOGIN_TIME_LIMIT = -20;

    int DESKTOP_POOL_SQL_MAX_NUM = 1000;
}
