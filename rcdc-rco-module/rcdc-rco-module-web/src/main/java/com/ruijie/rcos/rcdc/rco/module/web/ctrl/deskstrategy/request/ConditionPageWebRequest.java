package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.pagekit.api.Sort;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/11 20:59
 *
 * @author linrenjian
 */
public class ConditionPageWebRequest extends PageWebRequest {

    @Nullable
    private Sort[] sortArr;

    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(Sort[] sortArr) {
        this.sortArr = sortArr;
    }
}
