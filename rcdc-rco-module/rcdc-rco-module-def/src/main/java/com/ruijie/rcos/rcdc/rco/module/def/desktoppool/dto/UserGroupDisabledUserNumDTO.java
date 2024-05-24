package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

/**
 * Description: 用户组下不可选的用户数量
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-08-04
 *
 * @author linke
 */
public class UserGroupDisabledUserNumDTO {

    @JSONField(name = "group_id")
    private UUID groupId;

    @JSONField(name = "disabled_user_num")
    private Integer disabledUserNum;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public Integer getDisabledUserNum() {
        return disabledUserNum;
    }

    public void setDisabledUserNum(Integer disabledUserNum) {
        this.disabledUserNum = disabledUserNum;
    }
}
