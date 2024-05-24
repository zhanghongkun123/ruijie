package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;

/**
 *
 * Description: 派生云主机桌面DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 *
 * @author liuwc
 */
public class RcaHostDesktopDTO {

    private UUID id;

    /**
     * desktopId
     */
    private UUID cbbId;

    /**
     * 云应用-云主机id
     */
    private UUID rcaHostId;

    /**
     * 云应用-应用主机会话类型（单会话、多会话、云坞）
     */
    private RcaEnum.HostSessionType rcaHostSessionType;

    /**
     * 云应用-应用池类型（静态池、动态池）
     */
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
     * 云应用-使用会话数
     */
    private Integer rcaSessionUsage;

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
    private RcaEnum.LoadBalanceMode rcaLoadBalanceMode;

    /**
     * 云应用-负载均衡配置
     */
    private RcaEnum.SessionHoldConfigMode rcaSessionHoldConfigMode;

    /**
     * 外设策略id
     */
    private UUID rcaPeripheralStrategyId;

    /**
     * 云应用外设策略名称
     */
    private String rcaPeripheralStrategyName;

    /**
     * 单会话-纳管主机对应的用户信息
     */
    private IacUserDetailDTO rcaUserDetailDTO;

    /**
     * 纳管的应用主机的超时时间
     */
    private Integer rcaDaysLeft;

    /**
     * 是否超期，true说明超期
     */
    private Boolean isExpire;

    /**
     * 应用池的镜像名称
     */
    private String poolImageTemplateName;

    /**
     * 应用池的源镜像名称
     */
    private String poolRootImageName;

    /**
     * 云应用-当前主机的镜像是否和池镜像保持一致
     */
    private Boolean isDifferentImageWithRcaPool;

    private String desktopName;

    private String desktopImageName;

    private String desktopNetworkName;

    private String osVersion;

    private UUID userId;

    private String userName;

    private String realName;

    private String userType;

    private UUID userGroupId;

    private String userGroup;

    private String desktopState;

    private String desktopType;

    // conghaifeng 这部分代码后续应该是移动到rco使用double
    private Double memory;

    private Integer cpu;

    private Integer systemDisk;

    private Integer personDisk;

    private String imageName;

    private String osName;

    private String desktopIp;

    private String desktopMac;

    private Boolean autoDeskDhcp;

    private String deskGateway;

    private String deskMask;

    private Boolean autoDeskDns;

    private String deskDnsPrimary;

    private String deskSecondDnsPrimary;

    private String terminalId;

    private String terminalName;

    private String terminalGroup;

    private String terminalPlatform;

    private String terminalIp;

    private String terminalMac;

    private String terminalMask;

    private Date deleteTime;

    private Boolean isDelete;

    private UUID imageTemplateId;

    private UUID networkId;

    private Date latestLoginTime;


    /**
     * 最后启动时间
     */
    private Date latestRunningTime;

    /**
     * 保存编辑前的gt版本号
     */
    private String beforeEditGuestToolVersion;


    // 是否激活
    private Boolean isWindowsOsActive;

    private UUID physicalServerId;

    private String serverName;

    private String physicalServerIp;

    private String desktopRole;

    private Boolean faultState;

    private String faultDescription;

    // 用户描述
    private String userDescription;

    private Date faultTime;

    private Date createTime;

    private String computerName;

    @Enumerated(EnumType.STRING)
    private CbbNetworkAccessModeEnums networkAccessMode;

    private String wirelessIp;

    private String wirelessMacAddr;

    private Boolean autoWirelessDhcp;

    private String wirelessGateway;

    private String wirelessMask;

    private Boolean autoWirelessDns;

    private String wirelessDnsPrimary;

    private String wirelessSecondDnsPrimary;

    private Boolean enableProxy;

    /**
     * 桌面模式: IDV/VDI
     */
    private String desktopCategory;

    private UUID desktopStrategyId;

    /**
     * 是否需要刷新云桌面策略
     * false不需要
     */
    private Boolean needRefreshStrategy;

    private Boolean hasAutoJoinDomain;

    private String cbbImageType;

    private VgpuType vgpuType;

    private String vgpuModel;

    /**
     * 是否开协议代理
     */
    private Boolean enableAgreementAgency;

    /**
     * 是否开启网页客户端接入
     */
    private Boolean enableWebClient;

    /**
     * 云桌面标签
     */
    private String remark;

    /**
     * 是否独立配置的规格
     */
    private Boolean enableCustom;

    /**
     * 桌面创建方式
     */
    private String deskCreateMode;

