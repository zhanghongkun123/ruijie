package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * *扩展相等参数
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-1-7
 * 
 * @author artom
 */
public class MatchEqual {
    @NotNull
    private String name;

    @NotNull
    private Object[] valueArr;

    public MatchEqual() {

    }

    public MatchEqual(String name, Object[] valueArr) {
        this.name = name;
        this.valueArr = valueArr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object[] getValueArr() {
        return valueArr;
    }

    public void setValueArr(Object[] valueArr) {
        this.valueArr = valueArr;
    }
}
