package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import org.springframework.util.Assert;

/**
 * Description: 密码复杂度等级
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author zhang.zhiwen
 */
public enum CertificationStrategyLevelEnum {

    /**
     * 普通
     */
    LEVEL_ONE("1"),
    /**
     * 中等
     */
    LEVEL_TWO("2"),
    /**
     * 复杂
     */
    LEVEL_THREE("3"),
    /**
     * 普通
     */
    LEVEL_DEFAULT("-1");

    private String level;

    CertificationStrategyLevelEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    /**
     * 基于level字段获取对应的枚举
     *
     * @param level 枚举等级
     * @return CertificationStrategyLevelEnum
     */
    public static CertificationStrategyLevelEnum getByLevel(String level) {
        Assert.notNull(level, "level must not be null");
        for (CertificationStrategyLevelEnum levelEnum:values()) {
            if (levelEnum.getLevel().equals(level)) {
                return levelEnum;
            }
        }
        // 没有匹配的枚举值
        return null;
    }
}