    /**
     ** 云桌面镜像类型（win7、 win10...）
     */
    private CbbOsType desktopImageType;

    /**
     * 软件策略名称
     */
    private String softwareStrategyName;

    /**
     * 软件策略ID
     */
    private UUID softwareStrategyId;

    /**
     * 下载状态
     */
    private DownloadStateEnum downloadState;

    /**
     * 下载的错误码
     */
    private Integer failCode;

    /**
     * 下载结果提示语
     */
    private String downloadPromptMessage;

    /**
     * 下载时间
     */
    private Date downloadFinishTime;

    /*
     * 用户个人盘
     */
    private List<CbbAddExtraDiskDTO> extraDiskList;

    /**
     * 是否开启系统盘自动扩容
     */
    private Boolean enableFullSystemDisk;

    /**
     * 桌面池类型
     **/
    private String desktopPoolType;

    /**
     * 桌面池id
     **/
    private UUID desktopPoolId;

    /**
     * 池桌面策略是否失败
     **/
    private Boolean poolStrategyError;

    /**
     * 池独有的属性，是否打开维护模式
     */
    private Boolean isOpenMaintenance = false;

    /**
     * 云桌面管理属性，是否打开云桌面维护模式
     */
    private Boolean isOpenDeskMaintenance = false;

    private Date connectClosedTime;

    private UUID userProfileStrategyId;

    private String userProfileStrategyName;

    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 网络策略名称
     */
    private String networkName;

    /**
     * 镜像用途：桌面镜像/应用镜像
     */
    @Enumerated(EnumType.STRING)
    private ImageUsageTypeEnum imageUsage;

    /**
     * 云桌面修复模式状态
     */
    private String repairState;

    /**
     * 云桌面运行位置
     */
    private UUID clusterId;

    /**
     * 计算集群名称
     */
    private String clusterName;

    /**
     *
     */
    private String idvTerminalMode;

    /**
     * 云桌面关联的镜像的磁盘信息，指镜像的系统盘和数据D盘
     */
    @Nullable
    private List<CbbImageDiskInfoDTO> imageDiskInfoDTOList;

    private UUID imageId;

    private UUID rootImageId;

    private String rootImageName;

    private ImageRoleType imageRoleType;

    private UUID willApplyImageId;

    private Boolean hasAppDisk = false;

    private Boolean hasAppDiskTest = false;

    private Boolean disabled = false;


    /**
     * GT 状态
     */
    private CbbGtAgentState gtAgentState;

    /**
     * 用户状态
     */
    private IacUserStateEnum userState;

    /**
     * 用户账户过期时间
     */
    private String userAccountExpireDate;

    /**
     * 失效恢复时间
     */
    private Date userInvalidRecoverTime;

    /**
     * 用户失效天数
     */
    private Integer userInvalidTime;

    /**
     * 用户是否失效
     */
    private Boolean userInvalid;

    /**
     * 桌面池名称
     */
    private String desktopPoolName;

    /**
     * 是否在桌面右下角展示密码
     */
    private Boolean showRootPwd;

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
    private CloudPlatformType platformType;

    /**
     * 云平台状态
     */
    private CloudPlatformStatus platformStatus;

    /**
     * 云平台唯一标识
     */
    private String cloudPlatformId;

    /**
     * oa版本号
     */
    private String oneAgentVersion;

    @Nullable
    private CbbDeskRegisterState registerState;

    @Nullable
    private String registerMessage;

    @Nullable
    private Boolean enableHyperVisorImprove;


    /**
     * 设置当前网络信息为无线信息
     */
    public void setWirelessInfoToCurrent() {
        this.desktopIp = wirelessIp;
        this.deskDnsPrimary = wirelessDnsPrimary;
        this.deskGateway = wirelessGateway;
        this.desktopMac = wirelessMacAddr;
        this.deskSecondDnsPrimary = wirelessSecondDnsPrimary;
        this.deskMask = wirelessMask;
        this.autoDeskDhcp = autoWirelessDhcp;
        this.autoDeskDns = autoWirelessDns;
    }

    /**
     * 获取应用主机的Windows-server授权是否过期, true说明过期
     * @return 是否授权过期
     */
    public Boolean getIsExpireByTime() {
        if (Objects.isNull(this.isExpire)) {
            return false;
        }
        return this.isExpire;
    }

