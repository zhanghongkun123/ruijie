package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalWakeUpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.TerminalWakeUpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 终端唤醒API实现
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年4月17日
 *
 * @author zhengjingyong
 */
public class TerminalWakeUpAPIImpl implements TerminalWakeUpAPI, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalWakeUpAPIImpl.class);

    /**
     * 发送唤醒报文、检查终端上线时间间隔
     */
    private static Integer wakeupCheckInterval;

    /**
     * 唤醒重试时间
     */
    private static Integer wakeupTimeoutMillisecond;

    /**
     * 唤醒等待时间
     */
    private static Integer wakeupCheckTimeoutMillisecond;

    /**
     * 正在唤醒的终端缓存
     */
    private static Cache<String, String> WAKING_CACHE;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public void afterPropertiesSet() throws Exception {
        String paramValue = globalParameterService.findParameter(Constants.TERMINAL_WAKE_UP_CONFIG_KEY);
        TerminalWakeUpConfigDTO wakeUpConfigDTO;
        if (StringUtils.isEmpty(paramValue)) {
            wakeUpConfigDTO = new TerminalWakeUpConfigDTO();
        } else {
            wakeUpConfigDTO = JSON.parseObject(paramValue, TerminalWakeUpConfigDTO.class);
        }
        wakeupCheckInterval = Optional.ofNullable(wakeUpConfigDTO.getWakeCheckInterval()).orElse(5000);
        wakeupTimeoutMillisecond = Optional.ofNullable(wakeUpConfigDTO.getWakeupTimeout()).orElse(60000);
        wakeupCheckTimeoutMillisecond = Optional.ofNullable(wakeUpConfigDTO.getWakeupCheckTimeout()).orElse(240000);
        WAKING_CACHE = CacheBuilder.newBuilder().expireAfterWrite(wakeupCheckTimeoutMillisecond, TimeUnit.MILLISECONDS).maximumSize(5000).build();
    }


    @Override
    public void wakeUpTerminal(final String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId must has text");

        long startTime = System.currentTimeMillis();
        try {
            boolean isOnline = cbbTerminalOperatorAPI.isTerminalOnline(terminalId);
            while (!isOnline) {
                long currentTime = System.currentTimeMillis();
                // 超过90S终端还没上线，则认为唤醒失败
                if (currentTime - startTime > wakeupCheckTimeoutMillisecond) {
                    LOGGER.info("唤醒终端[{}]超时，不再等待", terminalId);
                    throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_WAKE_UP_TIMEOUT);
                }

                // 每隔5S重试一次，最多重试60S
                if (currentTime - startTime <= wakeupTimeoutMillisecond) {
                    // 唤醒超时时间内，继续发送唤醒报文
                    cbbTerminalOperatorAPI.wakeUpTerminal(terminalId);
                }

                // 休眠5S
                Thread.sleep(wakeupCheckInterval);
                isOnline = cbbTerminalOperatorAPI.isTerminalOnline(terminalId);
            }

        } catch (BusinessException | InterruptedException ex) {
            LOGGER.error("唤醒终端[{}]发生异常, 异常信息：", terminalId, ex);
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_WAKE_UP_TIMEOUT, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("成功唤醒终端[{}]，耗时[{}]ms", terminalId, (System.currentTimeMillis() - startTime));
        }
    }

    @Override
    public void wakeUpTerminal(String terminalId, Boolean enableParallelWake) throws BusinessException {
        Assert.hasText(terminalId, "terminalId must has text");
        Assert.notNull(enableParallelWake, "supportRepeatWake must not be null");

        if (enableParallelWake) {
            // 如果允许同时唤醒，则不用加入缓存，直接调用唤醒逻辑即可
            this.wakeUpTerminal(terminalId);
            return;
        }

        if (WAKING_CACHE.getIfPresent(terminalId) != null) {
            LOGGER.debug("终端[{}]缓存中数据不为空，不允许重复唤醒", terminalId);
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_EXISTS_WAKE_UP_TASK);
        }

        WAKING_CACHE.put(terminalId, terminalId);
        try {
            this.wakeUpTerminal(terminalId);
        } finally {
            // 无论是否唤醒成功，都从缓存中移除终端信息
            WAKING_CACHE.invalidate(terminalId);
        }
    }
}
