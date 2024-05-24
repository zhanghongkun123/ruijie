package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: 应用显示策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 * 
 * @author Ghang
 */
public class AppDisplayModeStrategyDTO {

    //应用显示模式策略:是否开启
    @NotNull
    private Boolean enableAppDisplayMode;

    public Boolean getEnableAppDisplayMode() {
        return enableAppDisplayMode;
    }

    public void setEnableAppDisplayMode(Boolean enableAppDisplayMode) {
        this.enableAppDisplayMode = enableAppDisplayMode;
    }


}
