package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.ImportUserProfilePathType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 路径列表展示的Entity
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/11
 *
 * @author zwf
 */
@Entity
@Table(name = "v_rco_user_profile_path")
public class ViewUserProfilePathEntity {
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
     *  组名
     */
    private String groupName;

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

    @Enumerated(EnumType.STRING)
    private ImportUserProfilePathType importUserProfilePathType;

    private Date createTime;

    private Date updateTime;

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

    public ImportUserProfilePathType getImportUserProfilePathType() {
        return importUserProfilePathType;
    }

    public void setImportUserProfilePathType(ImportUserProfilePathType importUserProfilePathType) {
        this.importUserProfilePathType = importUserProfilePathType;
    }
}
