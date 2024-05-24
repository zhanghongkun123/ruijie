package com.ruijie.rcos.rcdc.rco.module.impl.computer.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.enums.RcoComputerActionNotifyEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: pc终端操作通知
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/17
 *
 * @author zqj
 */
public class NotifyComputerDTO {

    private UUID id;

    private String ip;


    private String name;


    private String userName;


    private UUID userId;

    private String deskIp;

    private String mac;

    private UUID terminalGroupId;

    private String remark;

    private UUID desktopStrategyId;


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
    private String agentVersion;

    @NotNull
    private RcoComputerActionNotifyEnum actionNotifyEnum;

    private ComputerWorkModelEnum workModel;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UUID getDesktopStrategyId() {
        return desktopStrategyId;
    }

    public void setDesktopStrategyId(UUID desktopStrategyId) {
        this.desktopStrategyId = desktopStrategyId;
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

    public RcoComputerActionNotifyEnum getActionNotifyEnum() {
        return actionNotifyEnum;
    }

    public void setActionNotifyEnum(RcoComputerActionNotifyEnum actionNotifyEnum) {
        this.actionNotifyEnum = actionNotifyEnum;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public ComputerWorkModelEnum getWorkModel() {
        return workModel;
    }

    public void setWorkModel(ComputerWorkModelEnum workModel) {
        this.workModel = workModel;
    }
}
