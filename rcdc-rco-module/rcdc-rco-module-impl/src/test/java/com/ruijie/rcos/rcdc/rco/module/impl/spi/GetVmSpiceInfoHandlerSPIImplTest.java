package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVmGraphicsDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.GetVmSpiceInfoResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
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
 * Create Time: 2020/2/25 10:50
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class GetVmSpiceInfoHandlerSPIImplTest {

    @Tested
    private GetVmSpiceInfoHandlerSPIImpl getVmSpiceInfoHandlerSPI;

    @Injectable
    private CbbVDIDeskMgmtAPI vdiDeskMgmtAPI;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private VDIEditImageHelper helper;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Injectable
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    /**
     * 获取虚机spice信息，获取虚机ID异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageVmIdException() throws BusinessException {
        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setImageTemplateId(UUID.randomUUID());

        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = getVmSpiceInfoHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                helper.getTempVmId((UUID) any);
                result = new Exception("fail message");
                helper.getImageEditingInfoMustExist((UUID) any, (UUID) any, anyString);
                result = cbbImageTemplateEditingInfoDTO;
            }
        };

        CbbResponseShineMessage responseMessage = getVmSpiceInfoHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(0, responseMessage.getCode().intValue());
    }

    /**
     * 获取虚机spice信息，管理员不可操作
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageNotAllow() throws BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = getVmSpiceInfoHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                helper.getImageEditingInfoMustExist((UUID) any, (UUID) any, anyString);
                result = null;
            }
        };

        CbbResponseShineMessage responseMessage = getVmSpiceInfoHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(-1, responseMessage.getCode().intValue());
    }
}