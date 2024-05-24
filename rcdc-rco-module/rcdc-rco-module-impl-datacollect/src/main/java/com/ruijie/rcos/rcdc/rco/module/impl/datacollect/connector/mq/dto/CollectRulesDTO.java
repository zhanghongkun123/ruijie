package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * <br>
 * Description: 数据第一层规则类 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
public class CollectRulesDTO {
    private List<CollectRuleDTO> collectRuleList;

    public List<CollectRuleDTO> getCollectRuleList() {
        return collectRuleList;
    }

    public void setCollectRuleList(List<CollectRuleDTO> collectRuleList) {
        this.collectRuleList = collectRuleList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
