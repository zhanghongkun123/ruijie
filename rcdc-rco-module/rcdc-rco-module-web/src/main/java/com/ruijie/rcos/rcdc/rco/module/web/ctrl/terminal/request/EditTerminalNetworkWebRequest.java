package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.IPv4Mask;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 单台设置终端网络配置页面请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/16
 *
 * @author heruiyuan1
 */
public class EditTerminalNetworkWebRequest implements WebRequest {

    @ApiModelProperty(value = "终端ID", required = true)
    @NotNull
    private String id;

    @ApiModelProperty(value = "是否自动获取IP", required = true)
    @NotNull
    private Boolean autoDhcp;

    @ApiModelProperty(value = "IP地址")
    @IPv4Address
    @Nullable
    private String ipAddr;

    @ApiModelProperty(value = "子网掩码")
    @IPv4Mask
    @Nullable
    private String mask;

    @ApiModelProperty(value = "网关")
    @IPv4Address
    @Nullable
    private String gateway;

    @ApiModelProperty(value = "是否自动获取DNS", required = true)
    @NotNull
    private Boolean autoDns;

    @ApiModelProperty(value = "首选DNS")
    @Nullable
    @IPv4Address
    private String dns;

    @ApiModelProperty(value = "备用DNS")
    @Nullable
    @IPv4Address
    private String dnsBack;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAutoDhcp() {
        return autoDhcp;
    }

    public void setAutoDhcp(Boolean autoDhcp) {
        this.autoDhcp = autoDhcp;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Boolean getAutoDns() {
        return autoDns;
    }

    public void setAutoDns(Boolean autoDns) {
        this.autoDns = autoDns;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getDnsBack() {
        return dnsBack;
    }

    public void setDnsBack(String dnsBack) {
        this.dnsBack = dnsBack;
    }
}
