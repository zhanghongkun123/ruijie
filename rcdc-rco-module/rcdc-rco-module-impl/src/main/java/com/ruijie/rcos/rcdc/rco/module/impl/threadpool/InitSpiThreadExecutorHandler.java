package com.ruijie.rcos.rcdc.rco.module.impl.threadpool;

import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年11月18日
 *
 * @author zhanghongkun
 */
@Service
public class InitSpiThreadExecutorHandler {
    private final ExecutorService executorService = ThreadExecutors.newBuilder("custom_user_login_thread_pool").maxThreadNum(80).queueSize(1).build();

    private final ExecutorService onlineSpiExecutorService =
            ThreadExecutors.newBuilder("online_spi_thread_pool").maxThreadNum(80).queueSize(1).build();

    private final ExecutorService desktopPoolResourceExecutorService =
            ThreadExecutors.newBuilder("desktop_pool_resource_pool").maxThreadNum(80).queueSize(1).build();

    private static final ExecutorService HANDLE_THREAD_POOL =
            ThreadExecutors.newBuilder("report_config_wizard_thread_pool").maxThreadNum(80).queueSize(1).build();
}
