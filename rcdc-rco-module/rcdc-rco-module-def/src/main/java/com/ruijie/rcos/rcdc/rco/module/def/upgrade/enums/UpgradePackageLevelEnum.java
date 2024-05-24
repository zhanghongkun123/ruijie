package com.ruijie.rcos.rcdc.rco.module.def.upgrade.enums;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/13 19:01
 *
 * @author ketb
 */
public enum UpgradePackageLevelEnum {

    NORMAL("normal"),

    CRITICAL("critical");

    private String level;

    UpgradePackageLevelEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
