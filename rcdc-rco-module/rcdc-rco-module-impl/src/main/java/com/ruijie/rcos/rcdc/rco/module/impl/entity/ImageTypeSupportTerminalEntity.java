package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: 镜像支持的终端型号配置
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/26 18:42
 *
 * @author ypp
 */
@Entity
@Table(name = "t_rco_image_type_support_terminal")
public class ImageTypeSupportTerminalEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CbbImageType cbbImageType;

    @Enumerated(EnumType.STRING)
    private CbbOsType osType;

    private String productType;

    @Version
    private Integer version;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
