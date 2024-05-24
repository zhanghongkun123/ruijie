package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.UUID;

import javax.annotation.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月28日
 *
 * @author zhk
 */
public class UamAppTestDTO {

    private UUID id;

    private String name;

    private String description;

    private UUID[] appIdArr;

    private UUID[] deskIdArr;

    private String pushAppConfig;

    private UUID imageTemplateId;

    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    private String osVersion;

    private CbbImageType appSoftwarePackageType;

    private UUID appSoftwarePackageId;

    private String reason;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
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

    public CbbImageType getAppSoftwarePackageType() {
        return appSoftwarePackageType;
    }

    public void setAppSoftwarePackageType(CbbImageType appSoftwarePackageType) {
        this.appSoftwarePackageType = appSoftwarePackageType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppSoftwarePackageId() {
        return appSoftwarePackageId;
    }

    public void setAppSoftwarePackageId(UUID appSoftwarePackageId) {
        this.appSoftwarePackageId = appSoftwarePackageId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPushAppConfig() {
        return pushAppConfig;
    }

    public void setPushAppConfig(String pushAppConfig) {
        this.pushAppConfig = pushAppConfig;
    }
}
