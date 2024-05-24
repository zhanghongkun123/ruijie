package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/17
 *
 * @author TD
 */
@ApiModel("一键安装请求参数")
public class OneClickInstallWebRequest {

    @NotNull
    @ApiModelProperty("一键安装功能开关")
    private Boolean openOneInstall;

    @Nullable
    @ApiModelProperty("云服务器IP")
    private String serverIp;

    @Nullable
    @ApiModelProperty("代理服务器IP")
    private String proxyServerIp;

    @Nullable
    @Range(min = "1", max = "65535")
    @ApiModelProperty("代理端口")
    private Integer proxyPort;

    public Boolean getOpenOneInstall() {
        return openOneInstall;
    }

    public void setOpenOneInstall(Boolean openOneInstall) {
        this.openOneInstall = openOneInstall;
    }

    @Nullable
    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(@Nullable String serverIp) {
        this.serverIp = serverIp;
    }

    @Nullable
    public String getProxyServerIp() {
        return proxyServerIp;
    }

    public void setProxyServerIp(@Nullable String proxyServerIp) {
        this.proxyServerIp = proxyServerIp;
    }

    @Nullable
    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(@Nullable Integer proxyPort) {
        this.proxyPort = proxyPort;
    }
}
