package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月22日
 *
 * @author chenl
 */
public class DeskInfoSoftwareStrategyDTO {

    private UUID deskId;

    /**
     * 云桌面关联软件管控策略ID
     **/
    private UUID softwareStrategyId;


    public DeskInfoSoftwareStrategyDTO(UUID deskId, UUID softwareStrategyId) {
        this.deskId = deskId;
        this.softwareStrategyId = softwareStrategyId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }
}
