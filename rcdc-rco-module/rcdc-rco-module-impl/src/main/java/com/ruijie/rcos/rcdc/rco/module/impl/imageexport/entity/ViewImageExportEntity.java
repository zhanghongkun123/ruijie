package com.ruijie.rcos.rcdc.rco.module.impl.imageexport.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbExportState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/16 19:57
 *
 * @author ketb
 */
@Entity
@Table(name = "v_cbb_image_export")
public class ViewImageExportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID imageTemplateId;

    private String imageTemplateName;

    @Enumerated(EnumType.STRING)
    private ImageTemplateState imageTemplateState;

    private String exportFileName;

    private String exportFileDesc;

    private String exportFilePath;

    private Long exportFileCapacity;

    @Enumerated(EnumType.STRING)
    private CbbExportState exportState;

    private Date exportTime;

    @Version
    private Integer version;

    private UUID imageDiskId;

    @Enumerated(EnumType.STRING)
    private CbbImageDiskType imageDiskType;

    @Enumerated(EnumType.STRING)
    private CbbCpuArchType cpuArch;

    private UUID platformId;

    private String platformName;

    @Enumerated(value = EnumType.STRING)
    private CloudPlatformStatus platformStatus;

    @Enumerated(value = EnumType.STRING)
    private CloudPlatformType platformType;


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

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public String getExportFileDesc() {
        return exportFileDesc;
    }

    public void setExportFileDesc(String exportFileDesc) {
        this.exportFileDesc = exportFileDesc;
    }

    public String getExportFilePath() {
        return exportFilePath;
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }

    public Long getExportFileCapacity() {
        return exportFileCapacity;
    }

    public void setExportFileCapacity(Long exportFileCapacity) {
        this.exportFileCapacity = exportFileCapacity;
    }

    public CbbExportState getExportState() {
        return exportState;
    }

    public void setExportState(CbbExportState exportState) {
        this.exportState = exportState;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ImageTemplateState getImageTemplateState() {
        return imageTemplateState;
    }

    public void setImageTemplateState(ImageTemplateState imageTemplateState) {
        this.imageTemplateState = imageTemplateState;
    }

    public CbbImageDiskType getImageDiskType() {
        return imageDiskType;
    }

    public void setImageDiskType(CbbImageDiskType imageDiskType) {
        this.imageDiskType = imageDiskType;
    }

    public CbbCpuArchType getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(CbbCpuArchType cpuArch) {
        this.cpuArch = cpuArch;
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

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }
}
