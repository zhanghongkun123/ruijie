package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.IdvTerminalGroupDeskConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/26 14:36
 *
 * @author conghaifeng
 */
public interface IdvTerminalGroupDeskConfigDAO extends SkyEngineJpaRepository<IdvTerminalGroupDeskConfigEntity, UUID> {

    /**
     * 获取终端配置信息
     *
     * @param cbbTerminalGroupId 终端组id
     * @return 返回终端配置信息
     */
    List<IdvTerminalGroupDeskConfigEntity> findTerminalGroupDeskConfigEntityByCbbTerminalGroupId(UUID cbbTerminalGroupId);

    /**
     * 删除终端配置信息
     *
     * @param cbbTerminalGroupId 终端组id
     * @return 返回删除结果
     */
    @Modifying
    @Transactional
    int deleteByCbbTerminalGroupId(UUID cbbTerminalGroupId);

    /**
     * 查绑定桌面策略id的组
     *
     * @param cbbIdvDesktopStrategyId 桌面策略id
     * @return IdvTerminalGroupDeskConfigEntity
     */
    IdvTerminalGroupDeskConfigEntity findFirstByCbbIdvDesktopStrategyId(UUID cbbIdvDesktopStrategyId);

    /**
     * 查绑定镜像id的组
     *
     * @param cbbIdvDesktopImageId 镜像模板id
     * @return IdvTerminalGroupDeskConfigEntity
     */
    IdvTerminalGroupDeskConfigEntity findFirstByCbbIdvDesktopImageId(UUID cbbIdvDesktopImageId);

    /**
     * 通过镜像ID计算终端组条目
     * @param cbbIdvDesktopImageId 镜像ID
     * @return 终端组条目
     */
    int countByCbbIdvDesktopImageId(UUID cbbIdvDesktopImageId);

    /**
     * 查绑定桌面策略id的组
     *
     * @param cbbIdvDesktopStrategyId 桌面策略id
     * @return IdvTerminalGroupDeskConfigEntity
     */
    List<IdvTerminalGroupDeskConfigEntity> findByCbbIdvDesktopStrategyId(UUID cbbIdvDesktopStrategyId);

    /**
     * 查绑定用户配置策略id的组
     *
     * @param strategyId 用户配置策略ID
     * @return 组列表
     */
    List<IdvTerminalGroupDeskConfigEntity> findByUserProfileStrategyId(UUID strategyId);
}
