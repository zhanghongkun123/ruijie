package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.dto;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.RedundancyStrategy;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.StoragePoolHealthState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.StoragePoolMgmtState;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 存储池DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/18
 *
 * @author WuShengQiang
 */
public class RestStoragePoolDTO {

    private UUID id;

    /**
     * 存储集群ID
     */
    private UUID clusterId;

    private String name;

    private String description;

    private RedundancyStrategy redundancyStrategy;

    /**
     * 单位是Byte
     */
    private Long totalCapacity;

    /**
     * 单位是Byte
     */
    private Long usedCapacity;

    private StoragePoolMgmtState storagePoolMgmtState;

    private StoragePoolHealthState storagePoolHealthState;

    private Date createTime;

    private UUID platformId;

    private String platformName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
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

    public StoragePoolMgmtState getStoragePoolMgmtState() {
        return storagePoolMgmtState;
    }

    public void setStoragePoolMgmtState(StoragePoolMgmtState storagePoolMgmtState) {
        this.storagePoolMgmtState = storagePoolMgmtState;
    }

    public StoragePoolHealthState getStoragePoolHealthState() {
        return storagePoolHealthState;
    }

    public void setStoragePoolHealthState(StoragePoolHealthState storagePoolHealthState) {
        this.storagePoolHealthState = storagePoolHealthState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}
