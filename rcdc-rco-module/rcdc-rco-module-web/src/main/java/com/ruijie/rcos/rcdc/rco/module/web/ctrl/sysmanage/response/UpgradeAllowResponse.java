package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年03月30日
 *
 * @author GuoZhouYue
 */
public class UpgradeAllowResponse {

    private String httpPort;

    private String socketPort;

    private String socketPath;

    public String getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(String httpPort) {
        this.httpPort = httpPort;
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    public String getSocketPath() {
        return socketPath;
    }

    public void setSocketPath(String socketPath) {
        this.socketPath = socketPath;
    }
}
