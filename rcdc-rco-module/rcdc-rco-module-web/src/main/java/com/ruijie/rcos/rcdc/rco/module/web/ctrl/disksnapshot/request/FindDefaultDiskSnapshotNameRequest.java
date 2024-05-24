package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.request;


import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 获取默认磁盘快照名称请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月25日
 *
 * @author lyb
 */
public class FindDefaultDiskSnapshotNameRequest {

    @ApiModelProperty(value = "磁盘ID", required = true)
    @NotNull
    private UUID diskId;

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }
}
