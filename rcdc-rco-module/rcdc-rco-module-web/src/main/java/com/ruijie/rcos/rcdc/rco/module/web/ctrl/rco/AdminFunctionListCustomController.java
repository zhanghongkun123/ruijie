package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminFunctionListCustomAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FunctionListCustomRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FunctionListCustomResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.FunctionListCustomWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: 功能列表自定义管理
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月28日
 *
 * @author brq
 */
@Controller
@RequestMapping("/rco/listCustom")
public class AdminFunctionListCustomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminFunctionListCustomController.class);

    @Autowired
    private AdminFunctionListCustomAPI adminFunctionListCustomAPI;

    /**
     * 获取管理员在指定功能列表页面的自定义列数据
     * @param webRequest 请求参数
     * @param sessionContext session信息
     * @return 响应数据
     */
    @RequestMapping(value = "/getFunctionListCustom")
    public DefaultWebResponse getFunctionListCustom(FunctionListCustomWebRequest webRequest, SessionContext sessionContext) {
        Assert.notNull(webRequest, "webRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        LOGGER.info("获取管理员在指定功能列表页面的自定义列数据请求参数：webRequest=[{}]", JSONObject.toJSONString(webRequest));

        FunctionListCustomRequest request = new FunctionListCustomRequest();
        BeanUtils.copyProperties(webRequest, request);
        request.setAdminId(sessionContext.getUserId());
        FunctionListCustomResponse response = adminFunctionListCustomAPI.getFunctionListOfColumnMsg(request);
        LOGGER.info("AdminFunctionListCustomController.getFunctionListCustom response=[{}]",
            JSONObject.toJSONString(response));

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 保存管理员在指定功能列表页面的自定义列数据
     * @param webRequest 请求数据
     * @param sessionContext session信息
     * @throws BusinessException 业务异常
     * @return 响应数据
     */
    @RequestMapping(value = "/saveFunctionListCustom")
    public DefaultWebResponse saveFunctionListCustom(FunctionListCustomWebRequest webRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        FunctionListCustomRequest request = new FunctionListCustomRequest();
        BeanUtils.copyProperties(webRequest, request);
        request.setAdminId(sessionContext.getUserId());

        adminFunctionListCustomAPI.saveFunctionListOfColumnMsg(request);

        return DefaultWebResponse.Builder.success();
    }

}
