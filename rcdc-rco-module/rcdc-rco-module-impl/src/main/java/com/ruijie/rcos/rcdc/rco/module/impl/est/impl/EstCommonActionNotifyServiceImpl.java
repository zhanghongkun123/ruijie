package com.ruijie.rcos.rcdc.rco.module.impl.est.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * Description: Est subAction分发器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31 11:42
 *
 * @author lihengjing
 */
@Service
public class EstCommonActionNotifyServiceImpl implements EstCommonActionNotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstCommonActionNotifyServiceImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Override
    public void responseToEst(String terminalId, String subAction, String result) throws BusinessException {
        Assert.notNull(terminalId, "terminalId is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(result, "result is need not null");
        // 构造Shine透传Est返回对象 并且发起请求
        CbbShineMessageRequest cbbShineMessageRequest = CbbShineMessageRequest.create(ShineAction.RCDC_TRANSPARENT_EST_COMMON_ACTION, terminalId);
        cbbShineMessageRequest.setContent(result);
        LOGGER.info("RCDC响应EST用户自助快照应答，{}", JSON.toJSONString(cbbShineMessageRequest));

        cbbTranspondMessageHandlerAPI.asyncRequest(cbbShineMessageRequest, new CbbTerminalCallback() {
            @Override
            public void success(String terminalId, CbbShineMessageResponse msg) {
                Assert.notNull(terminalId, "terminalId cannot be null!");
                Assert.notNull(msg, "msg cannot be null!");
                LOGGER.info("通知终端用户自定义快照{}请求成功，terminalId[{}]，信息[{}]", subAction, terminalId, JSON.toJSONString(msg));
            }

            @Override
            public void timeout(String terminalId) {
                Assert.notNull(terminalId, "terminalId cannot be null!");
                LOGGER.error("通知终端用户自定义快照{}请求超时，terminalId[{}]", subAction, terminalId);
            }
        });
    }
}
