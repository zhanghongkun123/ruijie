package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.ViewRcoSoftwareStrategyRelatedSoftwareEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public interface ViewRcoSoftwareStrategyRelatedSoftwareDAO extends SkyEngineJpaRepository<ViewRcoSoftwareStrategyRelatedSoftwareEntity, UUID> {

    /**
     * 根据策略ID查询软件列表
     * 
     * @param strategyId 策略ID
     * @return 软件列表
     */
    List<ViewRcoSoftwareStrategyRelatedSoftwareEntity> findAllByStrategyId(UUID strategyId);

}
