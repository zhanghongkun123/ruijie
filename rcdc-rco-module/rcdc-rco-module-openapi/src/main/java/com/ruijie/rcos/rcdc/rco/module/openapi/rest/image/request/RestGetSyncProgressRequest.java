package com.ruijie.rcos.rcdc.rco.module.openapi.rest.image.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29 19:57
 *
 * @author yxq
 */
public class RestGetSyncProgressRequest {

    /**
     * 镜像id
     */
    @NotNull
    private UUID imageTemplateId;

    /**
     * 目标站点id
     */
    @NotNull
    private UUID targetSiteId;


    /**
     * 目标站点id
     */
    @NotNull
    private UUID targetDiskId;

    @NotNull
    private UUID syncTaskId;



    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getTargetSiteId() {
        return targetSiteId;
    }

    public void setTargetSiteId(UUID targetSiteId) {
        this.targetSiteId = targetSiteId;
    }

    public UUID getTargetDiskId() {
        return targetDiskId;
    }

    public void setTargetDiskId(UUID targetDiskId) {
        this.targetDiskId = targetDiskId;
    }

    public UUID getSyncTaskId() {
        return syncTaskId;
    }

    public void setSyncTaskId(UUID syncTaskId) {
        this.syncTaskId = syncTaskId;
    }
}
