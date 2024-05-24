package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/2 16:14
 *
 * @author linrenjian
 */
public class RoleVO {

    @ApiModelProperty(value = "ID")
    private UUID id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String describe;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否内置")
    private Boolean hasDefault;

    @ApiModelProperty(value = "是否超级管理员")
    private Boolean hasSuperPrivilege;

    @ApiModelProperty(value = "权限集合")
    private UUID[] permissionIdArr;

    @ApiModelProperty(value = "关联的管理员数量")
    private Long adminRelationCount;

    public Long getAdminRelationCount() {
        return adminRelationCount;
    }

    public void setAdminRelationCount(Long adminRelationCount) {
        this.adminRelationCount = adminRelationCount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getHasDefault() {
        return hasDefault;
    }

    public void setHasDefault(Boolean hasDefault) {
        this.hasDefault = hasDefault;
    }

    public Boolean getHasSuperPrivilege() {
        return hasSuperPrivilege;
    }

    public void setHasSuperPrivilege(Boolean hasSuperPrivilege) {
        this.hasSuperPrivilege = hasSuperPrivilege;
    }

    public UUID[] getPermissionIdArr() {
        return permissionIdArr;
    }

    public void setPermissionIdArr(UUID[] permissionIdArr) {
        this.permissionIdArr = permissionIdArr;
    }
}
