package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.response;


import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 获取磁盘快照默认名称响应
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月25日
 *
 * @author lyb
 */
public class FindDefaultDiskSnapshotNameResponse {

    @ApiModelProperty(value = "快照名称")
    private String name;

    public FindDefaultDiskSnapshotNameResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
