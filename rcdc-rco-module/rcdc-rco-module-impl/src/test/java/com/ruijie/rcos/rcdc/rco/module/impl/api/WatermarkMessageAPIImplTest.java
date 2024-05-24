package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailListDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.WatermarkMessageSender;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

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
public class WatermarkMessageAPIImplTest {

    @Tested
    private WatermarkMessageAPIImpl watermarkMessageAPIImpl;

    @Injectable
    private WatermarkMessageSender watermarkMessageSender;

    @Injectable
    private UserDesktopMgmtAPI userDesktopMgmtAPI;


    /**
     * 测试send方法-不存在运行中的云桌面
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testSendWithNoRunningDesk() throws BusinessException {

        new Expectations() {
            {
                userDesktopMgmtAPI.getAllRunningDesktopDetail();
                result = null;
            }
        };
        try {
            watermarkMessageAPIImpl.send();
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_WATERMARK_GET_RUNNING_DESKTOP_NOT_EXIST, e.getMessage());
        }
    }

    /**
     * 测试send方法-存在运行中的云桌面
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testSenWhileHasRunningDesktop() throws BusinessException {
        final CloudDesktopDetailListDTO desktopDetailListDTO = new CloudDesktopDetailListDTO();
        final List<CloudDesktopDetailDTO> deskList = Lists.newArrayList();
        final CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setId(UUID.randomUUID());
        cloudDesktopDetailDTO.setUserName("userName");
        cloudDesktopDetailDTO.setDesktopIp("192.168.123.1");
        cloudDesktopDetailDTO.setDesktopMac("deskMac");
        cloudDesktopDetailDTO.setDesktopName("deskName");
        deskList.add(cloudDesktopDetailDTO);
        desktopDetailListDTO.setCloudDesktopDetailList(deskList);


        new Expectations() {
            {
                userDesktopMgmtAPI.getAllRunningDesktopDetail();
                result = desktopDetailListDTO;
                watermarkMessageSender.send((CloudDesktopDetailDTO) any);
            }
        };

        watermarkMessageAPIImpl.send();


        new Verifications() {
            {

                watermarkMessageSender.send((CloudDesktopDetailDTO) any);
                times = 1;
            }
        };
    }
}
