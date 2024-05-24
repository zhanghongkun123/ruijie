package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;

/**
 * Description: 消息发送用户
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/25
 *
 * @author Jarman
 */
@Entity
@Table(name = "t_rco_user_message_user")
public class UserMessageUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID messageId;

    private UUID userId;

    @Version
    private Integer version;

    @Enumerated(EnumType.STRING)
    private UserMessageStateEnum state;

    @Enumerated(EnumType.STRING)
    private IacUserTypeEnum userType;

    private UUID desktopId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UserMessageStateEnum getState() {
        return state;
    }

    public void setState(UserMessageStateEnum state) {
        this.state = state;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    @Override
    public String toString() {
        return "UserMessageUserEntity{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", userId=" + userId +
                ", version=" + version +
                ", state=" + state +
                ", userType=" + userType +
                ", desktopId=" + desktopId +
                '}';
    }
}
