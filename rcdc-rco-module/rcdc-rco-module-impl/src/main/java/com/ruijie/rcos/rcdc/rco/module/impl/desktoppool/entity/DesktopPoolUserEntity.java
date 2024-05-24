package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;

/**
 * Description: 桌面池与用户关系表
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/18
 *
 * @author linke
 */
@Entity
@Table(name = "t_rco_desktop_pool_user")
public class DesktopPoolUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID desktopPoolId;

    private UUID relatedId;

    @Enumerated(EnumType.STRING)
    private IacConfigRelatedType relatedType;

    private Date createTime;

    @Version
    private Integer version;

    public DesktopPoolUserEntity() {
    }

    public DesktopPoolUserEntity(UUID relatedId, IacConfigRelatedType relatedType) {
        this.relatedId = relatedId;
        this.relatedType = relatedType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
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
}
