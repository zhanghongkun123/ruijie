package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

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
 * Create Time: 2022/12/28 16:40
 *
 * @author chenl
 */
public class EditAppDiskDeliveryGroupRequest implements WebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id", required = true)
    @NotNull
    private UUID id;

    /**
     * 交付组名称
     */
    @ApiModelProperty(value = "交付组名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String deliveryGroupName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Nullable
    private String deliveryGroupDesc;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    public String getDeliveryGroupDesc() {
        return deliveryGroupDesc;
    }

    public void setDeliveryGroupDesc(String deliveryGroupDesc) {
        this.deliveryGroupDesc = deliveryGroupDesc;
    }

}
