package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;

/**
 * Description: 云桌面应用ISO挂载信息实体类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/10
 *
 * @author wjp
 */
@Entity
@Table(name = "t_rco_cloud_desk_app_config")
@EntityListeners(AuditingEntityListener.class)
public class CloudDeskAppConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID appIsoId;

    @Enumerated(EnumType.STRING)
    private AppTypeEnum appType;

    private UUID deskId;

    private String isoVersion;

    @Version
    private Integer version;

    @LastModifiedDate
    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppIsoId() {
        return appIsoId;
    }

    public void setAppIsoId(UUID appIsoId) {
        this.appIsoId = appIsoId;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getIsoVersion() {
        return isoVersion;
    }

    public void setIsoVersion(String isoVersion) {
        this.isoVersion = isoVersion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
