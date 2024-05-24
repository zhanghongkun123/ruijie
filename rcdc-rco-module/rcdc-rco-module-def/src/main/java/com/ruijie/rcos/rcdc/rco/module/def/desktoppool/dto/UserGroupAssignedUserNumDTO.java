package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

/**
 * Description: 用户组下已分配的用户数量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-07-05
 *
 * @author linke
 */
public class UserGroupAssignedUserNumDTO {

    @JSONField(name = "group_id")
    private UUID groupId;

    @JSONField(name = "assigned_user_num")
    private Integer assignedUserNum;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public Integer getAssignedUserNum() {
        return assignedUserNum;
    }

    public void setAssignedUserNum(Integer assignedUserNum) {
        this.assignedUserNum = assignedUserNum;
    }
}
