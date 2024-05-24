package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月17日
 *
 * @author chenl
 */
public class FindParameterListRequest implements WebRequest {

    @NotEmpty
    private String[] keyArr;

    public FindParameterListRequest(String[] keyArr) {
        this.keyArr = keyArr;
    }

    public String[] getKeyArr() {
        return keyArr;
    }

    public void setKeyArr(String[] keyArr) {
        this.keyArr = keyArr;
    }
}
