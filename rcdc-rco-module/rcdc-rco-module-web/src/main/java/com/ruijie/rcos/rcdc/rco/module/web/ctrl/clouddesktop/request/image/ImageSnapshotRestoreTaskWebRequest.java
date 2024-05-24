package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.SnapshotRestoreType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021.10.09 <br>
 *
 * @author liangyifeng
 */
public class ImageSnapshotRestoreTaskWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像标识", required = true)
    @NotNull
    private UUID imageTemplateId;

    @ApiModelProperty(value = "快照标识", required = true)
    @NotNull
    private UUID snapshotId;

    @ApiModelProperty(value = "定时任务时间")
    @Nullable
    private String scheduleTime;

    @ApiModelProperty(value = "是否强制恢复", required = true)
    @NotNull
    private Boolean enableForceRestore;

    @ApiModelProperty(value = "快照恢复类型")
    @NotNull
    private SnapshotRestoreType restoreType;

    @ApiModelProperty(value = "通知任务时间")
    @Nullable
    private String noticeTime;

    @ApiModelProperty(value = "通知信息")
    @Nullable
    private String tipMsg;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(UUID snapshotId) {
        this.snapshotId = snapshotId;
    }

    @Nullable
    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(@Nullable String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Boolean getEnableForceRestore() {
        return enableForceRestore;
    }

    public void setEnableForceRestore(Boolean enableForceRestore) {
        this.enableForceRestore = enableForceRestore;
    }

    public SnapshotRestoreType getRestoreType() {
        return restoreType;
    }

    public void setRestoreType(SnapshotRestoreType restoreType) {
        this.restoreType = restoreType;
    }

    @Nullable
    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(@Nullable String noticeTime) {
        this.noticeTime = noticeTime;
    }

    @Nullable
    public String getTipMsg() {
        return tipMsg;
    }

    public void setTipMsg(@Nullable String tipMsg) {
        this.tipMsg = tipMsg;
    }
}
