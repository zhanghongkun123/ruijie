package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.EvaluationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ShineEvaluationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 获取评测功能策略SPI
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/5 10:03
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.SYNC_EVALUATION_STRATEGY)
public class GetEvaluationStrategySPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetEvaluationStrategySPIImpl.class);

    @Autowired
    private EvaluationAPI evaluationAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request must not null");

        LOGGER.info("接收到终端获取评测策略请求，请求参数为[{}]", request.getData());
        try {
            // 查询策略
            Boolean enableEvaluation = evaluationAPI.getEvaluationStrategy();
            // 构造DTO
            ShineEvaluationDTO shineEvaluationDTO = new ShineEvaluationDTO(enableEvaluation, System.currentTimeMillis());
            // 返回给SHINE
            responseMessage(request, CommonMessageCode.SUCCESS, shineEvaluationDTO);
        } catch (Exception e) {
            responseMessage(request, CommonMessageCode.CODE_ERR_OTHER, null);
            LOGGER.error("获取评测策略失败，失败原因：{}", e);
        }
    }

    private void responseMessage(CbbDispatcherRequest request, int code, ShineEvaluationDTO shineEvaluationDTO) {
        try {
            LOGGER.info("响应终端[{}]评测策略为[{}]，响应码为[{}]", request.getTerminalId(), JSON.toJSONString(shineEvaluationDTO), code);
            shineMessageHandler.responseContent(request, code, shineEvaluationDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("响应终端[{%s}]评测策略失败，失败原因：", request.getTerminalId()), e);
        }
    }
}
