package com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response;

import java.io.Serializable;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 16:26
 *
 * @author coderLee23
 */
public class RcdcUserSyncDataResponse implements Serializable {

    private static final long serialVersionUID = -8705617328280869949L;

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID
     */
    @NotNull
    private UUID clusterId;

    @NotNull
    private PageResponse<UserSyncDataDTO> userSyncDataPage;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }


    public PageResponse<UserSyncDataDTO> getUserSyncDataPage() {
        return userSyncDataPage;
    }

    public void setUserSyncDataPage(PageResponse<UserSyncDataDTO> userSyncDataPage) {
        this.userSyncDataPage = userSyncDataPage;
    }
}
