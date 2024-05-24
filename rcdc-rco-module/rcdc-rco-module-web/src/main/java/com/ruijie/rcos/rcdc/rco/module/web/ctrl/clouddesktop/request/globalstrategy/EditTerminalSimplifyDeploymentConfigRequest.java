package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 编辑自动扩容全局配置请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 9:39
 *
 * @author yxq
 */
@ApiModel("终端极简部署配置开关")
public class EditTerminalSimplifyDeploymentConfigRequest implements WebRequest {

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
