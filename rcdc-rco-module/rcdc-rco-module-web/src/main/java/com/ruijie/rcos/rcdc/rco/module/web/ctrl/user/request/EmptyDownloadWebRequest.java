package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/9
 *
 * @author Jarman
 */
public class EmptyDownloadWebRequest implements DownloadWebRequest {
    /**
     * 软件分组ID
     */
    @ApiModelProperty(value = "软件分组ID", required = true)
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
