package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.enums.TokenSourceEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/6
 *
 * @author chenjuan
 */
public interface AdminTokenLoginAPI {

    /**
     *
     * 校验登陆token
     * @param tokenSource TokenSourceEnum
     * @param token token
     * @throws BusinessException 业务异常
     */
    void isTokenValid(TokenSourceEnum tokenSource, String token) throws BusinessException;
}
