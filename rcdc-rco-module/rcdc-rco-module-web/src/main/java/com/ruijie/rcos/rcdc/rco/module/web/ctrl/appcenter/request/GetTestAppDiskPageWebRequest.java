package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
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
public class GetTestAppDiskPageWebRequest extends PageWebRequest {

    /**
     * 测试组id
     */
    @ApiModelProperty(value = " 测试组id")
    @Nullable
    private UUID testId;

    /**
     * 虚拟机类型
     */
    @ApiModelProperty(value = " 虚拟机类型", name = "cbbImageType")
    @Nullable
    @JSONField(name = "cbbImageType")
    private List<CbbImageType> cbbImageTypeList;

    /**
     * 操作系统类型
     */
    @ApiModelProperty(value = "操作系统", name = "cbbOsType")
    @Nullable
    @JSONField(name = "osType")
    private List<CbbOsType> cbbOsTypeList;

    /**
     * 应用状态
     */
    @ApiModelProperty(value = "应用状态", name = "appStatus")
    @Nullable
    @JSONField(name = "appStatus")
    private List<AppStatusEnum> appStatusList;

    @ApiModelProperty(value = "云平台状态", name = "platformStatus")
    @JSONField(name = "platformStatus")
    @Nullable
    private List<CloudPlatformStatus> platformStatusList;

    @Nullable
    public UUID getTestId() {
        return testId;
    }

    public void setTestId(@Nullable UUID testId) {
        this.testId = testId;
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
    public List<AppStatusEnum> getAppStatusList() {
        return appStatusList;
    }

    public void setAppStatusList(@Nullable List<AppStatusEnum> appStatusList) {
        this.appStatusList = appStatusList;
    }

    @Nullable
    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(@Nullable List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }
}
