package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 从SHINE获取下载状态DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/28 14:23
 *
 * @author yxq
 */
public class ShineNotifyImageDownloadStateDTO {

    private UUID imageId;

    private DownloadStateEnum state;

    private Integer failCode;

    private Date timeStamp;

    private UUID deskId;

    private UUID imageRecoveryPointId;

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public Integer getFailCode() {
        return failCode;
    }

    public void setFailCode(Integer failCode) {
        this.failCode = failCode;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public DownloadStateEnum getState() {
        return state;
    }

    public void setState(DownloadStateEnum state) {
        this.state = state;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getImageRecoveryPointId() {
        return imageRecoveryPointId;
    }

    public void setImageRecoveryPointId(UUID imageRecoveryPointId) {
        this.imageRecoveryPointId = imageRecoveryPointId;
    }
}
