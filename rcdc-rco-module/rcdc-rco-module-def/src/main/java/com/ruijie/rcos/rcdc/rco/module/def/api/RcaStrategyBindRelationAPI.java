package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppStrategyBindUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto.AdGroupListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.UUID;

/**
 * Description: 云应用策略绑定用户API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/09/28 16:04
 *
 * @author fanbaorong
 */
public interface RcaStrategyBindRelationAPI {

    /**
     * 分页获取云应策略绑定用户列表
     *
     * @param request 分页请求
     * @param rcaStrategyId 策略Id
     * @return 用户列表
     */
    DefaultPageResponse<RcaAppStrategyBindUserDTO> pageQueryRcaStrategyBindUser(UUID rcaStrategyId, PageSearchRequest request);

    /**
     * @param rcaStrategyId 策略id
     * @param request 请求
     * @return 用户列表
     */
    DefaultPageResponse<UserListDTO> pageQueryRcaMainStrategyBindUser(UUID rcaStrategyId,  PageSearchRequest request);

    /**
     * @param rcaStrategyId 策略id
     * @param request 请求
     * @return 应用池列表
     */
    DefaultPageResponse<RcaAppPoolBaseDTO> pageQueryRcaMainStrategyBindPool(UUID rcaStrategyId, PageSearchRequest request);

    /**
     * @param rcaStrategyId 策略id
     * @param request 请求
     * @return 安全组列表
     */
    DefaultPageResponse<AdGroupListDTO> pageQueryRcaMainStrategyBindSafetyGroup(UUID rcaStrategyId, PageSearchRequest request);


    /**
     * @param rcaStrategyId 策略id
     * @param request 请求
     * @return 用户列表
     */
    DefaultPageResponse<UserListDTO> pageQueryRcaPeripheralStrategyBindUser(UUID rcaStrategyId,  PageSearchRequest request);

    /**
     * @param rcaStrategyId 策略id
     * @param request 请求
     * @return 应用池列表
     */
    DefaultPageResponse<RcaAppPoolBaseDTO> pageQueryRcaPeripheralStrategyBindPool(UUID rcaStrategyId, PageSearchRequest request);

    /**
     * @param rcaStrategyId 策略id
     * @param request 请求
     * @return 安全组列表
     */
    DefaultPageResponse<AdGroupListDTO> pageQueryRcaPeripheralStrategyBindSafetyGroup(UUID rcaStrategyId, PageSearchRequest request);

}
