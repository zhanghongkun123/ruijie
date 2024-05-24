package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/18
 *
 * @author Jarman
 */
public class NetworkConfigVO {

    @NotNull
    @ApiModelProperty(value = "桌面地址",required = true)
    private DesktopAddress address;

    public DesktopAddress getAddress() {
        return address;
    }

    public void setAddress(DesktopAddress address) {
        this.address = address;
    }
}
