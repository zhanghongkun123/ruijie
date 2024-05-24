package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ConfigVmForEditImageTemplateWebRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/1
 *
 * @author wjp
 */
public class CreateImageTemplateByCloneDTO {

    private UUID id;

    private String imageName;

    private String note;

    private ConfigVmForEditImageTemplateWebRequest advancedConfig;

    private CbbImageType cbbImageType;

    private Boolean enableMultipleVersion;

    /**
     * 管理员ID
     */
    @NotNull
    private UUID adminId;

    // 新镜像的镜像类型
    private ImageUsageTypeEnum imageUsage;

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
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

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }
}
