package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 更新Computer规格
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/16
 *
 * @author zqj
 */
public class UpdateComputerSpecRequest implements Request {


    @NotNull
    private UUID computerId;

    @Nullable
    private String ip;

    @Nullable
    private Integer cpu;

    @Nullable
    private Integer memory;

    @Nullable
    private Integer personSize;

    @Nullable
    private Integer systemSize;

    @Nullable
    private String os;

    @Nullable
    private String osVersion;

    @Nullable
    private String agentVersion;

    private String subnetMask;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UUID getComputerId() {
        return computerId;
    }

    public void setComputerId(UUID computerId) {
        this.computerId = computerId;
    }

    @Nullable
    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(@Nullable Integer cpu) {
        this.cpu = cpu;
    }

    @Nullable
    public Integer getMemory() {
        return memory;
    }

    public void setMemory(@Nullable Integer memory) {
        this.memory = memory;
    }

    @Nullable
    public Integer getPersonSize() {
        return personSize;
    }

    public void setPersonSize(@Nullable Integer personSize) {
        this.personSize = personSize;
    }

    @Nullable
    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(@Nullable Integer systemSize) {
        this.systemSize = systemSize;
    }

    @Nullable
    public String getOs() {
        return os;
    }

    public void setOs(@Nullable String os) {
        this.os = os;
    }

    @Nullable
    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(@Nullable String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    @Nullable
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(@Nullable String osVersion) {
        this.osVersion = osVersion;
    }
}
