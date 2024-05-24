package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.ArrayList;
import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.ThirdUserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 导入的用户组数据校验规则
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/4/24
 *
 * @author zhangyichi
 */
public class UserGroupValidateRules {

    private UserGroupValidateRules() {
        throw new IllegalStateException("UserGroupValidateRules Utility class");
    }

    public static final int USER_GROUP_NAME_SIZE = TextMedium.TEXT_SIZE;

    public static final int IMAGE_TEMPLATE_NAME_SIZE = TextShort.TEXT_SIZE;

    public static final int VDI_IMAGE_TEMPLATE_NAME_SIZE = 64;

    public static final int STRATEGY_NAME_SIZE = TextShort.TEXT_SIZE;

    public static final int NETWORK_NAME_SIZE = TextShort.TEXT_SIZE;

    public static final int STORAGE_POOL_NAME_SIZE = TextShort.TEXT_SIZE;

    /**
     * 最多允许数据行
     */
    public static final int ALLOW_MAX_ROW = 1000;

    /**
     * 系统保留字段,用户组名不可使用
     */
    protected static final List<String> DEFAULT_USER_GROUP_NAMES = new ArrayList<>();

    static {
        DEFAULT_USER_GROUP_NAMES.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_RESERVED_GROUP_NAME_UNGROUPED));
        DEFAULT_USER_GROUP_NAMES.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_RESERVED_GROUP_NAME_OVERVIEW));
        DEFAULT_USER_GROUP_NAMES.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_RESERVED_AD_GROUP_NAME));
        DEFAULT_USER_GROUP_NAMES.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_RESERVED_LDAP_GROUP_NAME));
        DEFAULT_USER_GROUP_NAMES.add(LocaleI18nResolver.resolve(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_ROOT_USER_GROUP_NAME));
    }

}
