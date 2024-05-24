package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.Date;
import java.util.UUID;

/**
 * 用户硬件特征码记录DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public class UserHardwareCertificationDTO extends EqualsHashcodeSupport {

    /**
     * 记录ID
     */
    private UUID id;

    /**
     * 用户ID
     */
    private UUID userId;

    /**
     * 终端ID
     */
    private String terminalId;

    /**
     * 状态
     */
    private IacUserHardwareCertificationStateEnum state;

    /**
     * mac地址
     */
    private String macAddr;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
