package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;


import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: SHINE上报桌面是否开启系统盘自动扩容DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/28 11:04
 *
 * @author yxq
 */
public class ShineNotifyFullSystemDiskDTO {

    /**
     * 是否开启了系统盘自动扩容
     */
    @NotNull
    private Boolean enableFullSystemDisk;

    /**
     * 系统盘大小
     */
    @NotNull
    private Integer systemDiskSize;

    /**
     * 云桌面ID
     */
    @NotNull
    private UUID deskId;

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
