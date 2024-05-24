package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyRelatedEntity;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileRelatedTypeEnum;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户配置策略与路径关联DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/12
 *
 * @author WuShengQiang
 */
public interface UserProfileStrategyRelatedDAO extends SkyEngineJpaRepository<UserProfileStrategyRelatedEntity, UUID> {

    /**
     * 通过关联的对象ID和关联的对象类型 查找关联关系记录
     *
     * @param relatedId 关联对象ID
     * @param type      关联对象类型
     * @return 关联记录列表
     */
    List<UserProfileStrategyRelatedEntity> findByRelatedIdAndRelatedType(UUID relatedId, UserProfileRelatedTypeEnum type);

    /**
     * 通过关联的对象ID集合和关联的对象类型 查找关联关系记录
     *
     * @param relatedIdList 关联对象ID集合
     * @param type          关联对象类型
     * @return 关联记录列表
     */
    @Query("from UserProfileStrategyRelatedEntity where relatedId in ?1 and relatedType = ?2")
    List<UserProfileStrategyRelatedEntity> findByRelatedIdInAndRelatedType(List<UUID> relatedIdList, UserProfileRelatedTypeEnum type);

    /**
     * 根据策略ID查找关联对象列表
     *
     * @param id 策略ID
     * @return 响应对象
     */
    List<UserProfileStrategyRelatedEntity> findByStrategyId(UUID id);

    /**
     * 根据策略ID删除关联记录
     *
     * @param strategyId 策略ID
     */
    void deleteByStrategyId(UUID strategyId);

    /**
     * 根据策略ID和关联ID删除记录
     *
     * @param strategyId 策略ID
     * @param id         关联ID
     */
    @Transactional
    @Modifying
    void deleteByStrategyIdAndRelatedId(UUID strategyId, UUID id);

    /**
     * 找到个性化配置关联的策略
     *
     * @param relatedId 配置ID
     * @return 返回
     */
    @Query("select strategyId from UserProfileStrategyRelatedEntity where relatedId = ?1")
    List<UUID> findStrategyIdByRelatedId(UUID relatedId);

    /**
     * 找到个性化配置策略关联的配置
     *
     * @param strategyId    策略ID
     * @param userProfileId 需排除配置的ID
     * @return 返回
     */
    @Query("select relatedId from UserProfileStrategyRelatedEntity where strategyId = ?1 and relatedId != ?2")
    List<UUID> findRelatedIdByStrategyIdExceptId(UUID strategyId, UUID userProfileId);
}