package com.ruijie.rcos.rcdc.rco.module.web.ctrl.configuration.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * <br>
 * Description: 全局配置项 <br>
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022/8/5 <br>
 *
 * @author linhj
 */
public class GlobalConfigWebRequest extends GlobalConfigBaseWebRequest {

    @TextShort
    @NotBlank
    private String itemValue;

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}