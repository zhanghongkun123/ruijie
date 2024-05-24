package com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskRegisterState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 桌面信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-17 11:31:00
 *
 * @author zjy
 */
public class DesktopInfoDTO {

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
    private String terminalMask;

    @Nullable
    private String idvTerminalMode;

    @Nullable
    private CbbTerminalStateEnums terminalState;

    @Nullable
    private UUID clusterId;

    @Nullable
    private String clusterName;

    @Nullable
    private UUID platformId;

    @Nullable
    private String platformName;

    @Nullable
    private CbbDeskRegisterState registerState;

    @Nullable
    private String registerMessage;

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

    public CbbTerminalStateEnums getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(CbbTerminalStateEnums terminalState) {
        this.terminalState = terminalState;
    }

    @Nullable
    public String getTerminalMask() {
        return terminalMask;
    }

    public void setTerminalMask(@Nullable String terminalMask) {
        this.terminalMask = terminalMask;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(@Nullable String clusterName) {
        this.clusterName = clusterName;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }

    @Nullable
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(@Nullable String platformName) {
        this.platformName = platformName;
    }

    @Nullable
    public CbbDeskRegisterState getRegisterState() {
        return registerState;
    }

    public void setRegisterState(@Nullable CbbDeskRegisterState registerState) {
        this.registerState = registerState;
    }

    @Nullable
    public String getRegisterMessage() {
        return registerMessage;
    }

    public void setRegisterMessage(@Nullable String registerMessage) {
        this.registerMessage = registerMessage;
    }
}