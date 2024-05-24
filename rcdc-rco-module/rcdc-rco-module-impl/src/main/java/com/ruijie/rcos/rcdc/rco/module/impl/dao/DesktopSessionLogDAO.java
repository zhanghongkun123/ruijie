package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopSessionLogState;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 云桌面会话使用记录DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14 14:53
 *
 * @author yxq
 */
public interface DesktopSessionLogDAO extends SkyEngineJpaRepository<DesktopSessionLogEntity, UUID>, PageQueryDAO<DesktopSessionLogEntity> {

    /**
     * 根据会话id，查询在线的记录
     *
     * @param desktopId    桌面id
     * @param connectionId 会话id
     * @return 实体对象
     */
    DesktopSessionLogEntity findFirstByDesktopIdAndConnectionIdAndLogoutTimeIsNull(UUID desktopId, Long connectionId);

    /**
     * 根据会话id，查询在线的记录
     *
     * @param desktopId    桌面id
     * @param connectionId 会话id
     * @param state        状态
     * @return 实体对象
     */
    DesktopSessionLogEntity findFirstByDesktopIdAndConnectionIdAndState(UUID desktopId, Long connectionId, DesktopSessionLogState state);

    /**
     * 删除指定时间之前的记录
     *
     * @param date 时间
     */
    @Transactional
    @Modifying
    void deleteByCreateTimeBefore(Date date);

    /**
     * 查询在线的会话记录
     *
     * @return 在线的会话记录列表
     */
    List<DesktopSessionLogEntity> findByLogoutTimeIsNull();

    /**
     * 根据状态查询会话记录
     *
     * @param state state
     * @return 会话记录列表
     */
    List<DesktopSessionLogEntity> findByState(DesktopSessionLogState state);

    /**
     * 修改用户组名为新的名称
     *
     * @param userGroupId   用户组id
     * @param userGroupName 新的用户组名
     */
    @Transactional
    @Modifying
    @Query("UPDATE DesktopSessionLogEntity SET userGroupName = :userGroupName, version = version + 1 " +
            "WHERE userGroupId = :userGroupId AND version = version")
    void updateUserGroupName(@Param("userGroupId") UUID userGroupId, @Param("userGroupName") String userGroupName);

    /**
     * 修改桌面池名称名为新的名称
     *
     * @param desktopPoolId   用户组id
     * @param desktopPoolName 新的桌面池名称
     */
    @Transactional
    @Modifying
    @Query("UPDATE DesktopSessionLogEntity SET desktopPoolName = :desktopPoolName, version = version + 1 " +
            "WHERE relatedId = :desktopPoolId AND version = version")
    void updateDesktopPoolName(@Param("desktopPoolId") UUID desktopPoolId, @Param("desktopPoolName") String desktopPoolName);

    /**
     * 找到指定桌面所有没结束的会话
     *
     * @param desktopId 桌面id
     * @return 实体列表
     */
    List<DesktopSessionLogEntity> findByDesktopIdAndLogoutTimeIsNull(UUID desktopId);

    /**
     * 根据桌面ID、状态查询会话记录
     *
     * @param desktopId 桌面id
     * @param state     状态
     * @return 会话记录列表
     */
    List<DesktopSessionLogEntity> findByDesktopIdAndState(UUID desktopId, DesktopSessionLogState state);

    /**
     * 根据terminalId和状态查询列表
     *
     * @param terminalId terminalId
     * @param state      状态
     * @return List<DesktopSessionLogEntity>
     */
    List<DesktopSessionLogEntity> findAllByTerminalIdAndState(String terminalId, DesktopSessionLogState state);

    /**
     * 根据条件查询列表
     *
     * @param userId     userId
     * @param terminalId terminalId
     * @param state      状态
     * @param deskIdList deskIdList
     * @return List<DesktopSessionLogEntity>
     */
    List<DesktopSessionLogEntity> findByUserIdAndTerminalIdAndAndStateAndDesktopIdIn(UUID userId, String terminalId, DesktopSessionLogState state,
                                                                                     List<UUID> deskIdList);
}
