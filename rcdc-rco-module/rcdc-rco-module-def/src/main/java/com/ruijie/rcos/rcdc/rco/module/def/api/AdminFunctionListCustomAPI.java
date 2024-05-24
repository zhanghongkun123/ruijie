package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.FunctionListCustomRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FunctionListCustomResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 功能列表自定义管理api
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月28日
 *
 * @author brq
 */
public interface AdminFunctionListCustomAPI {

    /**
     * 获取管理员在指定功能列表页面的自定义列数据
     * @param request 请求数据
     * @return 响应数据
     */
    FunctionListCustomResponse getFunctionListOfColumnMsg(FunctionListCustomRequest request);

    /**
     * 保存管理员在指定功能列表页面的自定义列数据
     * @param request 请求数据
     * @throws BusinessException 业务异常
     */
    void saveFunctionListOfColumnMsg(FunctionListCustomRequest request) throws BusinessException;

}
