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
@ApiModel("获取默认名称返回体")
public class FindDefaultBackupNameResponse {

    @ApiModelProperty(value = "备份名称")
    private String name;

    public FindDefaultBackupNameResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
