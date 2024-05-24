package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月24日
 *
 * @author zhanghongkun
 */
@ApiModel(description = "授权兼容配置信息")
public class AuthCompatibleVO implements WebRequest {

    @ApiModelProperty(value = "授权兼容")
    @NotNull
    private Boolean enableAuthCompatible;

    public Boolean getEnableAuthCompatible() {
        return enableAuthCompatible;
    }

    public void setEnableAuthCompatible(Boolean enableAuthCompatible) {
        this.enableAuthCompatible = enableAuthCompatible;
    }
}
