package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置策略表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
@Entity
@Table(name = "t_rco_user_profile_strategy")
public class UserProfileStrategyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 策略名称
     */
    private String name;

    /**
     * 存储位置(本地/UNC路径)
     */
    @Enumerated(EnumType.STRING)
    private UserProfileStrategyStorageTypeEnum storageType;

    /**
     * 磁盘路径
     */
    private String diskPath;

    /**
     * 磁盘容量 单位:GB
     */
    private Integer diskSize;

    /**
     * 描述
     **/
    private String description;

    /**
     * 创建人
     */
    private String creatorUserName;

    private Date createTime;

    private Date updateTime;

    /**
     * 文件服务器id
     */
    private UUID externalStorageId;

    @Version
    private int version;

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

    public UserProfileStrategyStorageTypeEnum getStorageType() {
        return storageType;
    }

    public void setStorageType(UserProfileStrategyStorageTypeEnum storageType) {
        this.storageType = storageType;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }
}
