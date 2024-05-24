package com.ruijie.rcos.rcdc.rco.module.def.api.dto.image;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.PublishImageTemplateSnapshotDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: 创建空白的镜像模版dto
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
public class PublishSyncingImageTemplateDTO implements Serializable {

    @NotNull
    private UUID unifiedManageDataId;


    @Nullable
    private UUID imageTemplateId;


    @NotNull
    private UUID taskId;

    @NotNull
    private PublishImageTemplateSnapshotDTO publishSnapshot;


    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }


    public PublishImageTemplateSnapshotDTO getPublishSnapshot() {
        return publishSnapshot;
    }

    public void setPublishSnapshot(PublishImageTemplateSnapshotDTO publishSnapshot) {
        this.publishSnapshot = publishSnapshot;
    }

    @Nullable
    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(@Nullable UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }
}
