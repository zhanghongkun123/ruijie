package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.imagetemplate;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskImageTemplateState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月19日
 * 
 * @author zhfw
 */
public class ImageTemplateVO {

    private UUID id;
    
    private String imageName;
    
    private String imageOsName;
    
    private Integer systemDisk;
    
    private CbbCloudDeskImageTemplateState imageState;

    private UUID rootImageId;

    private String rootImageName;

    private ImageRoleType imageRoleType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageOsName() {
        return imageOsName;
    }

    public void setImageOsName(String imageOsName) {
        this.imageOsName = imageOsName;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public CbbCloudDeskImageTemplateState getImageState() {
        return imageState;
    }

    public void setImageState(CbbCloudDeskImageTemplateState imageState) {
        this.imageState = imageState;
    }

    public UUID getRootImageId() {
        return rootImageId;
    }

    public void setRootImageId(UUID rootImageId) {
        this.rootImageId = rootImageId;
    }

    public String getRootImageName() {
        return rootImageName;
    }

    public void setRootImageName(String rootImageName) {
        this.rootImageName = rootImageName;
    }

    public ImageRoleType getImageRoleType() {
        return imageRoleType;
    }

    public void setImageRoleType(ImageRoleType imageRoleType) {
        this.imageRoleType = imageRoleType;
    }
}
