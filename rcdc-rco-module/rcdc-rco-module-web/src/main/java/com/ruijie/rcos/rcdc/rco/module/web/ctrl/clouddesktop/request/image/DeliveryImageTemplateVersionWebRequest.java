package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.DeliveryObjectType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.EffectiveStrategy;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年06月30日
 *
 * @author xgx
 */
public class DeliveryImageTemplateVersionWebRequest {
    @NotNull
    private DeliveryObjectType deliveryObjectType;

    @Nullable
    private UUID[] deskPoolIdArr;

    @Nullable
    private UUID[] deskIdArr;

    @NotNull
    private EffectiveStrategy effectiveStrategy;

    @NotNull
    private UUID imageVersionId;

    public DeliveryObjectType getDeliveryObjectType() {
        return deliveryObjectType;
    }

    public void setDeliveryObjectType(DeliveryObjectType deliveryObjectType) {
        this.deliveryObjectType = deliveryObjectType;
    }

    @Nullable
    public UUID[] getDeskPoolIdArr() {
        return deskPoolIdArr;
    }

    public void setDeskPoolIdArr(@Nullable UUID[] deskPoolIdArr) {
        this.deskPoolIdArr = deskPoolIdArr;
    }

    @Nullable
    public UUID[] getDeskIdArr() {
        return deskIdArr;
    }

    public void setDeskIdArr(@Nullable UUID[] deskIdArr) {
        this.deskIdArr = deskIdArr;
    }

    public EffectiveStrategy getEffectiveStrategy() {
        return effectiveStrategy;
    }

    public void setEffectiveStrategy(EffectiveStrategy effectiveStrategy) {
        this.effectiveStrategy = effectiveStrategy;
    }


    public UUID getImageVersionId() {
        return imageVersionId;
    }

    public void setImageVersionId(UUID imageVersionId) {
        this.imageVersionId = imageVersionId;
    }
}
