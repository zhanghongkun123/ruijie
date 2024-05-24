package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbControlState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;

import java.util.Date;
import java.util.UUID;


/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/22 22:58
 *
 * @author coderLee23
 */
public class UamAppDiskDTO extends PlatformBaseInfoDTO {

    private UUID id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用类型
     */
    private AppTypeEnum appType;

    /**
     * 操作系统类型 WIN7_64、WIN_XP、WIN7_32等
     */
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    private String osVersion;

    /**
     * 应用软件库类型IDV/VDI/TCI
     */
    private CbbImageType cbbImageType;


    /**
     * 镜像模板id
     */
    private UUID imageTemplateId;

    /**
     * 镜像模板名称
     */
    private String imageTemplateName;


    /**
     * 应用状态
     */
    private AppStatusEnum appStatus;


    /**
     * 允许回滚[应用软件包支持]
     */
    private Boolean enableRollback;

    /**
     * 管理测试组，且应用测试中的数量
     */
    private Integer testProcessingCount;

    /**
     * 关联测试组，且测试结束的数量
     */
    private Integer testEndCount;

    /**
     * 关联的交付组数量
     */
    private Integer deliveryGroupCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 应用描述
     */
    private String appDesc;

    /**
     * 备份状态
     */
    private CbbControlState controlState;

    /**
     * 已经完成桌面数量
     */
    private Integer testingDesktopCount;

    private UUID rootImageId;

    private String rootImageName;

    private ImageRoleType imageRoleType;

    private Boolean isGlobalImage;

    private Boolean enableGlobalImage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
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

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public AppStatusEnum getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(AppStatusEnum appStatus) {
        this.appStatus = appStatus;
    }

    public Boolean getEnableRollback() {
        return enableRollback;
    }

    public void setEnableRollback(Boolean enableRollback) {
        this.enableRollback = enableRollback;
    }

    public Integer getTestProcessingCount() {
        return testProcessingCount;
    }

    public void setTestProcessingCount(Integer testProcessingCount) {
        this.testProcessingCount = testProcessingCount;
    }

    public Integer getTestEndCount() {
        return testEndCount;
    }

    public void setTestEndCount(Integer testEndCount) {
        this.testEndCount = testEndCount;
    }

    public Integer getDeliveryGroupCount() {
        return deliveryGroupCount;
    }

    public void setDeliveryGroupCount(Integer deliveryGroupCount) {
        this.deliveryGroupCount = deliveryGroupCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public CbbControlState getControlState() {
        return controlState;
    }

    public void setControlState(CbbControlState controlState) {
        this.controlState = controlState;
    }


    public Integer getTestingDesktopCount() {
        return testingDesktopCount;
    }

    public void setTestingDesktopCount(Integer testingDesktopCount) {
        this.testingDesktopCount = testingDesktopCount;
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

    public ImageRoleType getImageRoleType() {
        return imageRoleType;
    }

    public void setImageRoleType(ImageRoleType imageRoleType) {
        this.imageRoleType = imageRoleType;
    }

    public Boolean getIsGlobalImage() {
        return isGlobalImage;
    }

    public void setIsGlobalImage(Boolean globalImage) {
        isGlobalImage = globalImage;
    }

    public Boolean getEnableGlobalImage() {
        return enableGlobalImage;
    }

    public void setEnableGlobalImage(Boolean enableGlobalImage) {
        this.enableGlobalImage = enableGlobalImage;
    }
}
