package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums;

/**
 * Description: 统计的桌面信息类型
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13 19:33
 *
 * @author yxq
 */
public enum DesktopStatisticsTypeEnum {

    /**
     * 全部桌面池的总体使用率
     */
    TOTAL_USED_RATE,

    /**
     * 全部VDI静态桌面池的总体使用率
     */
    TOTAL_VDI_STATIC_USED_RATE,

    /**
     * 全部第三方静态桌面池总体使用率
     */
    TOTAL_THIRD_PARTY_STATIC_USED_RATE,

    /**
     * 全部VDI动态桌面池的总体使用率
     */
    TOTAL_VDI_DYNAMIC_USED_RATE,

    /**
     * 全部第三方动态桌面总体使用率
     */
    TOTAL_THIRD_PARTY_DYNAMIC_USED_RATE,

    /**
     * 单个桌面池使用率
     */
    SINGLE_USED_RATE,

    /**
     * 单个对象连接失败率
     */
    CONNECT_FAIL,

    /**
     * 全部VDI静态桌面池的总体失败率
     */
    TOTAL_VDI_STATIC_CONNECT_FAIL,

    /**
     * 全部第三方静态桌面池的总体失败率
     */
    TOTAL_THIRD_PARTY_STATIC_CONNECT_FAIL,

    /**
     * 全部VDI动态桌面池的总体失败率
     */
    TOTAL_VDI_DYNAMIC_CONNECT_FAIL,

    /**
     * 全部第三方动态桌面池的总体失败率
     */
    TOTAL_THIRD_PARTY_DYNAMIC_CONNECT_FAIL
}
