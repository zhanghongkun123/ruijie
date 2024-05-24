package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import static junit.framework.TestCase.assertTrue;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import mockit.Injectable;
import mockit.Tested;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月4日
 *
 * @author XiaoJiaXin
 */
@RunWith(SkyEngineRunner.class)
public class WatermarkDisplayFieldTest {

    @Tested
    private WatermarkDisplayField watermarkDisplayField;

    @Injectable
    private WatermarkDisplayContentDTO displayContentDTO;

    @Injectable
    private WatermarkFieldMappingValueDTO mappingValueDTO;

    /**
     * 测试USER_NAME mapping方法-参数为空
     * 
     * @throws Exception 异常
     */
    @Test
    public void testUserNameRequestIsNull() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("USER_NAME").mapping(null, displayContentDTO),
                "mappingValueDTO cannot be null");
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("USER_NAME").mapping(mappingValueDTO, null),
                "displayContentDTO cannot be null");
        assertTrue(true);
    }

    /**
     * 测试USER_NAME mapping方法
     */
    @Test
    public void testUserName() {
        final WatermarkFieldMappingValueDTO mappingValueDTO = getMappingValueDTO();
        final WatermarkDisplayContentDTO displayContentDTO = getDisplayContentDTO();
        watermarkDisplayField.valueOf("USER_NAME").mapping(mappingValueDTO, displayContentDTO);
        Assert.assertEquals(displayContentDTO.getUserName(), mappingValueDTO.getUserName());
    }

    /**
     * 测试DESK_NAME mapping方法-参数为空
     * 
     * @throws Exception 异常
     */
    @Test
    public void testDeskNameRequestIsNull() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("DESK_NAME").mapping(null, displayContentDTO),
                "mappingValueDTO cannot be null");
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("DESK_NAME").mapping(mappingValueDTO, null),
                "displayContentDTO cannot be null");
        assertTrue(true);
    }

    /**
     * 测试DESK_NAME mapping方法
     */
    @Test
    public void testDeskName() {
        final WatermarkFieldMappingValueDTO mappingValueDTO = getMappingValueDTO();
        final WatermarkDisplayContentDTO displayContentDTO = getDisplayContentDTO();
        watermarkDisplayField.valueOf("DESK_NAME").mapping(mappingValueDTO, displayContentDTO);
        Assert.assertEquals(displayContentDTO.getUserName(), mappingValueDTO.getUserName());
    }

    /**
     * 测试DESK_MAC mapping方法-参数为空
     * 
     * @throws Exception 异常
     */
    @Test
    public void testDeskMacRequestIsNull() throws Exception {

        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("DESK_MAC").mapping(null, displayContentDTO),
                "mappingValueDTO cannot be null");
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("DESK_MAC").mapping(mappingValueDTO, null),
                "displayContentDTO cannot be null");
        assertTrue(true);
    }

    /**
     * 测试DESK_MAC mapping方法
     */
    @Test
    public void testDeskMac() {
        final WatermarkFieldMappingValueDTO mappingValueDTO = getMappingValueDTO();
        final WatermarkDisplayContentDTO displayContentDTO = getDisplayContentDTO();
        watermarkDisplayField.valueOf("DESK_MAC").mapping(mappingValueDTO, displayContentDTO);
        Assert.assertEquals(displayContentDTO.getUserName(), mappingValueDTO.getUserName());
    }

    /**
     * 测试DESK_IP mapping方法-参数为空
     * 
     * @throws Exception 异常
     */
    @Test
    public void testDeskIpRequestIsNull() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("DESK_IP").mapping(null, displayContentDTO),
                "mappingValueDTO cannot be null");
        ThrowExceptionTester.throwIllegalArgumentException(() -> watermarkDisplayField.valueOf("DESK_IP").mapping(mappingValueDTO, null),
                "displayContentDTO cannot be null");
        assertTrue(true);
    }

    /**
     * 测试DESK_IP mapping方法
     */
    @Test
    public void testDeskIP() {
        final WatermarkFieldMappingValueDTO mappingValueDTO = getMappingValueDTO();
        final WatermarkDisplayContentDTO displayContentDTO = getDisplayContentDTO();
        watermarkDisplayField.valueOf("DESK_IP").mapping(mappingValueDTO, displayContentDTO);

        Assert.assertEquals(displayContentDTO.getUserName(), mappingValueDTO.getUserName());
    }

    private WatermarkDisplayContentDTO getDisplayContentDTO() {
        final WatermarkDisplayContentDTO displayContentDTO = new WatermarkDisplayContentDTO();
        displayContentDTO.setDeskIp("192.168.123.1");
        displayContentDTO.setDeskMac("BE:32:9F:B9:71:63");
        displayContentDTO.setDeskName("deskName");
        displayContentDTO.setUserName("userName");
        displayContentDTO.setCustomContent("customContent");
        return displayContentDTO;
    }

    private WatermarkFieldMappingValueDTO getMappingValueDTO() {
        final WatermarkFieldMappingValueDTO mappingValueDTO = new WatermarkFieldMappingValueDTO();
        mappingValueDTO.setDeskIp("192.168.123.1");
        mappingValueDTO.setDeskMac("BE:32:9F:B9:71:63");
        mappingValueDTO.setDeskName("deskName");
        mappingValueDTO.setUserName("userName");
        return mappingValueDTO;
    }
}
