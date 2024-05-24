package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/20
 *
 * @author zjy
 */
public class ImageTemplateCompareDTO {

    private Boolean isSupportTc;

    private Boolean enableFullSystemDisk;

    private Integer currentImageSystemSize;

    private Integer currentImageDataSize;

    private CbbOsType cbbOsType;


    public ImageTemplateCompareDTO(Boolean isSupportTc, Boolean enableFullSystemDisk, Integer currentImageSystemSize,
            Integer currentImageDataSize) {
        this.isSupportTc = isSupportTc;
        this.enableFullSystemDisk = enableFullSystemDisk;
        this.currentImageSystemSize = currentImageSystemSize;
        this.currentImageDataSize = currentImageDataSize;
    }

    public Boolean getSupportTc() {
        return isSupportTc;
    }

    public void setSupportTc(Boolean supportTc) {
        isSupportTc = supportTc;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public Integer getCurrentImageSystemSize() {
        return currentImageSystemSize;
    }

    public void setCurrentImageSystemSize(Integer currentImageSystemSize) {
        this.currentImageSystemSize = currentImageSystemSize;
    }

    public Integer getCurrentImageDataSize() {
        return currentImageDataSize;
    }

    public void setCurrentImageDataSize(Integer currentImageDataSize) {
        this.currentImageDataSize = currentImageDataSize;
    }

    public CbbOsType getCbbOsType() {
        return cbbOsType;
    }

    public void setCbbOsType(CbbOsType cbbOsType) {
        this.cbbOsType = cbbOsType;
    }
}
