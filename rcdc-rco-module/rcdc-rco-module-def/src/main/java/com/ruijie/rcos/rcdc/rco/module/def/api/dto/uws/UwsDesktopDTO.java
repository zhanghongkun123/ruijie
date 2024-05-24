package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: UWS 桌面信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-22 18:31:00
 *
 * @author zjy
 */
public class UwsDesktopDTO extends UwsBaseDTO {

    @NotNull
    private UUID desktopId;

    @Nullable
    private DesktopType desktopType;

    @Nullable
    private UserCloudDeskTypeEnum desktopCategory;

    @Nullable
    private CbbCloudDeskState desktopState;

    @Nullable
    private IacUserTypeEnum userType;

    @Nullable
    private String desktopName;

    @Nullable
    private CbbOsType systemType;

    @Nullable
    private String desktopIp;

    @Nullable
    private String desktopMac;

    @Nullable
    private String computerName;

    @Nullable
    private Boolean remoteWakeUp;

    @Nullable
    private String terminalId;

    @Nullable
    private String terminalIp;

    @Nullable
    private String terminalMac;

    @Nullable
    private String idvTerminalMode;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public DesktopType getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(DesktopType desktopType) {
        this.desktopType = desktopType;
    }

    public UserCloudDeskTypeEnum getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(UserCloudDeskTypeEnum desktopCategory) {
        this.desktopCategory = desktopCategory;
    }

    public CbbCloudDeskState getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(CbbCloudDeskState desktopState) {
        this.desktopState = desktopState;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public CbbOsType getSystemType() {
        return systemType;
    }

    public void setSystemType(CbbOsType systemType) {
        this.systemType = systemType;
    }

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getDesktopMac() {
        return desktopMac;
    }

    public void setDesktopMac(String desktopMac) {
        this.desktopMac = desktopMac;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Boolean getRemoteWakeUp() {
        return remoteWakeUp;
    }

    public void setRemoteWakeUp(Boolean remoteWakeUp) {
        this.remoteWakeUp = remoteWakeUp;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public String getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(String idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }
}