package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ruijie.rcos.sk.modulekit.api.ds.DataSourceNames;
import com.ruijie.rcos.sk.repositorykit.api.ds.JdbcTemplateHolder;

/**
 * Description: JDBCTEMPLATE
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/20
 *
 * @author xwx
 */
public abstract class AbstractCommonDAOImpl {
    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;


    /**
     * 获取jdbctemplate
     * @return jdbctemplate
     */
    public JdbcTemplate loadJdbcTemplate() {
        return jdbcTemplateHolder.loadJdbcTemplate(DataSourceNames.DEFAULT_DATASOURCE_BEAN_NAME);
    }
}
