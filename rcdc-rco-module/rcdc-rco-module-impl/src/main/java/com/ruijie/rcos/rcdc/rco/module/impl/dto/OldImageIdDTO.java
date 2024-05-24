package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年05月11日
 *
 * @author xgx
 */
public class OldImageIdDTO {
    @NotBlank
    private String oldImageId;

    public String getOldImageId() {
        return oldImageId;
    }

    public void setOldImageId(String oldImageId) {
        this.oldImageId = oldImageId;
    }
}
