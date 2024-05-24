package com.ruijie.rcos.rcdc.rco.module.impl.desktop.constant;

/**
 * Description: vm虚拟化事件通知常量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-07-26
 *
 * @author linke
 */
public interface DesktopVmEventConstants {

    /**
     * est 客户端登出
     */
    String EST_LOGOUT = "EST_LOGOUT";

    /**
     * est 客户端登入
     */
    String EST_LOGIN = "EST_LOGIN";

    /**
     * 关机
     */
    String SHUTDOWN = "SHUTDOWN";

    /**
     * 虚拟机被强制关机后抛出此事件
     */
    String DESTROY = "DESTROY";

    /**
     * QEMU执行异常后抛出此事件
     */
    String CRASH = "CRASH";

    /**
     * 虚拟机系统休眠后抛出此事件
     */
    String SUSPEND_DISK = "SUSPEND_DISK";

    /**
     * 音频异常
     */
    String AUDIO_NULL_EXCEPTION = "AUDIO_NULL_EXCEPTION";

}
