package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.12
 *
 * @author linhj
 */
public interface SyncUpgradeCode {

    int SUCCESS = 0;

    // 终端未接入
    int TERMINAL_NOT_FOUND = 1;

    // 终端部署模式错误
    int TERMINAL_MODE_ERROR = 2;

    // 绑定用户未找到
    int USER_NOT_FOUND = 3;

    // 绑定用户未开启 IDV 特性
    int USER_CONFIG_NOT_FOUND = 4;

    // 终端分组未找到
    int TERMINAL_GROUP_NOT_FOUND = 5;

    // 终端分组未开启 IDV 特性
    int TERMINAL_GROUP_CONFIG_NOT_FOUND = 6;

    // 终端绑定桌面不存在
    int TERMINAL_DESKTOP_NOT_FOUND = 7;

    // 未知异常
    int OTHER_ERROR = 99;
}
