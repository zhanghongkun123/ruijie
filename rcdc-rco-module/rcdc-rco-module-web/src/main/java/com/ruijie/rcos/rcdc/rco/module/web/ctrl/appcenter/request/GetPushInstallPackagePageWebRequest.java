package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 14:05
 *
 * @author coderLee23
 */
public class GetPushInstallPackagePageWebRequest extends PageWebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id")
    @Nullable
    private UUID deliveryGroupId;

    /**
     * 操作系统类型【WINDOWS、LINUX】
     */
    @ApiModelProperty(value = " 操作系统类型")
    @Nullable
    private OsPlatform osPlatform;

    @Nullable
    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(@Nullable UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    @Nullable
    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(@Nullable OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }
}
