package com.ruijie.rcos.rcdc.rco.module.web.service.userprofile;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 路径表格列定义
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/26
 *
 * @author WuShengQiang
 */
public enum PathExcelField implements TextExcelField {

    PATH_NAME(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_IMPORT_NAME), 0),

    PATH_DESC(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_IMPORT_DESCRIBE), 1),

    GROUP_NAME(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_IMPORT_GROUP_NAME), 2),

    GROUP_DESC(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_IMPORT_GROUP_DESCRIBE), 3),

    MODE(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_IMPORT_MODE), 4),

    TYPE(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_IMPORT_TYPE), 5),

    PATH(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_IMPORT_PATH), 6);

    private String header;
    private Integer index;

    PathExcelField(String header, Integer index) {
        this.header = header;
        this.index = index;
    }


    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public Integer getIndex() {
        return index;
    }
}