package com.ruijie.rcos.rcdc.rco.module.openapi.service.common;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.Consts;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.Optional;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public abstract class AbstractServerImpl {

    private final Logger logger = LoggerFactory.getLogger(AbstractServerImpl.class);

    /**
     * 抽象调用实现
     *
     * @param request  业务请求
     * @param key      唯一锁
     * @param runnable 业务运行实现
     * @param <T>      泛型
     * @throws BusinessException 业务异常
     */
    public <T> void invoke(T request, String key, LockableExecutor.LockableRunnable runnable) throws BusinessException {

        Assert.notNull(request, "request is not null");
        Assert.notNull(key, "key is not null");
        Assert.notNull(runnable, "consumer is not null");

        final ServerBusinessName name = request.getClass().getAnnotation(ServerBusinessName.class);
        String value = Optional.ofNullable(name).map(ServerBusinessName::value).orElseThrow(()
            -> new BusinessException(RestErrorCode.RCDC_CODE_SYSTEM_CONFIG_ERROR));

        StopWatch stopWatch = new StopWatch(UUID.randomUUID().toString());
        stopWatch.start();
        if (logger.isInfoEnabled()) {
            logger.info("开始调用{}接口，stopWatch：{}，入参：{}", value, stopWatch.getId(), JSON.toJSONString(request));
        }

        try {
            LockableExecutor.executeWithTryLock(key, runnable, Consts.LOCK_WAIT);
        } catch (Exception ex) {
            logger.error(String.format("%s操作失败", value), ex);
            throw RestErrorCodeMapping.convert(ex);
        } finally {
            stopWatch.stop();
            logger.info("完成调用{}接口，耗时：{}", value, stopWatch.getTotalTimeMillis());
        }
    }
}
