package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserSyncTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 获取AD域的组织和用户的请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-04-09 15:35
 *
 * @author zql
 */
public class GetDomainGroupAndUserTreeWebRequest extends DomainConfigBaseWebRequest {

    @Nullable
    private String orgDn;

    @NotNull
    private IacAdUserSyncTypeEnum type;

    public String getOrgDn() {
        return orgDn;
    }

    public void setOrgDn(String orgDn) {
        this.orgDn = orgDn;
    }

    public IacAdUserSyncTypeEnum getType() {
        return type;
    }

    public void setType(IacAdUserSyncTypeEnum type) {
        this.type = type;
    }
}
