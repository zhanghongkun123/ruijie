package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/03 19:25
 *
 * @author coderLee23
 */
public class UamDeliveryGroupObjectSpecDTO {


    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 应用软件库类型IDV/VDI/TCI
     */
    private CbbImageType cbbImageType;

    /**
     * 操作系统类型 WIN7_64、WIN_XP、WIN7_32等
     */

    private CbbOsType osType;


    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }
}
