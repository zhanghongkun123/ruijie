package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
@ApiModel("云桌面备份名称是否重复返回体")
public class CheckNameDuplicationResponse {

    @ApiModelProperty(value = "是否重复， true：表示重复， false：表示没重复")
    private Boolean hasDuplication;

    public CheckNameDuplicationResponse(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
