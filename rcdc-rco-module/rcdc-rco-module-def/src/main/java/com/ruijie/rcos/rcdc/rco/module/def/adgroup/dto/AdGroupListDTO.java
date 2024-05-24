package com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 安全组列表DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author TD
 */
public class AdGroupListDTO {

    private UUID id;

    private String name;

    private String email;

    /**
     * 域 ruijiead.com.cn
     */
    private String domain;

    private String remark;

    /**
     * 所在文件夹 部门1/部门2
     */
    private String ou;

    /**
     * ad域组主键Id
     */
    private String objectGuid;

    private DomainServerType serverType;

    private Date createTime;

    private Date updateTime;

    /**
     * 是否已分配
     */
    private Boolean isAssigned = false;

    /**
     * 是否需要禁用
     */
    private boolean disabled = false;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

    public DomainServerType getServerType() {
        return serverType;
    }

    public void setServerType(DomainServerType serverType) {
        this.serverType = serverType;
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

    public Boolean getAssigned() {
        return isAssigned;
    }

    public void setAssigned(Boolean assigned) {
        isAssigned = assigned;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "AdGroupListDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", domain='" + domain + '\'' +
                ", remark='" + remark + '\'' +
                ", ou='" + ou + '\'' +
                ", objectGuid='" + objectGuid + '\'' +
                ", serverType=" + serverType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isAssigned=" + isAssigned +
                ", disabled=" + disabled +
                '}';
    }
}
