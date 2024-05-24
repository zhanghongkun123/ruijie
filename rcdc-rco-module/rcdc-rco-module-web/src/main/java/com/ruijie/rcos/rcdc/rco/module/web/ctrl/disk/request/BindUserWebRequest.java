package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 给磁盘绑定/解绑用户
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
public class BindUserWebRequest implements WebRequest {

    @NotNull
    @ApiModelProperty(value = "磁盘池ID", required = true)
    private UUID diskPoolId;

    @NotNull
    @ApiModelProperty(value = "磁盘ID", required = true)
    private UUID diskId;

    @Nullable
    @ApiModelProperty(value = "用户ID")
    private UUID userId;

    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BindUserWebRequest{" +
                "diskPoolId=" + diskPoolId +
                ", diskId=" + diskId +
                ", userId=" + userId +
                '}';
    }
}
