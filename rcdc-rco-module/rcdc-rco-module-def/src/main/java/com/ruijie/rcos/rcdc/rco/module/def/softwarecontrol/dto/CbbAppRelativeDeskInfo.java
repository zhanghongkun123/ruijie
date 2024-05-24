package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppSoftwarePackageVersionState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年04月03日
 *
 * @author xgx
 */
public class CbbAppRelativeDeskInfo {
    private UUID desktopId;

    private String desktopName;

    private CbbCloudDeskState deskState;

    private Boolean isAllAppDetach;

    private CbbCloudDeskType deskType;

    private String deskIp;

    private String deskMac;

    private UUID appId;

    private UUID appVersionId;

    private AppSoftwarePackageVersionState appVersionState;

    private String userName;

    private CbbImageType appSoftwarePackageType;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public Boolean getIsAllAppDetach() {
        return this.isAllAppDetach;
    }

    public void setIsAllAppDetach(Boolean isAllAppDetach) {
        this.isAllAppDetach = isAllAppDetach;
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

    public AppSoftwarePackageVersionState getAppVersionState() {
        return appVersionState;
    }

    public void setAppVersionState(AppSoftwarePackageVersionState appVersionState) {
        this.appVersionState = appVersionState;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CbbImageType getAppSoftwarePackageType() {
        return appSoftwarePackageType;
    }

    public void setAppSoftwarePackageType(CbbImageType appSoftwarePackageType) {
        this.appSoftwarePackageType = appSoftwarePackageType;
    }
}
