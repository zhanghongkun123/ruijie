package com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: CheckExternalStorageNameWebResponse
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
public class CheckExternalStorageNameWebResponse {

    @ApiModelProperty(value = "是否重复， true：表示重复， false：表示没重复")
    private Boolean hasDuplication;

    public CheckExternalStorageNameWebResponse(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
