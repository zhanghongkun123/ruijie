package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 *
 * @author chenl
 */
public class DeskImageRelatedDTO {

    private UUID deskId;

    private String desktopName;

    private String desktopType;

    private CbbOsType desktopImageType;

    private String cbbImageType;

    private String osVersion;

    private UUID imageTemplateId;


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
}
