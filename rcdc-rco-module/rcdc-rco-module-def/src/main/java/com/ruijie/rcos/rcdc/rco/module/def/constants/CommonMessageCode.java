package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7
 *
 * @author wjp
 */
public interface CommonMessageCode {

    int SUCCESS = 0;

    int FAIL = 1;

    int CLUSTER_NO_NORMAL = 98;

    int CODE_ERR_OTHER = 99;

    /**
     * 线程满 请求处理中，请稍后再试
     */
    int THREAD_BUSY = 97;

    int DEFAULT_ERROR = -100;

    /**
     * 用户终端登录失效
     */
    int NO_USER_LOGIN_CACHE = 106;

    /**
     * cdc的vip填写异常
     */
    int RCDC_VIP_ERROR = 110;

    int USER_NOT_EXIST = 120;

}
