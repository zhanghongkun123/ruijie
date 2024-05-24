package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareStrategyEntity;
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
public interface RcoSoftwareStrategyDAO extends SkyEngineJpaRepository<RcoSoftwareStrategyEntity, UUID> {
    /**
     * 通过软件策略名称查找软件策略实体
     * @param name 软件策略名称
     * @return 软件策略实体
     */
    RcoSoftwareStrategyEntity findByName(String name);

    /**
     *
     * @param softwareStrategyIdList 策略ids
     * @return 策略总数
     */
    Integer countByIdIn(Iterable<UUID> softwareStrategyIdList);

    /**
     *
     * @param softwareStrategyIdList softwareStrategyIdList
     * @return 策略列表
     */
    List<RcoSoftwareStrategyEntity> findByIdIn(Iterable<UUID> softwareStrategyIdList);



}
