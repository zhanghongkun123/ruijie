package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.strategy.NetworkStrategyPageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageListInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 分页获取网络策略
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/20 15:23
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_GET_RCDC_NETWORK_STRATEGY_ACTION)
public class GetNetworkStrategyHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageIdRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetNetworkStrategyHandlerSPIImpl.class);

    private static final Integer DEFAULT_LIMIT = 50;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, VDIEditImageIdRequestDTO requestDTO, UUID adminId) {
        PageWebRequest pageRequest = new PageWebRequest();
        pageRequest.setLimit(DEFAULT_LIMIT);

        try {
            if (Objects.nonNull(requestDTO.getId())) {
                // 查询镜像模板的计算集群ID
                CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(requestDTO.getId());
                if (Objects.nonNull(imageTemplateDetail.getVmClusterId())) {
                    ExactMatch exactMatch = new ExactMatch();
                    exactMatch.setName(Constants.CLUSTER_ID);
                    exactMatch.setValueArr(new String[] {imageTemplateDetail.getVmClusterId().toString()});
                    pageRequest.setExactMatchArr(new ExactMatch[] {exactMatch});
                }
            }

            ArrayList<CbbDeskNetworkDetailDTO> networkList = getAllNetworks(pageRequest);
            return buildResponseMessage(request, new VDIEditImageListInfoDTO<>(networkList));
        } catch (Exception e) {
            LOGGER.error("获取网络策略失败", e);
            return buildErrorResponseMessage(request);
        }
    }

    private ArrayList<CbbDeskNetworkDetailDTO> getAllNetworks(PageWebRequest pageWebRequest) throws BusinessException {
        ArrayList<CbbDeskNetworkDetailDTO> allResultList = Lists.newArrayList();
        ArrayList<CbbDeskNetworkDetailDTO> pageResultList = Lists.newArrayList();
        int page = 0;
        while (page == 0 || pageResultList.size() != 0) {
            pageWebRequest.setPage(page);
            NetworkStrategyPageSearchRequest apiRequest = new NetworkStrategyPageSearchRequest(pageWebRequest);
            DefaultPageResponse<CbbDeskNetworkDetailDTO> apiResponse = cbbNetworkMgmtAPI.pageQuery(apiRequest);
            Assert.notNull(apiResponse, "network apiResponse cannot be null!");
            CbbDeskNetworkDetailDTO[] itemArr = apiResponse.getItemArr();
            pageResultList = Lists.newArrayList(itemArr);
            LOGGER.debug("查询网络策略第{}页，长度[{}]", page + 1, pageResultList.size());
            allResultList.addAll(pageResultList);
            page++;
        }
        return allResultList;
    }

    @Override
    VDIEditImageIdRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageIdRequestDTO.class);
    }
}
