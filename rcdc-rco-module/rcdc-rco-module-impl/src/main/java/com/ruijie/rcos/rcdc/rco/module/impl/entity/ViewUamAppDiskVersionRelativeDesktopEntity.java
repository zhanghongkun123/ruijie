package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppSoftwarePackageVersionState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月31日
 *
 * @author xgx
 */
@Table(name = "v_cbb_uam_app_version_relative_desk")
@Entity
public class ViewUamAppDiskVersionRelativeDesktopEntity {
    @Id
    private UUID deskId;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private CbbCloudDeskType deskType;

    @Enumerated(value = EnumType.STRING)
    private CbbImageType appSoftwarePackageType;

    private String deskIp;

    private String deskMac;

    @Enumerated(value = EnumType.STRING)
    private CbbCloudDeskState deskState;

    private UUID appId;

    private String userName;

    private UUID appVersionId;

    @Enumerated(value = EnumType.STRING)
    private AppSoftwarePackageVersionState appVersionState;

    /**
    * 版本号
    */
    @Version
    private Integer version;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CbbCloudDeskType getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbCloudDeskType deskType) {
        this.deskType = deskType;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getAppId() {
        return appId;
    }

    public void setAppId(UUID appId) {
        this.appId = appId;
    }

    public UUID getAppVersionId() {
        return appVersionId;
    }

    public void setAppVersionId(UUID appVersionId) {
        this.appVersionId = appVersionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AppSoftwarePackageVersionState getAppVersionState() {
        return appVersionState;
    }

    public void setAppVersionState(AppSoftwarePackageVersionState appVersionState) {
        this.appVersionState = appVersionState;
    }

    public CbbImageType getAppSoftwarePackageType() {
        return appSoftwarePackageType;
    }

    public void setAppSoftwarePackageType(CbbImageType appSoftwarePackageType) {
        this.appSoftwarePackageType = appSoftwarePackageType;
    }


}
