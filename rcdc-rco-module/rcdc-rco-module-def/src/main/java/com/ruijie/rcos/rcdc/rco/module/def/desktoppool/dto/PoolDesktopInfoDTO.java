package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 池中云桌面的简要信息试图
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/07
 *
 * @author linke
 */
public class PoolDesktopInfoDTO {

    private UUID deskId;

    private String desktopName;

    private UUID strategyId;

    private UUID networkId;

    private UUID imageTemplateId;

    private String configIp;

    private String desktopRole;

    private Boolean isDelete;

    private Date deleteTime;

    private String deskMac;

    private CbbCloudDeskState deskState;

    private CbbCloudDeskType deskType;

    private Boolean isWindowsOsActive;

    /**
     * 是否系统自行激活
     */
    private Boolean osActiveBySystem;

    private UUID physicalServerId;

    private Date createTime;

    private String computerName;

    /**
     *  桌面池类型
     **/
    private DesktopPoolType desktopPoolType;

    /**
     * 桌面池id
     **/
    private UUID desktopPoolId;

    private Integer cpu;

    private Integer memory;

    private Integer systemSize;

    private Integer personSize;

    @Enumerated(EnumType.STRING)
    private VgpuType vgpuType;

    private String vgpuExtraInfo;

    private VgpuInfoDTO vgpuInfoDTO;

    private Boolean enableHyperVisorImprove;

    /**
     * 云桌面标签
     */
    private String remark;

    private Boolean hasLogin;

    private String desktopType;

    private UUID userId;

    private String userName;

    private UUID userGroupId;

    private String userGroupName;

    private String terminalId;

    private Boolean hasTerminalRunning;

    private String poolName;

    private CbbDesktopPoolModel poolModel;

    private Boolean faultState;

    private UUID softwareStrategyId;

    private UUID userProfileStrategyId;

    private Date connectClosedTime;

    private Boolean isOpenDeskMaintenance;

    private Date assignmentTime;

    private CbbDesktopSessionType sessionType;

    private CbbEstProtocolType estProtocolType;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getConfigIp() {
        return configIp;
    }

    public void setConfigIp(String configIp) {
        this.configIp = configIp;
    }

    public String getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(String desktopRole) {
        this.desktopRole = desktopRole;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public CbbCloudDeskType getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbCloudDeskType deskType) {
        this.deskType = deskType;
    }

    public Boolean getIsWindowsOsActive() {
        return isWindowsOsActive;
    }

    public void setIsWindowsOsActive(Boolean isWindowsOsActive) {
        this.isWindowsOsActive = isWindowsOsActive;
    }

    public Boolean getOsActiveBySystem() {
        return osActiveBySystem;
    }

    public void setOsActiveBySystem(Boolean osActiveBySystem) {
        this.osActiveBySystem = osActiveBySystem;
    }

    public UUID getPhysicalServerId() {
        return physicalServerId;
    }

    public void setPhysicalServerId(UUID physicalServerId) {
        this.physicalServerId = physicalServerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(Integer systemSize) {
        this.systemSize = systemSize;
    }

    public Integer getPersonSize() {
        return personSize;
    }

    public void setPersonSize(Integer personSize) {
        this.personSize = personSize;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public String getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(String vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    public VgpuInfoDTO getVgpuInfoDTO() {
        return vgpuInfoDTO;
    }

    public void setVgpuInfoDTO(VgpuInfoDTO vgpuInfoDTO) {
        this.vgpuInfoDTO = vgpuInfoDTO;
    }

    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(Boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Boolean getHasTerminalRunning() {
        return hasTerminalRunning;
    }

    public void setHasTerminalRunning(Boolean hasTerminalRunning) {
        this.hasTerminalRunning = hasTerminalRunning;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public CbbDesktopPoolModel getPoolModel() {
        return poolModel;
    }

    public void setPoolModel(CbbDesktopPoolModel poolModel) {
        this.poolModel = poolModel;
    }

    public Boolean getFaultState() {
        return faultState;
    }

    public void setFaultState(Boolean faultState) {
        this.faultState = faultState;
    }

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public Date getConnectClosedTime() {
        return connectClosedTime;
    }

    public void setConnectClosedTime(Date connectClosedTime) {
        this.connectClosedTime = connectClosedTime;
    }

    public Boolean getIsOpenDeskMaintenance() {
        return isOpenDeskMaintenance;
    }

    public void setIsOpenDeskMaintenance(Boolean isOpenDeskMaintenance) {
        this.isOpenDeskMaintenance = isOpenDeskMaintenance;
    }

    public Date getAssignmentTime() {
        return assignmentTime;
    }

    public void setAssignmentTime(Date assignmentTime) {
        this.assignmentTime = assignmentTime;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
    }
}
