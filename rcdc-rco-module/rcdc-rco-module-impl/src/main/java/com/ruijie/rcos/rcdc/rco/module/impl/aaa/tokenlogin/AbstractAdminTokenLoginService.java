package com.ruijie.rcos.rcdc.rco.module.impl.aaa.tokenlogin;

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
public interface AbstractAdminTokenLoginService {

    /**
     * 获取token来源
     * @return TokenSourceEnum
     */
    TokenSourceEnum getTokenSource();

    /**
     * 是否token校验成功
     * @param token token
     * @throws BusinessException 业务异常
     */
    void validAdminToken(String token) throws BusinessException;
}
