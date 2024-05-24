package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28 16:40
 *
 * @author coderLee23
 */
public class CreateAppDiskDeliveryGroupRequest implements WebRequest {

    /**
     * 交付组名称
     */
    @ApiModelProperty(value = "交付组名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String deliveryGroupName;

    /**
     * 镜像模板
     */
    @ApiModelProperty(value = "镜像模板-前端无需传值,后端校验后自动赋值")
    @Nullable
    private UUID imageTemplateId;

    /**
     * 规格：操作系统类型
     */
    @ApiModelProperty(value = "操作系统类型-前端无需传值,后端校验后自动赋值")
    @Nullable
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    @ApiModelProperty(value = "操作系统版本号-前端无需传值,后端校验后自动赋值")
    @Nullable
    private String osVersion;

    /**
     * 规格：虚机类型
     */
    @ApiModelProperty(value = "虚机类型-前端无需传值,后端校验后自动赋值")
    @Nullable
    private CbbImageType cbbImageType;

    /**
     * 交付应用
     */
    @ApiModelProperty(value = "交付应用", required = true)
    @NotEmpty
    private List<UUID> appIdList;

    /**
     * 交付组名称
     */
    @ApiModelProperty(value = "交付对象", required = true)
    @NotEmpty
    private List<UUID> cloudDesktopIdList;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Nullable
    private String deliveryGroupDesc;

    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    @Nullable
    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(@Nullable UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    @Nullable
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(@Nullable String osVersion) {
        this.osVersion = osVersion;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public List<UUID> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<UUID> appIdList) {
        this.appIdList = appIdList;
    }

    public String getDeliveryGroupDesc() {
        return deliveryGroupDesc;
    }

    public void setDeliveryGroupDesc(String deliveryGroupDesc) {
        this.deliveryGroupDesc = deliveryGroupDesc;
    }

    public List<UUID> getCloudDesktopIdList() {
        return cloudDesktopIdList;
    }

    public void setCloudDesktopIdList(List<UUID> cloudDesktopIdList) {
        this.cloudDesktopIdList = cloudDesktopIdList;
    }
}
