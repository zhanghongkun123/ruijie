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


/**
 * Description: 用户及其分配信息列表
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/19
 *
 * @author jarman
 */
@Entity
@Table(name = "v_rco_disk_pool_user_assignment")
public class ViewDiskPoolUserAssignmentEntity {

    @Id
    private UUID id;

    private UUID diskPoolId;

    private UUID relatedId;

    @Enumerated(EnumType.STRING)
    private IacConfigRelatedType relatedType;

    private Date createTime;

    @Version
    private Integer version;

    private String userName;

    private String userGroupName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }
}
