package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dao.AbstractCommonDAOImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dao.IncrementMarkDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.entity.IncrementMarkEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.service.CommonExecuteSqlDAOCustomer;

/**
 * <br>
 * Description: 数据收集查询SQL <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
@Service
public class CommonExecuteSqlDAOCustomerImpl extends AbstractCommonDAOImpl implements CommonExecuteSqlDAOCustomer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonExecuteSqlDAOCustomerImpl.class);

    @Autowired
    private IncrementMarkDAO incrementMarkDAO;

    @Override
    public JSONArray queryCommonSql(String sql, String itemKey, String lastPositionField) {
        Assert.notNull(sql, "sql cant be null");
        Assert.notNull(itemKey, "sql cant be null");
        Assert.notNull(lastPositionField, "sql cant be null");

        SqlRowSet sqlRowSet = null;
        final JdbcTemplate jt = loadJdbcTemplate();

        // 新建JsonArray
        JSONArray jsonArray = new JSONArray();

        JSONObject lastRecord = new JSONObject();
        boolean isIncrementMark = false;
        if (StringUtils.hasText(lastPositionField)) {
            isIncrementMark = true;
        }

        sqlRowSet = jt.queryForRowSet(sql);
        SqlRowSetMetaData data = sqlRowSet.getMetaData();
        while (sqlRowSet.next()) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 1; i <= data.getColumnCount(); ++i) {
                // 列名
                String columnName = data.getColumnName(i);
                String rst = sqlRowSet.getString(i);
                jsonObject.put(columnName, rst);
            }
            jsonArray.add(jsonObject);
            if (isIncrementMark) {
                lastRecord = jsonObject;
            }
        }
        String mark = (String) lastRecord.get(lastPositionField);
        if (isIncrementMark && mark != null) {
            IncrementMarkEntity markEntity = incrementMarkDAO.findByItemKey(itemKey);
            if (markEntity == null) {
                IncrementMarkEntity incrementMarkEntity = new IncrementMarkEntity(itemKey, mark);
                incrementMarkDAO.save(incrementMarkEntity);
            } else {
                markEntity.setMark(mark);
                incrementMarkDAO.save(markEntity);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(jsonArray.toJSONString());
        }
        return jsonArray;
    }
}
