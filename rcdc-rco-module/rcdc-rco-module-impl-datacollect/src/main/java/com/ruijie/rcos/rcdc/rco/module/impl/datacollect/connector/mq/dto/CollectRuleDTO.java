package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto;

/**
 * <br>
 * Description: 数据收集第二层规则类 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
public class CollectRuleDTO {

    private String productVersionRegEx;

    private String itemKey;

    private QueryRuleDTO queryRule;

    public String getProductVersionRegEx() {
        return productVersionRegEx;
    }

    public void setProductVersionRegEx(String productVersionRegEx) {
        this.productVersionRegEx = productVersionRegEx;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public QueryRuleDTO getQueryRule() {
        return queryRule;
    }

    public void setQueryRule(QueryRuleDTO queryRule) {
        this.queryRule = queryRule;
    }
}
