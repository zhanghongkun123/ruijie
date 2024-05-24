package com.ruijie.springboot;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月27日
 *
 * @author 徐国祥
 */
public class DebugGlobalParameterAPIBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugGlobalParameterAPIBeanPostProcessor.class);

    private static final String RCCP_PLATFORM_IP_KEY = "rccp_platform_ip";


    @Value("${server.vip:rccptang}")
    private String vip;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Assert.notNull(bean, "bean can not be null");
        Assert.notNull(bean, "bean can not be null");

        if (GlobalParameterAPI.class.isAssignableFrom(bean.getClass())) {
            return new DebugGlobalParameterAPI((GlobalParameterAPI) bean);
        }

        return bean;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * sk全局表API静态代理类
     */
    class DebugGlobalParameterAPI implements GlobalParameterAPI {

        private GlobalParameterAPI globalParameterAPI;


        DebugGlobalParameterAPI(GlobalParameterAPI globalParameterAPI) {
            Assert.notNull(globalParameterAPI, "globalParameterAPI can not be null");
            this.globalParameterAPI = globalParameterAPI;
        }

        @Override
        public void updateParameter(String key, @Nullable String value) {
            Assert.notNull(key, "key can not be null");
            globalParameterAPI.updateParameter(key, value);
        }

        @Override
        public void batchUpdate(Map<String, String> map) {
            Assert.notEmpty(map, "map is not empty");
            globalParameterAPI.batchUpdate(map);
        }

        @Override
        public void batchUpdate(Supplier<Map<String, String>> supplier) {
            Assert.notNull(supplier, "supplier can not be null");
            globalParameterAPI.batchUpdate(supplier);
        }

        @Override
        public void resetToDefault(String key) {
            Assert.hasText(key, "key is not empty");
            globalParameterAPI.resetToDefault(key);
        }

        @Override
        public String findParameter(String key) {
            Assert.notNull(key, "key can not be null");
            if (RCCP_PLATFORM_IP_KEY.equals(key)) {
                return vip;
            }
            return globalParameterAPI.findParameter(key);
        }

    }

}
