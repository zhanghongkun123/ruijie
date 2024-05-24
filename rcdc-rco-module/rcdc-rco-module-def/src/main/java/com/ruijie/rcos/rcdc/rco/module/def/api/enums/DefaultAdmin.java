package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月12日
 *
 * @author xiejian
 */
public enum DefaultAdmin {

    ADMIN("admin", "超级管理员"),
    SYSADMIN("sysadmin", "系统管理员"),
    SECADMIN("secadmin", "安全管理员"),
    AUDADMIN("audadmin", "审计管理员");

    private String name;

    private String describe;

    DefaultAdmin(String name, String describe) {
        this.name = name;
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }
}
