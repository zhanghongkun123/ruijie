package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.common.query.RcdcJpaRepository;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserGroupDesktopConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 用户组云桌面配置操作DAO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月17日
 *
 * @author zhuangchenwu
 */
public interface UserGroupDesktopConfigDAO extends RcdcJpaRepository<UserGroupDesktopConfigEntity, UUID>, PageQueryDAO<UserGroupDesktopConfigEntity> {

    /**
     * 根据用户组id和桌面类型查用户桌面配置
     *
     * @param groupId  用户组id
     * @param deskType 桌面类型
     * @return UserDesktopConfigEntity
     */
    UserGroupDesktopConfigEntity findByGroupIdAndDeskType(UUID groupId, UserCloudDeskTypeEnum deskType);

    /**
     * 删除组桌面配置
     *
     * @param groupId  groupId
     * @param deskType deskType
     * @return 删除数
     */
    @Transactional
    @Modifying
    int deleteByGroupIdAndDeskType(UUID groupId, UserCloudDeskTypeEnum deskType);

    /**
     * 根据网络策略id获取配置列表
     *
     * @param networkId networkId
     * @return list
     */
    List<UserGroupDesktopConfigEntity> findByNetworkId(UUID networkId);

    /**
     * 根据策略id获取配置列表
     *
     * @param strategyId strategyId
     * @return list
     */
    List<UserGroupDesktopConfigEntity> findByStrategyId(UUID strategyId);

    /**
     * 获取第一个用户组桌面配置
     *
     * @param imageTemplateId imageTemplateId
     * @return UserGroupDesktopConfigEntity
     */
    UserGroupDesktopConfigEntity findFirstByImageTemplateId(UUID imageTemplateId);

    /**
     * @param imageTemplateIdList 镜像id列表
     * @return 返回第一次
     */
    UserGroupDesktopConfigEntity findFirstByImageTemplateIdIn(List<UUID> imageTemplateIdList);

    /**
     * 获取第一个用户组桌面配置
     *
     * @param networkId networkId
     * @return UserGroupDesktopConfigEntity
     */
    UserGroupDesktopConfigEntity findFirstByNetworkId(UUID networkId);

    /**
     * 获取第一个用户组桌面配置
     *
     * @param strategyId strategyId
     * @return UserGroupDesktopConfigEntity
     */
    UserGroupDesktopConfigEntity findFirstByStrategyId(UUID strategyId);

    /**
     * 根据传入的策略ID获取当前绑定用户组数量
     *
     * @param strategyId 策略ID
     * @return 绑定数量
     */
    int countByStrategyId(UUID strategyId);

    /**
     * 查询绑定镜像ID的用户组数目
     *
     * @param imageTemplateId 镜像ID
     * @return 绑定镜像ID的用户组数目
     */
    int countByImageTemplateId(UUID imageTemplateId);

    /**
     * 通过用户配置策略id，查询UserGroupDesktopConfigEntity列表
     *
     * @param strategyId 用户配置策略id
     * @return UserGroupDesktopConfigEntity列表
     */
    List<UserGroupDesktopConfigEntity> findByUserProfileStrategyId(UUID strategyId);

    /**
     * 根据网络策略id获取配置列表
     *
     * @param deskType 用户桌面枚举
     * @return list 用户桌面配置集合
     */
    List<UserGroupDesktopConfigEntity> findByDeskType(UserCloudDeskTypeEnum deskType);
}
