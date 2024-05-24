package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 编辑云桌面临时权限Request
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-05-08
 *
 * @author linke
 */
public class UpdateDesktopTempPermissionRequest extends CreateDesktopTempPermissionRequest {

    @ApiModelProperty(value = "临时权限记录ID", required = true)
    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
