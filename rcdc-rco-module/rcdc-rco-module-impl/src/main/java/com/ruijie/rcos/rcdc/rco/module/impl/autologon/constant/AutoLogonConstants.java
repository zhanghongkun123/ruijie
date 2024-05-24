package com.ruijie.rcos.rcdc.rco.module.impl.autologon.constant;

/**
 * Description: 自动登录常量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/3
 *
 * @author TD
 */
public interface AutoLogonConstants {


    /**
     * 自动登录开关
     */
    String AUTO_LOGON_KEY = "auto_logon_key";

    /**
     * 自动登录开启
     */
    String WINDOWS_AUTO_LOGON_ON = "Y";

    /**
     * 自动登录关闭
     */
    String WINDOWS_AUTO_LOGON_OFF = "N";

    /**
     * 返回成功
     */
    int WINDOWS_AUTO_LOGON_SUCCESS = 0;

    /**
     * 自动同步用户名
     */
    String WINDOWS_AUTO_SET_NAME_ON = "Y";

    /**
     * 不自动同步用户名
     */
    String WINDOWS_AUTO_SET_NAME_OFF = "N";


}