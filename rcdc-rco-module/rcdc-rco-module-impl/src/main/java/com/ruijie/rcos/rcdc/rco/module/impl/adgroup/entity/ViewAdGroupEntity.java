package com.ruijie.rcos.rcdc.rco.module.impl.adgroup.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * 用户AD域组关系视图持久化实体
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-28
 *
 * @author zqj
 */
@Entity
@Table(name = "v_rco_ad_group")
public class ViewAdGroupEntity {

    /**
     * AD域组Id
     */
    @Id
    private UUID id;

    /**
     * ad域服务器dn
     */
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


    private String objectGuid;

    @Enumerated(EnumType.STRING)
    private DomainServerType serverType;

    @Version
    private Integer version;

    private Date createTime;

    private Date updateTime;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }
}