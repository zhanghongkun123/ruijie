package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.osfile;

import java.util.UUID;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: EditOsFileWebRequest Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/04/22
 *
 * @author chixin
 */
public class EditOsFileWebRequest implements WebRequest {

    @ApiModelProperty(value = "ID")
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "描述")
    @TextMedium
    private String note;

    @ApiModelProperty(value = "磁盘类型")
    @Nullable
    private CbbImageDiskType imageDiskType;

    @ApiModelProperty(value = "cpu架构类型")
    @Nullable
    private CbbCpuArchType cpuArch;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Nullable
    public CbbImageDiskType getImageDiskType() {
        return imageDiskType;
    }

    public void setImageDiskType(@Nullable CbbImageDiskType imageDiskType) {
        this.imageDiskType = imageDiskType;
    }

    @Nullable
    public CbbCpuArchType getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(@Nullable CbbCpuArchType cpuArch) {
        this.cpuArch = cpuArch;
    }
}
