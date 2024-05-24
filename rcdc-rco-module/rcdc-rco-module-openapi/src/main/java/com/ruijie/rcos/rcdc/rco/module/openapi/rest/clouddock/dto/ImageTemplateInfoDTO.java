package com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PartType;

import java.util.List;
import java.util.UUID;

/**
 * Description: 镜像模板、磁盘和镜像文件信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/4
 *
 * @author chenjuan
 */
public class ImageTemplateInfoDTO {

    private UUID id;

    private String imageTemplateName;

    private String imageFileName;

    private ImageTemplateState imageTemplateState;

    private Integer vmCpuCoreCount;

    private Integer vmMemorySize;

    private CbbOsType osType;

    /**
     * 镜像文件总大小
     */
    private Long totalSize;

    /**
     * 镜像版本ID
     */
    private UUID lastRecoveryPointId;

    private CbbImageType cbbImageType;

    private List<ImageFileDTO> imageFileList;

    private PartType partType;

    private String guestToolVersion;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public ImageTemplateState getImageTemplateState() {
        return imageTemplateState;
    }

    public void setImageTemplateState(ImageTemplateState imageTemplateState) {
        this.imageTemplateState = imageTemplateState;
    }

    public Integer getVmCpuCoreCount() {
        return vmCpuCoreCount;
    }

    public void setVmCpuCoreCount(Integer vmCpuCoreCount) {
        this.vmCpuCoreCount = vmCpuCoreCount;
    }

    public Integer getVmMemorySize() {
        return vmMemorySize;
    }

    public void setVmMemorySize(Integer vmMemorySize) {
        this.vmMemorySize = vmMemorySize;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public UUID getLastRecoveryPointId() {
        return lastRecoveryPointId;
    }

    public void setLastRecoveryPointId(UUID lastRecoveryPointId) {
        this.lastRecoveryPointId = lastRecoveryPointId;
    }

    public List<ImageFileDTO> getImageFileList() {
        return imageFileList;
    }

    public void setImageFileList(List<ImageFileDTO> imageFileList) {
        this.imageFileList = imageFileList;
    }

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public String getGuestToolVersion() {
        return guestToolVersion;
    }

    public void setGuestToolVersion(String guestToolVersion) {
        this.guestToolVersion = guestToolVersion;
    }
}
