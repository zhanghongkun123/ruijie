package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 创建磁盘快照Web请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月22日
 *
 * @author lyb
 */
public class CreateDiskSnapshotRequest implements WebRequest {

    /**
     * 快照名称
     */
    @ApiModelProperty(value = "快照名称，通用名称格式", required = true)
    @NotBlank
    @Size(min = 1, max = 64)
    @TextName
    private String name;

    /**
     * 磁盘ID
     */
    @ApiModelProperty(value = "磁盘ID", required = true)
    @NotNull
    private UUID diskId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }
}
