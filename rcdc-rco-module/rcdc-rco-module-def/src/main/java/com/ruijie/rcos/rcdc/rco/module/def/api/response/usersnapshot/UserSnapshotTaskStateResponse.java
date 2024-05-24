package com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot;

import java.util.UUID;

/**
 * Description: 快照任务执行状态信息反馈对象
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/8
 *
 * @author liusd
 */
public class UserSnapshotTaskStateResponse {

    /**
     * 消息系列号
     */
    private UUID id;

    /**
     * 消息标题
     */
    private String msgName;

    /**
     * 消息描述
     */
    private String describe;

    /**
     * 消息创建时间
     */
    private long createTime;

    /**
     * 消息修改时间
     */
    private long lastUpdateTime;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息状态
     */
    private String msgState;

    public UserSnapshotTaskStateResponse() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }
}
