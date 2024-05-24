package com.ruijie.rcos.rcdc.rco.module.impl.desktop.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-07-23
 *
 * @author linke
 */
public class DesktopEventNoticeDTO {

    private UUID uuid;

    private Date timestamp;

    private String state;

    /**
     * 会话id
     */
    @JSONField(name = "connection-id")
    private Long connectionId;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
}
