package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.request;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class FindDefaultSnapshotNameRequest {

    @ApiModelProperty(value = "云桌面ID", required = true)
    @NotNull
    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
