package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.DiskPoolUserEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 磁盘池分配用户关系数据操作
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author TD
 */
public interface DiskPoolUserDAO extends SkyEngineJpaRepository<DiskPoolUserEntity, UUID> {

    /**
     * 删除指定磁盘池的数据
     * @param diskPoolId 磁盘池id
     */
    @Transactional
    @Modifying
    void deleteByDiskPoolId(UUID diskPoolId);

    /**
     * 根据磁盘池id查询
     *
     * @param diskPoolId 磁盘池Id
     * @return List<DiskPoolUserEntity>
     */
    List<DiskPoolUserEntity> findByDiskPoolId(UUID diskPoolId);

    /**
     * 根据磁盘池id和关联Id查询
     *
     * @param diskPoolId 磁盘池Id
     * @param relatedId 关联Id
     * @return DesktopPoolUserEntity
     */
    DiskPoolUserEntity findByDiskPoolIdAndRelatedId(UUID diskPoolId, UUID relatedId);

    /**
     * 根据磁盘池id和关联类型查询
     *
     * @param diskPoolId 磁盘池Id
     * @param relatedType 关联类型
     * @return List<DiskPoolUserEntity>
     */
    List<DiskPoolUserEntity> findAllByDiskPoolIdAndRelatedType(UUID diskPoolId, IacConfigRelatedType relatedType);

    /**
     * 根据关联对象ID查询磁盘池和用户关系
     * @param relatedId 关联对象ID
     * @return List<DiskPoolUserEntity>
     */
    List<DiskPoolUserEntity> findByRelatedId(UUID relatedId);

    /**
     * 根据关联对象ID列表查询磁盘池和用户关系
     * @param relatedIdList 对象ID列表
     * @return List<DiskPoolUserEntity>
     */
    List<DiskPoolUserEntity> findByRelatedIdIn(List<UUID> relatedIdList);

    /**
     * 删除指定关联ID的数据
     * @param relatedId 关联id
     */
    @Transactional
    @Modifying
    void deleteByRelatedId(UUID relatedId);

    /**
     * 根据池ID和用户组ID 获取关联用户组下已分配的用户数量
     *
     * @param groupId 用户组ID
     * @param diskPoolId 池ID
     * @return List<Map<String, Object>> 用户组下已分配的用户数量
     */
    @Query(value = "select count(pu3.id) assigned_user_num " +
            "from (select pu2.id, pu2.disk_pool_id, u3.group_id " +
            "      from t_rco_disk_pool_user pu2 inner join t_base_iac_user u3 on pu2.related_id = u3.id " +
            "      where pu2.related_type = 'USER' and pu2.disk_pool_id = ?2) pu3 " +
            "where pu3.group_id = ?1 group by pu3.disk_pool_id, pu3.group_id", nativeQuery = true)
    Integer countAssignedUserNumByGroup(UUID groupId, UUID diskPoolId);

    /**
     * 根据池ID获取关联用户组下已分配的用户数量
     *
     * @param diskPoolId 池ID
     * @return List<Map<String, Object>> 用户组下已分配的用户数量
     */
    @Query(value = "select CAST(user1.group_id AS VARCHAR) group_id, count(user1.id) assigned_user_num " +
            "from t_rco_disk_pool_user pool_user inner join t_base_iac_user user1 on pool_user.related_id = user1.id " +
            "where pool_user.related_type = 'USER' and pool_user.disk_pool_id = ?1 " +
            "group by user1.group_id", nativeQuery = true)
    List<Map<String, Object>> countAssignedUserNumInGroupByDesktopPoolId(UUID diskPoolId);

    /**
     * 根据池ID获取关联用户组下不可选的用户数量
     *
     * @param diskPoolId 池ID
     * @return List<Map<String, Object>> 用户组下不可选的用户数量
     */
    @Query(value = "select CAST(user1.group_id AS VARCHAR) group_id, count(user1.id) disabled_user_num " +
            "from t_rco_disk_pool_user pool_user inner join t_base_iac_user user1 on pool_user.related_id = user1.id " +
            "where pool_user.related_type = 'USER' and pool_user.disk_pool_id != ?1 " +
            "group by user1.group_id", nativeQuery = true)
    List<Map<String, Object>> countDisabledUserNumInGroupByDiskPoolId(UUID diskPoolId);

