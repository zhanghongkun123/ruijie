package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
public interface CommonMessageCode {

    int IN_MAINTENANCE = -2;

    int SUCCESS = 0;

    int USER_NOT_EXIST = 120;

    int CODE_ERR_OTHER = 99;

    int FAIL_CODE = -1;

    int DESKTOP_NOT_EXIST = -5;

    int EXIT_RCCM_MANAGED = 100;

    int RCCM_VIP_ERROR = 103;

    int OTHER_RCCM_MANAGED = 101;

    int ENTER_APP_TEST = 102;

    /**
     * 用户未在终端登陆||登陆信息过期||CDC重启会话清除
     */
    int CODE_USER_NO_LOGIN = 23997890;

    /**
     * 图形验证码错误
     */
    int CAPTCHA_ERROE = 66080083;

    /**
     * 图形验证码无效
     */
    int INVALID_CAPTCHA = 66080082;

    /**
     * 请输入图形验证码
     */
    int NOT_CAPTCHA = 66080084;

    /**
     * 用户不存在且开启图形验证码时被锁定
     */
    int USER_NOT_EXIST_WITH_CAPTCHA_LOCKED = 66080127;

    /**
     * 用户不存在且未开启图形验证码时被锁定
     */
    int USER_NOT_EXIST_LOCKED = 66061022;

}
