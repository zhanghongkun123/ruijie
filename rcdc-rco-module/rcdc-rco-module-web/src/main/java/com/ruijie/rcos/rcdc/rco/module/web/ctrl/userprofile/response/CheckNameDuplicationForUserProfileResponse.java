package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 用户配置重名检查响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class CheckNameDuplicationForUserProfileResponse {

    @ApiModelProperty(value = "是否重复， true：表示重复， false：表示没重复")
    private Boolean hasDuplication;

    public CheckNameDuplicationForUserProfileResponse(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}