package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:33
 *
 * @author wanglianyun
 */
public class BindDomainPasswordResponse extends Result {

    private Boolean enableDomainSso;

    public Boolean getEnableDomainSso() {
        return enableDomainSso;
    }

    public void setEnableDomainSso(Boolean enableDomainSso) {
        this.enableDomainSso = enableDomainSso;
    }

}
