package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * <p>Title: RoleType</p>
 * <p>Description: Function Description</p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2019</p>
 * <p>@Author: xiejian</p>
 * <p>@Date: 2021/8/14 14:40</p>
 */
public enum RoleType {

    ADMIN("超级管理员", "超级管理员"),

    SYSADMIN("sysadmin", "系统管理员"),

    SECADMIN("secadmin", "安全管理员"),

    AUDADMIN("audadmin", "审计管理员");

    private String name;

    private String describe;

    RoleType(String name, String describe) {
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
