package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageSessionRequestDTO;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/24 13:46
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class AbstractVDIEditImageHandlerSPIImplTest {

    @Mocked
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Mocked
    private AdminLoginOnTerminalCacheManager cacheManager;

    /**
     * 消息处理，正常流程
     */
    @Test
    public void testDispatch() {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData("{\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}");

        IacAdminDTO adminDTO = new IacAdminDTO();
        adminDTO.setId(UUID.randomUUID());
        AdminLoginOnTerminalCache cache = new AdminLoginOnTerminalCache(adminDTO, "terminalId");

        new Expectations() {
            {
                cacheManager.getIfPresent((UUID) any);
                result = cache;
            }
        };

        AbstractVDIEditImageHandlerSPIImpl handlerSPI = new MockVDIEditImageHandlerSPIImpl();
        Deencapsulation.setField(handlerSPI, "messageHandlerAPI", messageHandlerAPI);
        Deencapsulation.setField(handlerSPI, "cacheManager", cacheManager);
        handlerSPI.dispatch(request);

        new Verifications() {
            {
                CbbResponseShineMessage message;
                messageHandlerAPI.response(message = withCapture());
                Assert.assertEquals(0, message.getCode().intValue());
            }
        };
    }

    /**
     * 消息处理，会话不存在
     */
    @Test
    public void testDispatchSessionNotExist() {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData("{\"adminSessionId\":\"7c1d36d1-3fa1-4cbd-9213-09a069109baf\"}");

        new Expectations() {
            {
                cacheManager.getIfPresent((UUID) any);
                result = null;
            }
        };

        AbstractVDIEditImageHandlerSPIImpl handlerSPI = new MockVDIEditImageHandlerSPIImpl();
        Deencapsulation.setField(handlerSPI, "messageHandlerAPI", messageHandlerAPI);
        Deencapsulation.setField(handlerSPI, "cacheManager", cacheManager);
        handlerSPI.dispatch(request);

        new Verifications() {
            {
                CbbResponseShineMessage message;
                messageHandlerAPI.response(message = withCapture());
                Assert.assertEquals(-97, message.getCode().intValue());
            }
        };
    }

    private class MockVDIEditImageHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageSessionRequestDTO> {
        @Override
        CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, VDIEditImageSessionRequestDTO requestDTO, UUID adminId) {
            return buildResponseMessage(request, requestDTO);
        }

        @Override
        VDIEditImageSessionRequestDTO resolveJsonString(String dataJsonString) {
            return JSONObject.parseObject(dataJsonString, VDIEditImageSessionRequestDTO.class);
        }
    }
}