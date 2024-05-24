package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.TerminalOrderTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;

/**
 * Description: 可选择的终端的信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/3
 *
 * @author songxiang
 */
public class SelectableTerminalDTO {

    /**
     * 终端id
     */
    private String id;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端模式
     */
    private IdvTerminalModeEnums idvTerminalMode;

    /**
     * 所属分组ID
     */
    private UUID terminalGroupId;

    /**
     * 所属分组名称
     */
    private String terminalGroupName;


    /**
     * 绑定用户id
     */
    private UUID bindUserId;

    /**
     * 绑定用户名称
     */
    private String bindUserName;

    /**
     * 终端mac
     */
    private String macAddr;

    /**
     * 终端ip地址
     */
    private String ip;

    /**
     * 磁盘大小，单位G
     */
    private int personSize;

    /**
     * 终端状态
     */
    private CbbTerminalStateEnums terminalState;


    private Date createTime;

    /**
     * 优先级
     */
    private TerminalOrderTypeEnum orderType;

    public TerminalOrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(TerminalOrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
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

    public UUID getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(UUID bindUserId) {
        this.bindUserId = bindUserId;
    }

    public String getBindUserName() {
        return bindUserName;
    }

    public void setBindUserName(String bindUserName) {
        this.bindUserName = bindUserName;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPersonSize() {
        return personSize;
    }

    public void setPersonSize(int personSize) {
        this.personSize = personSize;
    }

    public CbbTerminalStateEnums getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(CbbTerminalStateEnums terminalState) {
        this.terminalState = terminalState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
