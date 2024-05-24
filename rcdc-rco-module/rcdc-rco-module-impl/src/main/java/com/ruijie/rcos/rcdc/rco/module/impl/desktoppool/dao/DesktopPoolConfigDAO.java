package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Description: 桌面池业务层配置信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/24
 *
 * @author linke
 */
public interface DesktopPoolConfigDAO extends SkyEngineJpaRepository<DesktopPoolConfigEntity, UUID> {

    /**
     * 根据桌面池ID查询配置信息
     *
     * @param desktopPoolId 桌面池ID
     * @return DesktopPoolConfigEntity
     */
    DesktopPoolConfigEntity findByDesktopPoolId(UUID desktopPoolId);

    /**
     * 删除指定桌面池id的数据
     * @param desktopPoolId 桌面池id
     */
    @Transactional
    @Modifying
    void deleteByDesktopPoolId(UUID desktopPoolId);

    /**
     * 查询是否有这个软件管控ID
     *
     * @param softwareStrategyId 软件管控策略ID
     * @return true有
     */
    boolean existsBySoftwareStrategyId(UUID softwareStrategyId);
}
