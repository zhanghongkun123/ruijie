package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 用户-磁盘-磁盘池-用户组-桌面关联视图操作DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
public interface ViewUserDiskDAO extends SkyEngineJpaRepository<ViewUserDiskEntity, UUID>, PageQueryDAO<ViewUserDiskEntity> {

    /**
     * 统计磁盘池已绑定的磁盘总数
     * 
     * @param diskPoolId 磁盘池ID
     * @return 已绑定的磁盘总数
     */
    int countByDiskPoolIdAndUserIdIsNotNull(UUID diskPoolId);

    /**
     * 统计磁盘池指定状态的磁盘总数
     * 
     * @param diskPoolId 磁盘池ID
     * @param state 磁盘状态
     * @return 磁盘总数
     */
    int countByDiskPoolIdAndDiskState(UUID diskPoolId, DiskStatus state);

    /**
     * 根据用户ID查询绑定的磁盘信息
     * 
     * @param userId 用户信息
     * @return ViewDiskPoolUserDiskEntity
     */
    ViewUserDiskEntity findByUserId(UUID userId);

    /**
     * 根据磁盘ID查询绑定的磁盘信息
     *
     * @param diskId 磁盘ID
     * @return ViewDiskPoolUserDiskEntity
     */
    ViewUserDiskEntity findByDiskId(UUID diskId);

    /**
     * 根据磁盘池ID列表查询磁盘
     * 
     * @param diskPoolIdList 磁盘池ID列表
     * @return 磁盘列表
     */
    List<ViewUserDiskEntity> findAllByDiskPoolIdIn(List<UUID> diskPoolIdList);

    /**
     * 根据桌面ID查询绑定的磁盘信息
     *
     * @param deskId 桌面ID
     * @return List<ViewUserDiskEntity>
     */
    List<ViewUserDiskEntity> findByDeskId(UUID deskId);

    /**
     * 根据池ID和组ID列表查询绑定的磁盘信息
     *
     * @param diskPoolId 池ID
     * @param groupIdList 组ID列表
     * @return List<ViewUserDiskEntity>
     */
    List<ViewUserDiskEntity> findByDiskPoolIdAndGroupIdIn(UUID diskPoolId, List<UUID> groupIdList);

    /**
     * 根据池ID和用户ID列表查询绑定的磁盘信息
     *
     * @param diskPoolId 池ID
     * @param userIdList 用户ID列表
     * @return List<ViewUserDiskEntity>
     */
    List<ViewUserDiskEntity> findByDiskPoolIdAndUserIdIn(UUID diskPoolId, List<UUID> userIdList);

    /**
     * 以磁盘池分组，统计POOL_DATA类型全部磁盘在使用中的磁盘数量
     * @return 以磁盘池分组，统计磁盘在使用中状态的数量集合
     */
    @Query(value = "SELECT CAST(disk_pool_id AS VARCHAR), sum(1) AS in_use_num FROM v_rco_user_disk WHERE disk_pool_type = 'POOL' "
            + "AND disk_state = 'IN_USE' GROUP BY disk_pool_id", nativeQuery = true)
    List<Map<String, Object>> countInUseDiskNumAll();

    /**
     * 以磁盘池分组，统计POOL_DATA类型已分配给用户的磁盘数量
     * @return 以磁盘池分组，统计磁盘已分配给用户的数量集合
     */
    @Query(value = "SELECT CAST(disk_pool_id AS VARCHAR), sum(1) as assign_num FROM v_rco_user_disk where disk_pool_type = 'POOL' " +
            "AND user_id is not null GROUP BY disk_pool_id", nativeQuery = true)
    List<Map<String, Object>> countAssignedDiskNumAll();

    /**
     * 查询指定磁盘类型处于关机的云桌面磁盘
     * @param type 磁盘池类型
     * @param desktopState 云桌面状态
     * @return 磁盘集合
     */
    List<ViewUserDiskEntity> findByDiskPoolTypeAndDesktopState(DiskPoolType type, CbbCloudDeskState desktopState);

    /**
     * 以磁盘池分组，统计POOL_DATA类型全部磁盘数量
     * @return 以磁盘池分组，统计全部磁盘数量集合
     */
    @Query(value = "SELECT CAST(disk_pool_id AS VARCHAR), sum(1) as all_num FROM v_rco_user_disk where disk_pool_type = 'POOL' " +
            "GROUP BY disk_pool_id", nativeQuery = true)
    List<Map<String, Object>> countAllDiskNum();

    /**
     * 查询POOL_DATA类型磁盘已绑定用户的集合
     * @param diskPoolType 磁盘池类型
     * @return 用户集合
     */
    @Query(value = "SELECT CAST(user_id AS VARCHAR) FROM v_rco_user_disk WHERE disk_pool_type = 'POOL' AND user_id is not null", nativeQuery =
            true)
    Set<String> queryAllByDiskPoolTypeToSet(DiskPoolType diskPoolType);

    /**
     * 查询指定磁盘池，以组进行统计绑定磁盘的数量
     * @param diskPoolId 磁盘池ID
     * @return 统计结果：用户组-绑定磁盘数量
     */
    @Query(value = "SELECT CAST(group_id AS VARCHAR), count(1) as bind_disk_num FROM v_rco_user_disk " +
            "WHERE disk_pool_id = ?1 and disk_pool_type = 'POOL' GROUP BY group_id HAVING group_id IS NOT NULL", nativeQuery = true)
    List<Map<String, Object>> countGroupBindDiskNumByDiskPoolId(UUID diskPoolId);
    
    /**
     * 查询存储信息为空的磁盘列表
     *
     * @return 磁盘列表
     */
    List<ViewUserDiskEntity> findByAssignStoragePoolIdsIsNull();
}
