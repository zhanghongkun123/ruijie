package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: 鉴权的类型（管理员、用户、终端管理密码）
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/27 21:14
 *
 * @author yxq
 */
public enum CertificationTypeEnum {
    /**
     * 管理员
     */
    ADMIN("ADMIN"),

    /**
     * 用户
     */
    USER("USER"),

    /**
     * 终端管理密码
     */
    TERMINAL("TERMINAL");

    private String type;

    CertificationTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
