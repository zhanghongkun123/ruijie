package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskRegisterState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * 派生云应用主机桌面视图查询对象
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 * 
 * @liuwc
 */
@Entity
@Table(name = "v_cbb_rca_host_desktop_detail")
public class ViewRcaHostDesktopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // DESKID
    private UUID cbbDesktopId;

    /**
     * 云应用-云主机id
     */
    private UUID rcaHostId;

    /**
     * 云应用-云主机id
     */
    private UUID rcaPoolId;

    /**
     * 云应用-应用主机会话类型（单会话、多会话、云坞）
     */
    @Enumerated(EnumType.STRING)
    private RcaEnum.HostSessionType rcaHostSessionType;

    /**
     * 云应用-应用池类型（静态池、动态池）
     */
    @Enumerated(EnumType.STRING)
    private RcaEnum.PoolType rcaPoolType;

    /**
     * 云应用-所属应用池名称
     */
    private String rcaPoolName;

    /**
     * 云应用-最大会话数
     */
    private Integer rcaMaxSessionCount;

    /**
     * 云应用-预启动数
     */
    private Integer rcaPreStartHostNum;

    /**
     * 云应用-会话保持时长
     */
    private Integer rcaSessionHoldTime;

    /**
     * 云应用-负载均衡配置
     */
    @Enumerated(EnumType.STRING)
    private RcaEnum.LoadBalanceMode rcaLoadBalanceMode;

    /**
     * 云应用-负载均衡配置
     */
    @Enumerated(EnumType.STRING)
    private RcaEnum.SessionHoldConfigMode rcaSessionHoldConfigMode;

    /**
     * 纳管的应用主机的超时时间
     */
    private Integer rcaDaysLeft;

    /**
     * 是否超期，true说明超期
     */
    private Boolean isExpire;

    private UUID cbbStrategyId;

    private UUID cbbNetworkId;

    private String terminalId;

    @Version
    private Integer version;

    private String desktopType;

    private Boolean isDelete;

    private Date deleteTime;

    private Date createTime;

    private String desktopName;

    private String deskMac;

    private String ip;

    private String deskState;// CbbCloudDeskState

    private String deskType;// CbbCloudDeskType

    private Boolean isWindowsOsActive;

    private Boolean osActiveBySystem;

    private UUID imageTemplateId;

    private String configIp;

    private String strategyName;

    private String pattern;// CbbCloudDeskPattern

    private Integer systemSize;

    private Integer cpu;

    private Integer memory;

    private Integer personSize;

    private Boolean enableHyperVisorImprove;

    @Enumerated(EnumType.STRING)
    private VgpuType vgpuType;

    private String vgpuExtraInfo;

    private Date latestLoginTime;

    /**
     * 最后启动时间
     */
    private Date latestRunningTime;

    private String imageTemplateName;

    private UUID rootImageId;

    private String rootImageName;

    @Enumerated(EnumType.STRING)
    private ImageRoleType imageRoleType;

    /**
     * 保存编辑前的gt版本号
     */
    private String beforeEditGuestToolVersion;

    /**
     * 变更镜像后的新镜像ID
     **/
    private UUID willApplyImageId;

    private String osType;

    private String cbbImageType;

    private UUID physicalServerId;

    private String physicalServerIp;

    /**
     * 是否加入AD域
     */
    private Boolean hasAutoJoinDomain;

    private String hostName;

    private String desktopRole;

    private String productType;

    private String terminalPlatform;// VDI DIV

    private Date lastOnlineTime;

    private Boolean enableProxy;

    private String computerName;

    private Boolean hasTerminalRunning;

    /**
     * 云桌面标签
     */
    private String remark;

    /**
     * 是否开启网页客户端接入
     **/
    private Boolean enableWebClient;

    /**
     * 是否独立配置的规格
     */
    private Boolean enableCustom;

    private String deskCreateMode;

    /**
     * 连接关闭时间
     */
    private Date connectClosedTime;

    private Boolean faultState;

    private String faultDescription;

    private Date faultTime;

    /**
     * 云桌面运行位置
     */
    private UUID clusterId;

    /**
     * 云桌面维护模式
     */
    private Boolean isOpenDeskMaintenance;

    private String osVersion;

    /**
     * 网络策略名称
     */
    private String networkName;

    /**
     * 镜像用途：桌面镜像/应用镜像
     */
    @Enumerated(EnumType.STRING)
    private ImageUsageTypeEnum imageUsage;

    private boolean hasLogin;

    private String oneAgentVersion;

    /**
     * 云平台ID
     */
    private UUID platformId;

    /**
     * 云平台名称
     */
    private String platformName;

    /**
     * 云平台类型
     */
    @Enumerated(EnumType.STRING)
    private CloudPlatformType platformType;

    /**
     * 云平台状态
     */
    @Enumerated(EnumType.STRING)
    private CloudPlatformStatus platformStatus;

    @Enumerated(EnumType.STRING)
    private CbbDeskRegisterState registerState;

    private String registerMessage;

    public Date getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(Date faultTime) {
        this.faultTime = faultTime;
    }

    public Boolean isFaultState() {
        return faultState;
    }

    public Boolean getFaultState() {
        return this.isFaultState();
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public UUID getCbbStrategyId() {
        return cbbStrategyId;
    }

    public void setCbbStrategyId(UUID cbbStrategyId) {
        this.cbbStrategyId = cbbStrategyId;
    }

    public UUID getCbbNetworkId() {
        return cbbNetworkId;
    }

    public void setCbbNetworkId(UUID cbbNetworkId) {
        this.cbbNetworkId = cbbNetworkId;
    }

    public UUID getCbbDesktopId() {
        return cbbDesktopId;
    }

    public void setCbbDesktopId(UUID cbbDesktopId) {
        this.cbbDesktopId = cbbDesktopId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
    }

    public String getDeskState() {
        return deskState;
    }

    public void setDeskState(String deskState) {
        this.deskState = deskState;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }


    public String getTerminalPlatform() {
        return terminalPlatform;
    }

    public void setTerminalPlatform(String terminalPlatform) {
        this.terminalPlatform = terminalPlatform;
    }


    public String getConfigIp() {
        return configIp;
    }

    public void setConfigIp(String configIp) {
        this.configIp = configIp;
    }

    public Date getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Date latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public Boolean getHasTerminalRunning() {
        return hasTerminalRunning;
    }

    public void setHasTerminalRunning(Boolean hasTerminalRunning) {
        this.hasTerminalRunning = hasTerminalRunning;
    }

    public UUID getPhysicalServerId() {
        return physicalServerId;
    }

    public void setPhysicalServerId(UUID physicalServerId) {
        this.physicalServerId = physicalServerId;
    }


    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
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

    public String getPhysicalServerIp() {
        return physicalServerIp;
    }

    public void setPhysicalServerIp(String physicalServerIp) {
        this.physicalServerIp = physicalServerIp;
    }

    public String getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(String desktopRole) {
        this.desktopRole = desktopRole;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Boolean getEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnableCustom() {
        return enableCustom;
    }

    public void setEnableCustom(Boolean enableCustom) {
        this.enableCustom = enableCustom;
    }

    public String getDeskCreateMode() {
        return deskCreateMode;
    }

    public void setDeskCreateMode(String deskCreateMode) {
        this.deskCreateMode = deskCreateMode;
    }

    public Boolean getEnableWebClient() {
        return enableWebClient;
    }

    public void setEnableWebClient(Boolean enableWebClient) {
        this.enableWebClient = enableWebClient;
    }

    public Boolean getHasAutoJoinDomain() {
        return hasAutoJoinDomain;
    }

    public void setHasAutoJoinDomain(Boolean hasAutoJoinDomain) {
        this.hasAutoJoinDomain = hasAutoJoinDomain;
    }

    public Date getConnectClosedTime() {
        return connectClosedTime;
    }

    public void setConnectClosedTime(Date connectClosedTime) {
        this.connectClosedTime = connectClosedTime;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public Boolean getIsOpenDeskMaintenance() {
        return isOpenDeskMaintenance;
    }

    public void setIsOpenDeskMaintenance(Boolean isOpenDeskMaintenance) {
        this.isOpenDeskMaintenance = isOpenDeskMaintenance;
    }

    public String getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(String cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public UUID getRootImageId() {
        return rootImageId;
    }

    public void setRootImageId(UUID rootImageId) {
        this.rootImageId = rootImageId;
    }

    public String getRootImageName() {
        return rootImageName;
    }

    public void setRootImageName(String rootImageName) {
        this.rootImageName = rootImageName;
    }

    public ImageRoleType getImageRoleType() {
        return imageRoleType;
    }

    public void setImageRoleType(ImageRoleType imageRoleType) {
        this.imageRoleType = imageRoleType;
    }

    public UUID getWillApplyImageId() {
        return willApplyImageId;
    }

    public void setWillApplyImageId(UUID willApplyImageId) {
        this.willApplyImageId = willApplyImageId;
    }

    public Date getLatestRunningTime() {
        return latestRunningTime;
    }

    public void setLatestRunningTime(Date latestRunningTime) {
        this.latestRunningTime = latestRunningTime;
    }

    public String getBeforeEditGuestToolVersion() {
        return beforeEditGuestToolVersion;
    }

    public void setBeforeEditGuestToolVersion(String beforeEditGuestToolVersion) {
        this.beforeEditGuestToolVersion = beforeEditGuestToolVersion;
    }

    public RcaEnum.HostSessionType getRcaHostSessionType() {
        return rcaHostSessionType;
    }

    public void setRcaHostSessionType(RcaEnum.HostSessionType rcaHostSessionType) {
        this.rcaHostSessionType = rcaHostSessionType;
    }

    public RcaEnum.PoolType getRcaPoolType() {
        return rcaPoolType;
    }

    public void setRcaPoolType(RcaEnum.PoolType rcaPoolType) {
        this.rcaPoolType = rcaPoolType;
    }

    public String getRcaPoolName() {
        return rcaPoolName;
    }

    public void setRcaPoolName(String rcaPoolName) {
        this.rcaPoolName = rcaPoolName;
    }

    public UUID getRcaHostId() {
        return rcaHostId;
    }

    public void setRcaHostId(UUID rcaHostId) {
        this.rcaHostId = rcaHostId;
    }

    public Integer getRcaMaxSessionCount() {
        return rcaMaxSessionCount;
    }

    public void setRcaMaxSessionCount(Integer rcaMaxSessionCount) {
        this.rcaMaxSessionCount = rcaMaxSessionCount;
    }

    public Integer getRcaPreStartHostNum() {
        return rcaPreStartHostNum;
    }

    public void setRcaPreStartHostNum(Integer rcaPreStartHostNum) {
        this.rcaPreStartHostNum = rcaPreStartHostNum;
    }

    public Integer getRcaSessionHoldTime() {
        return rcaSessionHoldTime;
    }

    public void setRcaSessionHoldTime(Integer rcaSessionHoldTime) {
        this.rcaSessionHoldTime = rcaSessionHoldTime;
    }

    public RcaEnum.LoadBalanceMode getRcaLoadBalanceMode() {
        return rcaLoadBalanceMode;
    }

    public void setRcaLoadBalanceMode(RcaEnum.LoadBalanceMode rcaLoadBalanceMode) {
        this.rcaLoadBalanceMode = rcaLoadBalanceMode;
    }

    public RcaEnum.SessionHoldConfigMode getRcaSessionHoldConfigMode() {
        return rcaSessionHoldConfigMode;
    }

    public void setRcaSessionHoldConfigMode(RcaEnum.SessionHoldConfigMode rcaSessionHoldConfigMode) {
        this.rcaSessionHoldConfigMode = rcaSessionHoldConfigMode;
    }

    public UUID getRcaPoolId() {
        return rcaPoolId;
    }

    public void setRcaPoolId(UUID rcaPoolId) {
        this.rcaPoolId = rcaPoolId;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Boolean getWindowsOsActive() {
        return isWindowsOsActive;
    }

    public void setWindowsOsActive(Boolean windowsOsActive) {
        isWindowsOsActive = windowsOsActive;
    }

    public Boolean getOpenDeskMaintenance() {
        return isOpenDeskMaintenance;
    }

    public void setOpenDeskMaintenance(Boolean openDeskMaintenance) {
        isOpenDeskMaintenance = openDeskMaintenance;
    }

    public Integer getRcaDaysLeft() {
        return rcaDaysLeft;
    }

    public void setRcaDaysLeft(Integer daysLeft) {
        this.rcaDaysLeft = daysLeft;
    }

    public Boolean getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(Boolean expire) {
        isExpire = expire;
    }

    public String getOneAgentVersion() {
        return oneAgentVersion;
    }

    public void setOneAgentVersion(String oneAgentVersion) {
        this.oneAgentVersion = oneAgentVersion;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public CbbDeskRegisterState getRegisterState() {
        return registerState;
    }

    public void setRegisterState(CbbDeskRegisterState registerState) {
        this.registerState = registerState;
    }

    public String getRegisterMessage() {
        return registerMessage;
    }

    public void setRegisterMessage(String registerMessage) {
        this.registerMessage = registerMessage;
    }
}
