package com.ruijie.rcos.rcdc.rco.module.impl.datasync.dto;

import java.io.Serializable;
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
public class RcdcUserGroupDataSyncRequest implements Serializable {

    private static final long serialVersionUID = -6120801548270220300L;

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID
     */
    @NotNull
    private UUID clusterId;

    @NotNull
    private UserGroupSyncDataDTO userGroupSyncDataDTO;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UserGroupSyncDataDTO getUserGroupSyncDataDTO() {
        return userGroupSyncDataDTO;
    }

    public void setUserGroupSyncDataDTO(UserGroupSyncDataDTO userGroupSyncDataDTO) {
        this.userGroupSyncDataDTO = userGroupSyncDataDTO;
    }
}
