package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto.AdGroupListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.List;
import java.util.UUID;

/**
 * Description: 安全组分配池相关API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author TD
 */
public interface AdGroupPoolMgmtAPI {


    /**
     * 查询池中安全组的分配信息
     *
     * @param poolId 池ID
     * @param key key
     * @param request PageSearchRequest
     * @return PageQueryResponse<AdGroupListDTO>
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<AdGroupListDTO> pageAdGroupWithAssignment(UUID poolId, String key, PageSearchRequest request) throws BusinessException;


    /**
     * 查询已分配池中的安全组
     *
     * @param poolId 池ID
     * @param key key
     * @param request PageSearchRequest
     * @return PageQueryResponse<AdGroupListDTO>
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<AdGroupListDTO> pageQueryPoolBindAdGroup(UUID poolId, String key, PageSearchRequest request) throws BusinessException;


    /**
     * @param request 请求
     * @param objectGuidList objectGuidList
     * @return 分页返回
     * @throws BusinessException
     */
    DefaultPageResponse<AdGroupListDTO> pageQueryAdGroupInObjectGuidList(PageSearchRequest request, List<String> objectGuidList);


    /**
     * @param request 请求
     * @param adGroupIdList adGroupIdList
     * @return 分页返回
     */
    DefaultPageResponse<AdGroupListDTO> pageQueryAdGroupInAdGroupIdList(PageSearchRequest request, List<UUID> adGroupIdList);
}
