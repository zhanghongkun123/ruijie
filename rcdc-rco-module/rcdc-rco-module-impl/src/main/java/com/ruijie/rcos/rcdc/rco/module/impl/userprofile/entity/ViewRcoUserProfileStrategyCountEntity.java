package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置策略数量视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/14
 *
 * @author WuShengQiang
 */
@Entity
@Table(name = "v_rco_user_profile_strategy_count")
public class ViewRcoUserProfileStrategyCountEntity {

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
     * 策略路径数量
     */
    private Long count;

    /**
     * 关联云桌面数量
     */
    private Long deskCount;

    /**
     * 策略描述
     */
    private String description;

    /**
     * 创建人
     */
    private String creatorUserName;

    private Date createTime;

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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getDeskCount() {
        return deskCount;
    }

    public void setDeskCount(Long deskCount) {
        this.deskCount = deskCount;
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
