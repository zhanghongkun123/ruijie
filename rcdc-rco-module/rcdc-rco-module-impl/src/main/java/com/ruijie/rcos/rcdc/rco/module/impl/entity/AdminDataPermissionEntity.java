package com.ruijie.rcos.rcdc.rco.module.impl.entity;


import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/22 17:06
 *
 * @author linrenjian
 */
@Entity
@Table(name = "t_rco_admin_data_permission")
public class AdminDataPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID adminId;

    private String  permissionDataId;

    /**
     *权限类型
     */
    @Enumerated(EnumType.STRING)
    private AdminDataPermissionType permissionDataType;

    private Date createDate;

    private Date updateDate;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public AdminDataPermissionType getPermissionDataType() {
        return permissionDataType;
    }

    public void setPermissionDataType(AdminDataPermissionType permissionDataType) {
        this.permissionDataType = permissionDataType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPermissionDataId() {
        return permissionDataId;
    }

    public void setPermissionDataId(String permissionDataId) {
        this.permissionDataId = permissionDataId;
    }
}
