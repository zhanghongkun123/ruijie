package com.ruijie.rcos.rcdc.rco.module.impl.spi.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.task.module.def.api.BaseMsgMgmtAPI;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetMsgRequest;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionSubResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: Est发起的 任务消息查询
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31 11:42
 *
 * @author lihengjing
 */
@Service
public class MessageImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageImpl.class);

    @Autowired
    private BaseMsgMgmtAPI baseMsgMgmtAPI;

    @Autowired
    private EstCommonActionNotifyService estCommonActionNotifyService;

    /**
     * 任务详情查询
     * 
     * @param dispatcherDTO 原始的分发器传输对象
     * @param subAction 真实动作
     * @param data 业务参数
     * @throws BusinessException 业务异常
     */
    public void detail(CbbDispatcherDTO dispatcherDTO, String subAction, JSONObject data) throws BusinessException {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        BaseGetMsgRequest baseGetMsgRequest = JSONObject.toJavaObject(data, BaseGetMsgRequest.class);
        Assert.notNull(baseGetMsgRequest.getMsgRelationId(), "msgRelationId is need not null");
        Assert.notNull(baseGetMsgRequest.getMsgType(), "msgType is need not null");
        BaseMsgDTO msg = baseMsgMgmtAPI.getMsg(baseGetMsgRequest);

        EstCommonActionResponse<BaseMsgDTO> estCommonActionResponse = new EstCommonActionResponse<>();
        estCommonActionResponse.setSubAction(subAction);
        estCommonActionResponse.setData(EstCommonActionSubResponse.success(msg));

        estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, JSON.toJSONString(estCommonActionResponse));
    }
}
