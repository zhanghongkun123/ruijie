package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
public interface RcoDeskInfoDAO extends SkyEngineJpaRepository<RcoDeskInfoEntity, UUID> {

    /**
     * * 根据deskId获取RcoDeskInfoEntity
     *
     * @param deskId cbb桌面ID
     * @return 返回DesktopEntity对象
     */
    RcoDeskInfoEntity findByDeskId(UUID deskId);


    /**
     * 根据deskId删除
     *
     * @param deskId cbb桌面ID
     * @return 删除条数
     */
    @Modifying
    @Transactional
    int deleteByDeskId(UUID deskId);

    /**
     * 根据软件管控策略ID查找云桌面
     * @param softwareStrategyId 软件管控策略ID
     * @return 返回RcoDeskInfoEntity对象列表
     */
    List<RcoDeskInfoEntity> findBySoftwareStrategyId(UUID softwareStrategyId);

    /**
     *
     * 根据软件管控策略ID查找云桌面id
     * @param softwareStrategyIdS 软控策略ids
     * @return 返回桌面列表
     */
    List<RcoDeskInfoEntity> findBySoftwareStrategyIdIn(Iterable<UUID> softwareStrategyIdS);

    /**
     * 根据策略ID查找关联的云桌面列表
     *
     * @param id 策略ID
     * @return 响应对象
     */
    List<RcoDeskInfoEntity> findByUserProfileStrategyId(UUID id);

    /**
     * 根据策略ID列表查询云桌面列表
     *
     * @param strategyIdList 策略ID列表
     * @return 云桌面列表
     */
    List<RcoDeskInfoEntity> findByUserProfileStrategyIdIn(List<UUID> strategyIdList);
}
