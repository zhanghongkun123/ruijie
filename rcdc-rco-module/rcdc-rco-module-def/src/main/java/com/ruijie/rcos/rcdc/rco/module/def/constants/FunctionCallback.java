package com.ruijie.rcos.rcdc.rco.module.def.constants;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 
 * Description: 方法回调
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/12
 *
 * @author TD
 */
@FunctionalInterface
public interface FunctionCallback {

    /**
     * Applies this function to the given argument.
     *
     * @throws BusinessException 业务异常
     */
    void callback() throws BusinessException;
}
