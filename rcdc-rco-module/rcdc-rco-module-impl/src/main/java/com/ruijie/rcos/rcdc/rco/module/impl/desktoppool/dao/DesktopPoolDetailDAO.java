package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.common.query.RcdcJpaRepository;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolDetailEntity;

import java.util.List;
import java.util.UUID;

/**
 * Description: 桌面池视图DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/11
 *
 * @author linke
 */
public interface DesktopPoolDetailDAO extends RcdcJpaRepository<DesktopPoolDetailEntity, UUID> {

    /**
     * 根据桌面池模式查询列表
     *
     * @param poolModelList poolModelList
     * @return List<DesktopPoolDetailEntity>
     */
    List<DesktopPoolDetailEntity> findByPoolModelIn(List<CbbDesktopPoolModel> poolModelList);

    /**
     * 根据upm策略ID查询桌面池列表
     *
     * @param userProfileStrategyId upm策略ID
     * @return 桌面池列表
     */
    List<DesktopPoolDetailEntity> findAllByUserProfileStrategyId(UUID userProfileStrategyId);

    /**
     * 根据镜像ID查询桌面池列表
     *
     * @param imageTemplateId 镜像ID
     * @return 桌面池列表
     */
    List<DesktopPoolDetailEntity> findAllByImageTemplateId(UUID imageTemplateId);
}
