package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response;

import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.PushAppConfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月24日
 *
 * @author zhk
 */
@ApiModel("测试任务信息返回")
public class TestInfoResponse extends PlatformBaseInfoDTO {

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "任务描述")
    private String description;

    @ApiModelProperty(value = "应用ID列表")
    private UUID[] appIdArr;

    @ApiModelProperty(value = "桌面ID列表")
    private UUID[] deskIdArr;

    @ApiModelProperty(value = "推送配置")
    PushAppConfig pushAppConfig;

    @ApiModelProperty(value = "id")
    private UUID id;

    @ApiModelProperty(value = "原因")
    private String reason;

    @ApiModelProperty(value = "状态")
    private TestTaskStateEnum state;

    /**
     * 镜像模板名称
     */
    @ApiModelProperty(value = "镜像模板名称")
    private String imageTemplateName;

    @ApiModelProperty(value = "镜像类型")
    private CbbImageType appSoftwarePackageType;

    @ApiModelProperty(value = "操作系统")
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    @ApiModelProperty(value = "操作系统版本号")
    private String osVersion;

    @ApiModelProperty(value = "应用软件包是否更新")
    private Boolean hasAppUpdate;

    @ApiModelProperty(value = "应用磁盘")
    private String appDisk;

    @ApiModelProperty(value = "是否所有桌面结束")
    private Boolean isAllDeskFinished;

    @ApiModelProperty(value = "源镜像id")
    private UUID rootImageId;

    @ApiModelProperty(value = "源镜像名称")
    private String rootImageName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID[] getAppIdArr() {
        return appIdArr;
    }

    public void setAppIdArr(UUID[] appIdArr) {
        this.appIdArr = appIdArr;
    }

    public UUID[] getDeskIdArr() {
        return deskIdArr;
    }

    public void setDeskIdArr(UUID[] deskIdArr) {
        this.deskIdArr = deskIdArr;
    }

    public PushAppConfig getPushAppConfig() {
        return pushAppConfig;
    }

    public void setPushAppConfig(PushAppConfig pushAppConfig) {
        this.pushAppConfig = pushAppConfig;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TestTaskStateEnum getState() {
        return state;
    }

    public void setState(TestTaskStateEnum state) {
        this.state = state;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public CbbImageType getAppSoftwarePackageType() {
        return appSoftwarePackageType;
    }

    public void setAppSoftwarePackageType(CbbImageType appSoftwarePackageType) {
        this.appSoftwarePackageType = appSoftwarePackageType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Boolean getHasAppUpdate() {
        return hasAppUpdate;
    }

    public void setHasAppUpdate(Boolean hasAppUpdate) {
        this.hasAppUpdate = hasAppUpdate;
    }

    public String getAppDisk() {
        return appDisk;
    }

    public void setAppDisk(String appDisk) {
        this.appDisk = appDisk;
    }

    public Boolean getIsAllDeskFinished() {
        return isAllDeskFinished;
    }

    public void setAllDeskFinished(Boolean allDeskFinished) {
        isAllDeskFinished = allDeskFinished;
    }

    public UUID getRootImageId() {
        return rootImageId;
    }

    public void setRootImageId(UUID rootImageId) {
        this.rootImageId = rootImageId;
    }

    public String getRootImageName() {
        return rootImageName;
    }

    public void setRootImageName(String rootImageName) {
        this.rootImageName = rootImageName;
    }
}
