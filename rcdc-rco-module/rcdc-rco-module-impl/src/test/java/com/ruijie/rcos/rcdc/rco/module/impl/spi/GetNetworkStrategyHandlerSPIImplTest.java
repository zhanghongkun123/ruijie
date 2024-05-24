package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
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
 * Create Time: 2020/2/24 14:18
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class GetNetworkStrategyHandlerSPIImplTest {

    @Tested
    private GetNetworkStrategyHandlerSPIImpl getNetworkStrategyHandlerSPI;

    @Injectable
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Injectable
    private VDIEditImageHelper helper;

    /**
     * 获取网络策略
     */
    @Test
    public void testGetResponseMessage() throws BusinessException {
        String dataJsonString = "{\"page\":1,\"limit\":10}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);

        CbbDeskNetworkDetailDTO networkDTO = new CbbDeskNetworkDetailDTO();
        UUID networkId = UUID.randomUUID();
        networkDTO.setId(networkId);
        networkDTO.setDeskNetworkName("networkName");
        DefaultPageResponse<CbbDeskNetworkDetailDTO> apiResponse = new DefaultPageResponse<>();
        apiResponse.setItemArr(new CbbDeskNetworkDetailDTO[]{networkDTO});

        DefaultPageResponse<CbbDeskNetworkDetailDTO> emptyApiResponse = new DefaultPageResponse<>();
        emptyApiResponse.setItemArr(new CbbDeskNetworkDetailDTO[0]);

        VDIEditImageIdRequestDTO requestDTO = getNetworkStrategyHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                cbbNetworkMgmtAPI.pageQuery((PageSearchRequest) any);
                returns(apiResponse, emptyApiResponse);
            }
        };

        CbbResponseShineMessage responseMessage = getNetworkStrategyHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(0, responseMessage.getCode().intValue());
    }

    /**
     * 获取网络策略，异常
     */
    @Test
    public void testGetResponseMessageException() throws BusinessException {
        String dataJsonString = "{\"page\":1,\"limit\":10}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);

        CbbDeskNetworkDTO networkDTO = new CbbDeskNetworkDTO();
        UUID networkId = UUID.randomUUID();
        networkDTO.setId(networkId);
        networkDTO.setDeskNetworkName("networkName");
        DefaultPageResponse<CbbDeskNetworkDTO> apiResponse = new DefaultPageResponse<>();
        apiResponse.setItemArr(new CbbDeskNetworkDTO[]{networkDTO});

        VDIEditImageIdRequestDTO requestDTO = getNetworkStrategyHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                cbbNetworkMgmtAPI.pageQuery((PageSearchRequest) any);
                result = new Exception("xxx");
            }
        };

        CbbResponseShineMessage responseMessage = getNetworkStrategyHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());
        Assert.assertEquals(-1, responseMessage.getCode().intValue());
    }
}