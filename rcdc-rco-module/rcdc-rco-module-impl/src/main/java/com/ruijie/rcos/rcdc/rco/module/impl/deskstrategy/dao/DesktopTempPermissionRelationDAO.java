package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dao;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.entity.DesktopTempPermissionRelationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 对象与临时权限关联记录DAO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/27
 *
 * @author linke
 */
public interface DesktopTempPermissionRelationDAO extends SkyEngineJpaRepository<DesktopTempPermissionRelationEntity, UUID> {

    /**
     * 根据临时权限ID获取关联对象ID和对象名称
     *
     * @param desktopTempPermissionId 临时权限ID
     * @return List<Map<String, Object>> 对象信息
     */
    @Query(value = "select CAST(t.related_id AS VARCHAR) related_id, t.related_type, t.has_send_expire_notice, " +
            "case when t.related_type = 'USER' then u.name " +
            "   when t.related_type = 'DESKTOP' then desk.name else null end as related_name " +
            "from t_rco_desktop_temp_permission_relation t  " +
            "   left join t_base_iac_user u on (t.related_type = 'USER' and u.id = t.related_id) " +
            "   left join t_cbb_desk_info desk on (t.related_type = 'DESKTOP' and desk.desk_id = t.related_id) " +
            "where t.desktop_temp_permission_id = ?1 ", nativeQuery = true)
    List<Map<String, Object>> findRelatedInfoByDesktopTempPermissionId(UUID desktopTempPermissionId);

    /**
     * 根据临时权限ID删除关联关系记录
     *
     * @param desktopTempPermissionId 临时权限ID
     */
    @Transactional
    @Modifying
    void deleteByDesktopTempPermissionId(UUID desktopTempPermissionId);

    /**
     * 根据ID列表删除关联关系记录
     *
     * @param idList ID列表
     */
    @Transactional
    @Modifying
    void deleteByIdIn(List<UUID> idList);

    /**
     * 根据云桌面ID列表和桌面类型查询桌面数量
     *
     * @param idList          云桌面ID列表
     * @param desktopPoolTypeList 类型
     * @return 桌面数
     */
    @Query(value = "select count(*) from t_cbb_desk_info t where t.desk_id in (?1) and t.desktop_pool_type in (?2) ", nativeQuery = true)
    Long countDesktopNumByIdAndDesktopPoolType(List<UUID> idList, List<String> desktopPoolTypeList);

    /**
     * 根据对象id和类型查询临时权限
     *
     * @param relatedIdList 对象id列表
     * @param type          类型
     * @return List<DesktopTempPermissionRelationEntity>
     */
    List<DesktopTempPermissionRelationEntity> findByRelatedIdInAndRelatedType(List<UUID> relatedIdList, DesktopTempPermissionRelatedType type);

    /**
     * 根据用户id、临时权限ID和桌面状态查询关联的桌面ID
     *
     * @param permissionId 临时权限ID
     * @param deskState    桌面状态
     * @return List<UUID>
     */
    @Query(value = "select CAST(t3.desk_id AS VARCHAR) desktop_id " +
            "from t_rco_desktop_temp_permission_relation t " +
            "left join t_rco_user_desktop t2 on (t.related_type = 'USER' and t2.user_id = t.related_id) " +
            "left join t_cbb_desk_info t3 on t2.cbb_desktop_id = t3.desk_id " +
            "where t.desktop_temp_permission_id = ?1 and t3.desk_state = ?2 ", nativeQuery = true)
    List<UUID> findDesktopIdByRelatedUserAndDeskState(UUID permissionId, String deskState);

    /**
     * 根据用户id、临时权限ID和桌面状态查询关联的多会话桌面ID
     *
     * @param permissionId 临时权限ID
     * @param deskState    桌面状态
     * @return List<UUID>
     */
    @Query(value = "select CAST(t3.desk_id AS VARCHAR) desktop_id " +
            "from t_rco_desktop_temp_permission_relation t " +
            "inner join t_rco_host_user t2 on (t.related_type = 'USER' and t2.user_id = t.related_id) " +
            "inner join t_cbb_desk_info t3 on t2.desktop_id = t3.desk_id " +
            "where t.desktop_temp_permission_id = ?1 and t3.desk_state = ?2 ", nativeQuery = true)
    List<UUID> findMultiDeskIdByRelatedUserAndDeskState(UUID permissionId, String deskState);

