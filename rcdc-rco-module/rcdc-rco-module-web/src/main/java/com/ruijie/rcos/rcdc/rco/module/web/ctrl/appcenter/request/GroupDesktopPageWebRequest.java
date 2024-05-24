package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月23日
 *
 * @author zhk
 */
public class GroupDesktopPageWebRequest extends PageWebRequest {


    /**
     * 镜像模板id
     */
    @ApiModelProperty(value = "镜像模板id")
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

    /**
     * 终端组id
     */
    @ApiModelProperty(value = " 终端组id")
    @Nullable
    private UUID groupId;

    /**
     * 测试组/交互组id
     */
    @ApiModelProperty(value = "测试组/交互组id")
    @Nullable
    private UUID currentTaskId;

    /**
     * 应用状态
     */
    @ApiModelProperty(value = " 云桌面状态", name = "deskStateList")
    @Nullable
    @JSONField(name = "deskState")
    private List<CbbCloudDeskState> deskStateList;


    @ApiModelProperty(value = "应用交付类型", required = true)
    @NotNull
    private AppDeliveryTypeEnum appDeliveryType;

    /**
     * 操作系统类型【WINDOWS、LINUX】
     */
    @ApiModelProperty(value = " 操作系统类型")
    @Nullable
    private OsPlatform osPlatform;

    @ApiModelProperty(value = "云平台状态", name = "platformStatus")
    @JSONField(name = "platformStatus")
    @Nullable
    private List<CloudPlatformStatus> platformStatusList;

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

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
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

    public List<CbbCloudDeskState> getDeskStateList() {
        return deskStateList;
    }

    public void setDeskStateList(List<CbbCloudDeskState> deskStateList) {
        this.deskStateList = deskStateList;
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

    @Nullable
    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(@Nullable List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }
}
