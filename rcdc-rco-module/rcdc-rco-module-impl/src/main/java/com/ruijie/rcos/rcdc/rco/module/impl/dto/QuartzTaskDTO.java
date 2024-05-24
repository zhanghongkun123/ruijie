package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: Quartz任务实体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-07-04
 *
 * @author zqj
 */
public class QuartzTaskDTO {

    private final UUID deskId;

    private final Set<UUID> userGroupChildrenSet;

    private final List<UUID> userIdList;

    private final List<UUID> deskList;

    private final Boolean allowCancel;

    public QuartzTaskDTO(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskList, Boolean allowCancel) {
        this.deskId = deskId;
        this.userGroupChildrenSet = userGroupChildrenSet;
        this.userIdList = userIdList;
        this.deskList = deskList;
        this.allowCancel = allowCancel;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public Set<UUID> getUserGroupChildrenSet() {
        return userGroupChildrenSet;
    }

    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public List<UUID> getDeskList() {
        return deskList;
    }

    public Boolean getAllowCancel() {
        return allowCancel;
    }

    @Override
    public String toString() {
        return "QuartzTaskDTO{" +
                "deskId=" + deskId +
                ", userGroupChildrenSet=" + userGroupChildrenSet +
                ", userIdList=" + userIdList +
                ", deskList=" + deskList +
                ", allowCancel=" + allowCancel +
                '}';
    }
}
