package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWatermarkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月4日
 *
 * @author XiaoJiaXin
 */
@RunWith(SkyEngineRunner.class)
public class WatermarkMessageSenderTest {

    @Tested
    private WatermarkMessageSender watermarkMessageSender;

    @Injectable
    private CbbWatermarkMgmtAPI cbbWatermarkMgmtAPI;

    @Injectable
    private WatermarkMessageParser<com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO> watermarkMessageParser;

    /**
     * 测试send方法-request为空
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testSendIsRequestIsNull() throws BusinessException {
        try {
            watermarkMessageSender.send(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("cloudDesktopDetailDTO cannot be null", e.getMessage());
        }
    }

    /**
     * 测试send方法-response为空
     */
    @Test
    public void testSendResponseIsNull() {
        final CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        new Expectations() {
            {
                cbbWatermarkMgmtAPI.getWatermarkConfig();
                result = null;
            }
        };
        try {
            watermarkMessageSender.send(cloudDesktopDetailDTO);
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_WATERMARK_GET_RUNNING_DESKTOP_NOT_EXIST, e.getMessage());
        }
    }

    /**
     * 测试send方法
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testSend() throws BusinessException {
        final CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setId(UUID.randomUUID());
        CbbWatermarkConfigDTO dto = new CbbWatermarkConfigDTO();
        JSONObject obj = new JSONObject();
        obj.put("customContent", "123");
        dto.setDisplayContent(obj.toJSONString());
        CbbWatermarkDisplayConfigDTO configDTO = new CbbWatermarkDisplayConfigDTO();
        dto.setDisplayConfig(configDTO);

        new Expectations() {
            {
                cbbWatermarkMgmtAPI.getWatermarkConfig();
                result = dto;
            }
        };
        watermarkMessageSender.send(cloudDesktopDetailDTO);
        new Verifications() {
            {
                cbbWatermarkMgmtAPI.getWatermarkConfig();
                times = 1;
            }
        };
    }
}
