package com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service;


import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryAppGroupDTO;
import org.springframework.data.domain.Page;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.entity.ViewAdGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Ad域组与池信息查询接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author TD
 */
public interface AdGroupPoolService {

    /**
     * 查询安全组的分配信息
     *
     * @param queryPoolDTO 查询信息
     * @return 安全组信息
     */
    Page<ViewAdGroupEntity> pageAdGroupInOrNotInPool(PageQueryPoolDTO queryPoolDTO);

    /**
     * 查询安全组的应用分组分配信息
     * @param queryAppGroupDTO 查询信息
     * @return 安全组信息
     */
    Page<ViewAdGroupEntity> pageAdGroupInOrNotInAppGroup(PageQueryAppGroupDTO queryAppGroupDTO);

    /**
     * @param request request
     * @param objectGuidList objectGuidList
     * @return 返回
     */
    Page<ViewAdGroupEntity> pageQueryAdGroupInObjectGuidList(PageSearchRequest request, List<String> objectGuidList);

    /**
     * @param request request
     * @param adGroupIdList adGroupIdList
     * @return 返回
     */
    Page<ViewAdGroupEntity> pageQueryAdGroupInAdGroupIdList(PageSearchRequest request, List<UUID> adGroupIdList);

    /**
     * 查询安全组列表
     *
     * @param adGroupIdList 安全组ID集合
     * @return 安全组列表
     * @throws BusinessException BusinessException
     */
    List<IacAdGroupEntityDTO> listAdGroupByIds(List<UUID> adGroupIdList) throws BusinessException;

    /**
     * 检查这些Id是否都存在
     *
     * @param adGroupIdList 组集合
     * @return 结果
     * @throws BusinessException BusinessException
     */
    boolean checkAllAdGroupExistByIds(List<UUID> adGroupIdList) throws BusinessException;
}
