package com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月17日
 *
 * @author xgx
 */
public class TaskNameCheckDuplicationWebRequest implements WebRequest {
    @Nullable
    private UUID id;

    @NotBlank
    @TextName
    @TextMedium
    private String taskName;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
