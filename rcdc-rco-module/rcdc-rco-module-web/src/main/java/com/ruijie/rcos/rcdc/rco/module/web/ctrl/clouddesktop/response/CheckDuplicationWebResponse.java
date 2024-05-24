package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * Description: 命名唯一性校验
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月14日
 * 
 * @author Ghang
 */
public class CheckDuplicationWebResponse {

    @ApiModelProperty(value = "是否重复， true：表示重复， false：表示没重复")
    private Boolean hasDuplication;

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
