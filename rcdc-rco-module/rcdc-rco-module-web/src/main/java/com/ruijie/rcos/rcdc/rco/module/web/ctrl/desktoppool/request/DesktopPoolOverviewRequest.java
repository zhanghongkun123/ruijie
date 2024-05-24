package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 桌面池总览请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/22
 *
 * @author linke
 */
public class DesktopPoolOverviewRequest {

    @ApiModelProperty(value = "桌面池模式")
    @Nullable
    private CbbDesktopPoolModel poolModel;

    @Nullable
    public CbbDesktopPoolModel getPoolModel() {
        return poolModel;
    }

    public void setPoolModel(@Nullable CbbDesktopPoolModel poolModel) {
        this.poolModel = poolModel;
    }
}
