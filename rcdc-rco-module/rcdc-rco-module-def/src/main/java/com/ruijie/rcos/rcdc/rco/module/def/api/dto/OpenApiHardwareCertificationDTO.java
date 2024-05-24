package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月23日
 *
 * @author zdc
 */
public class OpenApiHardwareCertificationDTO {

    /**
     * 记录ID
     */
    private UUID id;

    /**
     * 用户ID
     */
    private UUID userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户组ID
     */
    private UUID userGroupId;


    /**
     * 用户组名称
     */
    private String userGroupName;

    /**
     * 终端ID
     */
    private String terminalId;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端IP
     */
    private String ip;

    /**
     * 状态
     */
    private IacUserHardwareCertificationStateEnum state;

    private String macAddr;

    /**
     * 特征码
     */
    private String featureCode;

    /**
     * 创建时间
     */
    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public IacUserHardwareCertificationStateEnum getState() {
        return state;
    }

    public void setState(IacUserHardwareCertificationStateEnum state) {
        this.state = state;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
