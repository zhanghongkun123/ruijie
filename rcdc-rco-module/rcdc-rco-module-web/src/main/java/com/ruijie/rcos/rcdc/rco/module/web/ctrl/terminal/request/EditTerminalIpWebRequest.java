package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.IPv4Mask;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: EditTerminalIpWebRequest
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/16
 *
 * @author heruiyuan1
 */
public class EditTerminalIpWebRequest implements WebRequest {

    @ApiModelProperty(value = "终端ID数组", required = true)
    @NotNull
    @Size(min = 1)
    private String[] idArr;

    @ApiModelProperty(value = "是否自动获取IP", required = true)
    @NotNull
    private Boolean autoDhcp;

    @ApiModelProperty(value = "子网掩码")
    @Nullable
    @IPv4Mask
    private String mask;

    @ApiModelProperty(value = "网关")
    @Nullable
    @IPv4Address
    private String gateway;

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }

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
}
