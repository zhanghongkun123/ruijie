package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 14:05
 *
 * @author coderLee23
 */
public class GetAppDiskPageWebRequest extends PageWebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id")
    @Nullable
    private UUID deliveryGroupId;

    /**
     * 虚拟机类型
     */
    @ApiModelProperty(value = "虚拟机类型", name = "cbbImageType")
    @Nullable
    @JSONField(name = "cbbImageType")
    private List<CbbImageType> cbbImageTypeList;

    /**
     * 虚拟机类型
     */
    @ApiModelProperty(value = "操作系统", name = "cbbOsType")
    @Nullable
    @JSONField(name = "osType")
    private List<CbbOsType> cbbOsTypeList;

    @ApiModelProperty(value = "云平台状态", name = "platformStatus")
    @JSONField(name = "platformStatus")
    @Nullable
    private List<CloudPlatformStatus> platformStatusList;

    @Nullable
    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(@Nullable UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    @Nullable
    public List<CbbImageType> getCbbImageTypeList() {
        return cbbImageTypeList;
    }

    public void setCbbImageTypeList(@Nullable List<CbbImageType> cbbImageTypeList) {
        this.cbbImageTypeList = cbbImageTypeList;
    }

    @Nullable
    public List<CbbOsType> getCbbOsTypeList() {
        return cbbOsTypeList;
    }

    public void setCbbOsTypeList(@Nullable List<CbbOsType> cbbOsTypeList) {
        this.cbbOsTypeList = cbbOsTypeList;
    }

    @Nullable
    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(@Nullable List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }
}
