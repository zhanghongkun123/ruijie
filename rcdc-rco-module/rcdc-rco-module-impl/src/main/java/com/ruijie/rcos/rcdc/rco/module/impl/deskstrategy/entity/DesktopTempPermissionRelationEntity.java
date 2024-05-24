package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.entity;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 对象与临时策略关联记录
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/27
 *
 * @author linke
 */
@Entity
@Table(name = "t_rco_desktop_temp_permission_relation")
public class DesktopTempPermissionRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 桌面临时权限ID
     */
    private UUID desktopTempPermissionId;

    /**
     * 关联对象ID
     */
    private UUID relatedId;

    /**
     * 对象类型
     */
    @Enumerated(EnumType.STRING)
    private DesktopTempPermissionRelatedType relatedType;

    /**
     * 是否发送过到期通知
     */
    private Boolean hasSendExpireNotice;

    /**
     * 创建时间
     */
    private Date createTime;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopTempPermissionId() {
        return desktopTempPermissionId;
    }

    public void setDesktopTempPermissionId(UUID desktopTempPermissionId) {
        this.desktopTempPermissionId = desktopTempPermissionId;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public DesktopTempPermissionRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(DesktopTempPermissionRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public Boolean getHasSendExpireNotice() {
        return hasSendExpireNotice;
    }

    public void setHasSendExpireNotice(Boolean hasSendExpireNotice) {
        this.hasSendExpireNotice = hasSendExpireNotice;
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
