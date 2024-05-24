package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PartType;
import com.ruijie.rcos.rcdc.task.ext.module.def.dto.StateMachineConfigAndBatchTaskItemDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月07日
 *
 * @author xgx
 */
public class CreateImageTemplateDTO extends StateMachineConfigAndBatchTaskItemDTO {
    @NotNull
    private String id;

    @NotNull
    private CbbImageType cbbImageType;

    @NotBlank
    @TextName
    private String imageName;

    @NotBlank
    private String isoImageFileName;

    @NotNull
    private CbbOsType imageSystemType;

    @Nullable
    private PartType partType;

    @TextMedium
    private String note;


    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public CbbOsType getImageSystemType() {
        return imageSystemType;
    }

    public void setImageSystemType(CbbOsType imageSystemType) {
        this.imageSystemType = imageSystemType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIsoImageFileName() {
        return isoImageFileName;
    }

    public void setIsoImageFileName(String isoImageFileName) {
        this.isoImageFileName = isoImageFileName;
    }


    @Nullable
    public PartType getPartType() {
        return partType;
    }

    public void setPartType(@Nullable PartType partType) {
        this.partType = partType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
