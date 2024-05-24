package com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月29日
 *
 * @author zql
 */

public class SwitchScheduleTaskStateWebRequest implements WebRequest {

    @NotNull
    private UUID id;

    @NotNull
    private QuartzTaskState quartzTaskState;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public QuartzTaskState getQuartzTaskState() {
        return quartzTaskState;
    }

    public void setQuartzTaskState(QuartzTaskState quartzTaskState) {
        this.quartzTaskState = quartzTaskState;
    }
}
