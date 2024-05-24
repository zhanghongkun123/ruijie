package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: AD域配置的基本请求参数
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-04-09 15:35
 *
 * @author zouqi
 */
public class AdUserGroupAuthorityEditWebRequest implements WebRequest {
    
    @NotNull
    private UUID id;
    
    @NotNull
    private IacAdUserAuthorityEnum adUserAuthority;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }
    
}