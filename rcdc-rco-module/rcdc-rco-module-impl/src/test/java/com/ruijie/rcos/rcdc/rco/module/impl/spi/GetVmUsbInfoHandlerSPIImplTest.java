package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBAdvancedSettingMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBDeviceMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.usbdevice.CbbUSBDevicePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBDeviceDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/19 21:35
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class GetVmUsbInfoHandlerSPIImplTest {

    @Tested
    private GetVmUsbInfoHandlerSPIImpl getVmUsbInfoHandlerSPI;

    @Injectable
    private CbbVDIDeskMgmtAPI vdiDeskMgmtAPI;

    @Injectable
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Injectable
    private CbbUSBDeviceMgmtAPI cbbUSBDeviceMgmtAPI;

    @Injectable
    private CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI;

    @Injectable
    private VDIEditImageHelper helper;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    /**
     * 获取USB配置
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessage() throws BusinessException {
        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setImageTemplateId(UUID.randomUUID());

        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = getVmUsbInfoHandlerSPI.resolveJsonString(dataJsonString);

        CbbImageTemplateEditingInfoDTO editingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        UUID imageId = UUID.randomUUID();
        editingInfoDTO.setImageTemplateId(imageId);

        UUID strategyId = UUID.randomUUID();


        CbbUSBTypeDTO usbTypeDTO = new CbbUSBTypeDTO();
        usbTypeDTO.setId(UUID.randomUUID());

        CbbUSBDeviceDTO usbDeviceDTO = new CbbUSBDeviceDTO();
        usbDeviceDTO.setDeviceClass("deviceClass");
        usbDeviceDTO.setFirmFlag("firmFlag");
        usbDeviceDTO.setProductFlag("productFlag");
        usbDeviceDTO.setReleaseVersion("releaseVersion");
        DefaultPageResponse<CbbUSBDeviceDTO> usbDeviceResponse = new DefaultPageResponse<>();
        usbDeviceResponse.setItemArr(new CbbUSBDeviceDTO[]{usbDeviceDTO});

        new Expectations() {
            {
                helper.getImageEditingInfoMustExist((UUID) any, (UUID) any, anyString);
                result = editingInfoDTO;
                cbbUSBAdvancedSettingMgmtAPI.getUsbConfString((UUID) any);
                result = "usbConf";
                cbbUSBTypeMgmtAPI.getAllUSBType((CbbGetAllUSBTypeDTO) any);
                result = new CbbUSBTypeDTO[]{usbTypeDTO};
                cbbUSBDeviceMgmtAPI.pageQueryUSBDevice((CbbUSBDevicePageRequest) any);
                result = usbDeviceResponse;
            }
        };

        CbbResponseShineMessage responseMessage = getVmUsbInfoHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());

        Assert.assertEquals(0, responseMessage.getCode().intValue());
    }

    /**
     * 获取USB配置，管理员不可操作
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageAdminNotAllow() throws BusinessException {
        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setImageTemplateId(UUID.randomUUID());

        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = getVmUsbInfoHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                helper.getImageEditingInfoMustExist((UUID) any, (UUID) any, anyString);
                result = null;
            }
        };

        CbbResponseShineMessage responseMessage = getVmUsbInfoHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(-1, responseMessage.getCode().intValue());
    }

    /**
     * 获取USB配置，异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageException() throws BusinessException {
        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setImageTemplateId(UUID.randomUUID());

        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = getVmUsbInfoHandlerSPI.resolveJsonString(dataJsonString);

        CbbImageTemplateEditingInfoDTO editingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        UUID imageId = UUID.randomUUID();
        editingInfoDTO.setImageTemplateId(imageId);

        new Expectations() {
            {
                helper.getImageEditingInfoMustExist((UUID) any, (UUID) any, anyString);
                result = editingInfoDTO;
                cbbUSBAdvancedSettingMgmtAPI.getUsbConfString((UUID) any);
                result = new Exception("xxx");
            }
        };

        CbbResponseShineMessage responseMessage = getVmUsbInfoHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(-1, responseMessage.getCode().intValue());
    }
}