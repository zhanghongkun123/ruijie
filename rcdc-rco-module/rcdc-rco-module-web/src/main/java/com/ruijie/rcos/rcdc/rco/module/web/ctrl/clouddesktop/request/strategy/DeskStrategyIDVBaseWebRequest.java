package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;

/**
 * Description: IDV策略web请求的基类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/6
 *
 * @author songxiang
 */
public class DeskStrategyIDVBaseWebRequest {

    @NotBlank
    private String name;

    @NotNull
    private CbbCloudDeskPattern desktopType;

    @Range(min = "20", max = "100")
    private Integer systemDisk;

    @NotNull
    private UUID[] usbTypeIdArr;

    @NotNull
    private Boolean enableUsbReadOnly;

    @NotNull
    private Boolean enableAllowLocalDisk;

    @NotNull
    private Boolean enableOpenDesktopRedirect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public UUID[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    public Boolean getEnableUsbReadOnly() {
        return enableUsbReadOnly;
    }

    public void setEnableUsbReadOnly(Boolean enableUsbReadOnly) {
        this.enableUsbReadOnly = enableUsbReadOnly;
    }

    public Boolean getEnableAllowLocalDisk() {
        return enableAllowLocalDisk;
    }

    public void setEnableAllowLocalDisk(Boolean enableAllowLocalDisk) {
        this.enableAllowLocalDisk = enableAllowLocalDisk;
    }

    public Boolean getEnableOpenDesktopRedirect() {
        return enableOpenDesktopRedirect;
    }

    public void setEnableOpenDesktopRedirect(Boolean enableOpenDesktopRedirect) {
        this.enableOpenDesktopRedirect = enableOpenDesktopRedirect;
    }
}
