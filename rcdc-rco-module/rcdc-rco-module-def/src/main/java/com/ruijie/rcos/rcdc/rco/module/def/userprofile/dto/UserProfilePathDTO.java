package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.ImportUserProfilePathType;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置路径
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
@PageQueryDTOConfig(entityType = "ViewUserProfilePathEntity")
public class UserProfilePathDTO {

    /**
     * 路径ID
     */
    private UUID id;

    /**
     * 组ID
     */
    private UUID groupId;

    /**
     * 组名称
     */
    private String groupName;

    private String groupDescribe;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 子路径对象
     */
    private UserProfileChildPathDTO[] childPathArr;

    /**
     * 描述
     **/
    private String description;

    /**
     * 创建人
     */
    private String creatorUserName;

    /**
     * 是否系统自带
     */
    private Boolean isDefault;

    private ImportUserProfilePathType importUserProfilePathType;

    private Date createTime;

    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfileChildPathDTO[] getChildPathArr() {
        return childPathArr;
    }

    public void setChildPathArr(UserProfileChildPathDTO[] childPathArr) {
        this.childPathArr = childPathArr;
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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
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

    public String getGroupDescribe() {
        return groupDescribe;
    }

    public void setGroupDescribe(String groupDescribe) {
        this.groupDescribe = groupDescribe;
    }

    public ImportUserProfilePathType getImportUserProfilePathType() {
        return importUserProfilePathType;
    }

    public void setImportUserProfilePathType(ImportUserProfilePathType importUserProfilePathType) {
        this.importUserProfilePathType = importUserProfilePathType;
    }
}