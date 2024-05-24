package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.VerifyAdminRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/17
 *
 * @author jarman
 */
public interface RcdcTokenAPI {

    /**
     * 校验登录的token有效性
     * 
     * @param token token
     * @throws BusinessException 业务异常
     * @return 管理员id
     */
    UUID checkLoginToken(UUID token) throws BusinessException;

    /**
     * 申请token
     *
     * @param request 管理员id
     * @return token
     * @throws BusinessException 业务异常
     */
    String applyToken(VerifyAdminRequest request) throws BusinessException;

}
