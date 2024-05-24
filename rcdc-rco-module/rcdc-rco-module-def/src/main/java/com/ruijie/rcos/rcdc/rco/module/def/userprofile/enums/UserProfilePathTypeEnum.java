package com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

/**
 * Description: 路径类型
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public enum UserProfilePathTypeEnum {

    /**
     * 文件夹
     */
    FOLDER(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_TYPE_FOLDER)),

    /**
     * 文件
     */
    DOCUMENT(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_TYPE_DOCUMENT)),

    /**
     * 注册表项
     */
    REGISTRY_KEY(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_TYPE_REGISTRY_KEY)),

    /**
     * 注册表值
     */
    REGISTRY_VALUE(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_TYPE_REGISTRY_VALUE));

    private final String text;

    UserProfilePathTypeEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * 根据中文名返回枚举
     *
     * @param text 中文名
     * @return 枚举
     */
    public static UserProfilePathTypeEnum valueOfText(String text) {
        Assert.notNull(text, "id can not null");
        for (UserProfilePathTypeEnum typeEnum : values()) {
            if (text.equals(typeEnum.getText())) {
                return typeEnum;
            }
        }

        String errorMessage = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_PROFILE_TYPE_FAIL, new String[]{text});
        throw new IllegalArgumentException(errorMessage);
    }

    /**
     * 校验text是否有效
     *
     * @param text 中文名
     * @return 返回
     */
    public static boolean isValid(String text) {
        Assert.notNull(text, "text can not null");
        for (UserProfilePathTypeEnum typeEnum : values()) {
            if (text.equals(typeEnum.getText())) {
                return true;
            }
        }
        return false;
    }
}
