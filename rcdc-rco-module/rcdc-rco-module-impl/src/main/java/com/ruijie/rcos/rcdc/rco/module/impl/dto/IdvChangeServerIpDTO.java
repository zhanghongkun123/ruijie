package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description: 修改终端服务器地址消息体
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/03/21 16:40
 *
 * @author shiruifeng
 */
public class IdvChangeServerIpDTO {

    private String serverIp;

    public IdvChangeServerIpDTO() {

    }

    public IdvChangeServerIpDTO(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
