package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;

import java.util.UUID;

/**
 * Description: Computer网络信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */

public class ComputerNetInfoDTO {

    private UUID hostId;

    private String hostIp;

    private String mac;

    private String mask;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }
}
