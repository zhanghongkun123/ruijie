package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 启用AD域配置的请求参数
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-04-09 15:35
 *
 * @author zql
 */
public class EnableAdConfigWebRequest implements WebRequest {

    @NotNull
    private Boolean enable;

    @NotNull
    private DomainServerType serverType;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public DomainServerType getServerType() {
        return serverType;
    }

    public void setServerType(DomainServerType serverType) {
        this.serverType = serverType;
    }
}
