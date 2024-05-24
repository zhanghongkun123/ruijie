package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 统一登录配置信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-06-22
 *
 * @author lihengjing
 */
public class RccmServerConfigInfoDTO {

    public RccmServerConfigInfoDTO(Boolean isUnifiedLogin) {
        this.isUnifiedLogin = isUnifiedLogin;
    }

    @NotNull
    private Boolean isUnifiedLogin;

    public Boolean getIsUnifiedLogin() {
        return isUnifiedLogin;
    }

    public void setIsUnifiedLogin(Boolean isUnifiedLogin) {
        this.isUnifiedLogin = isUnifiedLogin;
    }
}
