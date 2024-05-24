package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/4 18:41
 *
 * @author ketb
 */
public class ComputerInfoResponse extends DefaultResponse {

    private UUID id;

    private UUID groupId;

    private String groupName;

    private String ip;

    private String mac;

    private String name;

    private String alias;

    private String os;

    private String disk;

    private String cpu;

    private String memory;

    private ComputerStateEnum state;

    private ComputerWorkModelEnum workModel;

    private String faultDescription;

    private Date createTime;

    private String agentVersion;

    private ComputerTypeEnum type;

    private Integer systemDisk;

    private Integer personDisk;

    private String remark;

    private String networkNumber;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public ComputerStateEnum getState() {
        return state;
    }

    public void setState(ComputerStateEnum state) {
        this.state = state;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getComputerIdForOptLog() {
        return getMac();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public ComputerTypeEnum getType() {
        return type;
    }

    public void setType(ComputerTypeEnum type) {
        this.type = type;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public Integer getPersonDisk() {
        return personDisk;
    }

    public void setPersonDisk(Integer personDisk) {
        this.personDisk = personDisk;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNetworkNumber() {
        return networkNumber;
    }

    public void setNetworkNumber(String networkNumber) {
        this.networkNumber = networkNumber;
    }

    public ComputerWorkModelEnum getWorkModel() {
        return workModel;
    }

    public void setWorkModel(ComputerWorkModelEnum workModel) {
        this.workModel = workModel;
    }
}
