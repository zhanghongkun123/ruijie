package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Description: ListTypeEnum
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月25日
 *
 * @author chenl
 */
public enum SoftwareStrategyRunTypeEnum {

    /**
     * 镜像编辑场景
     */
    EDIT("edit"),
    /**
     * gt启动场景
     */
    USER("user");

    private String code;

    SoftwareStrategyRunTypeEnum(String code) {
        this.code = code;
    }

    /**
     * @param code code
     * @return 返回对应枚举
     */
    public static SoftwareStrategyRunTypeEnum getByCode(String code) {
        Assert.notNull(code, "code is null");

        SoftwareStrategyRunTypeEnum[] softwareStrategyRunTypeEnumArr = SoftwareStrategyRunTypeEnum.values();
        for (SoftwareStrategyRunTypeEnum softwareStrategyRunTypeEnum : softwareStrategyRunTypeEnumArr) {
            if (softwareStrategyRunTypeEnum.code.equals(code)) {
                return softwareStrategyRunTypeEnum;
            }
        }
        //不满足条件，返回null
        return null;
    }
}
