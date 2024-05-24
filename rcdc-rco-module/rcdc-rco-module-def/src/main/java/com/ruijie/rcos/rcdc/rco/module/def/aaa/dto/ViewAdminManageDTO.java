package com.ruijie.rcos.rcdc.rco.module.def.aaa.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSource;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSourceNames;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/1 20:22
 *
 * @author wjp
 */
@DataSource(DataSourceNames.DEFAULT_DATASOURCE_BEAN_NAME)
public class ViewAdminManageDTO extends EqualsHashcodeSupport {

    private UUID id;

    private String userName;

    private String realName;

    private String email;

    private String describe;

    private Boolean hasDefault;

    private Date createTime;

    private Boolean enabled;

    /**
     * 三个内置管理员是否第一次启用的属性。其他管理员为null；
     */
    private Boolean firstEnable;

    private UUID roleId;

    private String roleName;

    /**
     * 管理员是否被锁定
     */
    private Boolean lock;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 普通用户类型
     */
    private IacUserTypeEnum userType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Boolean getHasDefault() {
        return hasDefault;
    }

    public void setHasDefault(Boolean hasDefault) {
        this.hasDefault = hasDefault;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getFirstEnable() {
        return firstEnable;
    }

    public void setFirstEnable(Boolean firstEnable) {
        this.firstEnable = firstEnable;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }
}
