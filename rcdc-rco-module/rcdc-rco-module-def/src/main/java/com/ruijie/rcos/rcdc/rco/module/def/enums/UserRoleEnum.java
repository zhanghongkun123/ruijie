package com.ruijie.rcos.rcdc.rco.module.def.enums;

import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 *
 * Description: 用户角色
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/29 21:04
 *
 * @author linrenjian
 */
public enum UserRoleEnum {


    /**
     * 是否是管理员
     */
    ADMIN("rcdc_rco_user_role_admin");

    UserRoleEnum(String text) {
        this.messageKey = text;
    }

    private String messageKey;

    public String getMessageKey() {
        return messageKey;
    }

    public String getI18nMessage() {
        return LocaleI18nResolver.resolve(messageKey);
    }

    /**
     * 根据中文名返回枚举
     *
     * @param text 显示名
     * @return 返回
     */
    public static UserRoleEnum valueOfI18nMessage(String text) {
        Assert.notNull(text, "id can not null");
        for (UserRoleEnum userRoleEnum : values()) {
            if (text.equals(userRoleEnum.getI18nMessage())) {
                return userRoleEnum;
            }
        }
        throw new IllegalArgumentException("用户角色【" + text + "】未定义");
    }

    /**
     * 校验text是否有效
     *
     * @param text 显示名
     * @return 返回
     */
    public static boolean isValid(String text) {
        Assert.notNull(text, "text can not null");
        for (UserRoleEnum userRoleEnum : values()) {
            if (text.equals(userRoleEnum.getI18nMessage())) {
                return true;
            }
        }
        return false;
    }

}
