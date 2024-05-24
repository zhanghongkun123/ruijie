package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 桌面池与PC终端关系表
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
@Entity
@Table(name = "v_rco_desktop_pool_computer")
public class ViewDesktopPoolComputerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID desktopPoolId;

    private UUID relatedId;

    @Enumerated(EnumType.STRING)
    private ComputerRelatedType relatedType;


    @Column(name = "group_id")
    private UUID terminalGroupId;


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

    @Enumerated(EnumType.STRING)
    private ComputerStateEnum state;


    private Date createTime;

    private Integer systemDisk;

    @Version
    private Integer version;

    private Integer personDisk;


    public ViewDesktopPoolComputerEntity() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public ComputerRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(ComputerRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
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

    public ComputerWorkModelEnum getWorkModel() {
        return workModel;
    }

    public void setWorkModel(ComputerWorkModelEnum workModel) {
        this.workModel = workModel;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
