package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
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
 * Create Time: 2024/2/26
 *
 * @author zqj
 */
public interface HostUserDAO extends SkyEngineJpaRepository<HostUserEntity, UUID> {

    /**
     * 获取主机和用户关系
     *
     * @param desktopPoolId desktopPoolId
     * @param userId        userId
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByDesktopPoolIdAndUserId(UUID desktopPoolId, UUID userId);


    /**
     * 移除主机和用户关系
     *
     * @param userId userId
     * @param deskId deskId
     */
    @Modifying
    @Transactional
    void deleteByUserIdAndDesktopId(UUID userId, UUID deskId);

    /**
     * 移除主机和用户关系
     *
     * @param deskId deskId
     */
    @Modifying
    @Transactional
    void deleteByDesktopId(UUID deskId);

    /**
     * 获取实体
     *
     * @param userId    userId
     * @param desktopId desktopId
     * @return HostUserEntity
     */
    HostUserEntity findByUserIdAndDesktopId(UUID userId, UUID desktopId);

    /**
     * 获取列表
     *
     * @param deskId deskId
     * @return 列表
     */
    List<HostUserEntity> findByDesktopId(UUID deskId);

    /**
     * 根据userID删除所有记录
     *
     * @param userId 用户ID
     */
    @Modifying
    @Transactional
    void deleteByUserId(UUID userId);

    /**
     * 根据userID查询记录列表
     *
     * @param userId 用户ID
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByUserId(UUID userId);

    /**
     * 根据terminalId查询
     *
     * @param terminalId terminalId
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByTerminalId(String terminalId);

    /**
     * 根据桌面ID列表查询记录
     *
     * @param desktopIdList 桌面ID列表
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByDesktopIdIn(List<UUID> desktopIdList);

    /**
     * 根据用户ID列表查询记录
     *
     * @param userIdList 用户ID列表
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByUserIdIn(List<UUID> userIdList);

    /**
     * 根据桌面池id获取用户id列表
     *
     * @param desktopPoolId 桌面池id
     * @return 用户id列表
     */
    @Query(value = "select CAST(t.user_id AS VARCHAR) user_id" +
            "            from t_rco_host_user t " +
            "            inner join t_cbb_desk_info t2 on  t2.desk_id = t.desktop_id" +
            "        WHERE t2.desk_state != 'RECYCLE_BIN' and t.desktop_pool_id = ?1", nativeQuery = true)
    List<UUID> findUserIdListByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据id批量删除
     *
     * @param idList idList
     */
    @Modifying
    @Transactional
    void deleteByIdIn(List<UUID> idList);

    /**
     * 根据桌面池id、用户id查询未删除的桌面ID
     *
     * @param userId        用户id
     * @param desktopPoolId 桌面池id
     * @return 桌面id列表
     */
    @Query(value = "select CAST(t.desktop_id AS VARCHAR) desktop_id " +
            "from t_rco_host_user t " +
            "inner join t_cbb_desk_info t2 on  t2.desk_id = t.desktop_id " +
            "where t2.is_delete = false and t.user_id = ?1 and t.desktop_pool_id = ?2", nativeQuery = true)
    List<UUID> findNormalDesktopIdByUserIdAndDesktopPoolId(UUID userId, UUID desktopPoolId);

    /**
     * 根据用户ID和池ID删除
     *
     * @param userId        用户ID
     * @param desktopPoolId 池ID
     */
    @Modifying
    @Transactional
    void deleteByUserIdAndDesktopPoolId(UUID userId, UUID desktopPoolId);
}
