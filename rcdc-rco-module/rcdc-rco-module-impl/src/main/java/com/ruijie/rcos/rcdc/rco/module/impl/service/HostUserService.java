package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;

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
public interface HostUserService {


    /**
     * 获取主机和用户关系
     * @param desktopPoolId desktopPoolId
     * @param userId userId
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByDesktopPoolIdAndUserId(UUID desktopPoolId, UUID userId);

    /**
     * 绑定主机和用户关系
     * @param desktopInfoDTO  desktopInfoDTO
     * @param userId userId
     */
    void poolDesktopBindUser(PoolDesktopInfoDTO desktopInfoDTO, UUID userId);


    /**
     * 移除主机和用户关系
     * @param id id
     */
    void removeById(UUID id);

    /**
     * 移除主机和用户关系
     * @param idList idList
     */
    void removeByIds(List<UUID> idList);

    /**
     * 移除主机和用户关系
     * @param userId  userId
     * @param deskId  deskId
     */
    void removeByUserIdAndDeskId(UUID userId, UUID deskId);

    /**
     *  移除主机
     * @param deskId deskId
     */
    void deleteByDeskId(UUID deskId);

    /**
     * 桌面绑定用户
     * @param request request
     */
    void desktopBindUser(UserDesktopBindUserRequest request);

    /**
     * 获取实体
     * @param id  id
     * @return HostUserEntity
     */
    HostUserEntity findById(UUID id);

    /**
     * 获取列表
     * @param deskId deskId
     * @return 列表
     */
    List<HostUserEntity> findByDeskId(UUID deskId);

    /**
     * 获取列表
     * @param userId userId
     * @return 列表
     */
    List<HostUserEntity> findByUserId(UUID userId);


    /**
     * 根据userID删除所有记录
     *
     * @param userId 用户ID
     */
    void deleteByUserId(UUID userId);

    /**
     * 获取关联记录
     * @param deskId  deskId
     * @param userId userId
     * @return HostUserEntity
     */
    HostUserEntity findByDeskIdAndUserId(UUID deskId, UUID userId);

    /**
     * 取消用户关联
     * @param deskId deskId
     * @param userId userId
     */
    void unBindUser(UUID deskId, UUID userId);

    /**
     * 清理终端信息
     *
     * @param terminalId terminalId
     */
    void clearTerminalIdByTerminalId(String terminalId);

    /**
     * 绑定终端
     *
     * @param userId     userId
     * @param desktopId  desktopId
     * @param terminalId terminalId
     */
    void bindTerminalId(UUID userId, UUID desktopId, String terminalId);

    /**
     * 根据桌面ID查询关联的终端标识列表
     *
     * @param deskId 桌面ID
     * @return List<String>
     */
    List<String> listTerminalIdByDeskId(UUID deskId);

    /**
     * 根据桌面ID列表查询记录
     *
     * @param desktopIdList 桌面ID列表
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByDeskIds(List<UUID> desktopIdList);

    /**
     * 根据用户ID列表查询记录
     *
     * @param userIdList 用户ID列表
     * @return List<HostUserEntity>
     */
    List<HostUserEntity> findByUserIds(List<UUID> userIdList);

    /**
     * 根据桌面池id获取用户id列表
     * @param desktopPoolId  桌面池id
     * @return 用户id列表
     */
    List<UUID> findUserIdListByDesktopPoolId(UUID desktopPoolId);

    /**
     * 更新entity
     *
     * @param entity entity
     */
    void updateHostUserEntity(HostUserEntity entity);

    /**
     * 根据桌面池id、用户id查询未删除的桌面ID
     *
     * @param userId        用户id
     * @param desktopPoolId 桌面池id
     * @return 桌面id列表
     */
    List<UUID> findNormalDesktopIdByUserIdAndDesktopPoolId(UUID userId, UUID desktopPoolId);

    /**
     * 根据用户ID和池ID删除
     *
     * @param userId        用户ID
     * @param desktopPoolId 池ID
     */
    void removeByUserIdAndDesktopPoolId(UUID userId, UUID desktopPoolId);
}
