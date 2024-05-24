package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;

/**
 * Description: 用户与磁盘池视图实体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/15
 *
 * @author TD
 */
@Entity
@Table(name = "v_rco_user_disk_pool")
public class UserDiskPoolEntity {

    @Id
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
    @Enumerated(EnumType.STRING)
    private CbbDiskPoolState poolState;

    private UUID relatedId;

    @Enumerated(EnumType.STRING)
    private IacConfigRelatedType relatedType;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

    /**
     * 计算集群位置
     */
    private UUID clusterId;

    /**
     * 存储池位置
     */
    private UUID storagePoolId;

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

    /**
     * 版本号
     **/
    @Version
    private Integer version;

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

    public String getDiskLetter() {
        return diskLetter;
    }

    public void setDiskLetter(String diskLetter) {
        this.diskLetter = diskLetter;
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

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
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
}
