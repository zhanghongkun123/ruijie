package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 云桌面变更用户配置策略请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/19
 *
 * @author WuShengQiang
 */
public class EditDesktopUserProfileStrategyWebRequest extends IdArrWebRequest {

    @ApiModelProperty(value = "用户配置策略ID", required = true)
    @Nullable
    private UUID userProfileStrategyId;

    @Nullable
    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }
}
