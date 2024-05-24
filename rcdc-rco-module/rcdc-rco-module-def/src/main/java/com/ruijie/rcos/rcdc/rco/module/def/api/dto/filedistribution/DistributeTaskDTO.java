package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 文件分发任务（父任务）DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 15:06
 *
 * @author zhangyichi
 */
public class DistributeTaskDTO {

    private UUID id;

    private UUID parameterId;

    private String taskName;

    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParameterId() {
        return parameterId;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
