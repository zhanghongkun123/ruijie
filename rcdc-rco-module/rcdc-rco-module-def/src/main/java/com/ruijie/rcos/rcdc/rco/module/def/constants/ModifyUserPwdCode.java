package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: CMS对接：修改用户密码错误码
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/28 15:15
 *
 * @author yxq
 */
public interface ModifyUserPwdCode extends CommonMessageCode {

    /**
     * 旧密码不正确
     */
    int OLD_PASSWORD_ERROR = 1;

    /**
     * AD域用户不允许修改密码
     */
    int AD_USER_NOT_ALLOW_CHANGE_PASSWORD = 2;

    /**
     * LDAP用户不允许修改密码
     */
    int LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD = 3;

    /**
     * 用户不存在
     */
    int NOT_THIS_USER = 4;

    /**
     * 修改密码失败
     */
    int CHANGE_PASSWORD_FAIL = 5;

    /**
     * 修改密码不符合密码策略要求
     */
    int CHANGE_PASSWORD_UNABLE_REQUIRE = 6;

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
}
