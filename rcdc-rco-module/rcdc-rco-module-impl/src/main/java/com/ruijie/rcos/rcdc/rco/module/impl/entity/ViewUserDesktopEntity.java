package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskRegisterState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * 云桌面视图查询对象
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author
 */
@Entity
@Table(name = "v_cbb_user_desktop_detail")
public class ViewUserDesktopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String desktopName;

    private boolean hasLogin;

    private String desktopType;

    @Enumerated(EnumType.STRING)
    private CbbDesktopSessionType sessionType;

    private Date createTime;

    private Date deleteTime;

    private UUID userId;

    private UUID cbbStrategyId;

    private UUID cbbNetworkId;

    private UUID cbbDesktopId;

    private String terminalId;

    private Boolean isDelete;

    private String deskMac;

    private String deskState;// CbbCloudDeskState

    private String deskType;// CbbCloudDeskType

    private UUID imageTemplateId;

    private UUID rootImageId;

    private String rootImageName;

    @Enumerated(EnumType.STRING)
    private ImageRoleType imageRoleType;

    private String cbbImageType;

    /**
     * 保存编辑前的gt版本号
     */
    private String beforeEditGuestToolVersion;

    private String ip;

    private String strategyName;

    private String pattern;// CbbCloudDeskPattern

    private Integer cpu;

    private Integer memory;

    private Integer systemSize;

    private Integer personSize;

    private Boolean enableHyperVisorImprove;

    private UUID userGroupId;

    private String realName;

    private String userType;

    private String userName;

    private String userDescription;

    private String password;

    private String phoneNum;

    private String email;

    private String userGroupName;

    private String userGroupDescription;

    private UUID terminalGroupId;

    private String terminalName;

    private String productType;

    private String terminalPlatform;// VDI DIV

    private String terminalIp;

    private String terminalMask;

    private String terminalGateway;

    private String terminalGroupName;

    private String configIp;

    private Date latestLoginTime;

    /**
     * 最后启动时间
     */
    private Date latestRunningTime;

    private Date userCreateTime;

    private Date lastOnlineTime;

    private String imageTemplateName;

    private String osType;

    private Boolean isWindowsOsActive;

    /**
     * 是否系统自行激活
     */
    private Boolean osActiveBySystem;

    private String terminalMac;

    @Version
    private Integer version;

    private Boolean hasTerminalRunning;

    private UUID physicalServerId;

    private String physicalServerIp;

    private String desktopRole;

    private String hostName;

    private Boolean faultState;

    private String faultDescription;

    private Date faultTime;

    /**
     * IDV终端模式:IdvTerminalModeEnums
     * 绑定终端、公用终端
     */
    private String idvTerminalModel;

    private String computerName;

    private Boolean enableProxy;

    @Enumerated(EnumType.STRING)
    private VgpuType vgpuType;

    private String vgpuExtraInfo;

    /**
     * 云桌面标签
     */
    private String remark;

    /**
     * 是否独立配置的规格
     */
    private Boolean enableCustom;

    private String deskCreateMode;

    /**
     * 是否加入AD域
     */
    private Boolean hasAutoJoinDomain;

    /**
     * 桌面池类型
     **/
    private String desktopPoolType;


    /**
     * 桌面池id
     **/
    private UUID desktopPoolId;

    private UUID userProfileStrategyId;

    private String userProfileStrategyName;

    /**
     * 软件策略名称
     */
    private String softwareStrategyName;

    /**
     * 软件策略ID
     */
    private UUID softwareStrategyId;

    /**
     * 镜像下载状态
     */
    @Enumerated(EnumType.STRING)
    private DownloadStateEnum downloadState;

    /**
     * 下载的错误码
     */
    private Integer failCode;

    /**
     * 镜像下载时间
     */
    private Date downloadFinishTime;

    /**
     * 是否开启网页客户端接入
     **/
    private Boolean enableWebClient;

    /**
     * 是否开启系统盘自动扩容
     */
    private Boolean enableFullSystemDisk;

    /**
     * 连接关闭时间
     */
    private Date connectClosedTime;

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
     * 云桌面运行位置
     */
    private UUID clusterId;

    /**
     * 云桌面维护模式
     */
    private Boolean isOpenDeskMaintenance;

    private String osVersion;

    /**
     * 关联的云桌面策略是否开启水印，云桌面策略未开启水印时使用全局策略配置的水印
     */
    private Boolean strategyEnableWatermark;

    /**
     * 变更镜像后的新镜像ID
     **/
    private UUID willApplyImageId;

    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    private IacUserStateEnum userState;

    /**
     * 用户账户过期时间
     */
    private Long userAccountExpireDate;

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
     * 云平台标识
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

    /**
     * 云平台唯一标识
     */
    private String cloudPlatformId;

    private String guestToolVersion;

    @Enumerated(EnumType.STRING)
    private CbbCpuArchType cpuArch;

    private UUID deskSpecId;

    private Boolean enableAgreementAgency;

    private Boolean enableForceUseAgreementAgency;

    private String strategyType;

    @Enumerated(EnumType.STRING)
    private CbbDeskRegisterState registerState;

    private String registerMessage;

    /**
     * 桌面是否支持客户端升级配置：1支持，0不支持
     */
    private Integer canUpgradeAgent;

    @Enumerated(EnumType.STRING)
    private CbbEstProtocolType estProtocolType;

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getUserGroupDescription() {
        return userGroupDescription;
    }

    public void setUserGroupDescription(String userGroupDescription) {
        this.userGroupDescription = userGroupDescription;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
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

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getTerminalMask() {
        return terminalMask;
    }

    public void setTerminalMask(String terminalMask) {
        this.terminalMask = terminalMask;
    }

    public String getTerminalGateway() {
        return terminalGateway;
    }

    public void setTerminalGateway(String terminalGateway) {
        this.terminalGateway = terminalGateway;
    }

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
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


    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
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

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    /**
     * convert view entity to dto
     *
     * @param entity view entity
     * @return dto
     */
    public static CloudDesktopDTO convertEntityToDTO(ViewUserDesktopEntity entity) {
        CloudDesktopDTO dto = new CloudDesktopDTO();
        dto.setId(entity.getCbbDesktopId());
        dto.setCbbId(entity.getCbbDesktopId());
        dto.setDesktopName(entity.getDesktopName());

        dto.setUserId(entity.getUserId());
        dto.setUserGroupId(entity.getUserGroupId());

        dto.setUserName(entity.getUserName());
        dto.setRealName(entity.getRealName());
        dto.setUserGroup(entity.getUserGroupName());
        dto.setDesktopState(entity.getDeskState());
        // 前端属性和后端定义不一致，前端desktoptype:个性/还原,而用的是pattern, 前端修订量大，暂时不改
        // desktopCategory --> desktopType
        dto.setDesktopCategory(entity.getDesktopType());
        // desktopType ---> pattern
        dto.setDesktopType(entity.getPattern());
        dto.setDesktopCategory(entity.getDesktopType());
        dto.setSessionType(entity.getSessionType());
        dto.setCpu(entity.getCpu());
        if (!ObjectUtils.isEmpty(entity.getMemory())) {
            dto.setMemory(CapacityUnitUtils.mb2GbMaintainOneFraction(entity.getMemory()));
        }
        dto.setSystemDisk(entity.getSystemSize());
        dto.setPersonDisk(entity.getPersonSize());
        dto.setDesktopIp(entity.getIp());
        dto.setDesktopMac(entity.getDeskMac());
        dto.setTerminalName(entity.getTerminalName());
        dto.setTerminalGroup(entity.getTerminalGroupName());
        dto.setTerminalPlatform(entity.getTerminalPlatform());
        dto.setUserType(entity.getUserType());
        dto.setTerminalIp(entity.getTerminalIp());
        dto.setTerminalId(entity.getTerminalId());
        dto.setDeleteTime(entity.getDeleteTime());
        dto.setIsDelete(entity.getIsDelete());
        dto.setLatestLoginTime(entity.getLatestLoginTime());
        //最后启动运行时间
        dto.setLatestRunningTime(entity.getLatestRunningTime());
        //镜像 GT 版本
        dto.setBeforeEditGuestToolVersion(entity.getBeforeEditGuestToolVersion());
        dto.setImageName(entity.getImageTemplateName());
        dto.setOsName(entity.getOsType());
        // 云桌面是否激活，可能存在是否激活信息为空的情况
        Boolean isWindowsOsActive = entity.getIsWindowsOsActive() != null ? entity.getIsWindowsOsActive() : Boolean.FALSE;
        dto.setIsWindowsOsActive(isWindowsOsActive);
        dto.setOsActiveBySystem(Optional.ofNullable(entity.getOsActiveBySystem()).orElse(Boolean.FALSE));
        dto.setPhysicalServerId(entity.getPhysicalServerId());
        dto.setPhysicalServerIp(entity.getPhysicalServerIp());
        dto.setDesktopRole(entity.getDesktopRole());
        dto.setFaultState(entity.isFaultState());
        dto.setFaultDescription(entity.getFaultDescription());
        dto.setFaultTime(entity.getFaultTime());
        dto.setCreateTime(entity.getCreateTime());
        dto.setComputerName(entity.getComputerName());

        dto.setEnableProxy(Optional.ofNullable(entity.getEnableProxy()).orElse(Boolean.FALSE));
        dto.setEnableWebClient(entity.getEnableWebClient());
        // 新增镜像类型区分
        dto.setCbbImageType(entity.getCbbImageType());
        dto.setStrategyType(entity.getStrategyType());
        dto.setDesktopStrategyId(entity.getCbbStrategyId());
        dto.setVgpuType(entity.getVgpuType());
        // 代理协议
        dto.setEnableAgreementAgency(entity.getEnableAgreementAgency());
        // 桌面标签
        dto.setRemark(entity.getRemark());
        // 软件策略配置
        dto.setSoftwareStrategyId(entity.getSoftwareStrategyId());
        dto.setSoftwareStrategyName(entity.getSoftwareStrategyName());

        // 用户配置策略
        dto.setUserProfileStrategyId(entity.getUserProfileStrategyId());
        dto.setUserProfileStrategyName(entity.getUserProfileStrategyName());

        dto.setEnableCustom(entity.getEnableCustom());
        dto.setDeskCreateMode(entity.getDeskCreateMode());
        dto.setHasAutoJoinDomain(Optional.ofNullable(entity.getHasAutoJoinDomain()).orElse(Boolean.FALSE));

        dto.setDownloadState(entity.getDownloadState());
        dto.setFailCode(entity.getFailCode());
        dto.setDownloadFinishTime(entity.getDownloadFinishTime());
        dto.setDownloadPromptMessage(DownloadStateEnum.getDownloadPromptMessage(entity.getFailCode()));
        dto.setEnableFullSystemDisk(entity.getEnableFullSystemDisk());
        dto.setTerminalMask(entity.getTerminalMask());


        dto.setDesktopPoolType(entity.getDesktopPoolType());
        dto.setDesktopPoolId(entity.getDesktopPoolId());
        dto.setConnectClosedTime(entity.getConnectClosedTime());
        dto.setStrategyName(entity.getStrategyName());
        dto.setNetworkName(entity.getNetworkName());
        dto.setImageUsage(entity.getImageUsage());

        dto.setIdvTerminalMode(entity.getIdvTerminalModel());
        dto.setTerminalId(entity.getTerminalId());

        dto.setUserDescription(entity.getUserDescription());
        dto.setIsOpenDeskMaintenance(entity.getIsOpenDeskMaintenance());
        dto.setImageId(entity.getImageTemplateId());
        dto.setRootImageId(entity.getRootImageId());
        dto.setRootImageName(entity.getRootImageName());
        dto.setImageRoleType(entity.getImageRoleType());
        dto.setWillApplyImageId(entity.getWillApplyImageId());
        dto.setUserState(entity.getUserState());
        dto.setUserInvalidRecoverTime(entity.getUserInvalidRecoverTime());
        dto.setUserInvalidTime(entity.getUserInvalidTime());
        dto.setUserInvalid(entity.getUserInvalid());
        dto.setDesktopPoolName(entity.getDesktopPoolName());
        dto.setShowRootPwd(entity.getShowRootPwd());
        // 云平台相关
        dto.setPlatformId(entity.getPlatformId());
        dto.setPlatformName(entity.getPlatformName());
        dto.setPlatformStatus(entity.getPlatformStatus());
        dto.setPlatformType(entity.getPlatformType());
        dto.setCloudPlatformId(entity.getCloudPlatformId());
        dto.setCpuArch(entity.getCpuArch());
        dto.setSessionType(entity.getSessionType());
        dto.setOsVersion(entity.getOsVersion());
        dto.setOsType(entity.getOsType());
        dto.setGuestToolVersion(entity.getGuestToolVersion());
        dto.setRegisterState(entity.getRegisterState());
        dto.setRegisterMessage(entity.getRegisterMessage());
        dto.setCanUpgradeAgent(entity.getCanUpgradeAgent());
        return dto;
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

    public DownloadStateEnum getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(DownloadStateEnum downloadState) {
        this.downloadState = downloadState;
    }

    public Date getDownloadFinishTime() {
        return downloadFinishTime;
    }

    public void setDownloadFinishTime(Date downloadFinishTime) {
        this.downloadFinishTime = downloadFinishTime;
    }

    public Boolean getEnableWebClient() {
        return enableWebClient;
    }

    public void setEnableWebClient(Boolean enableWebClient) {
        this.enableWebClient = enableWebClient;
    }

    public Integer getFailCode() {
        return failCode;
    }

    public void setFailCode(Integer failCode) {
        this.failCode = failCode;
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

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
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

    public Boolean getStrategyEnableWatermark() {
        return strategyEnableWatermark;
    }

    public void setStrategyEnableWatermark(Boolean strategyEnableWatermark) {
        this.strategyEnableWatermark = strategyEnableWatermark;
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

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(IacUserStateEnum userState) {
        this.userState = userState;
    }

    public Long getUserAccountExpireDate() {
        return userAccountExpireDate;
    }

    public void setUserAccountExpireDate(Long userAccountExpireDate) {
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

    public CbbCpuArchType getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(CbbCpuArchType cpuArch) {
        this.cpuArch = cpuArch;
    }

    public UUID getDeskSpecId() {
        return deskSpecId;
    }

    public void setDeskSpecId(UUID deskSpecId) {
        this.deskSpecId = deskSpecId;
    }

    /**
     * 转换枚举
     *
     * @param sessionType 会话类型
     * @return 会话枚举值
     */
    public CbbDesktopSessionType stringToEnum(@Nullable String sessionType) {
        if (sessionType != null) {
            return CbbDesktopSessionType.valueOf(CbbDesktopSessionType.class, sessionType.toUpperCase());
        }
        // 默认单会话
        return CbbDesktopSessionType.SINGLE;
    }

    public boolean isExclusive() {
        return CbbDesktopPoolModel.STATIC.name().equals(getDesktopPoolType()) && CbbDesktopSessionType.SINGLE.equals(getSessionType());
    }

    public String getGuestToolVersion() {
        return guestToolVersion;
    }

    public void setGuestToolVersion(String guestToolVersion) {
        this.guestToolVersion = guestToolVersion;
    }

    public Boolean getEnableForceUseAgreementAgency() {
        return enableForceUseAgreementAgency;
    }

    public void setEnableForceUseAgreementAgency(Boolean enableForceUseAgreementAgency) {
        this.enableForceUseAgreementAgency = enableForceUseAgreementAgency;
    }

    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
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

    public Integer getCanUpgradeAgent() {
        return canUpgradeAgent;
    }

    public void setCanUpgradeAgent(Integer canUpgradeAgent) {
        this.canUpgradeAgent = canUpgradeAgent;
    }

    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
    }
}
