package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DataSourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/09 17:09
 *
 * @author coderLee23
 */
public class SearchGroupDesktopRelatedDTO extends BasePermissionDTO {


    private UUID groupId;

    private List<UUID> groupIdList;

    /**
     * 支持云桌面名称和终端名称模糊查询
     */
    private String searchName;

    /**
     * 镜像模板
     */
    private UUID imageTemplateId;

    /**
     * 规格：操作系统类型
     */
    private CbbOsType osType;


    /**
     * 操作系统版本号
     */
    private String osVersion;

    /**
     * 规格：镜像类型
     */
    private CbbImageType cbbImageType;

    /**
     * 前端查询的数据来源
     */
    private DataSourceTypeEnum dataSourceType;

    /**
     * 过滤添加的桌面
     */
    private UUID filterGroupId;

    /**
     * 桌面状态
     */
    private List<CbbCloudDeskState> deskStateList;


    /**
     * 应用交付类型
     */
    private AppDeliveryTypeEnum appDeliveryType;

    /**
     * 操作系统类型【推送包交付组】
     */
    private OsPlatform osPlatform;

    /**
     * 镜像模板
     */
    private ImageUsageTypeEnum imageUsage;

    private List<CloudPlatformStatus> platformStatusList;


    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
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

    public DataSourceTypeEnum getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceTypeEnum dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public UUID getFilterGroupId() {
        return filterGroupId;
    }

    public void setFilterGroupId(UUID filterGroupId) {
        this.filterGroupId = filterGroupId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public List<UUID> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<UUID> groupIdList) {
        this.groupIdList = groupIdList;
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

    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }
}
