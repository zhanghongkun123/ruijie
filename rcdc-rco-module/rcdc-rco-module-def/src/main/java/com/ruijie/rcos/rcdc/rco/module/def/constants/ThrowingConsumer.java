package com.ruijie.rcos.rcdc.rco.module.def.constants;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/31
 *
 * @param <T> 泛型
 * @author TD
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {

    /**
     * 执行函数
     * @param t 入参
     * @throws BusinessException 业务异常
     */
    void accept(T t) throws BusinessException;
}
