package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileBodyMessageDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/7
 *
 * @author linke
 */
public class OaUserProfileStrategyDTO implements Serializable {

    @NotNull
    private UUID poolId;

    @NotNull
    private Boolean personalDataRetention = true;

    @NotNull
    private UserProfileBodyMessageDTO appDataPathRedirect;

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public Boolean getPersonalDataRetention() {
        return personalDataRetention;
    }

    public void setPersonalDataRetention(Boolean personalDataRetention) {
        this.personalDataRetention = personalDataRetention;
    }

    public UserProfileBodyMessageDTO getAppDataPathRedirect() {
        return appDataPathRedirect;
    }

    public void setAppDataPathRedirect(UserProfileBodyMessageDTO appDataPathRedirect) {
        this.appDataPathRedirect = appDataPathRedirect;
    }
}
