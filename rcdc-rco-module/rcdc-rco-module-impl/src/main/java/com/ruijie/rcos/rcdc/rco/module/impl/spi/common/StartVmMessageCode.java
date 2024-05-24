package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: 启动虚机业务code
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/3/21
 *
 * @author Jarman
 */
public interface StartVmMessageCode extends CommonMessageCode {

    /** 当前桌面处于不可用状态 */
    int CODE_ERR_STATE = 1;

    /** 用户未登录 */
    int CODE_ERR_HAS_NOT_LOGIN = 2;

    /** 系统处于维护模式 */
    int CODE_ERR_UNDER_MAINTENANCE = -2;

    /** 用户未登录 */
    int USER_NOT_LOGIN = -3;

    /** 云桌面处于维护模式 */
    int CODE_ERR_UNDER_DESKTOP_MAINTENANCE = -4;

    /** 云桌面因节点资源不足唤醒失败 */
    int CODE_ERR_WAKE_UP_FAIL_BY_RESOURCE_INSUFFICIENT = -6;
}
