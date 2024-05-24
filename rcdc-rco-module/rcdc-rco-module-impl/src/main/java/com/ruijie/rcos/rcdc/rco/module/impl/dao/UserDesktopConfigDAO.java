package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * 用户云桌面配置操作DAO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月17日
 *
 * @author zhuangchenwu
 */
public interface UserDesktopConfigDAO extends SkyEngineJpaRepository<UserDesktopConfigEntity, UUID> {

    /**
     * 根据用户id和桌面类型查用户桌面配置
     *
     * @param userId   用户id
     * @param deskType 桌面类型
     * @return UserDesktopConfigEntity
     */
    UserDesktopConfigEntity findByUserIdAndDeskType(UUID userId, UserCloudDeskTypeEnum deskType);

    /**
     * 删除用户桌面配置
     *
     * @param userId   userId
     * @param deskType deskType
     * @return 删除梳理
     */
    @Transactional
    @Modifying
    int deleteByUserIdAndDeskType(UUID userId, UserCloudDeskTypeEnum deskType);

    /**
     * 根据镜像模板id查询用户桌面配置
     *
     * @param imageTemplateId imageTemplateId
     * @return UserDesktopConfigEntity
     */
    UserDesktopConfigEntity findFirstByImageTemplateId(UUID imageTemplateId);

    /**
     * @param imageTemplateIdList 镜像id列表
     * @return 返回第一个
     */
    UserDesktopConfigEntity findFirstByImageTemplateIdIn(List<UUID> imageTemplateIdList);

    /**
     * 根据strategyId查询用户桌面配置
     *
     * @param strategyId strategyId
     * @return UserDesktopConfigEntity
     */
    UserDesktopConfigEntity findFirstByStrategyId(UUID strategyId);

    /**
     * 判断策略是否绑定用户
     *
     * @param strategyId 策略id
     * @return true 绑定用户 false 未绑定
     */
    int countByStrategyId(UUID strategyId);

    /**
     * 计算绑定用户数目
     *
     * @param imageTemplateId 镜像Id
     * @return 绑定用户数目
     */
    int countByImageTemplateId(UUID imageTemplateId);

    /**
     * 通过云桌面策略id，查询UserDesktopConfigEntity列表
     *
     * @param strategyId 云桌面策略ID
     * @return UserDesktopConfigEntity列表
     */
    List<UserDesktopConfigEntity> findByStrategyId(UUID strategyId);

    /**
     * 通过用户配置策略id，查询UserDesktopConfigEntity列表
     *
     * @param strategyId 用户配置策略id
     * @return 列表
     */
    List<UserDesktopConfigEntity> findByUserProfileStrategyId(UUID strategyId);
}
