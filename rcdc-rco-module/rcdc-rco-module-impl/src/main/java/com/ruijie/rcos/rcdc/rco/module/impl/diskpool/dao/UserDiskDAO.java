package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.UserDiskEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description: 磁盘与用户关系数据接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author TD
 */
public interface UserDiskDAO extends SkyEngineJpaRepository<UserDiskEntity, UUID>, PageQueryDAO<UserDiskEntity> {

    /**
     * 删除指定磁盘id的数据
     *
     * @param diskId 磁盘id
     */
    @Transactional
    @Modifying
    void deleteByDiskId(UUID diskId);

    /**
     * 根据磁盘ID查询指定关联用户信息
     *
     * @param diskId 磁盘ID
     * @return 用户磁盘关系
     */
    UserDiskEntity findByDiskId(UUID diskId);

    /**
     * 根据用户ID查询指定关联用户信息
     *
     * @param userId 用户ID
     * @return List<UserDiskEntity> 用户磁盘关系
     */
    List<UserDiskEntity> findByUserId(UUID userId);

    /**
     * 根据用户ID，查询diskId
     *
     * @param userId 用户ID
     * @return diskId列表
     */
    @Query("select diskId from UserDiskEntity where userId = ?1")
    List<UUID> findDiskIdByUserId(UUID userId);
}
