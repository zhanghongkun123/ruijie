package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.sk.base.annotation.*;
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
 * @author chenl
 */
public class CreatePushInstallPackageAppDiskDeliveryGroupRequest implements WebRequest {

    /**
     * 交付组名称
     */
    @ApiModelProperty(value = "交付组名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String deliveryGroupName;

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

    /**
     * 应用推送设置
     */
    @ApiModelProperty(value = "应用推送设置", required = true)
    @NotNull
    private PushAppConfig pushAppConfig;

    /**
     * 操作系统类型【推送包交付组】
     */
    @ApiModelProperty(value = "操作系统类型【推送包交付组】")
    @Nullable
    private OsPlatform osPlatform;


    public PushAppConfig getPushAppConfig() {
        return pushAppConfig;
    }

    public void setPushAppConfig(PushAppConfig pushAppConfig) {
        this.pushAppConfig = pushAppConfig;
    }


    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    public List<UUID> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<UUID> appIdList) {
        this.appIdList = appIdList;
    }

    public List<UUID> getCloudDesktopIdList() {
        return cloudDesktopIdList;
    }

    public void setCloudDesktopIdList(List<UUID> cloudDesktopIdList) {
        this.cloudDesktopIdList = cloudDesktopIdList;
    }

    @Nullable
    public String getDeliveryGroupDesc() {
        return deliveryGroupDesc;
    }

    public void setDeliveryGroupDesc(@Nullable String deliveryGroupDesc) {
        this.deliveryGroupDesc = deliveryGroupDesc;
    }

    @Nullable
    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(@Nullable OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }
}
