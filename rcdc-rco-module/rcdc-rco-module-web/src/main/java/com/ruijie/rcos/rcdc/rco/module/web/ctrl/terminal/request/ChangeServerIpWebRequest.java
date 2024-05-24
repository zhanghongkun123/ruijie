package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 修改终端服务器地址请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/15
 *
 * @author shiruifeng
 */
public class ChangeServerIpWebRequest implements WebRequest {

    @ApiModelProperty(value = "服务器VIP", required = true)
    @NotBlank
    @IPv4Address
    private String serverIp;

    public ChangeServerIpWebRequest() {

    }

    public ChangeServerIpWebRequest(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
