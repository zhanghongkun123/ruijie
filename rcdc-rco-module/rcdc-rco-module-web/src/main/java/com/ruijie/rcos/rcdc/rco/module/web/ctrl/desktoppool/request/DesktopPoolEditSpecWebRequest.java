package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/25
 *
 * @author linke
 */
public class DesktopPoolEditSpecWebRequest extends DeskSpecRequest {

    /**
     * 桌面池id
     */
    @ApiModelProperty(value = "桌面池ID")
    @NotNull
    private UUID id;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
