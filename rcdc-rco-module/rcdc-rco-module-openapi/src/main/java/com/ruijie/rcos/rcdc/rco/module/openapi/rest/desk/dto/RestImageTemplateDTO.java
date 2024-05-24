package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class RestImageTemplateDTO {

    private UUID id;

    private String imageName;

    private CbbImageType imageType;

    private ImageTemplateState imageState;

    private Integer imageSystemSize;

    private Integer cpu;

    private Integer memory;

    private Integer cloudDesktopNumOfAppLayer;

    private Integer cloudDesktopNumOfRecoverable;

    private Integer cloudDesktopNumOfPersonal;

    private UUID networkId;

    private String note;

    private Date createTime;

    /**
     * 计算集群ID
     */
    private UUID clusterId;

    /**
     * 存储位置ID
     */
    private UUID storagePoolId;

    /**
     * 是否启用镜像多版本
     */
    private Boolean enableMultipleVersion;

    /**
     * 是否镜像多版本最新版本
     */
    private Boolean isNewestVersion;

    /**
     * 镜像根ID
     */
    private UUID rootImageId;

    /**
     * 镜像根名称
     */
    private String rootImageName;

    /**
     * 平台ID
     */
    private UUID platformId;

    /**
     * 云平台名称
     */
    private String platformName;


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

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ImageTemplateState getImageState() {
        return imageState;
    }

    public void setImageState(ImageTemplateState imageState) {
        this.imageState = imageState;
    }

    public CbbImageType getImageType() {
        return imageType;
    }

    public void setImageType(CbbImageType imageType) {
        this.imageType = imageType;
    }

    public Integer getImageSystemSize() {
        return imageSystemSize;
    }

    public void setImageSystemSize(Integer imageSystemSize) {
        this.imageSystemSize = imageSystemSize;
    }

    public Integer getCloudDesktopNumOfRecoverable() {
        return cloudDesktopNumOfRecoverable;
    }

    public void setCloudDesktopNumOfRecoverable(Integer cloudDesktopNumOfRecoverable) {
        this.cloudDesktopNumOfRecoverable = cloudDesktopNumOfRecoverable;
    }

    public Integer getCloudDesktopNumOfPersonal() {
        return cloudDesktopNumOfPersonal;
    }

    public void setCloudDesktopNumOfPersonal(Integer cloudDesktopNumOfPersonal) {
        this.cloudDesktopNumOfPersonal = cloudDesktopNumOfPersonal;
    }

    public Integer getCloudDesktopNumOfAppLayer() {
        return cloudDesktopNumOfAppLayer;
    }

    public void setCloudDesktopNumOfAppLayer(Integer cloudDesktopNumOfAppLayer) {
        this.cloudDesktopNumOfAppLayer = cloudDesktopNumOfAppLayer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(UUID storagePoolId) {
        this.storagePoolId = storagePoolId;
    }

    public Boolean getEnableMultipleVersion() {
        return enableMultipleVersion;
    }

    public void setEnableMultipleVersion(Boolean enableMultipleVersion) {
        this.enableMultipleVersion = enableMultipleVersion;
    }

    public Boolean getNewestVersion() {
        return isNewestVersion;
    }

    public void setNewestVersion(Boolean newestVersion) {
        isNewestVersion = newestVersion;
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

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}