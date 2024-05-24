package com.ruijie.rcos.rcdc.rco.module.impl.message;


import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/15 09:15
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class MessageUtilsTest {

    /**
     * 转换，指定类型
     */
    @Test
    public void testParse() {
        String data = "{\"code\":0,\"content\":{\"message\":\"message\"}}";
        CbbShineMessageResponse<TestEntity> parsedResp = MessageUtils.parse(data, TestEntity.class);
        Assert.assertEquals("message", parsedResp.getContent().getMessage());
    }

    /**
     * 转换，未指定类型
     */
    @Test
    public void testParseClzNull() {
        String data = "{\"code\":0,\"content\":{\"message\":\"message\"}}";
        CbbShineMessageResponse parsedResp = MessageUtils.parse(data, null);
        Assert.assertEquals("{\"message\":\"message\"}", parsedResp.getContent().toString());
    }

    /**
     * 转换，数据报文错误
     */
    @Test
    public void testParseDataError() {
        String data = "xxx";
        try {
            MessageUtils.parse(data, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("报文数据格式转换错误"));
        }
    }

    /**
     * 转换，数据报文错误
     */
    @Test
    public void testParseDataError2() {
        String data = "{\"code\":0,\"content\":{\"xxx\":\"xxx\"}}";
        try {
            MessageUtils.parse(data, TestEntity2.class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("报文数据格式转换错误"));
        }
    }

    private static class TestEntity {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private class TestEntity2 {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}