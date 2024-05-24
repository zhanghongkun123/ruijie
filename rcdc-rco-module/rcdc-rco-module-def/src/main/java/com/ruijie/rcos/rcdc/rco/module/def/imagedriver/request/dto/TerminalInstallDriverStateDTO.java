package com.ruijie.rcos.rcdc.rco.module.def.imagedriver.request.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月12日
 *
 * @author xiejian
 */
public class TerminalInstallDriverStateDTO {

    private String isLimit;

    private String imageId;

    public String getIsLimit() {
        return isLimit;
    }

    public void setIsLimit(String isLimit) {
        this.isLimit = isLimit;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
