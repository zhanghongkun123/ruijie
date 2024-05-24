package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 编辑短信服务器配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public class UpdateSmsGatewayConfigWebRequest {

    /**
     * 网关开关
     */
    @NotNull
    @ApiModelProperty(value = "短信服务器开关", required = true)
    private Boolean enable;

    @Nullable
    @ApiModelProperty(value = "短信服务器配置信息")
    private SmsGatewayConfigWebRequest gatewayConfigWebRequest;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Nullable
    public SmsGatewayConfigWebRequest getGatewayConfigWebRequest() {
        return gatewayConfigWebRequest;
    }

    public void setGatewayConfigWebRequest(@Nullable SmsGatewayConfigWebRequest gatewayConfigWebRequest) {
        this.gatewayConfigWebRequest = gatewayConfigWebRequest;
    }
}
