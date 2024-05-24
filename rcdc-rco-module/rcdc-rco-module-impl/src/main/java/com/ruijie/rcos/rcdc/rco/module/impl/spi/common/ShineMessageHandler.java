package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Rco和Shine通信
 */
@Service("rcoShineMessageHandler")
public class ShineMessageHandler {

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    /**
     * json转对象
     *
     * @param json 待转的json字符串
     * @param clz 目标对象class
     * @param <T> 目标对象泛型
     * @return 返回目标对象
     * @exception Exception Exception
     */
    public <T> T parseObject(String json, Class<T> clz) throws Exception {
        Assert.hasText(json, "待转的json字符串不能为空");
        Assert.notNull(clz, "待转的目标对象class不能为null");
        return JSON.parseObject(json, clz);
    }

    /**
     * 应答成功消息
     *
     * @param request shine请求消息对象
     * @exception Exception 异常
     */
    public void responseSuccess(CbbDispatcherRequest request) throws Exception {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        responseContent(request, CommonMessageCode.SUCCESS, null);
    }

    /**
     * 应答成功消息
     *
     * @param request shine请求消息对象
     * @param content 消息体
     * @exception Exception Exception
     */
    public void responseSuccessContent(CbbDispatcherRequest request, @Nullable Object content) throws Exception {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        responseContent(request, CommonMessageCode.SUCCESS, content);
    }

    /**
     * 应答消息
     * 
     * @param request shine请求消息对象
     * @param resultCode 应答code
     * @exception Exception 异常
     */
    public void response(CbbDispatcherRequest request, Integer resultCode) throws Exception {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        Assert.notNull(resultCode, "resultCode can not null");
        responseContent(request, resultCode, null);
    }

    /**
     * 应答消息
     * 
     * @param request request
     * @param resultCode resultCode
     * @param message message
     * @throws Exception Exception
     */
    public void responseMessage(CbbDispatcherRequest request, Integer resultCode, String message) throws Exception {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        Assert.notNull(resultCode, "resultCode can not null");
        Assert.notNull(message, "message can not null");
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("message", message);
        responseContent(request, resultCode, msgMap);
    }

    /**
     * 应答消息
     *
     * @param request shine请求消息对象
     * @param resultCode 应答code
     * @param content 应答消息内容
     * @exception Exception 异常
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void responseContent(CbbDispatcherRequest request, Integer resultCode, @Nullable Object content) throws Exception {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        Assert.notNull(resultCode, "resultCode can not null");
        CbbResponseShineMessage shineMessage = new CbbResponseShineMessage();
        shineMessage.setCode(resultCode);
        shineMessage.setRequestId(request.getRequestId());
        shineMessage.setTerminalId(request.getTerminalId());
        shineMessage.setAction(request.getDispatcherKey());
        shineMessage.setContent(content);
        cbbTranspondMessageHandlerAPI.response(shineMessage);
    }

    /**
     * 请求消息
     * 
     * @param terminalId terminalId
     * @param action action
     * @throws Exception Exception
     */
    public void requestMessage(String terminalId, String action) throws Exception {
        Assert.hasText(terminalId, "terminalId can not empty");
        Assert.notNull(action, "action cannot empty");
        requestContent(terminalId, action, null);
    }

    /**
     * 请求消息
     * 
     * @param terminalId terminalId
     * @param content content
     * @param action action
     * @throws Exception Exception
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void requestContent(String terminalId, String action, @Nullable Object content) throws Exception {
        Assert.hasText(terminalId, "terminalId can not empty");
        Assert.notNull(action, "action cannot empty");
        CbbShineMessageRequest messageRequest = CbbShineMessageRequest.create(action, terminalId);
        messageRequest.setContent(content);
        cbbTranspondMessageHandlerAPI.request(messageRequest);
    }
}
