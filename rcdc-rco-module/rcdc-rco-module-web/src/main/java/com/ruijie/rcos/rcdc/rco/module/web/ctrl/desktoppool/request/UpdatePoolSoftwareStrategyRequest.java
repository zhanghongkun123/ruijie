package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 桌面池修改软件管控
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/31
 *
 * @author linke
 */
public class UpdatePoolSoftwareStrategyRequest extends IdWebRequest {

    @ApiModelProperty(value = "软件管控策略ID")
    @Nullable
    private UUID softwareStrategyId;

    @ApiModelProperty(value = "桌面池ID")
    @Nullable
    private UUID[] idArr;

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
