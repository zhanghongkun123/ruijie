package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/03/18
 *
 * @author guoyongxin
 */
public class NodeItemDTO {

    private UUID id;

    private UUID hostId;

    private UUID clusterId;

    private String ip;

    private String manageState;

    private String connectState;

    private String authState;

    private String sn;

    private String cpuVendorId;

    private String cpuModelName;

    private String cpuArchitecture;

    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getManageState() {
        return manageState;
    }

    public void setManageState(String manageState) {
        this.manageState = manageState;
    }

    public String getConnectState() {
        return connectState;
    }

    public void setConnectState(String connectState) {
        this.connectState = connectState;
    }

    public String getAuthState() {
        return authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCpuVendorId() {
        return cpuVendorId;
    }

    public void setCpuVendorId(String cpuVendorId) {
        this.cpuVendorId = cpuVendorId;
    }

    public String getCpuModelName() {
        return cpuModelName;
    }

    public void setCpuModelName(String cpuModelName) {
        this.cpuModelName = cpuModelName;
    }

    public String getCpuArchitecture() {
        return cpuArchitecture;
    }

    public void setCpuArchitecture(String cpuArchitecture) {
        this.cpuArchitecture = cpuArchitecture;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
