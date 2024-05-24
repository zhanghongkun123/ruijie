package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;


import java.io.Serializable;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageAdvanceDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbControlState;
import com.ruijie.rcos.sk.base.annotation.*;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年6月21日
 *
 * @author chenl
 */
public class CreateAppSoftwarePackageRequest implements Serializable {

    @ApiModelProperty(value = "应用软件包名称", required = true)
    @NotBlank
    @TextMedium
    @TextName
    private String name;

    @ApiModelProperty(value = "关联桌面模板ID", required = true)
    @NotNull
    private UUID imageTemplateId;

    @ApiModelProperty(value = "磁盘容量", required = true)
    @NotNull
    @Range(min = "1")
    private Integer diskSize;

    @ApiModelProperty(value = "描述")
    @Nullable
    @Size(max = 128)
    private String desc;

    @ApiModelProperty(value = "关联网络策略ID")
    @Nullable
    private UUID networkId;

    @ApiModelProperty(value = "临时虚机IP")
    @Nullable
    private String ip;

    @ApiModelProperty(value = "高级配置")
    @NotNull
    private AppSoftwarePackageAdvanceDTO advanced;


    /**
     * @return AppSoftwarePackageDTO
     */
    public AppSoftwarePackageDTO convertToDTO() {
        AppSoftwarePackageDTO appSoftwarePackageDTO = new AppSoftwarePackageDTO();
        appSoftwarePackageDTO.setControlState(CbbControlState.NONE);
        appSoftwarePackageDTO.setDiskSize(this.diskSize);
        appSoftwarePackageDTO.setName(this.name);
        appSoftwarePackageDTO.setDescription(this.desc);
        appSoftwarePackageDTO.setEnableFullClone(true);
        appSoftwarePackageDTO.setImageTemplateId(this.imageTemplateId);
        appSoftwarePackageDTO.setAdvanced(advanced);
        return appSoftwarePackageDTO;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public AppSoftwarePackageAdvanceDTO getAdvanced() {
        return advanced;
    }

    public void setAdvanced(AppSoftwarePackageAdvanceDTO advanced) {
        this.advanced = advanced;
    }

}
