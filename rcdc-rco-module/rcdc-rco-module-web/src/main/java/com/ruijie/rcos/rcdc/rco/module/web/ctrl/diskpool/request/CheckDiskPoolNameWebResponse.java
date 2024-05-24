package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
public class CheckDiskPoolNameWebResponse {

    @ApiModelProperty(value = "是否重复， true：表示重复， false：表示没重复")
    private Boolean hasDuplication;

    public CheckDiskPoolNameWebResponse(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
