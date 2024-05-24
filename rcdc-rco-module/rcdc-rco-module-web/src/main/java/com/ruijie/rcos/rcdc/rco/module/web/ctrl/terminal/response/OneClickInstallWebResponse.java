package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/17
 *
 * @author TD
 */
@ApiModel("一键安装返回参数")
public class OneClickInstallWebResponse {

    @ApiModelProperty("一键安装功能开关")
    private Boolean openOneInstall;

    @ApiModelProperty("云服务IP")
    private String serverIp;

    @ApiModelProperty("代理服务器IP")
    private String proxyServerIp;

    @ApiModelProperty("代理端口")
    private Integer proxyPort;

    public Boolean getOpenOneInstall() {
        return openOneInstall;
    }

    public void setOpenOneInstall(Boolean openOneInstall) {
        this.openOneInstall = openOneInstall;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getProxyServerIp() {
        return proxyServerIp;
    }

    public void setProxyServerIp(String proxyServerIp) {
        this.proxyServerIp = proxyServerIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }
}
