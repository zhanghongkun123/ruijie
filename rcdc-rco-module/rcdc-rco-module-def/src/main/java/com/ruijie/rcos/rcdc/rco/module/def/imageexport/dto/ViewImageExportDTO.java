package com.ruijie.rcos.rcdc.rco.module.def.imageexport.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbExportState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/16 20:22
 *
 * @author ketb
 */
@PageQueryDTOConfig(entityType = "ViewImageExportEntity")
public class ViewImageExportDTO extends PlatformBaseInfoDTO {

    @NotNull
    private UUID id;

    @NotNull
    private UUID imageTemplateId;

    @NotNull
    private String imageTemplateName;

    @NotNull
    private ImageTemplateState imageTemplateState;

    @NotNull
    private String exportFileName;

    @NotNull
    private String exportFileDesc;

    @NotNull
    private String exportFilePath;

    @Nullable
    private Long exportFileCapacity;

    @NotNull
    private CbbExportState exportState;

    @Nullable
    private Date exportTime;

    @NotNull
    private Integer version;

    @NotNull
    private UUID imageDiskId;

    @NotNull
    private CbbImageDiskType imageDiskType;

    @NotNull
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

    @Nullable
    public Long getExportFileCapacity() {
        return exportFileCapacity;
    }

    public void setExportFileCapacity(@Nullable Long exportFileCapacity) {
        this.exportFileCapacity = exportFileCapacity;
    }

    public CbbExportState getExportState() {
        return exportState;
    }

    public void setExportState(CbbExportState exportState) {
        this.exportState = exportState;
    }

    @Nullable
    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(@Nullable Date exportTime) {
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
}
