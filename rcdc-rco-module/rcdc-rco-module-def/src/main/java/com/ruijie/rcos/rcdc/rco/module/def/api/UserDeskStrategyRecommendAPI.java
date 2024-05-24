package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskStrategyRecommendDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskStrategyRecommendQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DeskStrategyRecommendDetailResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

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
public interface UserDeskStrategyRecommendAPI {

    /**
     *
     * 分页查询云桌面策略推荐
     *
     * @param request 请求参数
     * @return 分页查询云桌面策略推荐
     * @throws BusinessException BusinessException
     */
    
    DefaultPageResponse<DeskStrategyRecommendDTO> pageQuery(DeskStrategyRecommendQueryRequest request) throws BusinessException;

    /**
     * 修改云桌面策略推荐显示与否
     * 
     * @param uuidArr 请求参数
     * @throws BusinessException BusinessException
     */
    
    void deskStrategyRecommendEdit(UUID[] uuidArr) throws BusinessException;

    /**
     * 获取一条云桌面策略
     * 
     * @param strategyRecommendId strategyRecommendId
     * @return DeskStrategyRecommendDetailResponse DeskStrategyRecommendDetailResponse
     * @throws BusinessException 异常
     */
    
    DeskStrategyRecommendDetailResponse deskStrategyRecommendDetail(UUID strategyRecommendId) throws BusinessException;
}
