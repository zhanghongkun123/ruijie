package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbPublishImageTemplateDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.PublishImageRequestDTO;
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
 * Create Time: 2020/2/25 12:04
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class PublishImageHandlerSPIImplTest {

    @Tested
    private PublishImageHandlerSPIImpl publishImageHandlerSPI;

    @Injectable
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private VDIEditImageHelper helper;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    /**
     * 发布镜像
     * @throws BusinessException 异常
     * @throws InterruptedException 异常
     */
    @Test
    public void testGetResponseMessage() throws BusinessException, InterruptedException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        PublishImageRequestDTO requestDTO = publishImageHandlerSPI.resolveJsonString(dataJsonString);

        new MockUp<ThreadExecutors>() {
            @Mock
            void execute(String threadName, Runnable task) {
                task.run();
            }
        };

        CbbResponseShineMessage responseMessage = publishImageHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(0, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.publishImageTemplate((CbbPublishImageTemplateDTO) any);
                times = 1;
            }
        };
    }

    /**
     * 发布镜像，异常
     * @throws BusinessException 异常
     * @throws InterruptedException 异常
     */
    @Test
    public void testGetResponseMessageException() throws BusinessException, InterruptedException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        PublishImageRequestDTO requestDTO = publishImageHandlerSPI.resolveJsonString(dataJsonString);

        new MockUp<ThreadExecutors>() {
            @Mock
            void execute(String threadName, Runnable task) {
                task.run();
            }
        };

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.publishImageTemplate((CbbPublishImageTemplateDTO) any);
                result = new BusinessException("key");
            }
        };

        CbbResponseShineMessage responseMessage = publishImageHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(0, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.publishImageTemplate((CbbPublishImageTemplateDTO) any);
                times = 1;
            }
        };
    }

    /**
     * 发布镜像，管理员不可操作
     * @throws BusinessException 异常
     * @throws InterruptedException 异常
     */
    @Test
    public void testGetResponseMessageNotAllow() throws BusinessException, InterruptedException {
        String dataJsonString = "{" +
                "\"id\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"," +
                "\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);
        PublishImageRequestDTO requestDTO = publishImageHandlerSPI.resolveJsonString(dataJsonString);

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

        CbbResponseShineMessage responseMessage = publishImageHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Thread.sleep(500);

        Assert.assertEquals(-1, responseMessage.getCode().intValue());
        new Verifications() {
            {
                cbbImageTemplateMgmtAPI.publishImageTemplate((CbbPublishImageTemplateDTO) any);
                times = 0;
            }
        };
    }
}