package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/2  <br>
 *
 * @author yyz
 */
public class ConfigurationWizardCustomDataDTO {

    @NotNull
    private String key;

    @NotNull
    private Integer value;

    public ConfigurationWizardCustomDataDTO() {
        this.key = "userIndex";
        this.value = 0;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getKey() {

        return key;
    }

    public Integer getValue() {
        return value;
    }
}