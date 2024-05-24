package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年04月07日
 *
 * @author zhk
 */
public class RcoUserDesktopDTO {

    private UUID id;

    private UUID cbbDesktopId;

    private UUID userId;

    private String terminalId;

    private String desktopName;

    private boolean hasLogin;

    @Enumerated(EnumType.STRING)
    private CbbCloudDeskType desktopType;

    private Integer version;

    private Date createTime;

    private Date latestLoginTime;

    private Boolean hasTerminalRunning;

    /**
     * 是否加域
     */
    private Boolean hasAutoJoinDomain;

    /**
     * 是否开启系统盘自动扩容
     */
    private Boolean enableFullSystemDisk;

    private Date connectClosedTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCbbDesktopId() {
        return cbbDesktopId;
    }

    public void setCbbDesktopId(UUID cbbDesktopId) {
        this.cbbDesktopId = cbbDesktopId;
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

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public boolean isHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public CbbCloudDeskType getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskType desktopType) {
        this.desktopType = desktopType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Date latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public Boolean getHasTerminalRunning() {
        return hasTerminalRunning;
    }

    public void setHasTerminalRunning(Boolean hasTerminalRunning) {
        this.hasTerminalRunning = hasTerminalRunning;
    }

    public Boolean getHasAutoJoinDomain() {
        return hasAutoJoinDomain;
    }

    public void setHasAutoJoinDomain(Boolean hasAutoJoinDomain) {
        this.hasAutoJoinDomain = hasAutoJoinDomain;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public Date getConnectClosedTime() {
        return connectClosedTime;
    }

    public void setConnectClosedTime(Date connectClosedTime) {
        this.connectClosedTime = connectClosedTime;
    }
}
