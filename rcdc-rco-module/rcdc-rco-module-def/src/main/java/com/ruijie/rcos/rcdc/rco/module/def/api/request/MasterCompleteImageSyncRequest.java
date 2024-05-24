package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: 从端准备镜像同步接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
public class MasterCompleteImageSyncRequest implements Serializable {

    @NotNull
    private UUID imageTemplateId;

    @NotNull
    private UUID taskId;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}
