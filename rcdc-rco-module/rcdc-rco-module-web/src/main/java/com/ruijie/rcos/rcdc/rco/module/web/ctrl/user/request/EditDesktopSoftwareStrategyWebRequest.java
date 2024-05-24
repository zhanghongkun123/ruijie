package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:  2021-04-06
 *
 * @author chen zj
 */
public class EditDesktopSoftwareStrategyWebRequest extends IdArrWebRequest {

    @ApiModelProperty(value = "软件策略ID", required = true)
    @Nullable
    private UUID softwareStrategyId;

    @Nullable
    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(@Nullable UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }
}
