package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.IPv4Mask;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: EditTerminalIpAllWebRequest
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/16
 *
 * @author heruiyuan1
 */
public class EditTerminalIpAllWebRequest implements WebRequest {

    @ApiModelProperty(value = "是否自动获取IP", required = true)
    @NotNull
    private Boolean autoDhcp;

    @ApiModelProperty(value = "子网掩码")
    @IPv4Mask
    @Nullable
    private String mask;

    @ApiModelProperty(value = "网关")
    @IPv4Address
    @Nullable
    private String gateway;

    @ApiModelProperty(value = "终端组id", required = true)
    @NotNull
    private UUID terminalGroupId;

    public Boolean getAutoDhcp() {
        return autoDhcp;
    }

    public void setAutoDhcp(Boolean autoDhcp) {
        this.autoDhcp = autoDhcp;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }
}