    /**
     * 根据关联类型查询
     *
     * @param relatedType 关联类型
     * @return List<DiskPoolUserEntity>
     */
    List<DiskPoolUserEntity> findByRelatedType(IacConfigRelatedType relatedType);

    /**
     * 根据池ID和用户组ID获取related_type=USER的对象ID列表
     *
     * @param diskPoolId 池ID
     * @param userGroupIdList 用户组ID列表
     * @return List<UUID> 用户组下已分配的用户数量
     */
    @Query(value = "select CAST(u1.related_id AS VARCHAR) related_id from t_rco_disk_pool_user u1 " +
            "   inner join t_base_iac_user u2 on u2.id = u1.related_id " +
            "where u1.related_type = 'USER' and u1.disk_pool_id = ?1 and u2.group_id in (?2)", nativeQuery = true)
    List<UUID> listUserIdByDiskPoolIdAndUserGroupIds(UUID diskPoolId, List<UUID> userGroupIdList);

    /**
     * 根据池ID和对象ID列表删除
     *
     * @param diskPoolId 池ID
     * @param relatedType   对象类型
     * @param relatedIdList  关联对象ID列表
     */
    @Transactional
    @Modifying
    void deleteByDiskPoolIdAndRelatedTypeAndRelatedIdIn(UUID diskPoolId, IacConfigRelatedType relatedType, List<UUID> relatedIdList);

    /**
     * 根据池ID和对象类型删除
     *
     * @param relatedType 对象类型
     * @param diskPoolId  池ID
     */
    @Transactional
    @Modifying
    void deleteByRelatedTypeAndDiskPoolId(IacConfigRelatedType relatedType, UUID diskPoolId);

    /**
     * 根据池ID和对象ID及类型查询不在这个池中的绑定对象
     *
     * @param poolId 池ID
     * @param relatedIdList relatedIdList
     * @param relatedType relatedType
     * @return List<DiskPoolUserEntity>
     */
    List<DiskPoolUserEntity> findByDiskPoolIdNotAndRelatedIdInAndRelatedType(UUID poolId, List<UUID> relatedIdList,
            IacConfigRelatedType relatedType);

    /**
     * 计算非此磁盘池的已选用户的数据
     * @param diskPoolId 磁盘池ID
     * @param relatedType 用户类型
     * @return 用户的数据
     */
    List<DiskPoolUserEntity> findByDiskPoolIdNotAndRelatedType(UUID diskPoolId, IacConfigRelatedType relatedType);

    /**
     * 计算非此磁盘池的已选的数量
     *
     * @param diskPoolId  diskPoolId
     * @param relatedType relatedType
     * @return 数量
     */
    long countByDiskPoolIdNotAndRelatedType(UUID diskPoolId, IacConfigRelatedType relatedType);

    /**
     * 根据用户组ID和磁盘池ID，这个组内被其他磁盘池的已选的用户数量
     *
     * @param diskPoolId  diskPoolId
     * @param groupId groupId
     * @return 数量
     */
    @Query(value = "select count(puser.id) from t_rco_disk_pool_user puser inner join t_base_iac_user u1 on puser.related_id = u1.id " +
            "where u1.group_id = ?2 and puser.disk_pool_id != ?1 and puser.related_type ='USER'", nativeQuery = true)
    long countByDiskPoolIdNotAndUserGroupId(UUID diskPoolId, UUID groupId);

    /**
     * 根据磁盘池ID查询有分配关系的用户组
     * @param diskPoolId 磁盘池ID
     * @return 用户组集合
     */
    @Query(value = "SELECT DISTINCT CAST(CASE WHEN pu.related_type = 'USERGROUP' THEN ug.id ELSE us.group_id END AS VARCHAR) FROM " +
            "t_rco_disk_pool_user pu LEFT JOIN t_base_iac_user us ON pu.related_id = us.id LEFT JOIN t_base_iac_user_group ug " +
            "on pu.related_id = ug.id WHERE pu.disk_pool_id = ?1", nativeQuery = true)
    Set<String> findByDiskPoolIdToSet(UUID diskPoolId);


    /**
     * 根据关联对象ID集合及类型查询磁盘池关联关系
     *
     * @param relatedIdList relatedIdList
     * @param relatedType relatedType
     * @return List<DiskPoolUserEntity>
     */
    List<DiskPoolUserEntity> findByRelatedIdInAndRelatedType(List<UUID> relatedIdList, IacConfigRelatedType relatedType);
}
