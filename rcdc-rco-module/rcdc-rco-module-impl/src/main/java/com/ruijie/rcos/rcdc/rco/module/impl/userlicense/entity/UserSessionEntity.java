package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Description: 用户连接会话记录实体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
@Entity
@Table(name = "t_rco_user_session")
@EntityListeners(AuditingEntityListener.class)
public class UserSessionEntity {

    @Id
    private UUID id;

    private String terminalId;

    @Enumerated(EnumType.STRING)
    private TerminalTypeEnum terminalType;

    private UUID userId;

    private UUID resourceId;

    @Enumerated(EnumType.STRING)
    private ResourceTypeEnum resourceType;

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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public TerminalTypeEnum getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(TerminalTypeEnum terminalType) {
        this.terminalType = terminalType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
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

