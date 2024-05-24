package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/3/29 <br>
 *
 * @author yyz
 */
public class CreateImageTemplateByOsFileWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像文件名ID", required = true)
    @NotNull
    private IdLabelEntry isoImageFileId;

    @ApiModelProperty(value = "镜像名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String imageName;

    @ApiModelProperty(value = "描述", required = false)
    @TextMedium
    private String note;

    @ApiModelProperty(value = "操作系统", required = true)
    @NotNull
    private CbbOsType imageSystemType;

    @ApiModelProperty(value = "镜像类型", required = true)
    @NotNull
    private CbbImageType cbbImageType;

    @ApiModelProperty(value = "是否支持镜像多版本/在线发布", required = true)
    @Nullable
    private Boolean enableMultipleVersion;

    @ApiModelProperty(value = "高级配置", required = true)
    @NotNull
    private ConfigVmForEditImageTemplateWebRequest advancedConfig;

    @ApiModelProperty(value = "镜像用途", required = true)
    @NotNull
    private ImageUsageTypeEnum imageUsage;

    public IdLabelEntry getIsoImageFileId() {
        return isoImageFileId;
    }

    public void setIsoImageFileId(IdLabelEntry isoImageFileId) {
        this.isoImageFileId = isoImageFileId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CbbOsType getImageSystemType() {
        return imageSystemType;
    }

    public void setImageSystemType(CbbOsType imageSystemType) {
        this.imageSystemType = imageSystemType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ConfigVmForEditImageTemplateWebRequest getAdvancedConfig() {
        return advancedConfig;
    }

    public void setAdvancedConfig(ConfigVmForEditImageTemplateWebRequest advancedConfig) {
        this.advancedConfig = advancedConfig;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public Boolean getEnableMultipleVersion() {
        return enableMultipleVersion;
    }

    public void setEnableMultipleVersion(Boolean enableMultipleVersion) {
        this.enableMultipleVersion = enableMultipleVersion;
    }

    @Nullable
    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(@Nullable ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }
}
