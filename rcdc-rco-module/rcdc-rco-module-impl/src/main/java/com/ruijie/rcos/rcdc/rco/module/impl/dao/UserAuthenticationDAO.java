package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserAuthenticationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 *
 * Description: 用户拓展信息持久化接口.
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月24日
 *
 * @author linke
 */
public interface UserAuthenticationDAO extends SkyEngineJpaRepository<UserAuthenticationEntity, UUID> {

    /**
     * 根据用户ID查询拓展信息
     *
     * @param userId 用户ID
     * @return UserExtEntity
     */
    UserAuthenticationEntity findByUserId(UUID userId);

    /**
     * 根据用户ID删除
     *
     * @param userId 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserId(UUID userId);

    /**
     * 根据用户isLock查询拓展信息
     *
     * @param isLock isLock
     * @return List<UserAuthenticationEntity>
     */
    List<UserAuthenticationEntity> findByIsLock(Boolean isLock);

    /**
     * 根据用户ID列表查询用户拓展信息列表
     *
     * @param userIdList 用户ID列表
     * @return List<UserAuthenticationEntity>
     */
    List<UserAuthenticationEntity> findByUserIdIn(List<UUID> userIdList);
}
