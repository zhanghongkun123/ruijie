package com.ruijie.rcos.rcdc.rco.module.impl.sms.entity;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageBusinessType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessagePlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 外部消息记录表
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co.; Ltd.
 * Create Time: 2023/6/14
 *
 * @author TD
 */
@Entity
@Table(name = "t_rco_external_message_log")
public class ExternalMessageLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 关联消息类型
     */
    @Enumerated(EnumType.STRING)
    private MessageBusinessType relatedType;

    /**
     * 消息平台类型
     */
    @Enumerated(EnumType.STRING)
    private MessagePlatformType platformType;

    /**
     * 消息关联目标对象
     */
    private String relatedTarget;

    /**
     * 发送内容
     */
    private String sendContent;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 失败原因
     */
    private String failMsg;
    
    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MessageBusinessType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(MessageBusinessType relatedType) {
        this.relatedType = relatedType;
    }

    public MessagePlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(MessagePlatformType platformType) {
        this.platformType = platformType;
    }

    public String getRelatedTarget() {
        return relatedTarget;
    }

    public void setRelatedTarget(String relatedTarget) {
        this.relatedTarget = relatedTarget;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
