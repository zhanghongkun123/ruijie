package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImagePublishType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTaskStatus;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 镜像发布任务 DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.10.11
 *
 * @author liangyifeng
 */
public class ImageSnapshotRestoreTaskDTO {

    /**
     * 镜像ID
     */
    private UUID id;

    /**
     * 快照名称
     */
    private String name;

    /**
     * 发布时间
     */
    private String scheduleTime;

    /**
     * 通知时间
     */
    private String noticeTime;

    /**
     * 通知消息
     */
    private String tipMsg;

    /**
     * 发布错误消息
     */
    private String errorMsg;

    /**
     * 是否强制发布
     */
    private Boolean enableForceRestore;

    /**
     * 发布状态
     */
    private ImageTaskStatus status;

    /**
     * 发布类型
     */
    private ImagePublishType restoreType;

    private UUID snapshotId;

    /**
     * 更新时间
     */
    private Date modifyTime;

    public UUID getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(UUID snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getEnableForceRestore() {
        return enableForceRestore;
    }

    public void setEnableForceRestore(Boolean enableForceRestore) {
        this.enableForceRestore = enableForceRestore;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getTipMsg() {
        return tipMsg;
    }

    public void setTipMsg(String tipMsg) {
        this.tipMsg = tipMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ImageTaskStatus getStatus() {
        return status;
    }

    public void setStatus(ImageTaskStatus status) {
        this.status = status;
    }

    public ImagePublishType getRestoreType() {
        return restoreType;
    }

    public void setRestoreType(ImagePublishType restoreType) {
        this.restoreType = restoreType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
