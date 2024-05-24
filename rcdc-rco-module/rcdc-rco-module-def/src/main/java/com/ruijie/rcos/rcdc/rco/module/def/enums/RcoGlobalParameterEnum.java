package com.ruijie.rcos.rcdc.rco.module.def.enums;


import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/20 9:28
 *
 * @author chenl
 */
public enum RcoGlobalParameterEnum {

    /**
     * 软件白名单全局开关
     */
    ENABLE_SOFTWARE_STRATEGY(BusinessKey.RCDC_RCO_GLOBAL_STRATEGY_SOFT_STRATEGY);

    /**
     * 国际化提示
     */
    private final String messageKey;

    RcoGlobalParameterEnum(String i18nMessage) {
        this.messageKey = i18nMessage;
    }

    public String getI18nMessage() {
        return LocaleI18nResolver.resolve(messageKey);
    }
}
