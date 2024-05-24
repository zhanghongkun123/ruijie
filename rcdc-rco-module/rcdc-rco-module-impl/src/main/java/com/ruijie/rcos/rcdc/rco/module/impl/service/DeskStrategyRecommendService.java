package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskStrategyRecommendDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskStrategyRecommendQueryRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

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
public interface DeskStrategyRecommendService {

    /**
     * 分页查询云桌面策略
     * 
     * @param request request
     * @return 分页查询结果
     * @throws BusinessException 异常
     */
    DefaultPageResponse<DeskStrategyRecommendDTO> pageQuery(DeskStrategyRecommendQueryRequest request) throws BusinessException;

    /**
     * 桌面策略推荐编辑
     * 
     * @param uuidArr request
     * @return 默认结果
     */
    DefaultResponse deskStrategyRecommendEdit(UUID[] uuidArr);

    /**
     * 获取一条云桌面策略
     * 
     * @param strategyRecommendId id
     * @return 结果
     * @throws BusinessException 异常
     */
    DeskStrategyRecommendDTO deskStrategyRecommendDetail(UUID strategyRecommendId) throws BusinessException;
}
