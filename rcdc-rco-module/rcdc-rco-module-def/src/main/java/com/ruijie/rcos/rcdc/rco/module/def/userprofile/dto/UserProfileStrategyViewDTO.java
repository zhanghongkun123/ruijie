package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置策略详情DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/14
 *
 * @author WuShengQiang
 */
@PageQueryDTOConfig(entityType = "ViewRcoUserProfileStrategyCountEntity")
public class UserProfileStrategyViewDTO {

    /**
     * 策略ID
     */
    private UUID id;

    /**
     * 策略名称
     */
    private String name;

    /**
     * 存储位置(本地/UNC路径)
     */
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
     * 描述
     **/
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

    /**
     * 存在文件服务器
     */
    private Boolean hasExtStorage;

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

    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }

    public Boolean getHasExtStorage() {
        return externalStorageId != null;
    }

    public void setHasExtStorage(Boolean hasExtStorage) {
        this.hasExtStorage = hasExtStorage;
    }
}
