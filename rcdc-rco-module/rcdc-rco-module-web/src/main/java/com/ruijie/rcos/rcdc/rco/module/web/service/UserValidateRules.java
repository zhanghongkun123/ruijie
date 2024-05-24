package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 导入的用户数据校验规则
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/19
 *
 * @author Jarman
 */
public class UserValidateRules {

    private UserValidateRules() {
        throw new IllegalStateException("UserValidateRules Utility class");
    }

    public static final String USER_NAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5.-][0-9a-zA-Z\\u4e00-\\u9fa5_.-]*$";

    public static final String USER_GROUP_NAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5@.-][0-9a-zA-Z\\u4e00-\\u9fa5_@.-]*$";

    public static final int USER_NAME_SIZE = 20;

    public static final int USER_GROUP_NAME_SIZE = 128;

    public static final int REAL_NAME_SIZE = 32;

    /**
     * 最多允许数据行
     */
    public static final int ALLOW_MAX_ROW = 1000;

    /**
     * 系统保留字段,用户名不可使用
     */
    public static final List<String> DEFAULT_USER_NAMES = new ArrayList<>();

    protected static final List<String> DEFAULT_USER_GROUP_NAMES = new ArrayList<>();



    static {
        DEFAULT_USER_NAMES.add("admin");
        DEFAULT_USER_NAMES.add("public");
        DEFAULT_USER_NAMES.add("guest");
        DEFAULT_USER_NAMES.add("local");
        DEFAULT_USER_GROUP_NAMES.add("未分组");
        DEFAULT_USER_GROUP_NAMES.add("总览");
        DEFAULT_USER_GROUP_NAMES.add("AD域用户组");
        DEFAULT_USER_GROUP_NAMES.add("LDAP域用户组");
        DEFAULT_USER_GROUP_NAMES.add("第三方用户组");
    }
}
