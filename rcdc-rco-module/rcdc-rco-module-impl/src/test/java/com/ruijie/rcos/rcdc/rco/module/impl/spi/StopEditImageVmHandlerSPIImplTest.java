package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/25 14:55
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class StopEditImageVmHandlerSPIImplTest {

    @Tested
    private StopEditImageVmHandlerSPIImpl stopEditImageVmHandlerSPI;

    @Injectable
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private VDIEditImageHelper helper;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    /**
     * 关闭镜像
     * @throws InterruptedException 异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessage() throws InterruptedException, BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = stopEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new MockUp<ThreadExecutors>() {
            @Mock
            void execute(String threadName, Runnable task) {
                task.run();
            }
        };

        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setEditingOnVnc(false);
        cbbImageTemplateEditingInfoDTO.setEditingOnSpiceAmount(1);

        new Expectations() {
            {
                helper.getImageEditingInfoIfPresent((UUID) any, (UUID) any, anyString);
                result = cbbImageTemplateEditingInfoDTO;
            }
        };

        CbbResponseShineMessage responseMessage = stopEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(0, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.closeVm((UUID) any, Boolean.TRUE);
                times = 1;
            }
        };
    }

    /**
     * 关闭镜像，正在web编辑
     * @throws InterruptedException 异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageEditingOnWeb() throws InterruptedException, BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = stopEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new MockUp<ThreadExecutors>() {
            @Mock
            void execute(String threadName, Runnable task) {
                task.run();
            }
        };

        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setEditingOnVnc(true);
        cbbImageTemplateEditingInfoDTO.setEditingOnSpiceAmount(1);

        new Expectations() {
            {
                helper.getImageEditingInfoIfPresent((UUID) any, (UUID) any, anyString);
                result = cbbImageTemplateEditingInfoDTO;
            }
        };

        CbbResponseShineMessage responseMessage = stopEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(0, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.closeVm((UUID) any, Boolean.TRUE);
                times = 1;
            }
        };
    }

    /**
     * 关闭镜像，正在其它终端编辑
     * @throws InterruptedException 异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageEditingOnOtherTerminal() throws InterruptedException, BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = stopEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new MockUp<ThreadExecutors>() {
            @Mock
            void execute(String threadName, Runnable task) {
                task.run();
            }
        };

        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setEditingOnVnc(false);
        cbbImageTemplateEditingInfoDTO.setEditingOnSpiceAmount(2);

        new Expectations() {
            {
                helper.getImageEditingInfoIfPresent((UUID) any, (UUID) any, anyString);
                result = cbbImageTemplateEditingInfoDTO;
            }
        };

        CbbResponseShineMessage responseMessage = stopEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(0, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.closeVm((UUID) any, Boolean.TRUE);
                times = 1;
            }
        };
    }

    /**
     * 关闭镜像，异常
     * @throws InterruptedException 异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageException() throws InterruptedException, BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = stopEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new MockUp<ThreadExecutors>() {
            @Mock
            void execute(String threadName, Runnable task) {
                task.run();
            }
        };

        CbbImageTemplateEditingInfoDTO cbbImageTemplateEditingInfoDTO = new CbbImageTemplateEditingInfoDTO();
        cbbImageTemplateEditingInfoDTO.setEditingOnVnc(false);
        cbbImageTemplateEditingInfoDTO.setEditingOnSpiceAmount(1);

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.closeVm((UUID) any, Boolean.TRUE);
                result = new BusinessException("key");
                helper.getImageEditingInfoIfPresent((UUID) any, (UUID) any, anyString);
                result = cbbImageTemplateEditingInfoDTO;
            }
        };

        CbbResponseShineMessage responseMessage = stopEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(0, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.closeVm((UUID) any, Boolean.TRUE);
                times = 1;
            }
        };
    }

    /**
     * 关闭镜像，管理员不可操作
     * @throws InterruptedException 异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageNotAllow() throws InterruptedException, BusinessException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        request.setTerminalId("terminalId");
        VDIEditImageIdRequestDTO requestDTO = stopEditImageVmHandlerSPI.resolveJsonString(dataJsonString);

        new MockUp<ThreadExecutors>() {
            @Mock
            void execute(String threadName, Runnable task) {
                task.run();
            }
        };

        new Expectations() {
            {
                helper.getImageEditingInfoIfPresent((UUID) any, (UUID) any, anyString);
                result = new BusinessException("key");
            }
        };

        CbbResponseShineMessage responseMessage = stopEditImageVmHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(-1, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.closeVm((UUID) any, Boolean.TRUE);
                times = 0;
            }
        };
    }
}