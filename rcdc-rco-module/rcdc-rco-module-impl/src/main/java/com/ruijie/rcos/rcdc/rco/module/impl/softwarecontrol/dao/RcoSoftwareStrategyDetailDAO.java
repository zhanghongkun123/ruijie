package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareStrategyRelatedTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareStrategyDetailEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
public interface RcoSoftwareStrategyDetailDAO extends SkyEngineJpaRepository<RcoSoftwareStrategyDetailEntity, UUID> {

    /**
     * 通过关联的对象ID和关联的对象类型 查找关联关系记录
     * 
     * @param relatedId 关联的对象ID
     * @param type 关联的对象类型
     * @return 关联关系记录列表
     */
    List<RcoSoftwareStrategyDetailEntity> findByRelatedIdAndRelatedType(UUID relatedId, SoftwareStrategyRelatedTypeEnum type);

    /**
     * 通过软件策略ID 查找关联关系记录
     * 
     * @param id 软件策略ID
     * @return 关联关系记录列表
     */
    List<RcoSoftwareStrategyDetailEntity> findByStrategyId(UUID id);

    /**
     * 通过软件策略ID、关联的对象ID、关联的对象类型 查找关联关系记录
     * 
     * @param strategyId 软件策略ID
     * @param relatedId 关联的对象ID
     * @param type 关联的对象类型
     * @return 关联关系记录
     */
    RcoSoftwareStrategyDetailEntity findByStrategyIdAndRelatedIdAndRelatedType(UUID strategyId, UUID relatedId, SoftwareStrategyRelatedTypeEnum type);

    /**
     * 根据软件策略ID删除记录
     * 
     * @param strategyId 软件策略ID
     */
    @Modifying
    @Transactional
    void deleteByStrategyId(UUID strategyId);

    /**
     * 根据软件策略ID删除记录
     *
     * @param strategyId 软件策略ID
     * @param relatedId 关联id
     *
     *
     */
    @Modifying
    @Transactional
    void deleteByStrategyIdAndRelatedIdIn(UUID strategyId, Iterable<UUID> relatedId);


    /**
     *
     * @param relatedIds 关联id
     * @param relatedType 关联类型
     * @return 返回去重的策略ids
     */
    @Query("select distinct o.strategyId from RcoSoftwareStrategyDetailEntity o where o.relatedId in :relatedIds and o.relatedType = :relatedType")
    List<UUID> findStrategyIds(@Param("relatedIds") List<UUID> relatedIds, @Param("relatedType") SoftwareStrategyRelatedTypeEnum relatedType);

}
