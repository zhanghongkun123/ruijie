package com.ruijie.springboot;

import javax.sql.DataSource;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.repositorykit.api.ds.meta.RdbProviderType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月27日
 *
 * @author 徐国祥
 */
public class CdcPublicMemoryDataBaseInitBeanPostProcessor implements BeanPostProcessor, Ordered {

    private static final String PUBLIC_DB_SQL_FILE = "classpath:sql/schema.sql";

    @Value("${datasource.root.public.rdbType:POSTGRESQL}")
    private String rdbType;

    @Value("${datasource.root.public.dbname:rcdc_public}")
    private String dbName;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Assert.notNull(bean, "bean can not be null");
        Assert.notNull(beanName, "beanName can not be null");
        if (bean instanceof DataSource && RdbProviderType.H2.name().equals(rdbType) && beanName.contains(dbName)) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.setSqlScriptEncoding(CharEncoding.UTF_8);
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(PUBLIC_DB_SQL_FILE);
            if (resource.exists()) {
                populator.addScript(resource);
            }

            DatabasePopulatorUtils.execute(populator, (DataSource) bean);
        }
        return bean;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
