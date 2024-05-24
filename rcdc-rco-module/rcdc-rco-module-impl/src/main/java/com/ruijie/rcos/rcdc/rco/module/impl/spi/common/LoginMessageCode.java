package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: 登录业务code
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
public interface LoginMessageCode extends CommonMessageCode {

    /**
     * 用户名或密码错误
     */
    int USERNAME_OR_PASSWORD_ERROR = 1;

    /**
     * 当前为访客账号登录,不允许作为普通用户登录
     */
    int VISITOR_LOGIN = 2;

    /**
     * RCDC未授权 不允许登录
     */
    int NOT_LICENSE = 3;

    /**
     * AD域服务器异常
     */
    int AD_SERVER_ERROR = 4;

    /**
     * 用户被禁用
     */
    int AD_ACCOUNT_DISABLE = 5;

    /**
     * 当前时间不允许登录
     */
    int AD_LOGIN_LIMIT = 6;

    /**
     * AD账户过期
     */
    int AD_ACCOUNT_EXPIRE = 7;

    /**
     * 非访客用户不允许用于访客登录
     */
    int NOT_ALLOW_LOGIN_FOR_NOT_VISITOR = 8;

    /**
     * 终端和用户绑定关系不匹配（个人模式）
     */
    int TERMINAL_BIND_USER_NOT_MATCH_ERROR = 9;

    /**
     * 终端信息不存在
     */
    int TERMINAL_NOT_EXIST = 10;

    int LDAP_SERVER_ERROR = 11;

    /**
     * 用户被锁定
     */
    int USER_LOCKED = 12;

    /**
     * 提示剩余密码可错误次数
     */
    int REMIND_ERROR_TIMES = 13;

    /**
     * 硬件特征码审批中
     */
    int HARDWARE_PENDING_APPROVE = 14;

    /**
     * 硬件特征码认证被驳回
     */
    int HARDWARE_REJECTED = 15;

    /**
     * 硬件特征码认证超过最大数量
     */
    int HARDWARE_OVER_MAX = 16;

    /**
     * 用户本地认证禁用
     */
    int AD_ACCOUNT_LOCAL_AUTH_DISABLE = 17;

    /**
     * 用户本地认证密码过期
     */
    int AD_ACCOUNT_LOCAL_AUTH_PASSWORD_EXPIRE = 18;

    /**
     * 图形验证码错误且未启用账号锁定策略
     */
    int CAPTCHA_ERROE_AND_CLOSE_LOCK = 19;

    /**
     * 用户末开启CAS认证策略
     */
    int CAS_AUTH_UNOPENED = 30;

    /**
     * 用户末开启账号密码登录
     */
    int USERNAME_OR_PASSWORD_UNOPENED = 31;

    /**
     * 未开启全局CAS认证
     */
    int UNENABLE_GLOBAL_CAS = 50;

    /**
     * 用户名或动态口令错误
     */
    int USERNAME_OR_OTP_ERROR = 98;

    /**
     * 未开启全局动态口令
     */
    int UNENABLE_GLOBAL_OTP = 97;

    /**
     * 用户未开启动态口令
     */
    int UNENABLE_USER_OTP = 96;

    /**
     * 动态口令不一致
     */
    int OTP_INCONSISTENT = 95;

    /**
     * 用户未绑定过动态口令
     */
    int USER_NO_BIND_OTP = 94;

    /**
     * 密码解密异常
     */
    int DESCRYPT_PWD_ERROR = 93;

    /**
     * 用户绑定动态口令失败
     */
    int USER_BIND_OTP_ERROR = 92;

    /**
     * 账号失效
     */
    int ACCOUNT_INVALID = 33;

    /**
     * 图形验证码错误
     */
    int CAPTCHA_ERROE = 66080083;

    /**
     * 图形验证码过期
     **/
    int INVALID_CAPTCHA = 66080082;

}
