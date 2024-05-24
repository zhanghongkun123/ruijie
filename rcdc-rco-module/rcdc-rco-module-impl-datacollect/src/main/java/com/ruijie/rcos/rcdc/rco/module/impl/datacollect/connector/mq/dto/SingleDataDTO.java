package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto;

/**
 * <br>
 * Description: 数据收集结果第二层 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
public class SingleDataDTO {
    private String itemKey;

    private String data;

    private String sqlExcption;

    public String getSqlExcption() {
        return sqlExcption;
    }

    public void setSqlExcption(String sqlExcption) {
        this.sqlExcption = sqlExcption;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
