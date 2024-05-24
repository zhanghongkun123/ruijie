package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 云桌面详细页面DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public class CloudDesktopDetailDTO {

    private UUID id;

    private String desktopName;

    /**
     ** 云桌面状态（在线、离线、休眠、重启中、关闭中、重启中）
     */
    private String desktopState;

    /**
     ** 云桌面类型（个人、还原、应用分层）
     */
    private String desktopType;

    /**
     * 会话类型
     */
    @Enumerated(EnumType.STRING)
    private CbbDesktopSessionType sessionType;

    /**
     * 云桌面类型（IDV、VDI）
     */
    private String deskType;

    /**
     * 云桌面模式（CbbCloudDeskPattern）
     */
    private String desktopCategory;

    private Double memory;

    private Integer cpu;

    private Integer systemDisk;

    private Integer personDisk;

    private List<CbbAddExtraDiskDTO> extraDiskList;

    private String desktopIp;

    private String desktopMac;

    private UUID desktopImageId;

    private String desktopImageName;

    private UUID rootImageId;

    private String rootImageName;

    private ImageRoleType imageRoleType;

    /**
     * 镜像类型 IDV/VOI/VDI
     */
    private String cbbImageType;

    /**
     ** 云桌面镜像类型（win7、 win10...）
     */
    private CbbOsType desktopImageType;

    private UUID desktopStrategyId;

    private String desktopStrategyName;

    private UUID desktopNetworkId;

    private String desktopNetworkName;

    private UUID userId;

    private String userName;

    private String userRealName;

    private UUID userGroupId;

    private String userGroupName;

    private String[] userGroupNameArr;

    private String terminalId;

    private String terminalName;

    private String terminalGroupName;

    private String[] terminalGroupNameArr;

    /**
     * 终端类型（IDV、VDI）
     */
    private String terminalPlatform;

    private String terminalIp;

    private String terminalMask;

    /**
     * 创建时间
     */
    private Date createTime;

    private String userType;

    private String configIp;

    private Date latestLoginTime;

    private Date userCreateTime;

    private Date lastOnlineTime;

    /** 是否激活 */
    private Boolean isWindowsOsActive;

    private Boolean osActiveBySystem;

    private String terminalMac;

    private DesktopRole desktopRole;

    private String serverName;

    private String physicalServerIp;

    /**
     * IDV终端模式:IdvTerminalModeEnums
     * 绑定终端、公用终端
     */
    private String idvTerminalModel;

    private String computerName;

    @Enumerated(EnumType.STRING)
    private CbbNetworkAccessModeEnums networkAccessMode;

    private String wirelessIp;

    private String wirelessMacAddr;

    /**
     * 是否独立配置的规格
     */
    private Boolean enableCustom;

    /**
     * 云桌面标签
     */
    private String remark;

    /**
     * 云桌面创建方式
     */
    private String deskCreateMode;

    private ClusterInfoDTO clusterInfo;

    private UUID desktopSoftwareStrategyId;

    private String desktopSoftwareStrategyName;


    private String vgpuItem;

    private String vgpuModel;

    private VgpuType vgpuType;

    /**
     * 镜像下载状态
     */
    private DownloadStateEnum downloadState;

    /**
     * 下载的错误码
     */
    private Integer failCode;

    /**
     * 镜像下载结果提示语
     */
    private String downloadPromptMessage;

    /**
     * 镜像下载时间
     */
    private Date downloadFinishTime;

    /**
     * 是否开协议代理
     */
    private Boolean enableAgreementAgency;

    /**
     * 是否开启网页客户端接入
     **/
    private Boolean enableWebClient;

    /**
     *  桌面池类型
     **/
    private String desktopPoolType;

    /**
     * 桌面池id
     **/
    private UUID desktopPoolId;

    private UUID userProfileStrategyId;

    private String userProfileStrategyName;

    /**
     * 云桌面维护模式
     */
    private Boolean isOpenDeskMaintenance;

    /**
     * 计算集群名称
     */
    private String clusterName;

    /**
     * 关联交付组名称
     */
    private String deliveryGroupName;

    /**
     * 关联交付组的应用名称
     */
    private String deliveryGroupAppArrName;


    private List<CbbImageDiskInfoDTO> imageDiskList;

    private String osType;

    private String osVersion;

    /**
     * 关联的云桌面策略是否开启水印，云桌面策略未开启水印时使用全局策略配置的水印
     */
    private Boolean strategyEnableWatermark;

    /**
     * 云桌面临时权限名称
     */
    private String desktopTempPermissionName;

    /**
     * 桌面登录账号同步
     */
    @Nullable
    private Boolean desktopSyncLoginAccount;

    /**
     * 桌面登录密码同步
     */
    @Nullable
    private Boolean desktopSyncLoginPassword;

    /**
     * 桌面登录账号权限
     */
    @Nullable
    private CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission;

    /**
     * 是否开启高可用
     */
    private Boolean enableHa;

    /**
     * "配置HA优先级"
     */
    private Integer haPriority;

    private String desktopPoolName;

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

    @Enumerated(EnumType.STRING)
    private CbbEstProtocolType estProtocolType;

    private ImageUsageTypeEnum imageUsage;

    private Boolean enableForceUseAgreementAgency;

    private String strategyType;

    private String systemDiskStoragePoolName;

    private PlatformStoragePoolDTO systemDiskStoragePool;

    private String personDiskStoragePoolName;

    private PlatformStoragePoolDTO personDiskStoragePool;

    private CbbDeskRegisterState registerState;

    private String registerMessage;

    private String guestToolVersion;

    /**
     * 桌面详情转列表数据
     *
     * @return CloudDesktopDTO 详情转列表
     */
    public CloudDesktopDTO convertMinCloudDesktopDTO() {

        CloudDesktopDTO dto = new CloudDesktopDTO();
        dto.setId(this.id);
        dto.setCbbId(this.id);
        dto.setDesktopName(this.getDesktopName());

        dto.setUserId(this.getUserId());
        dto.setUserGroupId(this.getUserGroupId());

        dto.setUserName(this.getUserName());
        dto.setUserGroup(this.getUserGroupName());
        dto.setDesktopState(this.desktopState);
        // 前端属性和后端定义不一致，前端desktoptype:个性/还原,而用的是pattern, 前端修订量大，暂时不改
        // desktopCategory --> desktopType
        dto.setDesktopCategory(this.getDesktopType());
        // desktopType ---> pattern
        dto.setDesktopType(this.desktopType);
        dto.setDesktopCategory(this.getDesktopType());
        dto.setCpu(this.getCpu());
        dto.setSessionType(this.sessionType);

        dto.setSystemDisk(this.systemDisk);
        dto.setPersonDisk(this.personDisk);
        dto.setDesktopIp(this.desktopIp);
        dto.setDesktopMac(this.desktopMac);
        dto.setTerminalName(this.terminalName);
        dto.setTerminalGroup(this.getTerminalGroupName());
        dto.setTerminalPlatform(this.getTerminalPlatform());
        dto.setUserType(this.getUserType());
        dto.setTerminalIp(this.getTerminalIp());
        dto.setTerminalId(this.getTerminalId());
        dto.setLatestLoginTime(this.getLatestLoginTime());
        dto.setImageName(this.desktopImageName);
        // 云桌面是否激活，可能存在是否激活信息为空的情况
        Boolean isWindowsOsActive = this.getIsWindowsOsActive() != null ? this.getIsWindowsOsActive() : Boolean.FALSE;
        dto.setIsWindowsOsActive(isWindowsOsActive);
        dto.setOsActiveBySystem(Optional.ofNullable(this.getOsActiveBySystem()).orElse(Boolean.FALSE));
        dto.setPhysicalServerIp(this.getPhysicalServerIp());
        dto.setCreateTime(this.getCreateTime());
        dto.setComputerName(this.getComputerName());
        dto.setEnableWebClient(this.getEnableWebClient());
        // 新增镜像类型区分
        dto.setCbbImageType(this.getCbbImageType());
        dto.setVgpuType(this.getVgpuType());

        // 桌面标签
        dto.setRemark(this.getRemark());
        // 软件策略配置
        // 用户配置策略
        dto.setUserProfileStrategyId(this.getUserProfileStrategyId());
        dto.setUserProfileStrategyName(this.getUserProfileStrategyName());

        dto.setEnableCustom(this.getEnableCustom());
        dto.setDeskCreateMode(this.getDeskCreateMode());

        dto.setDownloadState(this.getDownloadState());
        dto.setFailCode(this.getFailCode());
        dto.setDownloadFinishTime(this.getDownloadFinishTime());
        dto.setDownloadPromptMessage(DownloadStateEnum.getDownloadPromptMessage(this.getFailCode()));
        dto.setTerminalMask(this.getTerminalMask());


        dto.setDesktopPoolType(this.getDesktopPoolType());
        dto.setDesktopPoolId(this.getDesktopPoolId());

        dto.setIdvTerminalMode(this.getIdvTerminalModel());
        dto.setTerminalId(this.getTerminalId());

        dto.setIsOpenDeskMaintenance(this.getIsOpenDeskMaintenance());
        dto.setRootImageId(this.getRootImageId());
        dto.setRootImageName(this.getRootImageName());
        dto.setImageRoleType(this.getImageRoleType());
        dto.setDesktopPoolName(this.getDesktopPoolName());
        // 云平台相关
        dto.setPlatformId(this.getPlatformId());
        dto.setPlatformName(this.getPlatformName());
        dto.setPlatformStatus(this.getPlatformStatus());
        dto.setPlatformType(this.getPlatformType());
        dto.setCloudPlatformId(this.getCloudPlatformId());
        return dto;
    }



    /**
     * 是否在桌面右下角展示密码
     */
    private Boolean showRootPwd;

    public List<CbbImageDiskInfoDTO> getImageDiskList() {
        return imageDiskList;
    }

    public void setImageDiskList(List<CbbImageDiskInfoDTO> imageDiskList) {
        this.imageDiskList = imageDiskList;
    }

    public String getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(String cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getPhysicalServerIp() {
        return physicalServerIp;
    }

    public void setPhysicalServerIp(String physicalServerIp) {
        this.physicalServerIp = physicalServerIp;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public DesktopRole getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(DesktopRole desktopRole) {
        this.desktopRole = desktopRole;
    }

    /**
     ** getID
     *
     * @return uuid
     */
    public UUID getId() {
        return id;
    }

    /**
     * * setId
     *
     * @param id uuid
     */
    public void setId(UUID id) {
        this.id = id;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
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

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
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

    public UUID getDesktopImageId() {
        return desktopImageId;
    }

    public void setDesktopImageId(UUID desktopImageId) {
        this.desktopImageId = desktopImageId;
    }

    public String getDesktopImageName() {
        return desktopImageName;
    }

    public void setDesktopImageName(String desktopImageName) {
        this.desktopImageName = desktopImageName;
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

    public CbbOsType getDesktopImageType() {
        return desktopImageType;
    }

    public void setDesktopImageType(CbbOsType desktopImageType) {
        this.desktopImageType = desktopImageType;
    }

    public UUID getDesktopStrategyId() {
        return desktopStrategyId;
    }

    public void setDesktopStrategyId(UUID desktopStrategyId) {
        this.desktopStrategyId = desktopStrategyId;
    }

    public String getDesktopStrategyName() {
        return desktopStrategyName;
    }

    public void setDesktopStrategyName(String desktopStrategyName) {
        this.desktopStrategyName = desktopStrategyName;
    }

    public UUID getDesktopNetworkId() {
        return desktopNetworkId;
    }

    public void setDesktopNetworkId(UUID desktopNetworkId) {
        this.desktopNetworkId = desktopNetworkId;
    }

    public String getDesktopNetworkName() {
        return desktopNetworkName;
    }

    public void setDesktopNetworkName(String desktopNetworkName) {
        this.desktopNetworkName = desktopNetworkName;
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

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
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

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
    }

    public String getTerminalPlatform() {
        return terminalPlatform;
    }

    public void setTerminalPlatform(String terminalPlatform) {
        this.terminalPlatform = terminalPlatform;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getConfigIp() {
        return configIp;
    }

    public void setConfigIp(String configIp) {
        this.configIp = configIp;
    }

    public String[] getUserGroupNameArr() {
        return userGroupNameArr;
    }

    public void setUserGroupNameArr(String[] userGroupNameArr) {
        this.userGroupNameArr = userGroupNameArr;
    }

    public String[] getTerminalGroupNameArr() {
        return terminalGroupNameArr;
    }

    public void setTerminalGroupNameArr(String[] terminalGroupNameArr) {
        this.terminalGroupNameArr = terminalGroupNameArr;
    }

    public Date getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Date latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public Date getUserCreateTime() {
        return userCreateTime;
    }

    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
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

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(String desktopCategory) {
        this.desktopCategory = desktopCategory;
    }

    public String getIdvTerminalModel() {
        return idvTerminalModel;
    }

    public void setIdvTerminalModel(String idvTerminalModel) {
        this.idvTerminalModel = idvTerminalModel;
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

    /**
     * 设置当前网络为无线信息
     */
    public void setWirelessNetworkToCurrent() {
        this.desktopIp = this.wirelessIp;
        this.desktopMac = this.wirelessMacAddr;
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

    public ClusterInfoDTO getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfoDTO clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public String getVgpuItem() {
        return vgpuItem;
    }

    public void setVgpuItem(String vgpuItem) {
        this.vgpuItem = vgpuItem;
    }

    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(String vgpuModel) {
        this.vgpuModel = vgpuModel;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public UUID getDesktopSoftwareStrategyId() {
        return desktopSoftwareStrategyId;
    }

    public void setDesktopSoftwareStrategyId(UUID desktopSoftwareStrategyId) {
        this.desktopSoftwareStrategyId = desktopSoftwareStrategyId;
    }

    public String getDesktopSoftwareStrategyName() {
        return desktopSoftwareStrategyName;
    }

    public void setDesktopSoftwareStrategyName(String desktopSoftwareStrategyName) {
        this.desktopSoftwareStrategyName = desktopSoftwareStrategyName;
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

    public List<CbbAddExtraDiskDTO> getExtraDiskList() {
        return extraDiskList;
    }

    public void setExtraDiskList(List<CbbAddExtraDiskDTO> extraDiskList) {
        this.extraDiskList = extraDiskList;
    }

    public String getTerminalMask() {
        return terminalMask;
    }

    public void setTerminalMask(String terminalMask) {
        this.terminalMask = terminalMask;
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

    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    public String getDeliveryGroupAppArrName() {
        return deliveryGroupAppArrName;
    }

    public void setDeliveryGroupAppArrName(String deliveryGroupAppArrName) {
        this.deliveryGroupAppArrName = deliveryGroupAppArrName;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Boolean getStrategyEnableWatermark() {
        return strategyEnableWatermark;
    }

    public void setStrategyEnableWatermark(Boolean strategyEnableWatermark) {
        this.strategyEnableWatermark = strategyEnableWatermark;
    }

    public String getDesktopTempPermissionName() {
        return desktopTempPermissionName;
    }

    public void setDesktopTempPermissionName(String desktopTempPermissionName) {
        this.desktopTempPermissionName = desktopTempPermissionName;
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

    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public Boolean getEnableForceUseAgreementAgency() {
        return enableForceUseAgreementAgency;
    }

    public void setEnableForceUseAgreementAgency(Boolean enableForceUseAgreementAgency) {
        this.enableForceUseAgreementAgency = enableForceUseAgreementAgency;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public String getSystemDiskStoragePoolName() {
        return systemDiskStoragePoolName;
    }

    public void setSystemDiskStoragePoolName(String systemDiskStoragePoolName) {
        this.systemDiskStoragePoolName = systemDiskStoragePoolName;
    }

    public PlatformStoragePoolDTO getSystemDiskStoragePool() {
        return systemDiskStoragePool;
    }

    public void setSystemDiskStoragePool(PlatformStoragePoolDTO systemDiskStoragePool) {
        this.systemDiskStoragePool = systemDiskStoragePool;
    }

    public String getPersonDiskStoragePoolName() {
        return personDiskStoragePoolName;
    }

    public void setPersonDiskStoragePoolName(String personDiskStoragePoolName) {
        this.personDiskStoragePoolName = personDiskStoragePoolName;
    }

    public PlatformStoragePoolDTO getPersonDiskStoragePool() {
        return personDiskStoragePool;
    }

    public void setPersonDiskStoragePool(PlatformStoragePoolDTO personDiskStoragePool) {
        this.personDiskStoragePool = personDiskStoragePool;
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

    public String getGuestToolVersion() {
        return guestToolVersion;
    }

    public void setGuestToolVersion(String guestToolVersion) {
        this.guestToolVersion = guestToolVersion;
    }
}
