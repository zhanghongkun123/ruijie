package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 
 * Description: 获取绑定镜像信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/19
 *
 * @author zhiweiHong
 */
public class GetBandImageInfoDTO extends IDVCloudDesktopDTO {

    @NotNull
    private Long imageFileSize;

    @NotNull
    private Integer strategySystemDiskSize;

    /**
     * 是否开启了系统盘自动扩容
     */
    @NotNull
    private Boolean enableFullSystemDisk;

    /**
     * 镜像模板系统盘大小
     */
    @NotNull
    private Integer imageSystemDiskSize;

    /**
     * 镜像数据盘，页面上填写的大小，可空
     */
    @Nullable
    private Integer imageDataDiskSize;

    @NotNull
    private UUID imageRecoveryPointId;

    public Long getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(Long imageFileSize) {
        this.imageFileSize = imageFileSize;
    }

    public Integer getStrategySystemDiskSize() {
        return strategySystemDiskSize;
    }

    public void setStrategySystemDiskSize(Integer strategySystemDiskSize) {
        this.strategySystemDiskSize = strategySystemDiskSize;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public Integer getImageSystemDiskSize() {
        return imageSystemDiskSize;
    }

    public void setImageSystemDiskSize(Integer imageSystemDiskSize) {
        this.imageSystemDiskSize = imageSystemDiskSize;
    }

    @Nullable
    public Integer getImageDataDiskSize() {
        return imageDataDiskSize;
    }

    public void setImageDataDiskSize(@Nullable Integer imageDataDiskSize) {
        this.imageDataDiskSize = imageDataDiskSize;
    }

    public UUID getImageRecoveryPointId() {
        return imageRecoveryPointId;
    }

    public void setImageRecoveryPointId(UUID imageRecoveryPointId) {
        this.imageRecoveryPointId = imageRecoveryPointId;
    }
}
