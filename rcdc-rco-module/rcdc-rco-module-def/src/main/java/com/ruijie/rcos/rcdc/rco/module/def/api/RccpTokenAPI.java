package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/17
 *
 * @author jarman
 */
public interface RccpTokenAPI {

    /**
     * 校验登录的token有效性
     * 
     * @param token token
     * @throws BusinessException 业务异常
     */
    void checkLoginToken(String token) throws BusinessException;

}
