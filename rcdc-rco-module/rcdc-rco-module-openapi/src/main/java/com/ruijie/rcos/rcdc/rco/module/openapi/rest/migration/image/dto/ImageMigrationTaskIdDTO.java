package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年05月27日
 *
 * @author xgx
 */
public class ImageMigrationTaskIdDTO {
    private UUID createTaskId;

    private UUID editTaskId;

    private UUID publishTaskId;

    public UUID getCreateTaskId() {
        return createTaskId;
    }

    public void setCreateTaskId(UUID createTaskId) {
        this.createTaskId = createTaskId;
    }

    public UUID getEditTaskId() {
        return editTaskId;
    }

    public void setEditTaskId(UUID editTaskId) {
        this.editTaskId = editTaskId;
    }

    public UUID getPublishTaskId() {
        return publishTaskId;
    }

    public void setPublishTaskId(UUID publishTaskId) {
        this.publishTaskId = publishTaskId;
    }
}
