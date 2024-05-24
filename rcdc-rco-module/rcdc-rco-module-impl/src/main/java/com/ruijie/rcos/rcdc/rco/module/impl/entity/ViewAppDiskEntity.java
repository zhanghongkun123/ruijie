package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbControlState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


/**
 * Description: 应用主表
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/22 12:43
 *
 * @author coderLee23
 */
@Table(name = "v_rco_uam_app_disk")
@Entity
public class ViewAppDiskEntity {
    @Id
    private UUID id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用类型
     */
    @Enumerated(value = EnumType.STRING)
    private AppTypeEnum appType;

    /**
     * 操作系统类型 WIN7_64、WIN_XP、WIN7_32等
     */
    @Enumerated(value = EnumType.STRING)
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    private String osVersion;

    /**
     * 应用软件库类型IDV/VDI/TCI
     */
    @Enumerated(value = EnumType.STRING)
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
    @Enumerated(value = EnumType.STRING)
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
     * 已经完成桌面数量
     */
    private Integer testingDesktopCount;

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
    @Enumerated(value = EnumType.STRING)
    private CbbControlState controlState;

    /**
     * 多版本根镜像模板id
     **/
    private UUID rootImageId;

    /**
     * 多版本根镜像模板名称
     **/
    private String rootImageName;

    /**
     * 多版本根镜像模板角色类型
     **/
    @Enumerated(EnumType.STRING)
    private ImageRoleType imageRoleType;

    private UUID platformId;

    @Enumerated(EnumType.STRING)
    private CloudPlatformType platformType;

    @Enumerated(EnumType.STRING)
    private CloudPlatformStatus platformStatus;

    private String platformName;

    @Version
    private Integer version;

    private Boolean isGlobalImage;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
