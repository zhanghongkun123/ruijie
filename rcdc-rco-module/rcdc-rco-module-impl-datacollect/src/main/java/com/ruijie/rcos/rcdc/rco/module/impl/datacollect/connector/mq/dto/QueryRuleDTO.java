package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto;

/**
 * <br>
 * Description: 数据收集第三层规则 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
public class QueryRuleDTO {
    private String tableName;

    private String queryFields;

    private String condition;

    private String sort;

    private String collectMethod;

    private String lastPositionField;

    private String maxSize;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getQueryFields() {
        return queryFields;
    }

    public void setQueryFields(String queryFields) {
        this.queryFields = queryFields;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCollectMethod() {
        return collectMethod;
    }

    public void setCollectMethod(String collectMethod) {
        this.collectMethod = collectMethod;
    }

    public String getLastPositionField() {
        return lastPositionField;
    }

    public void setLastPositionField(String lastPositionField) {
        this.lastPositionField = lastPositionField;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }
}
