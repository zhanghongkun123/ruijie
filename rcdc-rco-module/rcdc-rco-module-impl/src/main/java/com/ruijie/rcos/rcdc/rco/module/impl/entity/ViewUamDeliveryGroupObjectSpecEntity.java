package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.UUID;

import javax.persistence.*;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/03 19:33
 *
 * @author coderLee23
 */
@IdClass(UamDeliveryGroupObjectSpecUpk.class)
@Table(name = "v_rco_uam_delivery_group_object_spec")
@Entity
public class ViewUamDeliveryGroupObjectSpecEntity {

    /**
     * 交付组id
     */
    @Id
    private UUID deliveryGroupId;

    /**
     * 操作系统类型 WIN7_64、WIN_XP、WIN7_32等
     */
    @Id
    @Enumerated(value = EnumType.STRING)
    private CbbImageType cbbImageType;

    /**
     * 应用软件库类型IDV/VDI/TCI
     */
    @Id
    @Enumerated(value = EnumType.STRING)
    private CbbOsType osType;

    @Version
    private Integer version;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
