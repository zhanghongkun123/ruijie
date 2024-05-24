package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 18:46
 *
 * @author conghaifeng
 */
public class ShineMessageHandlerTest {

    @Tested
    private ShineMessageHandler shineMessageHandler;

    @Injectable
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Mocked
    private JSON json;
    
    /**
     *测试json转对象
     *
     * @throws Exception 异常
     */
    @Test
    public void testParseObject() throws Exception {
        new Expectations() {
            {
                json.parseObject(anyString, Object.class);
                result = "test";
            }
        };
        Object test = shineMessageHandler.parseObject("test", Object.class);
        Assert.assertEquals("test", test);
    }

    /**
     *测试应答成功消息
     *
     * @throws Exception 异常
     */
    @Test
    public void testResponseSuccess() throws Exception {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        shineMessageHandler.responseSuccess(request);
        new Verifications() {
            {
                cbbTranspondMessageHandlerAPI.response((CbbResponseShineMessage) any);
                times = 1;
            }
        };
    }

    /**
     *测试应答成功消息
     *
     * @throws Exception 异常
     */
    @Test
    public void testResponseSuccessContent() throws Exception {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        shineMessageHandler.responseSuccessContent(request, null);
        new Verifications() {
            {
                cbbTranspondMessageHandlerAPI.response((CbbResponseShineMessage) any);
                times = 1;
            }
        };
    }

    /**
     *测试应答消息
     *
     * @throws Exception 异常
     */
    @Test
    public void testResponse() throws Exception {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        shineMessageHandler.response(request, 1);
        new Verifications() {
            {
                cbbTranspondMessageHandlerAPI.response((CbbResponseShineMessage) any);
                times = 1;
            }
        };
    }

    /**
     *测试应答消息
     *
     * @throws Exception 异常
     */
    @Test
    public void testResponseMessage() throws Exception {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        shineMessageHandler.responseMessage(request, 1, "message");
        new Verifications() {
            {
                cbbTranspondMessageHandlerAPI.response((CbbResponseShineMessage) any);
                times = 1;
            }
        };
    }

    /**
     *测试请求消息
     *
     * @throws Exception 异常
     */
    @Test
    public void testRequestMessage() throws Exception {
        shineMessageHandler.requestMessage("terminalId", "action");
        new Verifications() {
            {
                cbbTranspondMessageHandlerAPI.request((CbbShineMessageRequest) any);
                times = 1;
            }
        };
    }



}