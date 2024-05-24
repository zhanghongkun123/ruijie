package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * Description: 池桌面基础信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年08月16日
 *
 * @author linke
 */
public class DesktopPoolBasicDTO {

    /**
     * 桌面池id
     **/
    private UUID id;

    /**
     * 桌面池名称
     **/
    private String name;

    /**
     * 云桌面名称前缀,为null时采用桌面池名称
     */
    private String desktopNamePrefix;

    /**
     * 池模式
     **/
    private CbbDesktopPoolModel poolModel;

    /**
     * 会话类型
     **/
    @Enumerated(EnumType.STRING)
    private CbbDesktopSessionType sessionType;

    /**
     * 空闲桌面自动回收时间（分钟）
     **/
    private Integer idleDesktopRecover;

    /**
     * 备注
     */
    private String description;

    /**
     * 镜像模板id
     **/
    private UUID imageTemplateId;

    /**
     * 多版本根镜像模板id
     **/
    private UUID rootImageId;

    /**
     * 多版本根镜像模板名称
     **/
    private String rootImageName;

    /**
     * 多版本根镜像模板角色
     **/
    private ImageRoleType imageRoleType;

    /**
     * 云桌面策略id
     **/
    private UUID strategyId;

    /**
     * 网络策略id
     **/
    private UUID networkId;

    /**
     * 桌面池状态
     **/
    private CbbDesktopPoolState poolState;

    /**
     * 桌面数量
     */
    private Integer desktopNum;

    /**
     * 维持预启动数
     */
    private Integer preStartDesktopNum;

    /**
     * 是否开启维护模式
     */
    private Boolean isOpenMaintenance;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

    private String strategyName;

    /**
     * 云桌面类型
     **/
    private CbbCloudDeskPattern desktopType;

    private Double memory;

    private Integer cpu;

    private Integer systemDisk;

    private Integer personDisk;

    private DeskCreateMode deskCreateMode;

    private String networkName;

    private String imageTemplateName;

    private CbbOsType osType;

    private Integer connectedNum;

    private UUID softwareStrategyId;

    private String softwareStrategyName;

    private UUID userProfileStrategyId;

    private String userProfileStrategyName;

    private UUID clusterId;

    private ClusterInfoDTO clusterInfo;

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

    private UUID deskSpecId;

    private UUID systemDiskStoragePoolId;

    private PlatformStoragePoolDTO systemDiskStoragePool;

    private UUID personDiskStoragePoolId;

    private PlatformStoragePoolDTO personDiskStoragePool;

    private Boolean enableHyperVisorImprove;

    private VgpuType vgpuType;

    private VgpuExtraInfo vgpuExtraInfo;

    private List<ExtraDiskDTO> extraDiskList;

    /**
     * 桌面池类型
     **/
    private CbbDesktopPoolType poolType;


    /**
     * 负载均衡计算策略
     */
    private CbbLoadBalanceStrategyEnum loadBalanceStrategy;

    /**
     * CPU利用率上限
     */
    private Integer cpuUsage;

    /**
     * 内存利用率上限
     */
    private Integer memoryUsage;

    /**
     * 系统盘利用率上限
     */
    private Integer systemDiskUsage;


    /**
     * 最大会话数：0~不做限制
     */
    private Integer maxSession;

    private Boolean hasSecondAdd;

    /**
     * 桌面池是否支持客户端升级配置：1支持，0不支持
     */
    private Integer canUpgradeAgent;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesktopNamePrefix() {
        return desktopNamePrefix;
    }

    public void setDesktopNamePrefix(String desktopNamePrefix) {
        this.desktopNamePrefix = desktopNamePrefix;
    }

    public CbbDesktopPoolModel getPoolModel() {
        return poolModel;
    }

    public void setPoolModel(CbbDesktopPoolModel poolModel) {
        this.poolModel = poolModel;
    }

    public Integer getIdleDesktopRecover() {
        return idleDesktopRecover;
    }

