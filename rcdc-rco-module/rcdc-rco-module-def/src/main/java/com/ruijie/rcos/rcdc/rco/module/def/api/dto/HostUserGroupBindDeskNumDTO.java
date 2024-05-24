package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/5/6
 *
 * @author linke
 */
public class HostUserGroupBindDeskNumDTO implements Serializable {

    private UUID groupId;

    private Long deskNum;

    public HostUserGroupBindDeskNumDTO() {

    }

    public HostUserGroupBindDeskNumDTO(UUID groupId, Long deskNum) {
        Assert.notNull(groupId, "groupId can not be null");
        this.groupId = groupId;
        this.deskNum = deskNum;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public Long getDeskNum() {
        return deskNum;
    }

    public void setDeskNum(Long deskNum) {
        this.deskNum = deskNum;
    }
}
