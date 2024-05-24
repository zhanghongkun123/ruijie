package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 校验磁盘快照名称是否重复请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月28日
 *
 * @author lyb
 */
public class CheckDiskSnapshotNameDuplicationRequest implements WebRequest {

    /**
     * 快照名称
     */
    @ApiModelProperty(value = "快照名称，通用名称格式", required = true)
    @NotBlank
    @Size(min = 1, max = 64)
    @TextName
    private String name;

    /**
     * 云桌面快照ID
     */
    @ApiModelProperty(value = "磁盘快照ID")
    @Nullable
    private UUID diskSnapshotId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public UUID getDiskSnapshotId() {
        return diskSnapshotId;
    }

    public void setDiskSnapshotId(@Nullable UUID diskSnapshotId) {
        this.diskSnapshotId = diskSnapshotId;
    }
}
