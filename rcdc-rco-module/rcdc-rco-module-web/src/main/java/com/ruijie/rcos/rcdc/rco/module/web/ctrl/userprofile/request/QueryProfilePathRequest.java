package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/18
 *
 * @author linke
 */
public class QueryProfilePathRequest implements WebRequest {

    @ApiModelProperty(value = "存储位置(本地/UNC路径)", required = true)
    @Nullable
    private UserProfileStrategyStorageTypeEnum storageType;

    @Nullable
    public UserProfileStrategyStorageTypeEnum getStorageType() {
        return storageType;
    }

    public void setStorageType(@Nullable UserProfileStrategyStorageTypeEnum storageType) {
        this.storageType = storageType;
    }
}
