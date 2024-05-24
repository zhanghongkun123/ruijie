package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;


import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;

/**
 * Description: RCO新增AD配置信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/24
 *
 * @author TD
 */
public class RcoDomainConfigDetailDTO extends IacDomainConfigDetailDTO {

    private Boolean adAutoLogon;

    public Boolean getAdAutoLogon() {
        return adAutoLogon;
    }

    public void setAdAutoLogon(Boolean adAutoLogon) {
        this.adAutoLogon = adAutoLogon;
    }
}
