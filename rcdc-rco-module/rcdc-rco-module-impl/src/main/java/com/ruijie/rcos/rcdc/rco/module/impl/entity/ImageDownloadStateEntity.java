package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 镜像模板下载状态实体类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/18 16:36
 *
 * @author yxq
 */
@Entity
@Table(name = "t_rco_image_download_state")
public class ImageDownloadStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String terminalId;

    private UUID imageId;

    @Enumerated(EnumType.STRING)
    private DownloadStateEnum downloadState;

    private Integer failCode;

    private Date downloadFinishTime;

    private Date createTime;

    private Date updateTime;

    private Date terminalDownloadFinishTime;

    private UUID imageRecoveryPointId;

    @Version
    private Integer version;

    public ImageDownloadStateEntity() {
    }

    public ImageDownloadStateEntity(String terminalId, Date createTime) {
        this.terminalId = terminalId;
        this.createTime = createTime;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
