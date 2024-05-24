package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEthType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/4
 *
 * @author xwx
 */
public class BuildWakeTerminalNetDataRequest {

    @NotNull
    private CbbEthType cbbEthType;

    @Nullable
    private Integer vlanId;

    @NotNull
    private String destIp;

    @NotNull
    private String destMac;

    @NotNull
    private Integer srcPort;

    @NotNull
    private Integer destPort;

    @NotNull
    private String terminalId;

    @NotNull
    private String terminalIp;

    public CbbEthType getCbbEthType() {
        return cbbEthType;
    }

    public void setCbbEthType(CbbEthType cbbEthType) {
        this.cbbEthType = cbbEthType;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public String getDestMac() {
        return destMac;
    }

    public void setDestMac(String destMac) {
        this.destMac = destMac;
    }

    public Integer getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(Integer srcPort) {
        this.srcPort = srcPort;
    }

    public Integer getDestPort() {
        return destPort;
    }

    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }
}
