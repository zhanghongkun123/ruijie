package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolUserEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 桌面池与用户关系数据接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/18
 *
 * @author gaoxueyuan
 */
public interface DesktopPoolUserDAO extends SkyEngineJpaRepository<DesktopPoolUserEntity, UUID> {

    /**
     * 根据池桌面id查询
     *
     * @param desktopPoolId 池桌面Id
     * @return DesktopPoolUserEntity
     */
    List<DesktopPoolUserEntity> findByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池id和关联Id查询
     *
     * @param desktopPoolId 池桌面Id
     * @param relatedId     关联Id
     * @return DesktopPoolUserEntity
     */
    DesktopPoolUserEntity findByDesktopPoolIdAndRelatedId(UUID desktopPoolId, UUID relatedId);

    /**
     * 根据桌面池id和关联类型查询
     *
     * @param desktopPoolId 池桌面Id
     * @param relatedType   关联类型
     * @return DesktopPoolUserEntity
     */
    List<DesktopPoolUserEntity> findAllByDesktopPoolIdAndRelatedType(UUID desktopPoolId, IacConfigRelatedType relatedType);

    /**
     * 删除指定桌面池id的数据
     *
     * @param desktopPoolId 桌面池id
     */
    @Transactional
    @Modifying
    void deleteByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池ID和对象ID列表删除
     *
     * @param desktopPoolId 桌面池ID
     * @param relatedType   对象类型
     * @param relatedIdList 关联对象ID列表
     */
    @Transactional
    @Modifying
    void deleteByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(UUID desktopPoolId, IacConfigRelatedType relatedType, List<UUID> relatedIdList);

    /**
     * 解除所有与该用户或用户组的绑定的桌面池
     *
     * @param relatedId relatedId
     */
    @Transactional
    @Modifying
    void deleteByRelatedId(UUID relatedId);

    /**
     * 获取指定关联id的所有数据
     *
     * @param relatedId 关联id
     * @return DesktopPoolUserEntity列表
     */
    List<DesktopPoolUserEntity> findAllByRelatedId(UUID relatedId);

    /**
     * 获取指定关联id的所有数据
     *
     * @param relatedIdList 关联id列表
     * @return DesktopPoolUserEntity列表
     */
    List<DesktopPoolUserEntity> findAllByRelatedIdIn(List<UUID> relatedIdList);

