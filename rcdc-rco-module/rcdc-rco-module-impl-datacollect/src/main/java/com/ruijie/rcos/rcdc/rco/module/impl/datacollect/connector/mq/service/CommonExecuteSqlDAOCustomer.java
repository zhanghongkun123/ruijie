package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.service;

import com.alibaba.fastjson.JSONArray;

/**
 * <br>
 * Description: 数据执行SQL <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
public interface CommonExecuteSqlDAOCustomer {

    /**
     * 执行SQL查询
     * @param sql sql
     * @param itemKey itemKey
     * @param lastPositionField lastPositionField
     * @return 查询结果
     */
    JSONArray queryCommonSql(String sql, String itemKey, String lastPositionField);
}
