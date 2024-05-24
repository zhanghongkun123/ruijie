package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.disk.DataDiskCroppedDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.storagepool.StoragePoolNodeDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.storagepool.StoragePoolOptionsDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 存储池返回体
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/5
 *
 * @author TD
 */
public class StoragePoolDetailResponse {

    private UUID id;

    private UUID storagePoolId;

    private List<StoragePoolNodeDTO> nodeList;

    /**
     * 计算集群ID
     */
    private UUID computeClusterId;

    private UUID storageClusterId;

    private Integer originalId;

    private String name;

    private String description;

    private HotspareStrategy hotspareStrategy;

    private RedundancyStrategy redundancyStrategy;

    /**
     * 单位是Byte
     */
    private Long totalCapacity;

    /**
     * 单位是Byte
     */
    private Long usedCapacity;

    private Long allocationCapacity;

    private StoragePoolMgmtState storagePoolMgmtState;

    private StoragePoolDiskType diskType;

    private Date createTime;

    private Boolean canOverweight;

    private StoragePoolHealthState storagePoolHealthState;

    private DataDiskCroppedDTO[] dataDiskCroppedDTOArr;

    private Integer dataDiskCount;

    /**
     * 平台ID
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
     * 存储扩展参数
     */
    private StoragePoolOptionsDTO options;

    /**
     * 存储类型
     */
    private StoragePoolType storagePoolType;

    private Boolean canUsed = Boolean.TRUE;

    private String canUsedMessage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<StoragePoolNodeDTO> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<StoragePoolNodeDTO> nodeList) {
        this.nodeList = nodeList;
    }

    public UUID getComputeClusterId() {
        return computeClusterId;
    }

    public void setComputeClusterId(UUID computeClusterId) {
        this.computeClusterId = computeClusterId;
    }

    public UUID getStorageClusterId() {
        return storageClusterId;
    }

    public void setStorageClusterId(UUID storageClusterId) {
        this.storageClusterId = storageClusterId;
    }

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HotspareStrategy getHotspareStrategy() {
        return hotspareStrategy;
    }

    public void setHotspareStrategy(HotspareStrategy hotspareStrategy) {
        this.hotspareStrategy = hotspareStrategy;
    }

    public RedundancyStrategy getRedundancyStrategy() {
        return redundancyStrategy;
    }

    public void setRedundancyStrategy(RedundancyStrategy redundancyStrategy) {
        this.redundancyStrategy = redundancyStrategy;
    }

    public Long getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Long totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Long getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Long usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Long getAllocationCapacity() {
        return allocationCapacity;
    }

    public void setAllocationCapacity(Long allocationCapacity) {
        this.allocationCapacity = allocationCapacity;
    }

    public StoragePoolMgmtState getStoragePoolMgmtState() {
        return storagePoolMgmtState;
    }

    public void setStoragePoolMgmtState(StoragePoolMgmtState storagePoolMgmtState) {
        this.storagePoolMgmtState = storagePoolMgmtState;
    }

    public StoragePoolDiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(StoragePoolDiskType diskType) {
        this.diskType = diskType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getCanOverweight() {
        return canOverweight;
    }

    public void setCanOverweight(Boolean canOverweight) {
        this.canOverweight = canOverweight;
    }

    public StoragePoolHealthState getStoragePoolHealthState() {
        return storagePoolHealthState;
    }

    public void setStoragePoolHealthState(StoragePoolHealthState storagePoolHealthState) {
        this.storagePoolHealthState = storagePoolHealthState;
    }

    public DataDiskCroppedDTO[] getDataDiskCroppedDTOArr() {
        return dataDiskCroppedDTOArr;
    }

    public void setDataDiskCroppedDTOArr(DataDiskCroppedDTO[] dataDiskCroppedDTOArr) {
        this.dataDiskCroppedDTOArr = dataDiskCroppedDTOArr;
    }

    public Integer getDataDiskCount() {
        return dataDiskCount;
    }

    public void setDataDiskCount(Integer dataDiskCount) {
        this.dataDiskCount = dataDiskCount;
    }

    public UUID getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(UUID storagePoolId) {
        this.storagePoolId = storagePoolId;
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

    public StoragePoolOptionsDTO getOptions() {
        return options;
    }

    public void setOptions(StoragePoolOptionsDTO options) {
        this.options = options;
    }

    public StoragePoolType getStoragePoolType() {
        return storagePoolType;
    }

    public void setStoragePoolType(StoragePoolType storagePoolType) {
        this.storagePoolType = storagePoolType;
    }

    public boolean isCanUsed() {
        return canUsed;
    }

    public void setCanUsed(boolean canUsed) {
        this.canUsed = canUsed;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }
}
