package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 文件分发任务（子任务）DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 15:52
 *
 * @author zhangyichi
 */
@PageQueryDTOConfig(entityType = "DistributeSubTaskEntity")
public class DistributeSubTaskDTO {

    private UUID id;

    private String targetId;

    private FileDistributionTargetType targetType;

    private UUID parentTaskId;

    private FileDistributionTaskStatus status = FileDistributionTaskStatus.WAITING;

    private String message;

    private Date createTime;

    private Date startTime;

    private FileDistributionStashTaskStatus stashStatus = FileDistributionStashTaskStatus.NONE;

    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public FileDistributionTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(FileDistributionTargetType targetType) {
        this.targetType = targetType;
    }

    public UUID getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(UUID parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public FileDistributionTaskStatus getStatus() {
        return status;
    }

    public void setStatus(FileDistributionTaskStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public FileDistributionStashTaskStatus getStashStatus() {
        return stashStatus;
    }

    public void setStashStatus(FileDistributionStashTaskStatus stashStatus) {
        this.stashStatus = stashStatus;
    }

}
