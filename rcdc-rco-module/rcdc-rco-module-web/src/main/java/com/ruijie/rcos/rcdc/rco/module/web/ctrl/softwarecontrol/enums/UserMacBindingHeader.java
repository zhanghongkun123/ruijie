package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.enums;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.HardwareCertificationBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.userprofile.TextExcelField;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/23 19:10
 *
 * @author yxq
 */
public enum UserMacBindingHeader implements TextExcelField {

    USER_NAME(LocaleI18nResolver.resolve(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_BINDING_USER_NAME), 0),
    TERMINAL_MAC(LocaleI18nResolver.resolve(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_BINDING_MAC), 1);

    private final String header;

    private final Integer index;

    UserMacBindingHeader(String header, Integer index) {
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
