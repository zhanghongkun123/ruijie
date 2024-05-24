package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author zhangsiming
 */
public class UpgradeImageProgressWebRequest extends DefaultWebRequest {
    @NotNull
    private Integer from;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }
}
