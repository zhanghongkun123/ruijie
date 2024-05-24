package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImagePublishType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTaskStatus;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 镜像发布任务 DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.06
 *
 * @author linhj
 */
public class ImageTemplatePublishTaskDTO {

    /**
     * 镜像标识
     */
    private UUID id;

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
    private Boolean enableForcePublish;

    /**
     * 发布状态
     */
    private ImageTaskStatus status;

    /**
     * 发布类型
     */
    private ImagePublishType publishType;


    /**
     * 快照名称
     */

    private String snapshotName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date modifyTime;

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

    public Boolean getEnableForcePublish() {
        return enableForcePublish;
    }

    public void setEnableForcePublish(Boolean enableForcePublish) {
        this.enableForcePublish = enableForcePublish;
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

    public ImagePublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(ImagePublishType publishType) {
        this.publishType = publishType;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}