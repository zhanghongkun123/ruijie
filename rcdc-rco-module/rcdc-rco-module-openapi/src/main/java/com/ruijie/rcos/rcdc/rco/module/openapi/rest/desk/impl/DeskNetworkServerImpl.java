package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkBasicInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.DeskNetworkServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestDeskNetworkDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class DeskNetworkServerImpl implements DeskNetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskNetworkServerImpl.class);

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Override
    public PageQueryResponse pageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "request must not be null");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("openApi desk_network pageQuery request:" + JSONObject.toJSONString(pageQueryRequest));
        }
        PageQueryResponse<CbbDeskNetworkBasicInfoDTO> response = cbbNetworkMgmtAPI.pageQuery(pageQueryRequest);
        return convert(response);
    }

    private PageQueryResponse<RestDeskNetworkDTO> convert(PageQueryResponse<CbbDeskNetworkBasicInfoDTO> pageQueryResponse) {
        List<RestDeskNetworkDTO> dtoList = new ArrayList<>();
        PageQueryResponse<RestDeskNetworkDTO> result = new PageQueryResponse<>();
        if (pageQueryResponse == null || pageQueryResponse.getItemArr() == null) {
            result.setTotal(0);
            result.setItemArr(dtoList.toArray(new RestDeskNetworkDTO[0]));
            return result;
        }

        Arrays.stream(pageQueryResponse.getItemArr()).forEach(networkBasicInfoDTO -> {
            RestDeskNetworkDTO dto = new RestDeskNetworkDTO();
            BeanUtils.copyProperties(networkBasicInfoDTO, dto);
            // 接口预留支持网络策略绑定多个计算集群
            dto.setClusterIdList(Collections.singletonList(networkBasicInfoDTO.getClusterId()));
            dtoList.add(dto);
        });

        result.setTotal(pageQueryResponse.getTotal());
        result.setItemArr(dtoList.toArray(new RestDeskNetworkDTO[dtoList.size()]));
        return result;
    }
}
