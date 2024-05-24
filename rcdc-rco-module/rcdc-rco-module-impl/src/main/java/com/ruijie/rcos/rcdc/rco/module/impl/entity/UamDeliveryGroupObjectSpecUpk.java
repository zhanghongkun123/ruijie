package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 联合主键
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/03 20:05
 *
 * @author coderLee23
 */
public class UamDeliveryGroupObjectSpecUpk implements Serializable {

    private static final long serialVersionUID = -4794409345856728379L;

    private UUID deliveryGroupId;

    /**
     * 操作系统类型 WIN7_64、WIN_XP、WIN7_32等
     */
    private CbbImageType cbbImageType;

    /**
     * 应用软件库类型IDV/VDI/TCI
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

    @Override
    @SuppressWarnings({"PMD.MethodParamAssertRule"})
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UamDeliveryGroupObjectSpecUpk that = (UamDeliveryGroupObjectSpecUpk) o;
        return Objects.equals(deliveryGroupId, that.deliveryGroupId) && cbbImageType == that.cbbImageType && osType == that.osType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryGroupId, cbbImageType, osType);
    }
}
