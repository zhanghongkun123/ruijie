package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月15日
 *
 * @author xwx
 */
public enum ConfigStartModeCode {

    SUCCESS(0, BusinessKey.RCDC_TERMINAL_CODE_SUCCESS),

    NOT_TCI(1, BusinessKey.RCDC_TERMINAL_CODE_NOT_TCI),

    NOT_SUPPORT_TC(2, BusinessKey.RCDC_TERMINAL_CODE_NOT_SUPPORT_TC),

    LOCAL_EDIT(3, BusinessKey.RCDC_TERMINAL_CODE_EDITING),

    CHECK_OR_FIX_IMAGE(4, BusinessKey.RCDC_TERMINAL_CODE_IMAGE_FIXING),

    WIN732_NO_UEFI(5, BusinessKey.RCDC_TERMINAL_CODE_WIN732_NOT_SUPPORT_TC),

    DOING_INITIAL(6, BusinessKey.RCDC_TERMINAL_CODE_INITIALING),

    DOING_CHANGE_START_MODE(7, BusinessKey.RCDC_TERMINAL_CODE_SWITCHING_START_MODE),

    OTHER(99, BusinessKey.RCDC_TERMINAL_CODE_OTHER);

    private final int code;

    private final String message;

    ConfigStartModeCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据code获取错误码
     * @param code code
     * @return 错误
     * @throws BusinessException 异常
     */
    public static ConfigStartModeCode getByCode(int code) throws BusinessException {
        for (ConfigStartModeCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        throw new BusinessException(BusinessKey.RCDC_CONFIG_START_CODE_NOT_FIND);
    }

}
