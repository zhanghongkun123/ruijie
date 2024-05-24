package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/10 19:54
 *
 * @author coderLee23
 */
public class SelectGroupDesktopRequest implements WebRequest {

    /**
     * 用户组id
     */
    @ApiModelProperty(value = " 用户组id", required = false)
    @Nullable
    private UUID groupId;

    /**
     * 交付组/测试组id
     */
    @ApiModelProperty(value = "交付组/测试组id")
    @Nullable
    private UUID currentTaskId;

    /**
     * 镜像模板
     */
    @ApiModelProperty(value = "镜像模板")
    @Nullable
    private UUID imageTemplateId;

    /**
     * 规格：操作系统类型
     */
    @ApiModelProperty(value = "操作系统类型")
    @Nullable
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    @ApiModelProperty(value = "操作系统版本号")
    @Nullable
    private String osVersion;

    /**
     * 规格：镜像类型
     */
    @ApiModelProperty(value = "镜像类型")
    @Nullable
    private CbbImageType cbbImageType;


    @ApiModelProperty(value = "应用交付类型", required = true)
    @NotNull
    private AppDeliveryTypeEnum appDeliveryType;

    /**
     * 操作系统类型【WINDOWS、LINUX】
     */
    @ApiModelProperty(value = " 操作系统类型")
    @Nullable
    private OsPlatform osPlatform;

    @Nullable
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable UUID groupId) {
        this.groupId = groupId;
    }

    @Nullable
    public UUID getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(@Nullable UUID currentTaskId) {
        this.currentTaskId = currentTaskId;
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

    public AppDeliveryTypeEnum getAppDeliveryType() {
        return appDeliveryType;
    }

    public void setAppDeliveryType(AppDeliveryTypeEnum appDeliveryType) {
        this.appDeliveryType = appDeliveryType;
    }

    @Nullable
    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(@Nullable OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }
}