    public void setIdleDesktopRecover(Integer idleDesktopRecover) {
        this.idleDesktopRecover = idleDesktopRecover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
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

    public CbbDesktopPoolState getPoolState() {
        return poolState;
    }

    public void setPoolState(CbbDesktopPoolState poolState) {
        this.poolState = poolState;
    }

    public Integer getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(Integer desktopNum) {
        this.desktopNum = desktopNum;
    }

    public Integer getPreStartDesktopNum() {
        return preStartDesktopNum;
    }

    public void setPreStartDesktopNum(Integer preStartDesktopNum) {
        this.preStartDesktopNum = preStartDesktopNum;
    }

    public Boolean getIsOpenMaintenance() {
        return isOpenMaintenance;
    }

    public void setIsOpenMaintenance(Boolean isOpenMaintenance) {
        this.isOpenMaintenance = isOpenMaintenance;
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

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
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

    public Integer getPersonDisk() {
        return personDisk;
    }

    public void setPersonDisk(Integer personDisk) {
        this.personDisk = personDisk;
    }

    public DeskCreateMode getDeskCreateMode() {
        return deskCreateMode;
    }

    public void setDeskCreateMode(DeskCreateMode deskCreateMode) {
        this.deskCreateMode = deskCreateMode;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public Integer getConnectedNum() {
        return connectedNum;
    }

    public void setConnectedNum(Integer connectedNum) {
        this.connectedNum = connectedNum;
    }

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public String getSoftwareStrategyName() {
        return softwareStrategyName;
    }

    public void setSoftwareStrategyName(String softwareStrategyName) {
        this.softwareStrategyName = softwareStrategyName;
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

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public ClusterInfoDTO getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfoDTO clusterInfo) {
        this.clusterInfo = clusterInfo;
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

    public UUID getDeskSpecId() {
        return deskSpecId;
    }

    public void setDeskSpecId(UUID deskSpecId) {
        this.deskSpecId = deskSpecId;
    }

    public UUID getSystemDiskStoragePoolId() {
        return systemDiskStoragePoolId;
    }

    public void setSystemDiskStoragePoolId(UUID systemDiskStoragePoolId) {
        this.systemDiskStoragePoolId = systemDiskStoragePoolId;
    }

    public PlatformStoragePoolDTO getSystemDiskStoragePool() {
        return systemDiskStoragePool;
    }

    public void setSystemDiskStoragePool(PlatformStoragePoolDTO systemDiskStoragePool) {
        this.systemDiskStoragePool = systemDiskStoragePool;
    }

    public UUID getPersonDiskStoragePoolId() {
        return personDiskStoragePoolId;
    }

    public void setPersonDiskStoragePoolId(UUID personDiskStoragePoolId) {
        this.personDiskStoragePoolId = personDiskStoragePoolId;
    }

    public PlatformStoragePoolDTO getPersonDiskStoragePool() {
        return personDiskStoragePool;
    }

    public void setPersonDiskStoragePool(PlatformStoragePoolDTO personDiskStoragePool) {
        this.personDiskStoragePool = personDiskStoragePool;
    }

    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public VgpuExtraInfo getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(VgpuExtraInfo vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    public List<ExtraDiskDTO> getExtraDiskList() {
        return extraDiskList;
    }

    public void setExtraDiskList(List<ExtraDiskDTO> extraDiskList) {
        this.extraDiskList = extraDiskList;
    }

    public CbbLoadBalanceStrategyEnum getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }

    public void setLoadBalanceStrategy(CbbLoadBalanceStrategyEnum loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    public Integer getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Integer getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Integer memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Integer getSystemDiskUsage() {
        return systemDiskUsage;
    }

    public void setSystemDiskUsage(Integer systemDiskUsage) {
        this.systemDiskUsage = systemDiskUsage;
    }

    public Integer getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(Integer maxSession) {
        this.maxSession = maxSession;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public CbbDesktopPoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(CbbDesktopPoolType poolType) {
        this.poolType = poolType;
    }

    public Boolean getHasSecondAdd() {
        return hasSecondAdd;
    }

    public void setHasSecondAdd(Boolean hasSecondAdd) {
        this.hasSecondAdd = hasSecondAdd;
    }

    public Integer getCanUpgradeAgent() {
        return canUpgradeAgent;
    }

    public void setCanUpgradeAgent(Integer canUpgradeAgent) {
        this.canUpgradeAgent = canUpgradeAgent;
    }
}
