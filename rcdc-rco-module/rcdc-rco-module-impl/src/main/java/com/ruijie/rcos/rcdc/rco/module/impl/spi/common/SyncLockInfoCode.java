package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: 同步用户锁定信息错误码
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/17 23:52
 *
 * @author yxq
 */
public interface SyncLockInfoCode extends CommonMessageCode  {

    /**
     * 判断用户是否锁定失败
     */
    int JUDGE_USER_LOCK_FAIL = 2;
}
