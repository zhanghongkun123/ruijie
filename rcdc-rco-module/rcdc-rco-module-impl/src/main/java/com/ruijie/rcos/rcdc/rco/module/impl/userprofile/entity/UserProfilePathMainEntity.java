package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.ImportUserProfilePathType;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置路径表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/8
 *
 * @author WuShengQiang
 */
@Entity
@Table(name = "t_rco_user_profile_path")
public class UserProfilePathMainEntity extends EqualsHashcodeSupport {

    /**
     * 路径ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 组ID
     */
    private UUID groupId;

    /**
     * 路径名称
     */
    private String name;

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
    private Boolean isDefault = false;

    private Date createTime;

    private Date updateTime;

    @Enumerated(EnumType.STRING)
    private ImportUserProfilePathType importUserProfilePathType;

    /**
     * 额外配置参数，json格式
     */
    private String extraConfigInfo;

    @Version
    private int version;

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

    public ImportUserProfilePathType getImportUserProfilePathType() {
        return importUserProfilePathType;
    }

    public void setImportUserProfilePathType(ImportUserProfilePathType importUserProfilePathType) {
        this.importUserProfilePathType = importUserProfilePathType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getExtraConfigInfo() {
        return extraConfigInfo;
    }

    public void setExtraConfigInfo(String extraConfigInfo) {
        this.extraConfigInfo = extraConfigInfo;
    }

}