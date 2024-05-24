package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public class ImportDesktopConfig {

    @NotNull
    @Range(min = "20", max = "200")
    private Integer systemDiskSize;

    @NotNull
    private Boolean allowLocalDisk;

    @NotNull
    private CbbCloudDeskPattern pattern;

    @Nullable
    private USBType[] usbTypeIdArr;

    @NotNull
    private Boolean openDesktopRedirect;

    @Nullable
    public USBType[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(@Nullable USBType[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    public Boolean getAllowLocalDisk() {
        return allowLocalDisk;
    }

    public void setAllowLocalDisk(Boolean allowLocalDisk) {
        this.allowLocalDisk = allowLocalDisk;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public CbbCloudDeskPattern getPattern() {
        return pattern;
    }

    public void setPattern(CbbCloudDeskPattern pattern) {
        this.pattern = pattern;
    }

    public Boolean getOpenDesktopRedirect() {
        return openDesktopRedirect;
    }

    public void setOpenDesktopRedirect(Boolean openDesktopRedirect) {
        this.openDesktopRedirect = openDesktopRedirect;
    }

    /**
     * Description: DeskSnapshotState
     * Copyright: Copyright (c) 2022
     * Company: Ruijie Co., Ltd.
     * Create Time: 2022.04.08
     *
     * @author linhj
     */
    public enum USBType {

        USB_CC("e3f8d1ee-1a5e-4b54-a6cb-3249f2239c6a"),     // 存储设备
        USB_SR("78b75b29-0f59-46c0-9de0-0f172eb23063"),     // 输入设备
        USB_UKEY("476cf4dd-68d7-474a-859a-41644933fd5e"),   // 摄像设备
        USB_BG("7b5c3b19-bf20-4f46-86d3-4c1e9d42bea7"),     // 办公设备
        USB_YP("6be5d94e-ed5e-4acc-90d5-8ee1ad75666d"),     // 音频设备
        USB_SJ("5077c12c-bde9-4396-b8fa-e566dea92cbc"),     // 手机设备
        USB_QT("fec22ab4-f565-4c91-9401-dd7b5465edcf"),     // 其他设备
        USB_BMD("c597546e-9a0e-400b-bd35-d1f946684504");    // 白名单

        final UUID id;

        USBType(String value) {
            this.id = UUID.fromString(value);
        }

        public UUID getId() {
            return id;
        }
    }
}
