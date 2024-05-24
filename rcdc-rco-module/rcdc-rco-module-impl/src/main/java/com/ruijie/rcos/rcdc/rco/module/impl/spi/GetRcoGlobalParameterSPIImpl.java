package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.RcoGlobalParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.RcoGlobalParameterResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: shine请求查询密码相关配置信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/1
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.GET_RCO_GLOBAL_PARAMETER)
public class GetRcoGlobalParameterSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetRcoGlobalParameterSPIImpl.class);

    @Autowired
    RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not null");
        String requestData = request.getData();
        Assert.notNull(requestData, "报文消息体不能为空");
        RcoGlobalParameterRequest parameterRequest = JSONObject.toJavaObject
                (JSONObject.parseObject(requestData), RcoGlobalParameterRequest.class);
        RcoGlobalParameterResponse parameterResponse = new RcoGlobalParameterResponse(parameterRequest.getParamKey(),
                rcoGlobalParameterAPI.findParameter(new FindParameterRequest(parameterRequest.getParamKey())).getValue());
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, CommonMessageCode.SUCCESS, parameterResponse);
        } catch (Exception e) {
            LOGGER.error("终端{}获取{}配置信息失败，e={}", request.getTerminalId(), parameterRequest.getParamKey(), e);
        }
    }
}
