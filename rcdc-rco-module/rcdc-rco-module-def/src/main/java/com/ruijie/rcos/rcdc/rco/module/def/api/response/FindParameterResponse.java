package com.ruijie.rcos.rcdc.rco.module.def.api.response;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author xiejian
 */
public class FindParameterResponse {

    private String value;

    public FindParameterResponse(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
