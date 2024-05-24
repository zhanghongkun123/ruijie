package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年02月21日
 *
 * @author dongquanyi
 */
public class UnRepairDesktopRequest implements WebRequest {

    @ApiModelProperty(value = "桌面ID", required = true)
    @NotNull
    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
