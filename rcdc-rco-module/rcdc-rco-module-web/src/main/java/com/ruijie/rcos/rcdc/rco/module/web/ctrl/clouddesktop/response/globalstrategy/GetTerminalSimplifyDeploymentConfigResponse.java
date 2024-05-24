package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 获取自动扩容全局配置响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月11日
 *
 * @author yxq
 */
@ApiModel("终端极简部署配置开关")
public class GetTerminalSimplifyDeploymentConfigResponse {

    @ApiModelProperty("终端极简部署配置开关")
    @NotNull
    private Boolean enableTerminalSimplifyDeployment;

    public Boolean getEnableTerminalSimplifyDeployment() {
        return enableTerminalSimplifyDeployment;
    }

    public void setEnableTerminalSimplifyDeployment(Boolean enableTerminalSimplifyDeployment) {
        this.enableTerminalSimplifyDeployment = enableTerminalSimplifyDeployment;
    }
}
