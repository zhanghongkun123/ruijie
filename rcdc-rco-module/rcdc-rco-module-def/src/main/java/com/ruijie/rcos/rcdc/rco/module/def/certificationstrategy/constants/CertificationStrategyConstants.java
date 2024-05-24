package com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants;

/**
 * 全局表常量
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月23日
 *
 * @author zhang.zhiwen
 */
public interface CertificationStrategyConstants {

    /**
     * 策略认证-配置数据
     */
    String CERTIFICATION_STRATEGY_SUMMARY = "certification_strategy_summary";

    /**
     * 永久锁定
     */
    int PERMANENT_LOCK = -1;

    /**
     * 永久锁定时锁定99年
     */
    int PERMANENT_LOCK_YEAR = 99;

    /**
     * 公共常量值
     */
    interface CommonValue {
        String ON = "1";
        String OFF = "0";
    }

    /** 认证策略配置-永久 */
    String RCDC_CERTIFICATION_STRATEGY_DEFAULT_FOREVER = "0";

    /** 认证策略配置-永久 */
    String RCDC_CERTIFICATION_STRATEGY_DEFAULT_CUSTOM = "1";

    /** 认证策略配置-密码有效期-自定义默认 */
    Integer RCDC_CERTIFICATION_STRATEGY_VALID_DEFAULT = 30;

    /** 认证策略配置-锁定时长-自定义默认 */
    Integer RCDC_CERTIFICATION_STRATEGY_LOCK_TIME_DEFAULT = 30;

    /** 认证策略配置-锁定时长-永久锁定 */
    Integer RCDC_CERTIFICATION_STRATEGY_LOCK_TIME_FOREVER = -1;

    /** 认证策略配置-启用认证策略 */
    Boolean RCDC_CERTIFICATION_STRATEGY_ENABLE_FOREVER = true;

    /** 认证策略配置-密码有效期 */
    Integer RCDC_CERTIFICATION_STRATEGY_UPDATE_DATE = -1;

    /** 认证策略配置-密码复杂度 */
    String RCDC_CERTIFICATION_STRATEGY_LEVEL = "1";

    /** 认证策略配置-启用防爆攻击 */
    Boolean RCDC_CERTIFICATION_STRATEGY_PREVENTS_BRUTE_FORCE = true;

    /** 认证策略配置-有效期或者时长永久 */
    Integer RCDC_DEFAULT_ON = 0;

    /** 认证策略配置-剩余天数提醒-自定义默认 */
    Integer RCDC_CERTIFICATION_STRATEGY_PWD_EXPIRE_REMIND_DAYS_DEFAULT = 10;

    /** 认证策略配置-密码剩余天数提醒-不提醒 */
    Integer RCDC_CERTIFICATION_STRATEGY_PWD_EXPIRE_REMIND_DAY = -1;

    /** 认证策略配置-防爆-用户锁定时长-自定义默认 */
    Integer RCDC_CERTIFICATION_STRATEGY_USER_LOCK_TIME_DEFAULT = 30;

    /** 认证策略配置-防爆-管理员允许错误次数 */
    Integer RCDC_CERTIFICATION_STRATEGY_ADMIN_ERROR_TIMES = 10;

    /** 认证策略配置-防爆-管理员锁定时长-自定义默认 */
    Integer RCDC_CERTIFICATION_STRATEGY_ADMIN_LOCK_TIME_DEFAULT = 30;

    /** 认证策略配置-防爆-终端管理密码允许错误次数 */
    Integer RCDC_CERTIFICATION_STRATEGY_TERMINAL_ERROR_TIMES = 10;

    /** 认证策略配置-防爆-终端管理密码锁定时长-自定义默认 */
    Integer RCDC_CERTIFICATION_STRATEGY_TERMINAL_LOCK_TIME_DEFAULT = 30;

    /** 认证策略配置-防爆-用户允许错误次数 */
    Integer RCDC_CERTIFICATION_STRATEGY_USER_ERROR_TIMES = 10;

    /** 认证策略配置-首次登录强制修改密码 */
    Boolean RCDC_CERTIFICATION_STRATEGY_FIRST_FORCE_UPDATE_PASSWORD_DEFAULT = Boolean.FALSE;

    /** 认证策略配置-首次登录强制修改密码 */
    Boolean RCDC_CERTIFICATION_STRATEGY_FIRST_FORCE_UPDATE_PASSWORD_DEFAULT_FROM_UNSUPPORTED_VERSION = Boolean.TRUE;

}
