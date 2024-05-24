package com.ruijie.rcos.rcdc.rco.module.impl.spi.exception;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/14
 *
 * @author zhengjingyong
 */
public class ShineUserNotLoginException extends BusinessException {

    public ShineUserNotLoginException(String key, String... args) {
        super(key, args);
    }

    public ShineUserNotLoginException(String key, Throwable throwable, String... args) {
        super(key, throwable, args);
    }
}
