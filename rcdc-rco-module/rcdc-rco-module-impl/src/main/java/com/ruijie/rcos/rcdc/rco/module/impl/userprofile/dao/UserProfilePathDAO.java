package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 路径DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
public interface UserProfilePathDAO extends SkyEngineJpaRepository<UserProfilePathEntity, UUID> {
    /**
     * 删除对应路径下的子路径对象
     *
     * @param userProfilePathId 路径ID
     */
    @Modifying
    @Transactional
    void deleteByUserProfilePathId(UUID userProfilePathId);

    /**
     * 查询对应路径下的子路径对象
     *
     * @param userProfilePathId 路径ID
     * @return 子路径列表
     */
    List<UserProfilePathEntity> findByUserProfilePathId(UUID userProfilePathId);

    /**
     * 查询对应子路径类型下的子路径
     *
     * @param userProfileChildPathId 子路径类型ID
     * @return 子路径列表
     */
    List<UserProfilePathEntity> findByUserProfileChildPathId(UUID userProfileChildPathId);

    /**
     * 统计对应路径ID的子路径数目
     *
     * @param userProfilePathIdArr 路径ID数组
     * @return 子路径数目
     */
    @Query("select count(id) from UserProfilePathEntity where userProfilePathId in ?1")
    int countByUserProfilePathIdArr(UUID[] userProfilePathIdArr);

    /**
     * 统计某一个性化配置下子路径的个数
     *
     * @param userProfilePathId 个性化配置ID
     * @return 个数
     */
    @Query("select count(id) from UserProfilePathEntity where userProfilePathId = ?1")
    int countByUserProfilePathId(UUID userProfilePathId);

    /**
     * 统计某一个性化子配置下子路径的个数
     *
     * @param userProfileChildPathId 个性化子配置ID
     * @return 个数
     */
    @Query("select count(id) from UserProfilePathEntity where userProfileChildPathId = ?1")
    int countByUserProfileChildPathId(UUID userProfileChildPathId);
}
