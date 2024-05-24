package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.response;


import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 校验磁盘快照是否超配响应
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月29日
 *
 * @author lyb
 */
public class CheckDiskSnapshotFullResponse {

    @ApiModelProperty(value = "是否满配， true：满配， false： 非满配")
    private Boolean hasFull;

    public CheckDiskSnapshotFullResponse(Boolean hasFull) {
        this.hasFull = hasFull;
    }

    public Boolean getHasFull() {
        return hasFull;
    }

    public void setHasFull(Boolean hasFull) {
        this.hasFull = hasFull;
    }
}
