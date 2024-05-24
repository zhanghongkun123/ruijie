package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;

/**
 * Description: 第三方主机的IP网段、管理员账号和密码的配置信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月27日
 *
 * @author liuwc
 */
public class RcaHostSegmentIpDTO {

    @IPv4Address
    private String startIp;

    @IPv4Address
    private String endIp;

    // 应用主机管理员账号
    private String hostAuthName;

    // 应用主机密码
    private String hostAuthCode;

    public String getHostAuthName() {
        return hostAuthName;
    }

    public void setHostAuthName(String hostAuthName) {
        this.hostAuthName = hostAuthName;
    }

    public String getHostAuthCode() {
        return hostAuthCode;
    }

    public void setHostAuthCode(String hostAuthCode) {
        this.hostAuthCode = hostAuthCode;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }
}
