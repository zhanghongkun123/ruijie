package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserSyncConfigDTO;

import io.swagger.annotations.ApiModelProperty;

/**
 * 第三方认证配置
 *
 * Description: 第三方认证配置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/13 16:56
 *
 * @author zjy
 */
public class ThirdPartyConfigWithUserSyncRequest extends ThirdPartyConfigBaseRequest {

    /**
     * 用户同步相关配置
     */
    @ApiModelProperty(value = "用户同步相关配置", required = true)
    @Nullable
    private BaseThirdPartyUserSyncConfigDTO userSyncConfig;

    @Nullable
    public BaseThirdPartyUserSyncConfigDTO getUserSyncConfig() {
        return userSyncConfig;
    }

    public void setUserSyncConfig(@Nullable BaseThirdPartyUserSyncConfigDTO userSyncConfig) {
        this.userSyncConfig = userSyncConfig;
    }
}