    /**
     * 获取用户组下已分配的用户数量
     *
     * @param groupId       用户组ID
     * @param desktopPoolId 桌面池ID
     * @return 用户组下已分配的用户数量
     */
    @Query(value = "select count(pu3.id) assigned_user_num " +
            "from (select pu2.id, pu2.desktop_pool_id, u3.group_id " +
            "      from t_rco_desktop_pool_user pu2 inner join t_base_iac_user u3 on pu2.related_id = u3.id " +
            "       where pu2.related_type = 'USER' and pu2.desktop_pool_id = ?2) pu3 " +
            "where pu3.group_id = ?1 group by pu3.desktop_pool_id, pu3.group_id", nativeQuery = true)
    Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId);

    /**
     * 根据桌面池ID获取关联用户组下已分配的用户数量
     *
     * @param desktopPoolId 桌面池ID
     * @return List<Map < String, Object>> 用户组下已分配的用户数量
     */
    @Query(value = "select CAST(user1.group_id AS VARCHAR) group_id, count(user1.id) assigned_user_num from t_rco_desktop_pool_user pool_user " +
            "           inner join t_base_iac_user user1 on pool_user.related_id = user1.id " +
            "where pool_user.related_type = 'USER' and pool_user.desktop_pool_id = ?1 " +
            "group by user1.group_id", nativeQuery = true)
    List<Map<String, Object>> countAssignedUserNumInGroupByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池ID和对象类型删除
     *
     * @param relatedType   对象类型
     * @param desktopPoolId 桌面池ID
     */
    @Transactional
    @Modifying
    void deleteByRelatedTypeAndDesktopPoolId(IacConfigRelatedType relatedType, UUID desktopPoolId);

    /**
     * 根据桌面池ID和用户组ID获取related_type=USER的对象ID列表
     *
     * @param desktopPoolId   桌面池ID
     * @param userGroupIdList 用户组ID列表
     * @return List<UUID> 用户组下已分配的用户数量
     */
    @Query(value = "select CAST(u1.related_id AS VARCHAR) related_id from t_rco_desktop_pool_user u1 " +
            "   inner join t_base_iac_user u2 on u2.id = u1.related_id " +
            "where u1.related_type = 'USER' and u1.desktop_pool_id = ?1 and u2.group_id in (?2)", nativeQuery = true)
    List<UUID> listUserIdByDesktopPoolIdAndUserGroupIds(UUID desktopPoolId, List<UUID> userGroupIdList);

    /**
     * 查询所有桌面池关联的用户,根据related_id,related_type去重
     *
     * @return 关联用户列表
     */
    @Query(value = "select new DesktopPoolUserEntity(relatedId, relatedType) from DesktopPoolUserEntity group by relatedId,relatedType")
    List<DesktopPoolUserEntity> findAllDistinctByRelatedIdAndRelatedType();

    /**
     * 根据关联ID列表查询数据,根据related_id,related_type去重
     *
     * @param relatedIdList 关联ID列表
     * @param relatedType   关联类型
     * @return 列表
     */
    @Query(value = "select new DesktopPoolUserEntity(relatedId, relatedType) from DesktopPoolUserEntity " +
            "where relatedId in (?1) and relatedType = ?2 group by relatedId,relatedType")
    List<DesktopPoolUserEntity> findAllDistinctByRelatedIdInAndRelatedType(List<UUID> relatedIdList, IacConfigRelatedType relatedType);

    /**
     * 根据桌面池ID查询有分配关系的用户组
     * @param desktopPoolId 桌面池ID
     * @return 用户组集合
     */
    @Query(value = "SELECT DISTINCT CAST(CASE WHEN pu.related_type = 'USERGROUP' THEN ug.id ELSE us.group_id END AS VARCHAR) " +
            "FROM t_rco_desktop_pool_user pu LEFT JOIN t_base_iac_user us ON pu.related_id = us.id LEFT JOIN t_base_iac_user_group ug " +
            "on pu.related_id = ug.id WHERE pu.desktop_pool_id = ?1", nativeQuery = true)
    Set<String> findByDesktopPoolIdToSet(UUID desktopPoolId);

    /**
     * 获取分配关系的数量
     * @param relatedType 关联类型
     * @return 数量
     */
    int countByRelatedType(IacConfigRelatedType relatedType);

    /**
     * 根据关联ID列表查询数据
     * @param relatedIdList 关联ID列表
     * @param relatedType 关联类型
     * @return 列表
     */
    List<DesktopPoolUserEntity> findByRelatedIdInAndRelatedType(List<UUID> relatedIdList, IacConfigRelatedType relatedType);

    /**
     * 根据池ID，关联对象类型，关联对象ID列表查询已存在的关联对象ID列表
     *
     * @param desktopPoolId   池ID
     * @param relatedType     关联对象类型
     * @param inRelatedIdList 关联对象ID列表
     * @return 已存在的关联对象ID列表
     */
    @Query(value = "select relatedId from DesktopPoolUserEntity where desktopPoolId = ?1 and relatedType = ?2 and relatedId in (?3) ")
    List<UUID> findRelatedIdByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(UUID desktopPoolId, IacConfigRelatedType relatedType,
                                                                        List<UUID> inRelatedIdList);


     /**
     * 根据桌面池id和关联Id查询
     *
     * @param relatedId     关联Id
     * @return DesktopPoolUserEntity
     */
    DesktopPoolUserEntity findByRelatedId(UUID relatedId);
}