    /**
     * 根据临时权限ID和桌面状态查询关联的桌面ID
     *
     * @param permissionId 临时权限ID
     * @param deskState    桌面状态
     * @return List<UUID>
     */
    @Query(value = "select CAST(t2.desk_id AS VARCHAR) desktop_id " +
            "from t_rco_desktop_temp_permission_relation t " +
            "inner join t_cbb_desk_info t2 on (t.related_type = 'DESKTOP' and t2.desk_id = t.related_id) " +
            "where t.desktop_temp_permission_id = ?1 and t2.desk_state = ?2 ", nativeQuery = true)
    List<UUID> findDesktopIdByRelatedDesktopAndState(UUID permissionId, String deskState);

    /**
     * 根据对象id、类型和临时权限状态查询临时权限ID列表
     *
     * @param relatedId 对象id
     * @param relatedType      类型
     * @param state     临时权限状态
     * @return List<UUID>  临时权限ID列表
     */
    @Query(value = "select CAST(t.id AS VARCHAR) id " +
            "from t_cbb_desktop_temp_permission t left join t_rco_desktop_temp_permission_relation t2 on t.id = t2.desktop_temp_permission_id " +
            "where t2.related_id = ?1 and t2.related_type = ?2 and t.state = ?3 ", nativeQuery = true)
    List<UUID> findPermissionIdByRelatedObjAndState(UUID relatedId, String relatedType, String state);

    /**
     * 根据临时权限ID，对象为桌面类型，查询这些桌面对应的用户ID列表
     *
     * @param desktopTempPermissionId 临时权限ID
     * @return List<UUID>  用户ID列表
     */
    @Query(value = "select CAST(t2.user_id AS VARCHAR) user_id from t_rco_desktop_temp_permission_relation t1 " +
            "left join t_rco_user_desktop t2 on (t1.related_type = 'DESKTOP' and t1.related_id = t2.cbb_desktop_id) " +
            "where t1.desktop_temp_permission_id = ?1 ", nativeQuery = true)
    List<UUID> findRelatedDesktopUserIdByPermissionId(UUID desktopTempPermissionId);

    /**
     * 根据临时权限ID，对象为桌面类型，查询这些多会话桌面对应的用户ID列表
     *
     * @param desktopTempPermissionId 临时权限ID
     * @return List<UUID>  用户ID列表
     */
    @Query(value = "select CAST(t2.user_id AS VARCHAR) user_id from t_rco_desktop_temp_permission_relation t1 " +
            "left join t_rco_host_user t2 on (t1.related_type = 'DESKTOP' and t1.related_id = t2.desktop_id) " +
            "where t1.desktop_temp_permission_id = ?1 ", nativeQuery = true)
    List<UUID> findRelatedMultiDesktopUserIdByPermissionId(UUID desktopTempPermissionId);

    /**
     * 根据临时权限ID，对象类型，查询记录
     *
     * @param desktopTempPermissionId 临时权限ID
     * @param relatedType             对象类型
     * @return List<DesktopTempPermissionRelationEntity>  记录列表
     */
    List<DesktopTempPermissionRelationEntity> findByDesktopTempPermissionIdAndRelatedType(UUID desktopTempPermissionId,
                                                                                          DesktopTempPermissionRelatedType relatedType);

    /**
     * 根据临时权限ID，修改hasSendExpireNotice
     *
     * @param permissionId        对象id
     * @param hasSendExpireNotice hasSendExpireNotice
     */
    @Modifying
    @Transactional
    @Query("UPDATE DesktopTempPermissionRelationEntity SET hasSendExpireNotice = ?2, version = version + 1 WHERE "
            + "desktopTempPermissionId = ?1  and version = version")
    void updateHasSendExpireNotice(UUID permissionId, Boolean hasSendExpireNotice);


    /**
     * 删除关联临时权限
     *
     * @param relatedId   relatedId
     * @param relatedType relatedType
     * @return 删除梳理
     */
    @Transactional
    @Modifying
    int deleteByRelatedIdAndRelatedType(UUID relatedId, DesktopTempPermissionRelatedType relatedType);

}
