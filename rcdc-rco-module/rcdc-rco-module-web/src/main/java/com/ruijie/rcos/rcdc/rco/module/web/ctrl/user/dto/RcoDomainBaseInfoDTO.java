package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

/**
 * Description: RCO新增AD配置基础信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-27
 *
 * @author zqj
 */
public class RcoDomainBaseInfoDTO  {

    private Boolean enable;

    private String domainName;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
