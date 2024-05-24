package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.enums;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.consts.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author zhangsiming
 */
public enum PreparationCheckResultEnum {
    /**
     * 成功结果
     */
    RET_SUCCESS("0", LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_PREPARE_CHECK_SUCCESS)),


    /**
     * samba未挂载
     */
    RET_SAMBA_NOT_MOUNTED("10000", LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_PREPARE_CHECK_SAMBA_NOT_MOUNTED)),

    /**
     * 维护模式
     */
    RET_IN_MAINTENANCE("10001", LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_PREPARE_CHECK_IN_MAINTENANCE)),

    /**
     * 服务器备份中
     */
    RET_IN_BACKUP("10002", LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_PREPARE_CHECK_IN_BACKUP)),

    /**
     * 镜像操作繁忙
     */
    RET_IN_IMAGE_BUSY("10003", LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_PREPARE_CHECK_IN_IMAGE_BUSY)),


    /**
     * 服务器处于配置向导中
     */
    RET_IN_CONFIG_WIZARD("10004", LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_PREPARE_CHECK_IN_CONFIG_WIZARD)),

    /**
     * 未知业务异常
     */
    RET_UNKNOWN("10999", LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_PREPARE_CHECK_UNKNOWN));

    private String code;
    private String msg;

    PreparationCheckResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
