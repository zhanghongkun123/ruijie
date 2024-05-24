package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 云坞Token API
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @author chenjuan
 */
public interface CloudDockTokenAPI {

    /**
     * 校验登录的token有效性
     *
     * @param token token
     * @throws BusinessException 业务异常
     */
    void checkLoginToken(String token) throws BusinessException;
}
