package com.ruijie.rcos.rcdc.rco.module.web.ctrl.configuration.dto;

import com.ruijie.rcos.rcdc.rco.module.def.configuration.enums.GlobalConfigItem;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * <br>
 * Description: 全局配置项 <br>
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022/8/5 <br>
 *
 * @author linhj
 */
public class GlobalConfigBaseWebRequest {

    @NotNull
    private GlobalConfigItem itemKey;

    public GlobalConfigItem getItemKey() {
        return itemKey;
    }

    public void setItemKey(GlobalConfigItem itemKey) {
        this.itemKey = itemKey;
    }
}