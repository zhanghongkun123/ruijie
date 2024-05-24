package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25 13:47
 *
 * @author linrenjian
 */
public class OpLoginInfoChangeRequestDTO  {

    /**
     * 集群id
     */
    @NotNull
    private UUID clusterId;

    @NotNull
    private LoginInfoChangeRequestDTO loginInfoChangeRequestDTO;


    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public LoginInfoChangeRequestDTO getLoginInfoChangeRequestDTO() {
        return loginInfoChangeRequestDTO;
    }

    public void setLoginInfoChangeRequestDTO(LoginInfoChangeRequestDTO loginInfoChangeRequestDTO) {
        this.loginInfoChangeRequestDTO = loginInfoChangeRequestDTO;
    }
}
