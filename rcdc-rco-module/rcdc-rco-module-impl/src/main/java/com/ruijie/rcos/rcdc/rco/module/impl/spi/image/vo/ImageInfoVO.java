package com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTerminalLocalEditType;

/**
 * Description: VOI 镜像列表传输类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.25
 *
 * @author linhj
 */
public class ImageInfoVO {

    /**
     * 镜像的唯一标识
     */
    private UUID imageId;

    /**
     * 镜像名称
     */
    private String imageName;

    /**
     * 镜像类型
     */
    private CbbOsType osType;

    /**
     * ！！使用云桌面策略的配置！！系统盘大小
     */
    private Integer systemDiskSize;

    /**
     * 实际文件大小
     */
    private Integer baseFileSize;

    /**
     * 是否为黄金镜像
     */
    private Boolean supportGoldenImage;

    /**
     * 是否开启桌面重定向
     */
    private Boolean desktopRedirect;

    /**
     * 镜像类型
     */
    private CbbImageType cbbImageType;


    /**
     * 镜像关联的磁盘列表，包括系统盘和数据盘
     */
    private List<CbbImageDiskInfoDTO> imageDiskList;

    private ImageTerminalLocalEditType terminalLocalEditType;

    private String diskController;

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public Boolean getSupportGoldenImage() {
        return supportGoldenImage;
    }

    public void setSupportGoldenImage(Boolean supportGoldenImage) {
        this.supportGoldenImage = supportGoldenImage;
    }

    public Boolean getDesktopRedirect() {
        return desktopRedirect;
    }

    public void setDesktopRedirect(Boolean desktopRedirect) {
        this.desktopRedirect = desktopRedirect;
    }

    public Integer getBaseFileSize() {
        return baseFileSize;
    }

    public void setBaseFileSize(Integer baseFileSize) {
        this.baseFileSize = baseFileSize;
    }

    public List<CbbImageDiskInfoDTO> getImageDiskList() {
        return imageDiskList;
    }

    public void setImageDiskList(List<CbbImageDiskInfoDTO> imageDiskList) {
        this.imageDiskList = imageDiskList;
    }

    public ImageTerminalLocalEditType getTerminalLocalEditType() {
        return terminalLocalEditType;
    }

    public void setTerminalLocalEditType(ImageTerminalLocalEditType terminalLocalEditType) {
        this.terminalLocalEditType = terminalLocalEditType;
    }

    public String getDiskController() {
        return diskController;
    }

    public void setDiskController(String diskController) {
        this.diskController = diskController;
    }
}

