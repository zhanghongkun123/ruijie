package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/26 12:06
 *
 * @author coderLee23
 */
public class GetGroupDesktopCountRequest {



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
    @ApiModelProperty(value = "操作系统类型")
    @Nullable
    private String osVersion;

    /**
     * 规格：镜像类型
     */
    @ApiModelProperty(value = "镜像类型")
    @Nullable
    private CbbImageType cbbImageType;

    /**
     * 交付组/测试组id
     */
    @ApiModelProperty(value = "交付组/测试组id")
    @Nullable
    private UUID currentTaskId;


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
    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(@Nullable UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    @Nullable
    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(@Nullable CbbOsType osType) {
        this.osType = osType;
    }

    @Nullable
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(@Nullable String osVersion) {
        this.osVersion = osVersion;
    }

    @Nullable
    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(@Nullable CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    @Nullable
    public UUID getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(@Nullable UUID currentTaskId) {
        this.currentTaskId = currentTaskId;
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
