package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSource;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSourceNames;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;


/**
 * Description: 用户及其分配信息列表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/07/06
 *
 * @author td
 */
@DataSource(DataSourceNames.DEFAULT_DATASOURCE_BEAN_NAME)
//@PageQueryDTOConfig(dmql = "v_rco_disk_pool_user_assignment.dmql")
@PageQueryDTOConfig(entityType = "ViewDiskPoolUserAssignmentEntity")
public class DiskPoolUserWithAssignmentDTO {

    private UUID id;

    private UUID diskPoolId;

    private UUID relatedId;

    private IacConfigRelatedType relatedType;

    private Date createTime;

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