    public String getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(String cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public Date getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(Date faultTime) {
        this.faultTime = faultTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCbbId() {
        return cbbId;
    }

    public void setCbbId(UUID cbbId) {
        this.cbbId = cbbId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(String desktopState) {
        this.desktopState = desktopState;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
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

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getTerminalGroup() {
        return terminalGroup;
    }

    public void setTerminalGroup(String terminalGroup) {
        this.terminalGroup = terminalGroup;
    }

    public String getTerminalPlatform() {
        return terminalPlatform;
    }

    public void setTerminalPlatform(String terminalPlatform) {
        this.terminalPlatform = terminalPlatform;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getPersonDisk() {
        return personDisk;
    }

    public void setPersonDisk(Integer personDisk) {
        this.personDisk = personDisk;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Date getDeleteTime() {

        return deleteTime;
    }

    public Date getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Date latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public Boolean getIsWindowsOsActive() {
        return isWindowsOsActive;
    }

    public void setIsWindowsOsActive(Boolean isWindowsOsActive) {
        this.isWindowsOsActive = isWindowsOsActive;
    }

    public Boolean getWindowsOsActive() {
        return this.getIsWindowsOsActive();
    }

    public void setWindowsOsActive(Boolean windowsOsActive) {
        isWindowsOsActive = windowsOsActive;
    }

    public UUID getPhysicalServerId() {
        return physicalServerId;
    }

    public void setPhysicalServerId(UUID physicalServerId) {
        this.physicalServerId = physicalServerId;
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


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
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

    public String getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(String desktopCategory) {
        this.desktopCategory = desktopCategory;
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

    public CbbNetworkAccessModeEnums getNetworkAccessMode() {
        return networkAccessMode;
    }

    public void setNetworkAccessMode(CbbNetworkAccessModeEnums networkAccessMode) {
        this.networkAccessMode = networkAccessMode;
    }

    public String getWirelessIp() {
        return wirelessIp;
    }

    public void setWirelessIp(String wirelessIp) {
        this.wirelessIp = wirelessIp;
    }

    public String getWirelessMacAddr() {
        return wirelessMacAddr;
    }

    public void setWirelessMacAddr(String wirelessMacAddr) {
        this.wirelessMacAddr = wirelessMacAddr;
    }

    public Boolean getAutoDeskDhcp() {
        return autoDeskDhcp;
    }

    public void setAutoDeskDhcp(Boolean autoDeskDhcp) {
        this.autoDeskDhcp = autoDeskDhcp;
    }

    public String getDeskGateway() {
        return deskGateway;
    }

    public void setDeskGateway(String deskGateway) {
        this.deskGateway = deskGateway;
    }

    public String getDeskMask() {
        return deskMask;
    }

    public void setDeskMask(String deskMask) {
        this.deskMask = deskMask;
    }

    public Boolean getAutoDeskDns() {
        return autoDeskDns;
    }

    public void setAutoDeskDns(Boolean autoDeskDns) {
        this.autoDeskDns = autoDeskDns;
    }

    public String getDeskDnsPrimary() {
        return deskDnsPrimary;
    }

    public void setDeskDnsPrimary(String deskDnsPrimary) {
        this.deskDnsPrimary = deskDnsPrimary;
    }

    public String getDeskSecondDnsPrimary() {
        return deskSecondDnsPrimary;
    }

    public void setDeskSecondDnsPrimary(String deskSecondDnsPrimary) {
        this.deskSecondDnsPrimary = deskSecondDnsPrimary;
    }

    public Boolean getAutoWirelessDhcp() {
        return autoWirelessDhcp;
    }

    public void setAutoWirelessDhcp(Boolean autoWirelessDhcp) {
        this.autoWirelessDhcp = autoWirelessDhcp;
    }

    public String getWirelessGateway() {
        return wirelessGateway;
    }

    public void setWirelessGateway(String wirelessGateway) {
        this.wirelessGateway = wirelessGateway;
    }

    public String getWirelessMask() {
        return wirelessMask;
    }

    public void setWirelessMask(String wirelessMask) {
        this.wirelessMask = wirelessMask;
    }

    public Boolean getAutoWirelessDns() {
        return autoWirelessDns;
    }

    public void setAutoWirelessDns(Boolean autoWirelessDns) {
        this.autoWirelessDns = autoWirelessDns;
    }

    public String getWirelessDnsPrimary() {
        return wirelessDnsPrimary;
    }

    public void setWirelessDnsPrimary(String wirelessDnsPrimary) {
        this.wirelessDnsPrimary = wirelessDnsPrimary;
    }

    public String getWirelessSecondDnsPrimary() {
        return wirelessSecondDnsPrimary;
    }

    public void setWirelessSecondDnsPrimary(String wirelessSecondDnsPrimary) {
        this.wirelessSecondDnsPrimary = wirelessSecondDnsPrimary;
    }

    public Boolean isEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public UUID getDesktopStrategyId() {
        return desktopStrategyId;
    }

    public void setDesktopStrategyId(UUID desktopStrategyId) {
        this.desktopStrategyId = desktopStrategyId;
    }

    public Boolean isNeedRefreshStrategy() {
        return needRefreshStrategy;
    }

    public void setNeedRefreshStrategy(Boolean needRefreshStrategy) {
        this.needRefreshStrategy = needRefreshStrategy;
    }

    public Boolean isHasAutoJoinDomain() {
        return hasAutoJoinDomain;
    }

    public void setHasAutoJoinDomain(Boolean hasAutoJoinDomain) {
        this.hasAutoJoinDomain = hasAutoJoinDomain;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(String vgpuModel) {
        this.vgpuModel = vgpuModel;
    }

    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
    }

    public Boolean getEnableWebClient() {
        return enableWebClient;
    }

    public void setEnableWebClient(Boolean enableWebClient) {
        this.enableWebClient = enableWebClient;
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

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public CbbOsType getDesktopImageType() {
        return desktopImageType;
    }

    public void setDesktopImageType(CbbOsType desktopImageType) {
        this.desktopImageType = desktopImageType;
    }

    public String getSoftwareStrategyName() {
        return softwareStrategyName;
    }

    public void setSoftwareStrategyName(String softwareStrategyName) {
        this.softwareStrategyName = softwareStrategyName;
    }

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public DownloadStateEnum getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(DownloadStateEnum downloadState) {
        this.downloadState = downloadState;
    }

    public String getDownloadPromptMessage() {
        return downloadPromptMessage;
    }

    public void setDownloadPromptMessage(String downloadPromptMessage) {
        this.downloadPromptMessage = downloadPromptMessage;
    }

    public Date getDownloadFinishTime() {
        return downloadFinishTime;
    }

    public void setDownloadFinishTime(Date downloadFinishTime) {
        this.downloadFinishTime = downloadFinishTime;
    }

    public Integer getFailCode() {
        return failCode;
    }

    public void setFailCode(Integer failCode) {
        this.failCode = failCode;
    }

    public List<CbbAddExtraDiskDTO> getExtraDiskList() {
        return extraDiskList;
    }

    public void setExtraDiskList(List<CbbAddExtraDiskDTO> extraDiskList) {
        this.extraDiskList = extraDiskList;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public String getTerminalMask() {
        return terminalMask;
    }

    public void setTerminalMask(String terminalMask) {
        this.terminalMask = terminalMask;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(String desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public Boolean getPoolStrategyError() {
        return poolStrategyError;
    }

    public void setPoolStrategyError(Boolean poolStrategyError) {
        this.poolStrategyError = poolStrategyError;
    }

    public Boolean getIsOpenMaintenance() {
        return isOpenMaintenance;
    }

    public void setIsOpenMaintenance(Boolean isOpenMaintenance) {
        this.isOpenMaintenance = isOpenMaintenance;
    }

    public Date getConnectClosedTime() {
        return connectClosedTime;
    }

    public void setConnectClosedTime(Date connectClosedTime) {
        this.connectClosedTime = connectClosedTime;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public String getUserProfileStrategyName() {
        return userProfileStrategyName;
    }

    public void setUserProfileStrategyName(String userProfileStrategyName) {
        this.userProfileStrategyName = userProfileStrategyName;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Boolean getIsOpenDeskMaintenance() {
        return isOpenDeskMaintenance;
    }

    public void setIsOpenDeskMaintenance(Boolean isOpenDeskMaintenance) {
        this.isOpenDeskMaintenance = isOpenDeskMaintenance;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(String idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    @Nullable
    public List<CbbImageDiskInfoDTO> getImageDiskInfoDTOList() {
        return imageDiskInfoDTOList;
    }

    public void setImageDiskInfoDTOList(@Nullable List<CbbImageDiskInfoDTO> imageDiskInfoDTOList) {
        this.imageDiskInfoDTOList = imageDiskInfoDTOList;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public Boolean getHasAppDisk() {
        return hasAppDisk;
    }

    public void setHasAppDisk(Boolean hasAppDisk) {
        this.hasAppDisk = hasAppDisk;
    }

    public Boolean getHasAppDiskTest() {
        return hasAppDiskTest;
    }

    public void setHasAppDiskTest(Boolean hasAppDiskTest) {
        this.hasAppDiskTest = hasAppDiskTest;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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

    public CbbGtAgentState getGtAgentState() {
        return gtAgentState;
    }

    public void setGtAgentState(CbbGtAgentState gtAgentState) {
        this.gtAgentState = gtAgentState;
    }

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(IacUserStateEnum userState) {
        this.userState = userState;
    }

    public String getUserAccountExpireDate() {
        return userAccountExpireDate;
    }

    public void setUserAccountExpireDate(String userAccountExpireDate) {
        this.userAccountExpireDate = userAccountExpireDate;
    }

    public Date getUserInvalidRecoverTime() {
        return userInvalidRecoverTime;
    }

    public void setUserInvalidRecoverTime(Date userInvalidRecoverTime) {
        this.userInvalidRecoverTime = userInvalidRecoverTime;
    }

    public Integer getUserInvalidTime() {
        return userInvalidTime;
    }

    public void setUserInvalidTime(Integer userInvalidTime) {
        this.userInvalidTime = userInvalidTime;
    }

    public Boolean getUserInvalid() {
        return userInvalid;
    }

    public void setUserInvalid(Boolean userInvalid) {
        this.userInvalid = userInvalid;
    }

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
    }

    public Boolean getShowRootPwd() {
        return showRootPwd;
    }

    public void setShowRootPwd(Boolean showRootPwd) {
        this.showRootPwd = showRootPwd;
    }

    public UUID getRcaHostId() {
        return rcaHostId;
    }

    public void setRcaHostId(UUID rcaHostId) {
        this.rcaHostId = rcaHostId;
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

    public Integer getRcaMaxSessionCount() {
        return rcaMaxSessionCount;
    }

    public void setRcaMaxSessionCount(Integer rcaMaxSessionCount) {
        this.rcaMaxSessionCount = rcaMaxSessionCount;
    }

    public Integer getRcaSessionUsage() {
        return rcaSessionUsage;
    }

    public void setRcaSessionUsage(Integer rcaSessionUsage) {
        this.rcaSessionUsage = rcaSessionUsage;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public UUID getRcaPeripheralStrategyId() {
        return rcaPeripheralStrategyId;
    }

    public void setRcaPeripheralStrategyId(UUID rcaPeripheralStrategyId) {
        this.rcaPeripheralStrategyId = rcaPeripheralStrategyId;
    }

    public String getRcaPeripheralStrategyName() {
        return rcaPeripheralStrategyName;
    }

    public void setRcaPeripheralStrategyName(String rcaPeripheralStrategyName) {
        this.rcaPeripheralStrategyName = rcaPeripheralStrategyName;
    }

    public IacUserDetailDTO getRcaUserDetailDTO() {
        return rcaUserDetailDTO;
    }

    public void setRcaUserDetailDTO(IacUserDetailDTO rcaUserDetailDTO) {
        this.rcaUserDetailDTO = rcaUserDetailDTO;
    }

    public String getDesktopImageName() {
        return desktopImageName;
    }

    public void setDesktopImageName(String desktopImageName) {
        this.desktopImageName = desktopImageName;
    }

    public String getDesktopNetworkName() {
        return desktopNetworkName;
    }

    public void setDesktopNetworkName(String desktopNetworkName) {
        this.desktopNetworkName = desktopNetworkName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Boolean getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(Boolean expire) {
        isExpire = expire;
    }


    public Integer getRcaDaysLeft() {
        return rcaDaysLeft;
    }

    public void setRcaDaysLeft(Integer rcaDaysLeft) {
        this.rcaDaysLeft = rcaDaysLeft;
    }

    public Boolean getExpire() {
        return isExpire;
    }

    public void setExpire(Boolean expire) {
        isExpire = expire;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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

    public String getCloudPlatformId() {
        return cloudPlatformId;
    }

    public void setCloudPlatformId(String cloudPlatformId) {
        this.cloudPlatformId = cloudPlatformId;
    }

    public String getOneAgentVersion() {
        return oneAgentVersion;
    }

    public void setOneAgentVersion(String oneAgentVersion) {
        this.oneAgentVersion = oneAgentVersion;
    }

    public Boolean getDifferentImageWithRcaPool() {
        return isDifferentImageWithRcaPool;
    }

    public void setDifferentImageWithRcaPool(Boolean sameImageWithRcaPool) {
        isDifferentImageWithRcaPool = sameImageWithRcaPool;
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

    @Nullable
    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(@Nullable Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    public String getPoolImageTemplateName() {
        return poolImageTemplateName;
    }

    public void setPoolImageTemplateName(String poolImageTemplateName) {
        this.poolImageTemplateName = poolImageTemplateName;
    }

    public String getPoolRootImageName() {
        return poolRootImageName;
    }

    public void setPoolRootImageName(String poolRootImageName) {
        this.poolRootImageName = poolRootImageName;
    }
}
