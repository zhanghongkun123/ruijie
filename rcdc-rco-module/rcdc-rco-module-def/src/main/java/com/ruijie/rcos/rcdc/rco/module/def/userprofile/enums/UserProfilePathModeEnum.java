package com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

/**
 * Description: 路径配置方式
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public enum UserProfilePathModeEnum {

    /**
     * 同步
     */
    SYNCHRO(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_MODE_SYNCHRO)),

    /**
     * 排除
     */
    EXCLUDE(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_MODE_EXCLUDE));

    private String text;

    UserProfilePathModeEnum(String text) {
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
    public static UserProfilePathModeEnum valueOfText(String text) {
        Assert.notNull(text, "id can not null");
        for (UserProfilePathModeEnum modeEnum : values()) {
            if (text.equals(modeEnum.getText())) {
                return modeEnum;
            }
        }

        String errorMessage = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_PROFILE_MODE_FAIL, new String[]{text});
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
        for (UserProfilePathModeEnum modeEnum : values()) {
            if (text.equals(modeEnum.getText())) {
                return true;
            }
        }
        return false;
    }
}
