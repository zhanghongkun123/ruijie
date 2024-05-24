package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CloudDesktopDetailResponse;
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

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月4日
 *
 * @author XiaoJiaXin
 */
@RunWith(SkyEngineRunner.class)
public class DesktopWatermarkSPIImplTest {

    @Tested
    private DesktopWatermarkSPIImpl desktopWatermarkSPI;

    @Injectable
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Injectable
    private WatermarkMessageSender watermarkMessageSender;

    /**
     * 测试receive方法-request为空
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testReceiveRequestIsNull() throws BusinessException {
        try {
            desktopWatermarkSPI.receive(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertEquals("request can not be null", e.getMessage());
        }
    }

    /**
     * 测试receive方法 - 桌面状态为运行中
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testReceiveDesktopStateIsRunning() throws BusinessException {
        final CbbGuestToolSPIReceiveRequest request = new CbbGuestToolSPIReceiveRequest();
        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(123);
        dto.setDeskId(UUID.fromString("199eb839-10fc-4c06-bcd1-c9d750e72c5e"));
        request.setDto(dto);
        final CloudDesktopDetailResponse desktopDetailResponse = new CloudDesktopDetailResponse();
        final CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopMac("A6:B7:CB:6B:16:EC");
        cloudDesktopDetailDTO.setId(UUID.fromString("199eb839-10fc-4c06-bcd1-c9d750e72c5e"));
        cloudDesktopDetailDTO.setUserName("userName");
        cloudDesktopDetailDTO.setDesktopName("deskName");

        desktopDetailResponse.setCloudDesktopDetailDTO(cloudDesktopDetailDTO);
        desktopDetailResponse.getCloudDesktopDetailDTO().setDesktopState("RUNNING");

        new Expectations() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
            }
        };
        desktopWatermarkSPI.receive(request);
        new Verifications() {
            {
                watermarkMessageSender.send((CloudDesktopDetailDTO) any);
                times = 1;
            }
        };
    }

    /**
     * 测试receive方法 - 桌面不存在
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testReceiveDesktopNotExist() throws BusinessException {
        final CbbGuestToolSPIReceiveRequest request = new CbbGuestToolSPIReceiveRequest();
        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(123);
        dto.setDeskId(UUID.fromString("199eb839-10fc-4c06-bcd1-c9d750e72c5e"));
        request.setDto(dto);

        new Expectations() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = null;
            }
        };

        try {
            desktopWatermarkSPI.receive(request);
            fail();
        } catch (BusinessException e) {
            assertEquals(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_NOT_EXIST, e.getKey());
        }

        new Verifications() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 测试receive方法 - 桌面状态为启动中
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testReceiveDesktopStateIsStartUp() throws BusinessException {
        final CbbGuestToolSPIReceiveRequest request = new CbbGuestToolSPIReceiveRequest();
        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(123);
        dto.setDeskId(UUID.fromString("199eb839-10fc-4c06-bcd1-c9d750e72c5e"));
        request.setDto(dto);
        final CloudDesktopDetailResponse desktopDetailResponse = new CloudDesktopDetailResponse();
        final CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopMac("A6:B7:CB:6B:16:EC");
        cloudDesktopDetailDTO.setId(UUID.fromString("199eb839-10fc-4c06-bcd1-c9d750e72c5e"));
        cloudDesktopDetailDTO.setUserName("userName");
        cloudDesktopDetailDTO.setDesktopName("deskName");

        desktopDetailResponse.setCloudDesktopDetailDTO(cloudDesktopDetailDTO);
        desktopDetailResponse.getCloudDesktopDetailDTO().setDesktopState("START_UP");

        new Expectations() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
            }
        };
        desktopWatermarkSPI.receive(request);
        new Verifications() {
            {
                watermarkMessageSender.send((CloudDesktopDetailDTO) any);
                times = 1;
            }
        };
    }

    /**
     * 测试receive方法-校验云桌面状态不为RUNNING
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testReceiveDeskStateIsNotRunningOrStartUp() throws BusinessException {
        final CbbGuestToolSPIReceiveRequest request = new CbbGuestToolSPIReceiveRequest();
        CbbGuesttoolMessageDTO  dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(123);
        dto.setDeskId(UUID.fromString("199eb839-10fc-4c06-bcd1-c9d750e72c5e"));
        request.setDto(dto);
        final CloudDesktopDetailResponse desktopDetailResponse = new CloudDesktopDetailResponse();

        final CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopMac("A6:B7:CB:6B:16:EC");
        cloudDesktopDetailDTO.setId(UUID.fromString("199eb839-10fc-4c06-bcd1-c9d750e72c5e"));
        cloudDesktopDetailDTO.setUserName("userName");
        cloudDesktopDetailDTO.setDesktopName("deskName");

        desktopDetailResponse.setCloudDesktopDetailDTO(cloudDesktopDetailDTO);
        desktopDetailResponse.getCloudDesktopDetailDTO().setDesktopState("NotRUNNING");
        new Expectations() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
            }
        };
        try {
            desktopWatermarkSPI.receive(request);
        } catch (BusinessException e) {
            assertEquals(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_STATE_IS_NOT_RUNNING, e.getMessage());
        }
    }
}
