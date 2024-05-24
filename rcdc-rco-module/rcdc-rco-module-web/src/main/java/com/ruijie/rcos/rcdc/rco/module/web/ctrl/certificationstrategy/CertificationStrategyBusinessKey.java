package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy;

/**
 *
 * Description: 认证策略日志
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月09日
 *
 * @author zhang.zhiwen
 */
public interface CertificationStrategyBusinessKey {

    /** 认证策略配置成功 */
    String RCDC_CERTIFICATION_STRATEGY_CONFIG_SUCCESS = "rcdc_certification_strategy_config_success";

    /** 认证策略配置失败 */
    String RCDC_CERTIFICATION_STRATEGY_CONFIG_FAIL = "rcdc_certification_strategy_config_fail";

    /** 锁定用户解锁成功 */
    String RCDC_LOCK_USER_UNLOCK_SUCCESS = "rcdc_lock_user_unlock_success";

    /** 审核日志-锁定用户解锁成功 */
    String RCDC_LOCK_USER_UNLOCK_SUCCESS_WITH_USERNAME = "rcdc_lock_user_unlock_success_with_username";

    /** 锁定用户解锁失败 */
    String RCDC_LOCK_USER_UNLOCK_FAIL = "rcdc_lock_user_unlock_fail";

    /** 审核日志-锁定用户解锁失败 */
    String RCDC_LOCK_USER_UNLOCK_FAIL_WITH_USERNAME = "rcdc_lock_user_unlock_fail_with_username";

    /** 自动设置Windows本地管理员密码 */
    String RCDC_USER_WINDOW_AUTO_LOGIN_SUCCESS = "rcdc_user_window_auto_login_success";

    /** 用户已解锁 */
    String RCDC_LOCK_USER_HAS_BEEN_UNLOCKED = "23200766";

    /** 自动设置Windows本地管理员密码 */
    String RCDC_USER_WINDOW_AUTO_LOGIN_FAIL = "23201630";

    /**
     * 锁定管理员解锁失败
     */
    String RCDC_LOCKED_ADMIN_UNLOCK_FAIL = "23200759";

    /**
     * 管理员不能解锁自己
     */
    String RCDC_ADMIN_CAN_NOT_UNLOCK_YOURSELF = "23200760";

    /**
     * 锁定管理员解锁成功
     */
    String RCDC_LOCKED_ADMIN_UNLOCK_SUCCESS = "rcdc_locked_admin_unlock_success";

    /**
     * 管理员处于未锁定状态
     */
    String RCDC_ADMIN_HAVE_NOT_CLOCK = "23200761";

    /**
     * 没有权限解锁管理员
     */
    String RCDC_NOT_AUTH_UNLOCK_ADMIN = "23200762";

    /**
     * 锁定管理员{}解锁失败
     */
    String RCDC_LOCKED_ADMIN_UNLOCK_FAIL_WITH_NAME = "23200763";

    /**
     * 锁定管理员{}解锁成功
     */
    String RCDC_LOCKED_ADMIN_UNLOCK_SUCCESS_WITH_NAME = "rcdc_locked_admin_unlock_success_with_name";

    /**
     * 管理员{}处于于锁定状态
     */
    String RCDC_ADMIN_HAVE_NOT_CLOCK_WITH_NAME = "rcdc_admin_have_not_clock_with_name";

    /**
     * 没有权限解锁管理员{}
     */
    String RCDC_NOT_AUTH_UNLOCK_ADMIN_WITH_NAME = "rcdc_not_auth_unlock_admin_with_name";

    /**
     * 终端处于未锁定状态
     */
    String RCDC_TERMINAL_HAVE_NOT_CLOCK = "23200764";

    /**
     * 终端{}处于未锁定状态
     */
    String RCDC_TERMINAL_HAVE_NOT_CLOCK_WITH_NAME = "rcdc_terminal_have_not_clock_with_name";

    /**
     * 锁定终端{}解锁失败
     */
    String RCDC_LOCK_TERMINAL_UNLOCK_FAIL_WITH_NAME = "rcdc_lock_terminal_unlock_fail_with_name";

    /**
     * 锁定终端解锁失败
     */
    String RCDC_LOCK_TERMINAL_UNLOCK_FAIL = "23200765";

    /**
     * 锁定终端{}解锁成功
     */
    String RCDC_LOCK_TERMINAL_UNLOCK_SUCCESS_WITH_NAME = "rcdc_lock_terminal_unlock_success_with_name";

    /**
     * 锁定终端解锁成功
     */
    String RCDC_LOCK_TERMINAL_UNLOCK_SUCCESS = "rcdc_lock_terminal_unlock_success";
}
