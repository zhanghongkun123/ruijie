package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.request;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ConfigVmForEditImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.CreateImageTemplateByOsFileWebRequest;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.lang.Nullable;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/7/24 <br>
 *
 * @author linrenjian
 */

public class CreateImageTemplateByOsFileRequest {

    /**
     * 镜像文件名ID
     */
    @NotNull
    private IdLabelEntry isoImageFileId;

    /***
     * 镜像名称
     */
    @NotBlank
    @TextShort
    @TextName
    private String imageName;

    /**
     * 描述
     */
    @TextMedium
    private String note;

    /***
     * 操作系统
     */
    @NotNull
    private CbbOsType imageSystemType;

    /**
     * 镜像类型
     */
    @NotNull
    private CbbImageType cbbImageType;


    /**
     * 是否支持镜像多版本
     */
    @NotNull
    private Boolean enableMultipleVersion;

    /**
     * 高级配置
     */
    @NotNull
    private ConfigVmForEditImageTemplateWebRequest advancedConfig;

    /**
     * 管理员ID
     */
    @NotNull
    private UUID adminId;

    /**
     * 镜像用途
     */
    @Nullable
    private ImageUsageTypeEnum imageUsage;

    /**
     * 构造CreateImageTemplateByOsFileRequest
     * 
     * @param createImageTemplateByOsFileWebRequest createImageTemplateByOsFileWebRequest
     */
    public void buildCreateImageTemplateByOsFileRequest(CreateImageTemplateByOsFileWebRequest createImageTemplateByOsFileWebRequest) {
        // 镜像文件名ID
        this.isoImageFileId = createImageTemplateByOsFileWebRequest.getIsoImageFileId();
        // 镜像名称
        this.imageName = createImageTemplateByOsFileWebRequest.getImageName();
        // 描述
        this.note = createImageTemplateByOsFileWebRequest.getNote();
        // 操作系统
        this.imageSystemType = createImageTemplateByOsFileWebRequest.getImageSystemType();
        // 镜像类型
        this.cbbImageType = createImageTemplateByOsFileWebRequest.getCbbImageType();
        // 高级配置
        this.advancedConfig = createImageTemplateByOsFileWebRequest.getAdvancedConfig();
        //镜像多版本, 未填则为false
        this.enableMultipleVersion = createImageTemplateByOsFileWebRequest.getEnableMultipleVersion() != null ?
                createImageTemplateByOsFileWebRequest.getEnableMultipleVersion() : false;
        this.imageUsage = createImageTemplateByOsFileWebRequest.getImageUsage();
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

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
