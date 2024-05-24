package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 镜像下载DTO类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/28 15:31
 *
 * @author yxq
 */
public class ImageDownloadStateDTO {

    private UUID id;

    private String terminalId;

    private UUID imageId;

    private DownloadStateEnum downloadState;

    private Integer failCode;

    private Date downloadFinishTime;

    private Date terminalDownloadFinishTime;

    private UUID imageRecoveryPointId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public DownloadStateEnum getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(DownloadStateEnum downloadState) {
        this.downloadState = downloadState;
    }

    public Date getDownloadFinishTime() {
        return downloadFinishTime;
    }

    public void setDownloadFinishTime(Date downloadFinishTime) {
        this.downloadFinishTime = downloadFinishTime;
    }

    public Integer getFailCode() {
        return failCode;
    }

    public void setFailCode(Integer failCode) {
        this.failCode = failCode;
    }

    public Date getTerminalDownloadFinishTime() {
        return terminalDownloadFinishTime;
    }

    public void setTerminalDownloadFinishTime(Date terminalDownloadFinishTime) {
        this.terminalDownloadFinishTime = terminalDownloadFinishTime;
    }

    public UUID getImageRecoveryPointId() {
        return imageRecoveryPointId;
    }

    public void setImageRecoveryPointId(UUID imageRecoveryPointId) {
        this.imageRecoveryPointId = imageRecoveryPointId;
    }
}
