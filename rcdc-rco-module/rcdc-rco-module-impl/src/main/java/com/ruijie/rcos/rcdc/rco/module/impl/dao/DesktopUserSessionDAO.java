package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopUserSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.SessionStatusEnums;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author wangjie9
 */
public interface DesktopUserSessionDAO extends SkyEngineJpaRepository<DesktopUserSessionEntity, UUID> {


    /**
     * 根据用户ID、会话ID、主机ID查找会话记录
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param deskId 主机ID
     * @return 会话记录
     */
    List<DesktopUserSessionEntity> findByUserIdAndSessionIdAndDeskId(UUID userId, Integer sessionId, UUID deskId);

    /**
     * 根据桌面id删除记录
     * @param deskId deskId
     */
    @Transactional
    @Modifying
    void deleteByDeskId(UUID deskId);

    /**
     * 根据主键id删除记录
     * @param id id
     */
    @Transactional
    @Modifying
    void deleteById(UUID id);

    /**
     *  根据桌面id集合获取数据
     * @param deskIdList 应用主机id列表
     * @return 主机会话类表
     */
    List<DesktopUserSessionEntity> findAllByDeskIdIn(List<UUID> deskIdList);

    /**
     * 根据桌面id、会话状态查询记录
     * @param cbbDesktopId 桌面id
     * @param destroying 会话状态
     * @return 云桌面会话列表
     */
    List<DesktopUserSessionEntity> findAllByDeskIdAndSessionStatus(UUID cbbDesktopId, SessionStatusEnums destroying);

    /**
     * 统计数量
     * @param deskId deskId
     * @return 数量
     */
    int countByDeskId(UUID deskId);

    /**
     * 获取会话
     * @param userId userId
     * @return DesktopUserSessionEntity
     */
    List<DesktopUserSessionEntity> findByUserId(UUID userId);

    /**
     * 获取会话
     * @param userIdList userIdList
     * @return DesktopUserSessionEntity
     */
    List<DesktopUserSessionEntity> findByUserIdIn(List<UUID> userIdList);

    /**
     * 获取会话
     * @param deskId deskId
     * @return DesktopUserSessionEntity
     */
    List<DesktopUserSessionEntity> findByDeskId(UUID deskId);

    /**
     * 获取注销中的会话信息
     *
     * @param id 会话主键id
     * @param sessionStatus 会话状态
     * @return 会话信息
     */
    List<DesktopUserSessionEntity> findAllByIdAndSessionStatus(UUID id, SessionStatusEnums sessionStatus);

    /**
     * 获取会话信息
     * @param userId  userId
     * @param desktopId desktopId
     * @return DesktopUserSessionEntity
     */
    DesktopUserSessionEntity findByUserIdAndDeskId(UUID userId, UUID desktopId);

    /**
     * 根据桌面池ID和用户ID，查询会话
     *
     * @param userId        userId
     * @param desktopPoolId desktopPoolId
     * @return List<UUID>
     */
    @Query(value = "select CAST(us.desk_id AS VARCHAR) desk_id from t_rco_desk_user_session us " +
            "inner join t_cbb_desk_info t on us.desk_id = t.desk_id " +
            "where us.user_id = ?1 and t.desktop_pool_id = ?2 ", nativeQuery = true)
    List<UUID> findDeskIdByUserIdAndDesktopPoolId(UUID userId, UUID desktopPoolId);

    /**
     * 删除桌面下指定用户记录
     *
     * @param userId 用户ID
     * @param deskId 桌面ID
     */
    @Transactional
    @Modifying
    void deleteByUserIdAndDeskId(UUID userId, UUID deskId);
}
