package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.io.Serializable;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年1月10日
 *
 * @author zdc
 */
@ApiModel("查询应用关联交付组列表请求")
public class AppBindDeliveryRequset implements Serializable {

    @ApiModelProperty(value = "应用id", required = true)
    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
