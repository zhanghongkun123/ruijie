package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 导入的PC数据校验规则
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/19
 *
 * @author zqj
 */
public class ComputerValidateRules {

    private ComputerValidateRules() {
        throw new IllegalStateException("ComputerValidateRules Utility class");
    }

    public static final String NAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5@.-][0-9a-zA-Z\\u4e00-\\u9fa5_@.-]*$";

    public static final String IP = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public static final String GROUP_NAME = NAME;

    public static final int GROUP_NAME_SIZE = 32;

    protected static final List<String> DEFAULT_USER_GROUP_NAMES = new ArrayList<>();


    static {
        DEFAULT_USER_GROUP_NAMES.add("未分组");
        DEFAULT_USER_GROUP_NAMES.add("总览");
    }
}
