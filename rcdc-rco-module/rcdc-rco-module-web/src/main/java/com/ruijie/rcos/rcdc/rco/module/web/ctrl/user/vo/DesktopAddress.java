package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/21
 *
 * @author Jarman
 */
public class DesktopAddress extends IdLabelEntry {

    @Nullable
    @ApiModelProperty(value = "ip")
    private String ip;

    @Nullable
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
