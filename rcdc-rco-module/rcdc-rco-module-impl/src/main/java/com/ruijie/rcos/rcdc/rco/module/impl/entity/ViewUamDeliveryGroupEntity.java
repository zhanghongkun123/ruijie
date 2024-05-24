package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/22 14:42
 *
 * @author coderLee23
 */
@Table(name = "v_rco_uam_delivery_group")
@Entity
public class ViewUamDeliveryGroupEntity {

    @Id
    private UUID id;

    /**
     * 交付组名称
     */
    private String deliveryGroupName;

    /**
     * 镜像模板
     */
    private UUID imageTemplateId;


    /**
     * 镜像模板名称
     */
    private String imageTemplateName;

    /**
     * 源镜像模板id
     */
    private UUID rootImageId;


    /**
     * 源镜像模板名称
     */
    private String rootImageName;

    /**
     * 规格：操作系统类型
     */
    @Enumerated(value = EnumType.STRING)
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    private String osVersion;

    /**
     * 规格：虚机类型
     */
    @Enumerated(value = EnumType.STRING)
    private CbbImageType cbbImageType;

    /**
     * 云桌面类型
     */
    @Enumerated(value = EnumType.STRING)
    private CbbCloudDeskPattern desktopType;

    /**
     * 应用交付类型
     */
    @Enumerated(value = EnumType.STRING)
    private AppDeliveryTypeEnum appDeliveryType;

    /**
     * 应用推送设置
     */
    private String pushAppConfig;

    /**
     * 描述
     */
    private String deliveryGroupDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Version
    private Integer version;

    @Enumerated(value = EnumType.STRING)
    private OsPlatform osPlatform;

    private UUID platformId;

    @Enumerated(EnumType.STRING)
    private CloudPlatformStatus platformStatus;

    @Enumerated(EnumType.STRING)
    private CloudPlatformType platformType;

    private String platformName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
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

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public AppDeliveryTypeEnum getAppDeliveryType() {
        return appDeliveryType;
    }

    public void setAppDeliveryType(AppDeliveryTypeEnum appDeliveryType) {
        this.appDeliveryType = appDeliveryType;
    }

    public String getPushAppConfig() {
        return pushAppConfig;
    }

    public void setPushAppConfig(String pushAppConfig) {
        this.pushAppConfig = pushAppConfig;
    }

    public String getDeliveryGroupDesc() {
        return deliveryGroupDesc;
    }

    public void setDeliveryGroupDesc(String deliveryGroupDesc) {
        this.deliveryGroupDesc = deliveryGroupDesc;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
