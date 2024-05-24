package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayFieldDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.WatermarkMessageParserImpl;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Description: mServiceImplTest
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-14
 *
 * @author hli
 */
@RunWith(SkyEngineRunner.class)
public class WatermarkMessageParserImplTest {

    @Tested
    private WatermarkMessageParserImpl watermarkMessageParser;

    @Capturing
    private Logger logger;

    /**
     * 测试parse方法-request为空
     * 
     * @throws Exception 异常
     */
    @Test
    public void testParseRequestDTOIsNull() throws Exception {
        final WatermarkFieldMappingValueDTO mappingValue = new WatermarkFieldMappingValueDTO();
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkMessageParser.parse(null, "displayContent"), "mappingValue cannot be null");
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkMessageParser.parse(mappingValue, null), "displayContent cannot be empty");
        assertTrue(true);
    }

    /**
     * 测试send方法-自定义显示的水印字段为空，不解析
     */
    @Test
    public void testParseWithFieldNameIsBlank() {
        final WatermarkFieldMappingValueDTO mappingValue = getWatermarkDisplayFieldMappingValueDTO();
        final WatermarkDisplayFieldDTO watermarkDisplayFieldDTO = new WatermarkDisplayFieldDTO();
        // 自定义显示的水印字段为空
        String[] fieldArr = {""};
        watermarkDisplayFieldDTO.setFieldArr(fieldArr);
        watermarkDisplayFieldDTO.setCustomContent("customContent");
        String displayContent = JSON.toJSONString(watermarkDisplayFieldDTO);

        new MockUp<Logger>() {
            @Mock
            void error(String key, String[] msgArgs) {
                if (Objects.equals(key, "自定义显示的水印字段为空，不解析") == false) {
                    throw new RuntimeException();
                }
            }
        };

        watermarkMessageParser.parse(mappingValue, displayContent);

        new Verifications() {
            {
                String operatekey;
                logger.error(operatekey = withCapture());
                assertEquals("自定义显示的水印字段为空，不解析", operatekey);
            }

        };
    }

    /**
     * 测试send方法-找不到定义的水印显示字段
     */
    @Test
    public void testParseWithFieldNameIsNotFind() {
        final WatermarkFieldMappingValueDTO mappingValue = getWatermarkDisplayFieldMappingValueDTO();
        final WatermarkDisplayFieldDTO watermarkDisplayFieldDTO = new WatermarkDisplayFieldDTO();
        // 自定义显示的水印字段为空
        String[] fieldArr = {"NotFind"};
        watermarkDisplayFieldDTO.setFieldArr(fieldArr);
        watermarkDisplayFieldDTO.setCustomContent("customContent");
        String displayContent = JSON.toJSONString(watermarkDisplayFieldDTO);

        watermarkMessageParser.parse(mappingValue, displayContent);

        new Verifications() {
            {
                logger.error("找不到定义的水印显示字段[NotFind]，请检查前后端定义字段是否一致", (IllegalArgumentException) any);
                times = 1;
            }
        };
    }

    /**
     * 测试send方法
     */
    @Test
    public void testParse() {
        final WatermarkFieldMappingValueDTO mappingValue = getWatermarkDisplayFieldMappingValueDTO();
        String displayContent = getString();

        WatermarkDisplayContentDTO parse = watermarkMessageParser.parse(mappingValue, displayContent);
        Assert.assertEquals(mappingValue.getUserName(), parse.getUserName());
        Assert.assertEquals(mappingValue.getDeskName(), parse.getDeskName());
        Assert.assertEquals(mappingValue.getDeskIp(), parse.getDeskIp());
        Assert.assertEquals(mappingValue.getDeskMac(), parse.getDeskMac());
    }

    private String getString() {
        final WatermarkDisplayFieldDTO watermarkDisplayFieldDTO = new WatermarkDisplayFieldDTO();
        String[] fieldArr = {"USER_NAME", "DESK_NAME", "DESK_IP", "DESK_MAC"};
        watermarkDisplayFieldDTO.setFieldArr(fieldArr);
        watermarkDisplayFieldDTO.setCustomContent("customContent");
        return JSON.toJSONString(watermarkDisplayFieldDTO);
    }

    private WatermarkFieldMappingValueDTO getWatermarkDisplayFieldMappingValueDTO() {
        final WatermarkFieldMappingValueDTO mappingValue = new WatermarkFieldMappingValueDTO();
        mappingValue.setDeskIp("192.168.123.1");
        mappingValue.setDeskName("deskName");
        mappingValue.setUserName("userName");
        mappingValue.setDeskMac("BE:1B:6F:D9:36:B5");
        return mappingValue;
    }

}
