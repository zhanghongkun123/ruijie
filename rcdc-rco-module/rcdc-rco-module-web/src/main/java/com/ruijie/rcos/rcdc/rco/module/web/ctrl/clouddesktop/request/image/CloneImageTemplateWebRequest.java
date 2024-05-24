package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import java.util.UUID;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/3/29 <br>
 *
 * @author yyz
 */
public class CloneImageTemplateWebRequest implements WebRequest {

    @ApiModelProperty(value = "源镜像ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "镜像名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String imageName;

    @ApiModelProperty(value = "描述", required = false)
    @TextMedium
    private String note;

    @ApiModelProperty(value = "高级配置", required = true)
    @NotNull
    private ConfigVmForEditImageTemplateWebRequest advancedConfig;

    @ApiModelProperty(value = "镜像类型", required = true)
    @NotNull
    private CbbImageType cbbImageType;

    @ApiModelProperty(value = "是否支持镜像多版本/在线发布", required = false)
    @Nullable
    private Boolean enableMultipleVersion;

    @ApiModelProperty(value = "镜像用途", required = false)
    @Nullable
    private ImageUsageTypeEnum imageUsage;

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ConfigVmForEditImageTemplateWebRequest getAdvancedConfig() {
        return advancedConfig;
    }

    public void setAdvancedConfig(ConfigVmForEditImageTemplateWebRequest advancedConfig) {
        this.advancedConfig = advancedConfig;
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
