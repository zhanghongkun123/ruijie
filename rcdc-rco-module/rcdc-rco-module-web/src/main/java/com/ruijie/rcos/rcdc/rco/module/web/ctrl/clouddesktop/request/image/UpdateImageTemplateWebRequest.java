package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import java.util.UUID;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
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
public class UpdateImageTemplateWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像ID", required = true)
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

    @ApiModelProperty(value = "操作系统", required = true)
    @NotNull
    private CbbOsType imageSystemType;

    @ApiModelProperty(value = "高级配置", required = true)
    @NotNull
    private ConfigVmForEditImageTemplateWebRequest advancedConfig;

    @ApiModelProperty(value = "镜像用途", required = false)
    @Nullable
    private ImageUsageTypeEnum imageUsage;

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

    public ConfigVmForEditImageTemplateWebRequest getAdvancedConfig() {
        return advancedConfig;
    }

    public void setAdvancedConfig(ConfigVmForEditImageTemplateWebRequest advancedConfig) {
        this.advancedConfig = advancedConfig;
    }

    @Nullable
    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(@Nullable ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }
}
