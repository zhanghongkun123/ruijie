package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.impl.enums.SessionStatusEnums;

import javax.persistence.*;
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
 * Description: 会话管理-云桌面会话信息表
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author wangjie9
 */
@Entity
@Table(name = "t_rco_desk_user_session")
public class DesktopUserSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 云桌面id
     */
    private UUID deskId;

    /**
     * 终端id
     */
    private UUID terminalId;

    /**
     * 用户id
     */
    private UUID userId;

    /**
     * 会话ID
     */
    private Integer sessionId;

    /**
     * 会话状态：在线、注销中
     */
    @Enumerated(EnumType.STRING)
    private SessionStatusEnums sessionStatus;

    /**
     * 会话最近建立时间
     */
    private Date lastCreateTime;

    /**
     * 会话最近空闲开始时间
     */
    private Date lastIdleTime;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(UUID terminalId) {
        this.terminalId = terminalId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public SessionStatusEnums getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatusEnums sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Date getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(Date lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    public Date getLastIdleTime() {
        return lastIdleTime;
    }

    public void setLastIdleTime(Date lastIdleTime) {
        this.lastIdleTime = lastIdleTime;
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
