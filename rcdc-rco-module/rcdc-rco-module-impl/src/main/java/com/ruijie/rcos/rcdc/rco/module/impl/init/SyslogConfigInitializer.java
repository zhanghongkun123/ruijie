package com.ruijie.rcos.rcdc.rco.module.impl.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.syslog.SyslogConfigManager;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;


/**
 * Description: syslog策略初始化
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 10:00
 *
 * @author yxq
 */
@Service
public class SyslogConfigInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyslogConfigInitializer.class);

    /**
     * 初始化syslog策略失败重试时间，5分钟重试一次
     */
    private static final long SLEEP_TIME = 300000L;

    /**
     * 线程池名称
     */
    private static final String THREAD_POOL_NAME = "initSyslogConfig";

    private static final ThreadExecutor THREAD_EXECUTOR = ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(1).queueSize(0).build();

    @Autowired
    private SyslogConfigManager syslogConfigManager;

    @Override
    public void safeInit() {
        LOGGER.info("开始执行syslog策略初始化方法");

        // 进行syslog策略初始化
        THREAD_EXECUTOR.execute(this::initSyslogConfig);

        LOGGER.info("成功执行syslog策略初始化方法");
    }

    private void initSyslogConfig() {
        LOGGER.info("进行syslog配置初始化");

        while (true) {
            try {
                syslogConfigManager.initConfig();
                return;
            } catch (BusinessException e) {
                LOGGER.error("进行syslog配置初始化失败，进行重试，失败原因：[{}]", e.getMessage());
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException exception) {
                    LOGGER.error("进行syslog配置初始化失败，间隔[{}]毫秒出现异常，异常原因：[{}]", SLEEP_TIME, exception.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
