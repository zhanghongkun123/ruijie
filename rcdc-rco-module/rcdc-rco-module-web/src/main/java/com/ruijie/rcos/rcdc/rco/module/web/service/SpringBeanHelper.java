package com.ruijie.rcos.rcdc.rco.module.web.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: springBean辅助类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/04/27
 */
@Service
public class SpringBeanHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     *
     * @param bean beanId
     * @return Object
     */
    public static Object getBean(String bean) {
        Assert.notNull(bean, "bean cannot null");
        return applicationContext.getBean(bean);
    }

    /**
     *
     * @param beanClass beanClass
     * @param <T> 对应beanClass
     * @return 对应bean实例
     */
    public static <T> T getBean(Class<T> beanClass) {
        Assert.notNull(beanClass, "beanClass cannot null");
        return applicationContext.getBean(beanClass);
    }
}
