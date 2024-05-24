package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Description: 发送
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 16:53
 *
 * @author yxq
 */
public class ShineConfigFullSystemDiskDTO {

    /**
     * 是否开启系统盘自动满配
     */
    @NotNull
    private Boolean enableFullSystemDisk;

    /**
     * 是否清空本地盘
     */
    @NotNull
    private Boolean needClearDataDisk;

    /**
     * 云桌面id
     */
    @Nullable
    private UUID deskId;

    public ShineConfigFullSystemDiskDTO(Boolean enableFullSystemDisk, Boolean needClearDataDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
        this.needClearDataDisk = needClearDataDisk;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public Boolean getNeedClearDataDisk() {
        return needClearDataDisk;
    }

    public void setNeedClearDataDisk(Boolean needClearDataDisk) {
        this.needClearDataDisk = needClearDataDisk;
    }

    @Nullable
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }
}
