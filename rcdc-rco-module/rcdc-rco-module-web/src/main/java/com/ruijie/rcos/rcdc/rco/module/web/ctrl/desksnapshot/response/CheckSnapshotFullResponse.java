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
public class CheckSnapshotFullResponse {

    @ApiModelProperty(value = "是否满配， true：满配， false： 非满配")
    private Boolean hasFull;

    @ApiModelProperty(value = "用户快照是否满配")
    private Boolean hasUserSnapshotFull;

    public CheckSnapshotFullResponse(Boolean hasFull, Boolean hasUserSnapshotFull) {
        this.hasFull = hasFull;
        this.hasUserSnapshotFull = hasUserSnapshotFull;
    }

    public Boolean getHasFull() {
        return hasFull;
    }

    public void setHasFull(Boolean hasFull) {
        this.hasFull = hasFull;
    }

    public Boolean getHasUserSnapshotFull() {
        return hasUserSnapshotFull;
    }

    public void setHasUserSnapshotFull(Boolean hasUserSnapshotFull) {
        this.hasUserSnapshotFull = hasUserSnapshotFull;
    }
}
