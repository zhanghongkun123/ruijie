package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年07月28日
 *
 * @author xgx
 */
public class DeskInstallSoftDTO {

    @JSONField(name = "DisplayIcon")
    private String displayIcon;

    @JSONField(name = "DisplayName")
    private String displayName;

    @JSONField(name = "DisplayVersion")
    private String displayVersion;

    @JSONField(name = "InstallLocation")
    private String installLocation;

    @JSONField(name = "InstallDate")
    private String installDate;

    @JSONField(name = "InstallSource")
    private String installSource;

    @JSONField(name = "UninstallPath")
    private String uninstallPath;

    @JSONField(name = "Publisher")
    private String publisher;

    @JSONField(name = "SoftwareName")
    private String softwareName;

    public String getDisplayIcon() {
        return displayIcon;
    }

    public void setDisplayIcon(String displayIcon) {
        this.displayIcon = displayIcon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }

    public void setDisplayVersion(String displayVersion) {
        this.displayVersion = displayVersion;
    }

    public String getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(String installLocation) {
        this.installLocation = installLocation;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public String getInstallSource() {
        return installSource;
    }

    public void setInstallSource(String installSource) {
        this.installSource = installSource;
    }

    public String getUninstallPath() {
        return uninstallPath;
    }

    public void setUninstallPath(String uninstallPath) {
        this.uninstallPath = uninstallPath;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }
}
