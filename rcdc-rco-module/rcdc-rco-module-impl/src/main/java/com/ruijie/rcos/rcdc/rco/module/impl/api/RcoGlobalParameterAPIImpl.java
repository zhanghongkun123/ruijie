package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterListRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.global.GlobalParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.globalparameter.GlobalParameterModifiedEventContext;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月17日
 *
 * @author xiejian
 */
public class RcoGlobalParameterAPIImpl implements RcoGlobalParameterAPI {

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private GlobalParameterModifiedEventContext globalParameterModifiedEventContext;

    @Override
    public FindParameterResponse findParameter(FindParameterRequest request) {
        Assert.notNull(request, "request must not be null");

        String value = globalParameterService.findParameter(request.getKey());
        return new FindParameterResponse(value);
    }

    @Override
    public DefaultResponse updateParameter(UpdateParameterRequest request) {
        Assert.notNull(request, "request must not be null");

        globalParameterService.updateParameter(request.getKey(), request.getValue());
        //通知策略
        globalParameterModifiedEventContext.notifyEvent(request.getKey());
        return new DefaultResponse();
    }

    @Override
    public DefaultResponse saveParameter(UpdateParameterRequest request) {
        Assert.notNull(request, "request must not be null");
        RcoGlobalParameterEntity parameterEntity = new RcoGlobalParameterEntity();
        parameterEntity.setParamKey(request.getKey());
        parameterEntity.setParamValue(request.getValue());
        parameterEntity.setCreateTime(new Date());
        parameterEntity.setUpdateTime(new Date());
        globalParameterService.saveParameter(parameterEntity);
        return new DefaultResponse();
    }

    /**
     * @param request 请求
     * @return
     */
    @Override
    public List<FindParameterListResponse> findParameters(FindParameterListRequest request) {
        Assert.notNull(request, "FindParameterListRequest must not be null");
        List<FindParameterListResponse> findParameterListResponseList = new ArrayList<>();
        Set<String> paramKeys = new HashSet<>();
        Arrays.stream(request.getKeyArr()).forEach((key) -> {
            paramKeys.add(key);
        });

        List<GlobalParameterDTO> globalParameterDTOList = globalParameterService.findParameters(paramKeys);
        Map<String, GlobalParameterDTO> globalParameterDTOMap =
                globalParameterDTOList.stream().collect(Collectors.toMap(GlobalParameterDTO::getParamKey, dto -> dto));

        Arrays.stream(request.getKeyArr()).forEach((key) -> {
            FindParameterListResponse findParameterListResponse = new FindParameterListResponse();
            findParameterListResponse.setKey(key);
            GlobalParameterDTO globalParameterDTO = globalParameterDTOMap.get(key);
            findParameterListResponse.setValue(globalParameterDTO != null ? globalParameterDTO.getParamValue() : null);
            findParameterListResponseList.add(findParameterListResponse);
        });
        return findParameterListResponseList;
    }

}
