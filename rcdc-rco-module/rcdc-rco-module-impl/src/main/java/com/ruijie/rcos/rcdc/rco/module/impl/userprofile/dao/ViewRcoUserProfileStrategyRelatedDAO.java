package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.ViewRcoUserProfileStrategyRelatedEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户配置策略关联详情视图DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
public interface ViewRcoUserProfileStrategyRelatedDAO extends SkyEngineJpaRepository<ViewRcoUserProfileStrategyRelatedEntity, UUID> {
    /**
     * 通过策略ID查询路径列表
     *
     * @param userProfileStrategyId 策略ID
     * @return 路径列表
     */
    List<ViewRcoUserProfileStrategyRelatedEntity> findByStrategyId(UUID userProfileStrategyId);

    /**
     * 通过策略ID查询排序后的路径列表 排序规则:mode和小写的path
     *
     * @param userProfileStrategyId 策略ID
     * @return 路径列表
     */
    @Query("FROM ViewRcoUserProfileStrategyRelatedEntity WHERE strategyId =?1 ORDER BY mode DESC , lower(path) ASC")
    List<ViewRcoUserProfileStrategyRelatedEntity> findByStrategyIdOrderByModeAndPath(UUID userProfileStrategyId);
}