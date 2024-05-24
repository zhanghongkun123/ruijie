package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/4 18:50
 *
 * @author ketb
 */
public class ComputerDTO {
    private UUID id;

    private UUID terminalGroupId;

    private String terminalGroupName;

    private String ip;

    private String mac;

    private String name;

    private String alias;

    private String os;

    private String agentVersion;

    private ComputerTypeEnum type;

    private ComputerWorkModelEnum workModel;

    private String disk;

    private String cpu;

    private String memory;

    private ComputerStateEnum state;

    private Boolean faultState;

    private String faultDescription;

    private Date createTime;

    private String serverIp;

    private Date faultTime;

    private String remark;

    private Integer systemDisk;

    private Integer personDisk;

    private boolean canUsed = Boolean.TRUE;

    private String canUsedMessage;

    private String deskName;

    private String deskPoolName;

    private Boolean hasDesk;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
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

    /**
     * 内存转为GB
     * @return 内存
     */
    public String getMemory() {
        if (type == ComputerTypeEnum.PC) {
            return memory;
        }
        if (StringUtils.isEmpty(memory)) {
            return "0.0";
        }
        double conversionOfNumber = 102.4;
        // server报上来的内存会少1kb，比如2.5GB的内存，win-server里面体现的回事2559，2GB->2047 此时需要小数点右移一位后再向上取整
        double memoryGb = Math.ceil(Double.parseDouble(memory) / conversionOfNumber) / 10;
        return Double.toString(memoryGb);
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

    public Boolean getFaultState() {
        return faultState;
    }

    public void setFaultState(Boolean faultState) {
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

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Date getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(Date faultTime) {
        this.faultTime = faultTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public ComputerWorkModelEnum getWorkModel() {
        return workModel;
    }

    public void setWorkModel(ComputerWorkModelEnum workModel) {
        this.workModel = workModel;
    }

    public boolean isCanUsed() {
        return canUsed;
    }

    public void setCanUsed(boolean canUsed) {
        this.canUsed = canUsed;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskPoolName() {
        return deskPoolName;
    }

    public void setDeskPoolName(String deskPoolName) {
        this.deskPoolName = deskPoolName;
    }

    public Boolean getHasDesk() {
        return hasDesk;
    }

    public void setHasDesk(Boolean hasDesk) {
        this.hasDesk = hasDesk;
    }
}
