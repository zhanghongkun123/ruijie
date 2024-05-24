package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年05月11日
 *
 * @author xgx
 */
public class NewImageIdDTO {
    @Nullable
    private String newImageId;

    public NewImageIdDTO() {

    }

    public NewImageIdDTO(String newImageId) {
        this.newImageId = newImageId;
    }

    public String getNewImageId() {
        return newImageId;
    }

    public void setNewImageId(String newImageId) {
        this.newImageId = newImageId;
    }
}
