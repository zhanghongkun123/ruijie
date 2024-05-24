package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathMainEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户配置路径DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/12
 *
 * @author WuShengQiang
 */
public interface UserProfileMainPathDAO extends SkyEngineJpaRepository<UserProfilePathMainEntity, UUID> {


    /**
     * 根据路径ID列表,批量移动路径分组
     *
     * @param idArr         路径id列表
     * @param targetGroupId 组id
     */
    @Modifying()
    @Transactional
    @Query("update UserProfilePathMainEntity o set o.groupId = :targetGroupId, update_time = now(), version = version + 1 " +
            "where (o.id in :ids) and version = version")
    void updateUserProfilePathGroupId(@Param("ids") Iterable<UUID> idArr, @Param("targetGroupId") UUID targetGroupId);

    /**
     * 根据组ID查找路径列表
     *
     * @param groupId 组ID
     * @return 路径列表
     */
    List<UserProfilePathMainEntity> findByGroupId(UUID groupId);

    /**
     * 根据id列表查询路径对象
     *
     * @param ids 路径id列表
     * @return 路径对象列表
     */
    List<UserProfilePathMainEntity> findByIdIn(List<UUID> ids);

    /**
     * 根据名称查询路径对象
     *
     * @param name 路径名称
     * @return 路径对象
     */
    UserProfilePathMainEntity findByName(String name);

    /**
     * 根据组ID和排序规则查询路径列表
     *
     * @param id   组ID
     * @param sort 排序规则
     * @return 路径列表
     */
    List<UserProfilePathMainEntity> findByGroupId(UUID id, Sort sort);

    /**
     * 根据ID组查询
     *
     * @param idArr ID组
     * @return 查询结果
     */
    @Query("select id from UserProfilePathMainEntity where id in ?1")
    List<UUID> getUserProfilePathIdByIdIn(UUID[] idArr);

    /**
     * 根据ID查找配置名
     *
     * @param id 配置ID
     * @return 配置名
     */
    @Query("select name from  UserProfilePathMainEntity where id = ?1")
    String findNameById(UUID id);
}