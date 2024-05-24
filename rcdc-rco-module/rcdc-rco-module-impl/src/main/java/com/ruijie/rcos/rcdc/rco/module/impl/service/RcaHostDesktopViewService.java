package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 
 * Description: 派生云应用主机云桌面视图接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 * 
 * @author liuwc
 */
public interface RcaHostDesktopViewService {

    /**
     * 
     * @param request search request
     * @return view page
     */
    Page<ViewRcaHostDesktopEntity> pageQuery(PageSearchRequest request);

    /**
     * 根据桌面池ID 查询
     *
     * @param poolId 应用池id
     * @return List<ViewRcaHostDesktopEntity> 云桌面列表
     */
    List<ViewRcaHostDesktopEntity> findAllByPoolId(UUID poolId);


    /**
     * 根据主机id列表查询主机桌面详情
     *
     * @param hostIdList 主机id列表
     * @return 主机桌面详情列表
     * @throws BusinessException 业务异常
     */
    List<ViewRcaHostDesktopEntity> listByHostIdIn(List<UUID> hostIdList) throws BusinessException;
}
