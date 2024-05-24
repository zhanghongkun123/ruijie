package com.ruijie.rcos.rcdc.rco.module.def.enums;

import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Description: 登录 token来源
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @author chenjuan
 */
public enum TokenSourceEnum {

    RCCP("rccp", "admin"),

    CLOUD_DOCK("cloud-dock","admin"),

    UNKNOWN("UNKNOWN","");
    String source;

    String userName;

    TokenSourceEnum(String source, String userName) {
        this.source = source;
        this.userName = userName;
    }

    /**
     * 类型转换
     * @param source source
     * @return TokenSourceEnum
     */
    public static TokenSourceEnum convert(@Nullable String source) {
        return Stream.of(TokenSourceEnum.values()).filter(value -> Objects.equals(value.getSource(), source)).findFirst().orElse(UNKNOWN);
    }

    public String getSource() {
        return source;
    }

    public String getUserName() {
        return userName;
    }
}
