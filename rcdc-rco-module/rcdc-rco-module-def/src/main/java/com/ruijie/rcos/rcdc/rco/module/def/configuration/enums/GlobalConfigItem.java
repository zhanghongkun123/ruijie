package com.ruijie.rcos.rcdc.rco.module.def.configuration.enums;

/**
 * <br>
 * Description: 全局配置项 <br>
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022/8/5 <br>
 *
 * @author linhj
 */
public enum GlobalConfigItem {

    USER_PROTOCOL("is_read_user_protocol");

    private String value;

    GlobalConfigItem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @SuppressWarnings("unused")
    private void setValue(String value) {
        this.value = value;
    }
}