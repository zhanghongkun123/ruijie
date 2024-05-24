package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dto;

/**
 * Description: GetCmLauncherVersionDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class GetCmLauncherVersionDTO {

    private String client;

    private String version;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
