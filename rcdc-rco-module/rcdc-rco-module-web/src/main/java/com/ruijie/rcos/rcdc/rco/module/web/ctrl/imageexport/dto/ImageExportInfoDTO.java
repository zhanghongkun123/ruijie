package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbExportState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/21 9:48
 *
 * @author ketb
 */
public class ImageExportInfoDTO extends PlatformBaseInfoDTO {

    @ApiModelProperty(value = "导出镜像任务id")
    private UUID id;

    @ApiModelProperty(value = "关联镜像模板id")
    private UUID imageTemplateId;

    @ApiModelProperty(value = "导出状态")
    private CbbExportState exportStatus;

    @ApiModelProperty(value = "镜像名称")
    private String imageName;

    @ApiModelProperty(value = "镜像文件名称")
    private String imageFileName;

    @ApiModelProperty(value = "镜像状态")
    private ImageTemplateState imageTemplateState;

    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    @ApiModelProperty(value = "导出描述")
    private String description;

    @ApiModelProperty(value = "导出时间")
    private Date exportTime;

    @ApiModelProperty(value = "磁盘状态")
    private CbbImageDiskType imageDiskType;

    @ApiModelProperty(value = "磁盘ID")
    private UUID imageDiskId;

    @ApiModelProperty(value = "CPU架构类型")
    private CbbCpuArchType cpuArch;


    public UUID getImageDiskId() {
        return imageDiskId;
    }

    public void setImageDiskId(UUID imageDiskId) {
        this.imageDiskId = imageDiskId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public CbbExportState getExportStatus() {
        return exportStatus;
    }

    public void setExportStatus(CbbExportState exportStatus) {
        this.exportStatus = exportStatus;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public CbbImageDiskType getImageDiskType() {
        return imageDiskType;
    }

    public void setImageDiskType(CbbImageDiskType imageDiskType) {
        this.imageDiskType = imageDiskType;
    }

    public ImageTemplateState getImageTemplateState() {
        return imageTemplateState;
    }

    public void setImageTemplateState(ImageTemplateState imageTemplateState) {
        this.imageTemplateState = imageTemplateState;
    }

    public CbbCpuArchType getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(CbbCpuArchType cpuArch) {
        this.cpuArch = cpuArch;
    }
}
