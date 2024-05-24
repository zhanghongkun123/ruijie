package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: 登录结果码
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/09/28
 *
 * @author linke
 */
public interface UserUnifiedLoginResultCode {

    int SUCCESS = 0;

    /** 用户名或密码错误 */
    int NAME_OR_PASSWORD_ERROR = 1;

    /** 当前为访客帐号登录,不允许作为普通用户登录 */
    int VISITOR_LOGIN = 2;

    /** RCDC未授权 不允许登录 */
    int NOT_LICENSE = 3;

    /** AD域服务器异常 */
    int AD_SERVER_ERROR = 4;

    /** 用户被禁用 */
    int AD_ACCOUNT_DISABLE = 5;

    /** 当前时间不允许登录 */
    int AD_LOGIN_LIMIT = 6;

    /** AD帐户过期 */
    int AD_ACCOUNT_EXPIRE = 7;

    /** 非访客用户不允许用于访客登录 */
    int NOT_ALLOW_LOGIN_FOR_NOT_VISITOR = 8;

    /** 终端和用户绑定关系不匹配（个人模式） */
    int TERMINAL_BIND_USER_NOT_MATCH_ERROR = 9;

    /** 终端信息不存在 */
    int TERMINAL_NOT_EXIST = 10;

    /** 用户被锁定 */
    int USER_LOCKED = 12;

    /** 提示剩余密码可错误次数 */
    int REMIND_ERROR_TIMES = 13;

    /** 硬件特征码审批中 */
    int HARDWARE_PENDING_APPROVE = 14;

    /** 硬件特征码认证被驳回 */
    int HARDWARE_REJECTED = 15;

    /** 硬件特征码认证超过最大数量 */
    int HARDWARE_OVER_MAX = 16;

    /**
     * 用户末开启CAS认证策略
     */
    int CAS_AUTH_UNOPENED = 30;

    /**
     * 用户末开启帐号密码登录
     */
    int USERNAME_OR_PASSWORD_UNOPENED = 31;

    /**
     * 账号失效
     */
    int ACCOUNT_INVALID = 33;

    /**
     * 未开启全局CAS认证
     */
    int UNENABLE_GLOBAL_CAS = 50;

    /**
     * 用户未绑定过动态口令
     */
    int USER_NO_BIND_OTP = 94;

    /**
     * 动态口令不一致
     */
    int OTP_INCONSISTENT = 95;

    /**
     * 用户未开启动态口令
     */
    int UNENABLE_USER_OTP = 96;

    /**
     * 未开启全局动态口令
     */
    int UNENABLE_GLOBAL_OTP = 97;

    /**
     * 用户不存在
     */
    int USER_NOT_EXIST = 98;

    //region CAS扫码登录错误码

    /**
     * CAS 配置信息不存在
     */
    int CAS_CONFIG_NON_EXISTENT = -33;

    /**
     * 二维码未被扫码
     */
    int QR_CODE_NOT_SCAN_CODE = -35;

    /**
     * 二维码过期
     */
    int QR_CODE_EXPIRE = -36;

    /**
     * CAS服务器异常
     */
    int API_FAILURE = -37;

    /**
     * 输入参数错误
     */
    int PARAM_ERROR = -38;

    /**
     * 令牌不存在
     */
    int TICKET_NOT_EXISTS = -39;

    /**
     * 设备变更
     */
    int ACCOUNT_LOCKED = -40;

    /**
     * 帐号被锁
     */
    int DEVICE_CHANGED = -41;

    /**
     * 密码过期
     */
    int PASSWORD_EXPIRED = -42;


    /**
     * 扫码用户不存在
     */
    int QR_USER_NOT_EXIST = -43;

    /**
     * 服务端异常
     */
    int SERVER_EXCEPTION = -44;

    /**
     * TICKET验证失败
     */
    int TICKET_VERIFY_FAIL = -45;

    /**
     * 未知
     */
    int QR_UNKNOWN = -66;
    //endregion


    int NO_CLUSTER_CACHE = -111;

    /**
     * rcdc接口内部校验节点信息不通过
     */
    int RCDC_CLUSTER_VALIDATE_FAIL = -500;

    /**
     * 调用http接口时出现异常
     */
    int REQUEST_RCDC_HTTP_ERROR = -9500;
}
