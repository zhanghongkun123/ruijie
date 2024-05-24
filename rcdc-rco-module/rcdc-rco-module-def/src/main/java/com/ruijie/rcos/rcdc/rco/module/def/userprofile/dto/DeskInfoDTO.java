package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import java.util.UUID;

/**
 * Description: 云桌面关联策略DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/12
 *
 * @author WuShengQiang
 */
public class DeskInfoDTO {

    private UUID deskId;

    private UUID userProfileStrategyId;

    public DeskInfoDTO(UUID deskId, UUID userProfileStrategyId) {
        this.deskId = deskId;
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }
}