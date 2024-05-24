package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.ConfigEditImageVmRequestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
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
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/24 14:04
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class ConfigEditImageVmHandlerSPIImplTest {

    @Tested
    private ConfigEditImageVmHandlerSPIImpl configEditImageVmHandlerSPI;

    @Injectable
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private VDIEditImageHelper helper;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    /**
     * 配置镜像编辑虚机
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessage() throws BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"cpu\":4," +
                "\"systemDisk\":60," +
                "\"memory\":8," +
                "\"networkId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        ConfigEditImageVmRequestDTO requestDTO = configEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        CbbResponseShineMessage responseMessage = configEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(0, responseMessage.getCode().intValue());

        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.prepareEditImageTemplate((UUID) any);
                times = 1;

                CbbConfigVmForEditImageTemplateDTO templateRequest;
                cbbImageTemplateMgmtAPI.configVmForEditImageTemplate(templateRequest = withCapture());
                Assert.assertEquals(requestDTO.getId(), templateRequest.getImageTemplateId());
            }
        };
    }

    /**
     * 配置镜像编辑虚机，异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageException() throws BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"cpu\":4," +
                "\"systemDisk\":60," +
                "\"memory\":8," +
                "\"networkId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        ConfigEditImageVmRequestDTO requestDTO = configEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.prepareEditImageTemplate((UUID) any);
                result = new Exception("xxx");
            }
        };

        CbbResponseShineMessage responseMessage = configEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(0, responseMessage.getCode().intValue());
    }

    /**
     * 配置镜像编辑虚机，业务异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageBusinessException() throws BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"cpu\":4," +
                "\"systemDisk\":60," +
                "\"memory\":8," +
                "\"networkId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        ConfigEditImageVmRequestDTO requestDTO = configEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations(LocaleI18nResolver.class) {
            {
                cbbImageTemplateMgmtAPI.prepareEditImageTemplate((UUID) any);
                result = new BusinessException("key");
                LocaleI18nResolver.resolve(anyString, (String[]) any);
                result = "message";
            }
        };

        CbbResponseShineMessage responseMessage = configEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(0, responseMessage.getCode().intValue());
    }

    /**
     * 配置镜像编辑虚机，管理员不可操作
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageNotAllow() throws BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"cpu\":4," +
                "\"systemDisk\":60," +
                "\"memory\":8," +
                "\"networkId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        ConfigEditImageVmRequestDTO requestDTO = configEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                helper.getImageEditingInfoIfPresent((UUID) any, (UUID) any, anyString);
                result = new BusinessException("key");
            }
        };

        CbbResponseShineMessage responseMessage = configEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(-1, responseMessage.getCode().intValue());
    }
}