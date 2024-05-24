package com.ruijie.rcos.rcdc.rco.module.def.cmscomponent.enums;

/**
 * Description: CMS组件启用情况
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/09/04
 *
 * @author wjp
 */
public enum CmsComponentEnum {

    INIT_STATE("init", "未初始化"),
    DISABLED_STATE("disabled", "未启用"),
    ENABLED_STATE("enabled", "已启用");

    private String name;
    private String desc;

    CmsComponentEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
