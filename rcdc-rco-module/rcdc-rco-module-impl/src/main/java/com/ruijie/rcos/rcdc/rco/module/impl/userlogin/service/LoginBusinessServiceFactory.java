package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service;

import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.UserLoginBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 用户登录处理类获取工厂
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/09
 *
 * @author linke
 */
@Service
public class LoginBusinessServiceFactory implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginBusinessServiceFactory.class);

    private Map<String, LoginBusinessService> loginBusinessServiceMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    /**
     * 获取处理类
     *
     * @param key 登录处理类的Key
     * @return LoginBusinessService
     * @throws BusinessException 业务异常
     */
    public LoginBusinessService getLoginBusinessService(String key) throws BusinessException {
        Assert.hasText(key, "key不能为空");
        LoginBusinessService service = loginBusinessServiceMap.get(key);
        if (Objects.isNull(service)) {
            LOGGER.error("不存在key为{}的LoginBusinessService", key);
            throw new BusinessException(UserLoginBusinessKey.NOT_EXSIT_LOGIN_BUSINESS_SERVICE, key);
        }
        return service;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        loginBusinessServiceMap.clear();
        Map<String, LoginBusinessService> beanMap = applicationContext.getBeansOfType(LoginBusinessService.class);
        beanMap.forEach((k, service) -> loginBusinessServiceMap.put(service.getKey(), service));
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Assert.notNull(context, "context cannot be null");
        applicationContext = context;
    }
}