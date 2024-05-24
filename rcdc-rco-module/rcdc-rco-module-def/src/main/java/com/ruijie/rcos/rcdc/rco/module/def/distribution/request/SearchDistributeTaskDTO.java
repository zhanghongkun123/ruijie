package com.ruijie.rcos.rcdc.rco.module.def.distribution.request;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.BasePermissionDTO;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/03 14:56
 *
 * @author coderLee23
 */
public class SearchDistributeTaskDTO extends BasePermissionDTO {

    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
