package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportDesktopConfig;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
public class ImportRelationDataRequest {


    @NotBlank
    @Size(max = 64)
    private String imageTemplateName;

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    private String id;

    @NotBlank
    private String imageId;

    @NotNull
    @Range(min = "20", max = "200")
    private Integer systemDisk;

    @NotNull
    private CbbCloudDeskPattern desktopType;

    /**
     * use_local_disk_enable
     */
    @NotNull
    private Boolean useLocalDisk;

    @NotNull
    private Boolean openDesktopRedirect;

    @NotNull
    private ImportDesktopConfig.USBType[] usbTypeIdArr;


    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public Boolean getUseLocalDisk() {
        return useLocalDisk;
    }

    public void setUseLocalDisk(Boolean useLocalDisk) {
        this.useLocalDisk = useLocalDisk;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Nullable
    public ImportDesktopConfig.USBType[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(@Nullable ImportDesktopConfig.USBType[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    public Boolean getOpenDesktopRedirect() {
        return openDesktopRedirect;
    }

    public void setOpenDesktopRedirect(Boolean openDesktopRedirect) {
        this.openDesktopRedirect = openDesktopRedirect;
    }
}
