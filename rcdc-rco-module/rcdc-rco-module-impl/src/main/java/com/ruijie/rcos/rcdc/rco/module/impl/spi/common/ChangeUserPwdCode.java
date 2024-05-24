package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: 终端用户修改密码返回的状态码
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/21
 *
 * @author Jarman
 */
public interface ChangeUserPwdCode extends CommonMessageCode {

    /** 旧密码不正确 */
    int OLD_PASSWORD_ERROR = 1;

    /** AD域用户不允许修改密码 */
    int AD_USER_NOT_ALLOW_CHANGE_PASSWORD = 2;

    /** LDAP用户不允许修改密码 */
    int LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD = 3;

    /** 查无此人 */
    int NOT_THIS_USER = 4;

    /** 修改密码失败 */
    int CHANGE_PASSWORD_FAIL = 5;

    /** 修改密码不符合密码策略要求 */
    int CHANGE_PASSWORD_UNABLE_REQUIRE = 6;

    /** 访客不允许修改密码 */
    int VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD = 7;

    /**
     * 用户名和密码一致
     */
    int NAME_LIKE_PASWARD = 8;

    /**
     * 系统处于维护模式状态
     */
    int IN_MAINTENANCE = -7;

    /**
     * 用户处于锁定状态
     */
    int IN_LOCKED = -10;

    /**
     * 提示剩余密码可错误次数
     */
    int REMIND_ERROR_TIMES = -11;

    /**
     * token失效或不一致
     */
    int TOKEN_ERROR = -12;

    /** 
     * 查无此人-短信 
     */
    int SMS_NOT_THIS_USER = -13;
    
    /** 
     * 新密码不能与旧密码相同
     */
    int NEW_OLD_PWD_NOT_EQUAL = -14;

    /**
     * 弱密码
     */
    int WEAK_PWD = -17;

    /** 第三方用户不允许修改密码 */
    int THIRD_PARTY_USER_NOT_ALLOW_CHANGE_PASSWORD = -16;
}
