package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/10
 *
 * @author wuShengQiang
 */
@ApiModel("备份数量限制返回体")
public class CheckBackupFullResponse {

    @ApiModelProperty(value = "是否超配， true：超配， false： 非超配")
    private Boolean hasFull;

    public CheckBackupFullResponse(Boolean hasFull) {
        this.hasFull = hasFull;
    }

    public Boolean getHasFull() {
        return hasFull;
    }

    public void setHasFull(Boolean hasFull) {
        this.hasFull = hasFull;
    }
}
