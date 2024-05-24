package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 磁盘池列表信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
public class DiskPoolStatisticDTO {

    /**
     * 磁盘池id
     **/
    private UUID id;

    /**
     * 磁盘池名称
     **/
    private String name;

    /**
     * 磁盘数量
     */
    private Integer diskNum;

    /**
     * 是否开启自动创建磁盘
     */
    private Boolean enableCreateDisk;

    /**
     * 盘符
     */
    private String diskLetter;

    /**
     * 磁盘配置大小
     */
    private Integer diskSize;

    /**
     * 磁盘池名称前缀,为null时采用磁盘池名称
     */
    private String diskNamePrefix;

    /**
     * 备注
     */
    private String description;

    /**
     * 磁盘池状态
     **/
    private CbbDiskPoolState poolState;

    /**
     * 磁盘使用中的数量
     */
    private int inUseNum;

    /**
     * 已分配的数量
     */
    private int assignedNum;

    /**
     * 用户数量
     */
    private long userNum;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

    /**
     * 计算集群信息
     */
    private ClusterInfoDTO clusterInfo;

    /**
     * 存储池信息
     */
    private PlatformStoragePoolDTO storagePool;

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
    private CloudPlatformType platformType;

    /**
     * 云平台状态
     */
    private CloudPlatformStatus platformStatus;

    /**
     * 云平台唯一标识
     */
    private String cloudPlatformId;

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

    public Integer getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(Integer diskNum) {
        this.diskNum = diskNum;
    }

    public Boolean getEnableCreateDisk() {
        return enableCreateDisk;
    }

    public void setEnableCreateDisk(Boolean enableCreateDisk) {
        this.enableCreateDisk = enableCreateDisk;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public String getDiskNamePrefix() {
        return diskNamePrefix;
    }

    public void setDiskNamePrefix(String diskNamePrefix) {
        this.diskNamePrefix = diskNamePrefix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CbbDiskPoolState getPoolState() {
        return poolState;
    }

    public void setPoolState(CbbDiskPoolState poolState) {
        this.poolState = poolState;
    }

    public int getInUseNum() {
        return inUseNum;
    }

    public void setInUseNum(int inUseNum) {
        this.inUseNum = inUseNum;
    }

    public int getAssignedNum() {
        return assignedNum;
    }

    public void setAssignedNum(int assignedNum) {
        this.assignedNum = assignedNum;
    }

    public long getUserNum() {
        return userNum;
    }

    public void setUserNum(long userNum) {
        this.userNum = userNum;
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

    public String getDiskLetter() {
        return diskLetter;
    }

    public void setDiskLetter(String diskLetter) {
        this.diskLetter = diskLetter;
    }

    public ClusterInfoDTO getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfoDTO clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public PlatformStoragePoolDTO getStoragePool() {
        return storagePool;
    }

    public void setStoragePool(PlatformStoragePoolDTO storagePool) {
        this.storagePool = storagePool;
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
}
