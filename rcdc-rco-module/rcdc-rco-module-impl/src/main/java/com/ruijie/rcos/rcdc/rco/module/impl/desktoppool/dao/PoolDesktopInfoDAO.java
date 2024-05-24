package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewPoolDesktopInfoEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 池中云桌面的简要信息试图DAO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/07s
 *
 * @author linke
 */
public interface PoolDesktopInfoDAO extends SkyEngineJpaRepository<ViewPoolDesktopInfoEntity, UUID> {

    /**
     * 根据桌面池ID 查询
     *
     * @param desktopPoolId 桌面池Id
     * @return List<ViewPoolDesktopInfoEntity> 云桌面列表
     */
    List<ViewPoolDesktopInfoEntity> findAllByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池ID列表 查询
     *
     * @param desktopPoolIdList 桌面池Id列表
     * @return List<ViewPoolDesktopInfoEntity> 云桌面列表
     */
    List<ViewPoolDesktopInfoEntity> findAllByDesktopPoolIdIn(List<UUID> desktopPoolIdList);

    /**
     * 根据桌面池ID和用户ID查询池中桌面信息
     *
     * @param desktopPoolId 桌面池ID
     * @param userId        用户ID
     * @return List<ViewPoolDesktopInfoEntity> 云桌面列表
     */
    List<ViewPoolDesktopInfoEntity> findByDesktopPoolIdAndUserId(UUID desktopPoolId, UUID userId);

    /**
     * 根据桌面池ID和用户ID列表查询池中桌面信息
     *
     * @param desktopPoolId 桌面池ID
     * @param userIdList        用户ID列表
     * @return List<ViewPoolDesktopInfoEntity> 云桌面列表
     */
    List<ViewPoolDesktopInfoEntity> findByDesktopPoolIdAndUserIdIn(UUID desktopPoolId, List<UUID> userIdList);

    /**
     * 根据桌面ID和查询池中桌面信息
     *
     * @param deskId 桌面ID
     * @return ViewPoolDesktopInfoEntity 云桌面
     */
    ViewPoolDesktopInfoEntity findByDeskId(UUID deskId);

    /**
     * 根据桌面池ID和用户组ID列表查询池中桌面信息
     *
     * @param desktopPoolId 桌面池ID
     * @param userGroupIdList   用户组ID列表
     * @return List<ViewPoolDesktopInfoEntity> 云桌面列表
     */
    List<ViewPoolDesktopInfoEntity> findByDesktopPoolIdAndUserGroupIdIn(UUID desktopPoolId, List<UUID> userGroupIdList);

    /**
     * 根据桌面的桌面池类型列表和用户ID列表查询池中桌面信息
     *
     * @param desktopPoolTypeList 桌面的桌面池类型列表
     * @param userIdList          用户ID列表
     * @return List<ViewPoolDesktopInfoEntity> 云桌面列表
     */
    List<ViewPoolDesktopInfoEntity> findByDesktopPoolTypeInAndUserIdIn(List<DesktopPoolType> desktopPoolTypeList, List<UUID> userIdList);

    /**
     * 根据桌面ID列表和池类型查询按桌面池ID和用户ID进行分组统计绑定的桌面数量
     *
     * @param desktopIdList 桌面ID
     * @param poolModel     池类型
     * @return List<UserBindDesktopNumDTO>
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO(entity.desktopPoolId, entity.userId, " +
            "count(entity.deskId)) from ViewPoolDesktopInfoEntity entity where entity.deskId in (:deskIds) and entity.poolModel = :poolModel " +
            " and entity.userId is not null GROUP BY entity.desktopPoolId, entity.userId ")
    List<UserBindDesktopNumDTO> findUserBindDesktopNumByDeskIdsAndPoolModel(@Param("deskIds") List<UUID> desktopIdList,
                                                                            @Param("poolModel") CbbDesktopPoolModel poolModel);

    /**
     * 根据桌面池，查询池内已绑定用户的桌面列表
     *
     * @param desktopByPoolId 桌面池ID
     * @return List<ViewPoolDesktopInfoEntity>
     */
    List<ViewPoolDesktopInfoEntity> findByDesktopPoolIdAndUserIdNotNull(UUID desktopByPoolId);

    /**
     * 根据桌面ID列表查询桌面列表
     *
     * @param deskIdList 桌面ID列表
     * @return List<ViewPoolDesktopInfoEntity>
     */
    List<ViewPoolDesktopInfoEntity> findByDeskIdIn(List<UUID> deskIdList);

    /**
     * 根据池类型列表查询池的统计数据
     *
     * @param poolModelList     池类型
     * @return List<Map<String, Object>>
     */
    @Query(value = "select CAST(p1.pool_model AS VARCHAR) pool_model, count(*) as total_num, " +
            "sum(case when t1.desk_state = 'RUNNING' then 1 else 0 end) running_num, " +
            "sum(case when f1.fault_state = true then 1 else 0 end) fault_num, " +
            "sum(case when dt_u.user_id is null and (host_count.user_count = 0 or host_count.user_count is null) then 1 else 0 end) free_num " +
            "from t_cbb_desk_info t1 inner join t_cbb_desktop_pool_info p1 on t1.desktop_pool_id = p1.id " +
            "left join t_rco_user_desktop dt_u on t1.desk_id = dt_u.cbb_desktop_id " +
            "left join t_rco_desk_fault_info f1 on f1.desk_id = t1.desk_id " +
            "left join (select desktop_id, count(*) user_count from t_rco_host_user GROUP BY desktop_id) host_count " +
            "   on host_count.desktop_id = t1.desk_id " +
            "where p1.pool_model in ?1 and t1.is_delete = false " +
            "group by p1.pool_model ", nativeQuery = true)
    List<Map<String, Object>> countPoolOverviewByPoolModel(List<String> poolModelList);

    /**
     * 根据池ID列表查询池的统计数据
     *
     * @param poolIdList 池ID列表
     * @return List<Map < String, Object>>
     */
    @Query(value = "select CAST(p1.pool_model AS VARCHAR) pool_model, count(*) as total_num, " +
            "sum(case when t1.desk_state = 'RUNNING' then 1 else 0 end) running_num, " +
            "sum(case when f1.fault_state = true then 1 else 0 end) fault_num, " +
            "sum(case when dt_u.user_id is null and (host_count.user_count = 0 or host_count.user_count is null) then 1 else 0 end) free_num " +
            "from t_cbb_desk_info t1 inner join t_cbb_desktop_pool_info p1 on t1.desktop_pool_id = p1.id " +
            "left join t_rco_user_desktop dt_u on t1.desk_id = dt_u.cbb_desktop_id " +
            "left join t_rco_desk_fault_info f1 on f1.desk_id = t1.desk_id " +
            "left join (select desktop_id, count(*) user_count from t_rco_host_user GROUP BY desktop_id) host_count " +
            "   on host_count.desktop_id = t1.desk_id " +
            "where p1.id in ?1 and t1.is_delete = false " +
            "group by p1.pool_model ", nativeQuery = true)
    List<Map<String, Object>> countPoolOverviewByPoolId(List<UUID> poolIdList);
}
