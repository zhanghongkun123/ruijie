package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageFormat;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsFileState;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 镜像文件DTO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29
 *
 * @author xiao'yong'deng
 */
public class RestOsFileDTO {

    private UUID id;

    private Date uploadTime;

    private String imageFileName;

    private long imageFileSize;

    private Long virtualSize;

    private OsFileState imageFileState;

    private String note;

    private CbbImageFormat cbbImageFormat;

    private CbbImageDiskType imageDiskType;

    private ImageUsageTypeEnum imageUsage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public long getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(long imageFileSize) {
        this.imageFileSize = imageFileSize;
    }

    public OsFileState getImageFileState() {
        return imageFileState;
    }

    public void setImageFileState(OsFileState imageFileState) {
        this.imageFileState = imageFileState;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CbbImageFormat getCbbImageFormat() {
        return cbbImageFormat;
    }

    public void setCbbImageFormat(CbbImageFormat cbbImageFormat) {
        this.cbbImageFormat = cbbImageFormat;
    }

    public Long getVirtualSize() {
        return virtualSize;
    }

    public void setVirtualSize(Long virtualSize) {
        this.virtualSize = virtualSize;
    }

    public CbbImageDiskType getImageDiskType() {
        return imageDiskType;
    }

    public void setImageDiskType(CbbImageDiskType imageDiskType) {
        this.imageDiskType = imageDiskType;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }
}
