package com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 16:26
 *
 * @author coderLee23
 */
public class RcdcUserGroupSyncDataResponse implements Serializable {

    private static final long serialVersionUID = 6661418704987235448L;

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID
     */
    @NotNull
    private UUID clusterId;

    @NotNull
    private List<UserGroupSyncDataDTO> userGroupSyncDataList;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public List<UserGroupSyncDataDTO> getUserGroupSyncDataList() {
        return userGroupSyncDataList;
    }

    public void setUserGroupSyncDataList(List<UserGroupSyncDataDTO> userGroupSyncDataList) {
        this.userGroupSyncDataList = userGroupSyncDataList;
    }
}
