package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileChildPathEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 子路径类型DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/28
 *
 * @author zwf
 */
public interface UserProfileChildPathDAO extends SkyEngineJpaRepository<UserProfileChildPathEntity, UUID> {
    /**
     * 根据路径ID删除对应的子路径类型
     *
     * @param userProfilePathId 路径ID
     */
    @Modifying
    @Transactional
    void deleteByUserProfilePathId(UUID userProfilePathId);

    /**
     * 获取路径ID对应的子路径类型列表
     *
     * @param userProfilePathId 路径ID
     * @return 子路径类型列表
     */
    List<UserProfileChildPathEntity> findByUserProfilePathIdOrderByIndex(UUID userProfilePathId);

    /**
     * 获取路径ID对应的子路径类型列表
     *
     * @param userProfilePathId 路径ID
     * @return 子路径类型列表
     */
    List<UserProfileChildPathEntity> findByUserProfilePathId(UUID userProfilePathId);
}
