package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;

/**
 * Description: 第三方主机的IP、管理员账号和密码的配置信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月27日
 *
 * @author liuwc
 */
public class RcaHostSingleIpDTO {

    // 应用主机IP
    @IPv4Address
    private String ip;

    // 应用主机管理员账号
    private String hostAuthName;

    // 应用主机密码
    private String hostAuthCode;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

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

}
