package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 可抛出业务异常的函数接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/18 17:14
 *
 * @param <T> 请求参数
 * @param <R> 返回参数
 * @author zhangyichi
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

    /**
     * 执行函数
     * @param t 入参
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    R apply(T t) throws BusinessException;
}
