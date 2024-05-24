package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.DeliveryObjectType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.EffectiveStrategy;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 交付应用镜像
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月26日
 *
 * @author liuwc
 */
public class DeliveryAppImageTemplateVersionWebRequest {

    @NotNull
    private DeliveryObjectType deliveryObjectType;

    @Nullable
    private UUID[] rcaPoolIdArr;

    @NotNull
    private UUID imageVersionId;

    @Nullable
    public UUID[] getRcaPoolIdArr() {
        return rcaPoolIdArr;
    }

    public void setRcaPoolIdArr(@Nullable UUID[] rcaPoolIdArr) {
        this.rcaPoolIdArr = rcaPoolIdArr;
    }

    public UUID getImageVersionId() {
        return imageVersionId;
    }

    public void setImageVersionId(UUID imageVersionId) {
        this.imageVersionId = imageVersionId;
    }

    public DeliveryObjectType getDeliveryObjectType() {
        return deliveryObjectType;
    }

    public void setDeliveryObjectType(DeliveryObjectType deliveryObjectType) {
        this.deliveryObjectType = deliveryObjectType;
    }
}
