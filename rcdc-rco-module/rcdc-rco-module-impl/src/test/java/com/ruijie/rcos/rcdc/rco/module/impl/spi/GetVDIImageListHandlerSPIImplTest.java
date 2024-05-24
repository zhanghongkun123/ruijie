package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageSessionRequestDTO;
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
 * Create Time: 2020/2/24 14:51
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class GetVDIImageListHandlerSPIImplTest {

    @Tested
    private GetVDIImageListHandlerSPIImpl getVDIImageListHandlerSPI;

    @Injectable
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Injectable
    private VDIEditImageHelper helper;

    /**
     * 获取镜像列表
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessage() throws BusinessException {
        String dataJsonString = "{\"page\":1,\"limit\":10}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);

        CbbImageTemplateDetailDTO templateDTO = new CbbImageTemplateDetailDTO();
        DefaultPageResponse<CbbImageTemplateDetailDTO> apiResponse = new DefaultPageResponse<>();
        apiResponse.setItemArr(new CbbImageTemplateDetailDTO[]{templateDTO});

        DefaultPageResponse<CbbImageTemplateDetailDTO> emptyApiResponse = new DefaultPageResponse<>();
        emptyApiResponse.setItemArr(new CbbImageTemplateDetailDTO[0]);

        VDIEditImageSessionRequestDTO requestDTO = getVDIImageListHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate((LocalImageTemplatePageRequest) any);
                returns(apiResponse, emptyApiResponse);
            }
        };

        CbbResponseShineMessage responseMessage = getVDIImageListHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());

        Assert.assertEquals(0, responseMessage.getCode().intValue());
    }

    /**
     * 获取镜像列表，异常
     * @throws BusinessException 异常
     */
    @Test
    public void testGetResponseMessageException() throws BusinessException {
        String dataJsonString = "{\"page\":1,\"limit\":10}";
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData(dataJsonString);

        CbbImageTemplateDetailDTO templateDTO = new CbbImageTemplateDetailDTO();
        DefaultPageResponse<CbbImageTemplateDetailDTO> apiResponse = new DefaultPageResponse<>();
        apiResponse.setItemArr(new CbbImageTemplateDetailDTO[]{templateDTO});

        VDIEditImageSessionRequestDTO requestDTO = getVDIImageListHandlerSPI.resolveJsonString(dataJsonString);

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate((LocalImageTemplatePageRequest) any);
                result = new Exception("xxx");
            }
        };

        CbbResponseShineMessage responseMessage = getVDIImageListHandlerSPI.getResponseMessage(request, requestDTO, UUID.randomUUID());

        Assert.assertEquals(-1, responseMessage.getCode().intValue());
    }
}