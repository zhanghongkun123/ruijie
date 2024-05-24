package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time:  2020/3/3
 *
 * @author songxiang
 */
public class DomainServerTypeRequest implements WebRequest {

    @NotNull
    private DomainServerType serverType;


    public DomainServerType getServerType() {
        return serverType;
    }

    public void setServerType(DomainServerType serverType) {
        this.serverType = serverType;
    }
}
