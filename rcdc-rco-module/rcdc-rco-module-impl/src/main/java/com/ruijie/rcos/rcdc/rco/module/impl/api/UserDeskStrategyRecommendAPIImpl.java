package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserDeskStrategyRecommendAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskStrategyRecommendDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskStrategyRecommendQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DeskStrategyRecommendDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DeskStrategyRecommendService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/4/3 <br>
 *
 * @author yyz
 */
public class UserDeskStrategyRecommendAPIImpl implements UserDeskStrategyRecommendAPI {


    @Autowired
    private DeskStrategyRecommendService deskStrategyRecommendService;


    @Override
    public DefaultPageResponse<DeskStrategyRecommendDTO> pageQuery(DeskStrategyRecommendQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        return deskStrategyRecommendService.pageQuery(request);
    }

    @Override
    public void deskStrategyRecommendEdit(UUID[] uuidArr) throws BusinessException {
        Assert.notNull(uuidArr, "uuidArr is not null");
        deskStrategyRecommendService.deskStrategyRecommendEdit(uuidArr);
    }

    @Override
    public DeskStrategyRecommendDetailResponse deskStrategyRecommendDetail(UUID strategyRecommendId) throws BusinessException {
        Assert.notNull(strategyRecommendId, "strategyRecommendId is not null");

        DeskStrategyRecommendDetailResponse response = new DeskStrategyRecommendDetailResponse();
        DeskStrategyRecommendDTO deskStrategyRecommendDTO = deskStrategyRecommendService.deskStrategyRecommendDetail(strategyRecommendId);
        BeanUtils.copyProperties(deskStrategyRecommendDTO, response);
        return response;
    }

}
