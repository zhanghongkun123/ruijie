package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 任务临时信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/09
 *
 * @author linke
 */
public class StartupTaskInfoDTO {

    private UUID deskId;

    private UUID taskId;

    public StartupTaskInfoDTO(UUID deskId, UUID taskId) {
        this.deskId = deskId;
        this.taskId = taskId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object obj) {
        Assert.notNull(obj, "Object can not be null");
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StartupTaskInfoDTO that = (StartupTaskInfoDTO) obj;
        return Objects.equals(that.getTaskId(), this.getTaskId()) && Objects.equals(that.getDeskId(), this.getDeskId());
    }
}
