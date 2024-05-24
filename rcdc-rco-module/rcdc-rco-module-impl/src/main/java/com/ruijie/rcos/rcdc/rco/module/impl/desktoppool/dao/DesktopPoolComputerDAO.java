package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolComputerEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 桌面池与PC终端关系表
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
public interface DesktopPoolComputerDAO extends SkyEngineJpaRepository<DesktopPoolComputerEntity, UUID> {

    /**
     * 根据池桌面id查询
     *
     * @param desktopPoolId 池桌面Id
     * @return DesktopPoolComputerEntity
     */
    List<DesktopPoolComputerEntity> findByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池id和关联Id查询
     *
     * @param desktopPoolId 池桌面Id
     * @param relatedId     关联Id
     * @return DesktopPoolComputerEntity
     */
    DesktopPoolComputerEntity findByDesktopPoolIdAndRelatedId(UUID desktopPoolId, UUID relatedId);

    /**
     * 根据桌面池id和关联类型查询
     *
     * @param desktopPoolId 池桌面Id
     * @param relatedType   关联类型
     * @return DesktopPoolComputerEntity
     */
    List<DesktopPoolComputerEntity> findAllByDesktopPoolIdAndRelatedType(UUID desktopPoolId, ComputerRelatedType relatedType);

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
    void deleteByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(UUID desktopPoolId, ComputerRelatedType relatedType, List<UUID> relatedIdList);

    /**
     * 解除所有与该PC终端或PC终端组的绑定的桌面池
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
     * @return DesktopPoolComputerEntity列表
     */
    List<DesktopPoolComputerEntity> findAllByRelatedId(UUID relatedId);

    /**
     * 获取用户组下已分配的用户数量
     *
     * @param groupId       用户组ID
     * @param desktopPoolId 桌面池ID
     * @return 用户组下已分配的用户数量
     */
    @Query(value = "select count(pu3.id) assigned_user_num " +
            "from (select pu2.id, pu2.desktop_pool_id, u3.group_id " +
            "      from t_rco_desktop_pool_computer pu2 inner join t_cbb_user u3 on pu2.related_id = u3.id " +
            "       where pu2.related_type = 'USER' and pu2.desktop_pool_id = ?2) pu3 " +
            "where pu3.group_id = ?1 group by pu3.desktop_pool_id, pu3.group_id", nativeQuery = true)
    Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId);

    /**
     * 根据桌面池ID获取关联用户组下已分配的用户数量
     *
     * @param desktopPoolId 桌面池ID
     * @return List<Map < String, Object>> 用户组下已分配的用户数量
     */
    @Query(value = "select CAST(user1.group_id AS VARCHAR) group_id, count(user1.id) assigned_user_num from t_rco_desktop_pool_computer pool_user " +
            "           inner join t_cbb_user user1 on pool_user.related_id = user1.id " +
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
    void deleteByRelatedTypeAndDesktopPoolId(ComputerRelatedType relatedType, UUID desktopPoolId);

    /**
     * 根据桌面池ID和用户组ID获取related_type=USER的对象ID列表
     *
     * @param desktopPoolId   桌面池ID
     * @param userGroupIdList 用户组ID列表
     * @return List<UUID> 用户组下已分配的用户数量
     */
    @Query(value = "select CAST(u1.related_id AS VARCHAR) related_id from t_rco_desktop_pool_computer u1 " +
            "   inner join t_cbb_user u2 on u2.id = u1.related_id " +
            "where u1.related_type = 'USER' and u1.desktop_pool_id = ?1 and u2.group_id in (?2)", nativeQuery = true)
    List<UUID> listUserIdByDesktopPoolIdAndUserGroupIds(UUID desktopPoolId, List<UUID> userGroupIdList);

    /**
     * 查询所有桌面池关联的用户,根据related_id,related_type去重
     *
     * @return 关联用户列表
     */
    @Query(value = "select new DesktopPoolComputerEntity(relatedId, relatedType) from DesktopPoolComputerEntity group by relatedId,relatedType")
    List<DesktopPoolComputerEntity> findAllDistinctByRelatedIdAndRelatedType();

    /**
     * 根据关联ID列表查询数据,根据related_id,related_type去重
     *
     * @param relatedIdList 关联ID列表
     * @param relatedType   关联类型
     * @return 列表
     */
    @Query(value = "select new DesktopPoolComputerEntity(relatedId, relatedType) from DesktopPoolComputerEntity " +
            "where relatedId in (?1) and relatedType = ?2 group by relatedId,relatedType")
    List<DesktopPoolComputerEntity> findAllDistinctByRelatedIdInAndRelatedType(List<UUID> relatedIdList, ComputerRelatedType relatedType);

    /**
     * 根据桌面池ID查询有分配关系的终端
     *
     * @param desktopPoolId 桌面池ID
     * @param relatedType relatedType
     * @return 终端组集合
     */
    List<DesktopPoolComputerEntity> findByDesktopPoolIdAndRelatedType(UUID desktopPoolId, ComputerRelatedType relatedType);



    /**
     * 根据池ID，关联对象类型，关联对象ID列表查询已存在的关联对象ID列表
     *
     * @param desktopPoolId   池ID
     * @param relatedType     关联对象类型
     * @param inRelatedIdList 关联对象ID列表
     * @return 已存在的关联对象ID列表
     */
    @Query(value = "select relatedId from DesktopPoolComputerEntity where desktopPoolId = ?1 and relatedType = ?2 and relatedId in (?3) ")
    List<UUID> findRelatedIdByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(UUID desktopPoolId, ComputerRelatedType relatedType,
                                                                        List<UUID> inRelatedIdList);



    /**
     * 获取桌面池下已分配的pc终端数量
     *
     * @param computerIdArr   终端ID数组
     * @param desktopPoolId 桌面池ID
     * @return 用户组下已分配的用户数量
     */
    @Query(value = "select count(id) assigned_num " +
            "from t_rco_desktop_pool_computer where related_type = 'COMPUTER' and desktop_pool_id = ?2 and related_id in (?1)"
            , nativeQuery = true)
    int countAssignedNumInComputerByDesktopPoolId(UUID[] computerIdArr, UUID desktopPoolId);

    /**
     * 获取桌面池下已分配的pc终端数量
     *
     * @param computerIdArr   终端ID数组
     * @return 用户组下已分配的用户数量
     */
    List<DesktopPoolComputerEntity> findByRelatedIdIn(List<UUID> computerIdArr);


    /**
     * 获取实体
     * @param relatedId relatedId
     * @return DesktopPoolComputerEntity
     */
    DesktopPoolComputerEntity findByRelatedId(UUID relatedId);

    /**
     * 获取列表
     * @param computerRelatedType  computerRelatedType
     * @return 列表
     */
    List<DesktopPoolComputerEntity> findByRelatedType(ComputerRelatedType computerRelatedType);

    /**
     * 解除桌面池与PC终端关系
     * @param poolId poolId
     * @param relatedId relatedId
     */
    @Transactional
    @Modifying
    void deleteByDesktopPoolIdAndRelatedId(UUID poolId, UUID relatedId);
}

