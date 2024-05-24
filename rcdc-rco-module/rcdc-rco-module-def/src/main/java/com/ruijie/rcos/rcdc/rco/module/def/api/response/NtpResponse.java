package com.ruijie.rcos.rcdc.rco.module.def.api.response;

/**
 * Description: ntp返回
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-04-25 17:38
 *
 * @author wanglianyun
 */


public class NtpResponse {

    private String server;

    private String description;

    private Boolean external;

    private Boolean rebootSwitch;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public Boolean getRebootSwitch() {
        return rebootSwitch;
    }

    public void setRebootSwitch(Boolean rebootSwitch) {
        this.rebootSwitch = rebootSwitch;
    }
}
