package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/31 0:47
 *
 * @author coderLee23
 */
public class CheckDeliveryGroupNameDuplicationRequest implements WebRequest {


    @ApiModelProperty(value = "交付组id")
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "交付组名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String deliveryGroupName;

    @ApiModelProperty(value = "交付组类型", required = true)
    @NotNull
    private AppDeliveryTypeEnum appDeliveryType;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    public AppDeliveryTypeEnum getAppDeliveryType() {
        return appDeliveryType;
    }

    public void setAppDeliveryType(AppDeliveryTypeEnum appDeliveryType) {
        this.appDeliveryType = appDeliveryType;
    }
}
