package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

import javax.persistence.*;
import java.util.UUID;

/**
 * 云桌面镜像视图查询对象
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author
 */
@Entity
@Table(name = "v_cbb_desktop_image_related")
public class ViewDesktopImageRelatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID deskId;

    private String desktopName;

    private String desktopType;

    @Enumerated(EnumType.STRING)
    private CbbOsType desktopImageType;

    /**
     * 镜像类型 IDV/VOI/VDI
     */
    private String cbbImageType;

    private String osVersion;

    private UUID imageTemplateId;

    @Version
    private int version;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public CbbOsType getDesktopImageType() {
        return desktopImageType;
    }

    public void setDesktopImageType(CbbOsType desktopImageType) {
        this.desktopImageType = desktopImageType;
    }

    public String getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(String cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
