package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

import javax.persistence.*;
import java.util.UUID;


/**
 * Description: 镜像类型支持操作系统版本配置表(未配置操作系统则默认支持所有操作系统版本，有配置则仅支持配置版本)
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
@Entity
@Table(name = "t_rco_image_type_support_osversion")
public class ImageTypeSupportOsVersionEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CbbImageType cbbImageType;

    @Enumerated(EnumType.STRING)
    private CbbOsType osType;

    private String osVersion;

    @Version
    private Integer version;

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

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
