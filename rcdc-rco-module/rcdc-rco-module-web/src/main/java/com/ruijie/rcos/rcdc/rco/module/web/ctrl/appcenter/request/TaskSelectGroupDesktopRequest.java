package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/10 19:54
 *
 * @author coderLee23
 */
public class TaskSelectGroupDesktopRequest implements WebRequest {

    /**
     * 用户组id
     */
    @ApiModelProperty(value = " 用户组id", required = false)
    @Nullable
    private UUID groupId;

    @Nullable
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable UUID groupId) {
        this.groupId = groupId;
    }
}
