package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/25
 *
 * @author Jarman
 */
public interface UserMessageUserDAO extends SkyEngineJpaRepository<UserMessageUserEntity, UUID> {

    /**
     * 根据消息id和用户id查询记录
     * 
     * @param messageId 消息id
     * @param userId 用户id
     * @return 记录对象
     */
    UserMessageUserEntity findUserMessageUserEntityByMessageIdAndUserId(UUID messageId, UUID userId);

    /**
     * 根据消息记录查询发送对象
     * 
     * @param messageId 消息id
     * @return 用户消息发送对象列表
     */
    List<UserMessageUserEntity> findByMessageId(UUID messageId);

    /**
     * 统计消息对应用户发送状态的数量
     * 
     * @param messageId 消息id
     * @param state 消息状态
     * @return 统计数量
     */
    int countByMessageIdAndState(UUID messageId, UserMessageStateEnum state);

    /**
     * 查找用户对应状态消息
     * 
     * @param userId 用户id
     * @param stateList 消息状态集合
     * @return 消息列表
     */
    List<UserMessageUserEntity> findByUserIdAndStateIn(UUID userId, List<UserMessageStateEnum> stateList);

    /**
     * 根据用户消息id及桌面id查找记录
     *
     * @param messageId 消息id
     * @param desktopId 云桌面id
     * @return 记录对象
     */
    UserMessageUserEntity findByMessageIdAndDesktopId(UUID messageId, UUID desktopId);

    /**
     * 根据用户id查询消息记录
     *
     * @param userId 用户id
     * @return 用户消息记录列表
     */
    List<UserMessageUserEntity> findByUserId(UUID userId);

    /**
     * 根据用户id和消息id查询消息记录
     * @param userId 用户id
     * @param messageId 消息id
     * @return 返回
     */
    List<UserMessageUserEntity> findByUserIdAndMessageId(UUID userId, UUID messageId);

    /**
     * 根据云桌面id查询消息记录
     *
     * @param desktopId 云桌面id
     * @return 用户消息记录列表
     */
    List<UserMessageUserEntity> findByDesktopId(UUID desktopId);
}
