package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;

/**
 *
 * Description: 线程池工具
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年04月13日
 *
 * @author linke
 */
public class ThreadExecutorsUtils {

    /**
     * 线程池
     */
    private static final ExecutorService UPDATE_DESKTOP_STRATEGY_THREAD_POOL =
            ThreadExecutors.newBuilder("update_desktop_strategy_thread").maxThreadNum(30).queueSize(100).build();

    /** 下发云桌面磁盘映射策略变更通知线程池 */
    private static final ExecutorService SEND_DISK_MAPPING_CHANGE_THREAD_POOL =
            ThreadExecutors.newBuilder("send_disk_mapping_message_thread").maxThreadNum(20).queueSize(100).build();

    /**
     * 云桌面更新策略信息
     *
     * @param runnable runnable
     */
    public static void executeUpdateDesktopStrategy(Runnable runnable) {
        Assert.notNull(runnable, "runnable must not be null");
        UPDATE_DESKTOP_STRATEGY_THREAD_POOL.execute(runnable);
    }

    /**
     * 执行云桌面临时权限消息通知
     *
     * @param runnable runnable
     */
    public static void executeDesktopTempPermissionMessage(Runnable runnable) {
        Assert.notNull(runnable, "runnable must not be null");
        SEND_DISK_MAPPING_CHANGE_THREAD_POOL.execute(runnable);
    }

}
