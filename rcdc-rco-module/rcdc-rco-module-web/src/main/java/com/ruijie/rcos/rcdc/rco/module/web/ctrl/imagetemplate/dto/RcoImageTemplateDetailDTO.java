package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.dto;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTemplatePublishTaskDTO;

/**
 * Description: RcoImageTemplateDetailDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class RcoImageTemplateDetailDTO {

    private Double memoryForGb;

    private String[] editErrorMessageArr;

    private Boolean enableGpu;

    private VgpuType vgpuType;

    private String graphicsMemorySize;

    private String vgpuModel;

    private ImageTemplatePublishTaskDTO publishTaskDTO;

    private Boolean guestToolVersionLastest;

    /**
     * 是否锁定，同步中的镜像需要锁定
     */
    private Boolean lock;

    public ImageTemplatePublishTaskDTO getPublishTaskDTO() {
        return publishTaskDTO;
    }

    public void setPublishTaskDTO(ImageTemplatePublishTaskDTO publishTaskDTO) {
        this.publishTaskDTO = publishTaskDTO;
    }

    public Double getMemoryForGb() {
        return memoryForGb;
    }

    public void setMemoryForGb(Double memoryForGb) {
        this.memoryForGb = memoryForGb;
    }

    public String[] getEditErrorMessageArr() {
        return editErrorMessageArr;
    }

    public void setEditErrorMessageArr(String[] editErrorMessageArr) {
        this.editErrorMessageArr = editErrorMessageArr;
    }

    public Boolean getEnableGpu() {
        return enableGpu;
    }

    public void setEnableGpu(Boolean enableGpu) {
        this.enableGpu = enableGpu;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public String getGraphicsMemorySize() {
        return graphicsMemorySize;
    }

    public void setGraphicsMemorySize(String graphicsMemorySize) {
        this.graphicsMemorySize = graphicsMemorySize;
    }

    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(String vgpuModel) {
        this.vgpuModel = vgpuModel;
    }

    public Boolean getGuestToolVersionLastest() {
        return guestToolVersionLastest;
    }

    public void setGuestToolVersionLastest(Boolean guestToolVersionLastest) {
        this.guestToolVersionLastest = guestToolVersionLastest;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }
}
