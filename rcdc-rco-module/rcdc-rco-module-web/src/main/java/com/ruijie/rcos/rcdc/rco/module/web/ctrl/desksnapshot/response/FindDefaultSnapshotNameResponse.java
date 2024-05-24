package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response;


import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class FindDefaultSnapshotNameResponse {

    @ApiModelProperty(value = "快照名称")
    private String name;

    public FindDefaultSnapshotNameResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
