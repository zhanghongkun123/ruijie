package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;

/**
 * Description: 用户授权记录实体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
@Entity
@Table(name = "t_rco_user_license")
@EntityListeners(AuditingEntityListener.class)
public class UserLicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /** 
     * 用户ID
    **/
    private UUID userId;

    /**
     * 要求的授权类型
     */
    private String authMode;

    /**
     * 获得的授权类型
     */
    private String licenseType;

    /**
     * 授权持续类型
     */
    @Enumerated(EnumType.STRING)
    private CbbLicenseDurationEnum licenseDuration;

    @Version
    private Integer version;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public CbbLicenseDurationEnum getLicenseDuration() {
        return licenseDuration;
    }

    public void setLicenseDuration(CbbLicenseDurationEnum licenseDuration) {
        this.licenseDuration = licenseDuration;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
}

