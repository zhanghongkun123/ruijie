package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.network;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.IPv4Mask;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 更新网卡信息web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 *
 * @author fyq
 */
public class BaseUpdateNetworkWebRequest implements WebRequest {

    @NotNull
    @IPv4Address
    private String ip;

    @NotNull
    @IPv4Mask
    private String netmask;

    @NotNull
    @IPv4Address
    private String gateway;

    @IPv4Address
    private String dns;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }
}
