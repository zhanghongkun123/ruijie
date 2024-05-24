package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/30 15:58
 *
 * @author ketb
 */
@Entity
@Table(name = "t_rco_computer")
public class ComputerEntity {

    @Id
    private UUID id;

    @Column(name = "group_id")
    private UUID terminalGroupId;


    private UUID terminalId;

    private String ip;

    private String mac;

    private String name;

    private String alias;

    private String os;

    private String agentVersion;

    @Enumerated(EnumType.STRING)
    private ComputerTypeEnum type;


    @Enumerated(EnumType.STRING)
    private ComputerWorkModelEnum workModel;

    private String disk;

    private String cpu;

    private String memory;

    private Integer systemDisk;

    private Integer personDisk;

    private String assistPwd;

    @Enumerated(EnumType.STRING)
    private ComputerStateEnum state;

    private boolean faultState;

    private String faultDescription;

    private Date createTime;

    private Date faultTime;

    @Version
    private Integer version;

    private String subnetMask;

    private String networkNumber;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
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

    public String getAssistPwd() {
        return assistPwd;
    }

    public void setAssistPwd(String assistPwd) {
        this.assistPwd = assistPwd;
    }

    public ComputerStateEnum getState() {
        return state;
    }

    public void setState(ComputerStateEnum state) {
        this.state = state;
    }

    public boolean getFaultState() {
        return faultState;
    }

    public void setFaultState(boolean faultState) {
        this.faultState = faultState;
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

    public Date getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(Date faultTime) {
        this.faultTime = faultTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public boolean isFaultState() {
        return faultState;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getNetworkNumber() {
        return networkNumber;
    }

    public void setNetworkNumber(String networkNumber) {
        this.networkNumber = networkNumber;
    }

    public UUID getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(UUID terminalId) {
        this.terminalId = terminalId;
    }

    public ComputerWorkModelEnum getWorkModel() {
        return workModel;
    }

    public void setWorkModel(ComputerWorkModelEnum workModel) {
        this.workModel = workModel;
    }
}
