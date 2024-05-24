package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.GetSetTester;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/14 17:05
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class ConfigImageVmResponseDTOTest {

    /**
     * 测试get/set
     */
    @Test
    public void testGetSet() {
        GetSetTester tester = new GetSetTester(ConfigImageVmResponseDTO.class);
        try {
            tester.runTest();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * 构建成功消息
     */
    @Test
    public void testBuildSuccessDTO() {
        ConfigImageVmResponseDTO responseDTO = ConfigImageVmResponseDTO.buildSuccessDTO();
        Assert.assertEquals(0, responseDTO.getCode().intValue());
    }

    /**
     * 构建失败消息
     */
    @Test
    public void testBuildFailDTO() {
        ConfigImageVmResponseDTO responseDTO = ConfigImageVmResponseDTO.buildFailDTO("message");
        Assert.assertEquals(-1, responseDTO.getCode().intValue());
        Assert.assertEquals("message", responseDTO.getMessage());
    }
}