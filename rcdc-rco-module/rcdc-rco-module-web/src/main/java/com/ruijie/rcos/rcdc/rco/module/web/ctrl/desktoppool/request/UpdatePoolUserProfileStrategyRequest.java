package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 变更池桌面配置策略的请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/18
 *
 * @author zwf
 */
public class UpdatePoolUserProfileStrategyRequest extends IdWebRequest {
    @ApiModelProperty(value = "UPM配置策略ID")
    @Nullable
    private UUID userProfileStrategyId;

    @ApiModelProperty(value = "桌面池ID")
    @Nullable
    private UUID[] idArr;

    @Nullable
    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setIdArr(@Nullable UUID[] idArr) {
        this.idArr = idArr;
    }

    @Nullable
    public UUID[] getIdArr() {
        return idArr;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }
}
