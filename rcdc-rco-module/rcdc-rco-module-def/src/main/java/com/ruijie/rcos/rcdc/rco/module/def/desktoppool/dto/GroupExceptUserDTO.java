package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Description: 更新桌面池用户或用户组关联关系中，需要去除指定组内某些用户，添加其他用户
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class GroupExceptUserDTO {

    @NotNull
    private UUID groupId;

    @NotNull
    private List<UUID> exceptUserIdList;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public List<UUID> getExceptUserIdList() {
        return exceptUserIdList;
    }

    public void setExceptUserIdList(List<UUID> exceptUserIdList) {
        this.exceptUserIdList = exceptUserIdList;
    }
}
