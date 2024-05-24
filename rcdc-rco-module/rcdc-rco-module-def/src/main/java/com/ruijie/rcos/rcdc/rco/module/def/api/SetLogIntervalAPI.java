package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetLogIntervalRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 日志周期设置API
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/17
 *
 * @author zhiweiHong
 */
public interface SetLogIntervalAPI {
    /**
     * 设置日志周期
     * @param request SetLogIntervalRequest
     * @return DefaultResponse
     * @throws  BusinessException BusinessException
     */

    DefaultResponse setLogInterval(SetLogIntervalRequest request) throws BusinessException;
}
