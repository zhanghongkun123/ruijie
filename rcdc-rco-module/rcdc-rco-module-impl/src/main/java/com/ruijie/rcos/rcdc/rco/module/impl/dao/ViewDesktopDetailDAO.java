package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.common.query.RcdcJpaRepository;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CountCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopSoftwareNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopStateNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description: 查询云桌面明细视图
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @author
 */
public interface ViewDesktopDetailDAO extends RcdcJpaRepository<ViewUserDesktopEntity, UUID>, PageQueryDAO<ViewUserDesktopEntity> {


    /**
     * 根据桌面id查询桌面视图
     *
     * @param cbbDesktopId cbbDesktopId
     * @return ViewUserDesktopEntity
     */
    ViewUserDesktopEntity findByCbbDesktopId(UUID cbbDesktopId);

    /**
     * * 根据是否删除标记查询
     *
     * @param isDelete 是否删除标记
     * @return view list
     */
    List<ViewUserDesktopEntity> findByIsDelete(boolean isDelete);


    /**
     * * 根据user id查询
     *
     * @param userId user id
     * @param isDelete is moveDesktopToRecycleBin
     * @return view list
     */
    List<ViewUserDesktopEntity> findByUserIdAndIsDelete(UUID userId, boolean isDelete);



    /**
     * * 根据桌面ID数组 桌面状态 是否删除查询
     *
     * @param idList idList
     * @param isDelete is moveDesktopToRecycleBin
     * @param deskState is 桌面状态
     * @param cbbImageType is 镜像类型
     * @return view list
     */
    List<ViewUserDesktopEntity> findBycbbDesktopIdInAndIsDeleteAndDeskStateAndCbbImageType(List<UUID> idList, boolean isDelete, String deskState,
            String cbbImageType);


    /**
     * * 根据桌面ID id查询
     *
     * @param idList idList
     * @param deskStateList is 桌面状态
     * @return view list
     */
    List<ViewUserDesktopEntity> findBycbbDesktopIdInAndDeskStateIn(List<UUID> idList, List<String> deskStateList);

    /**
     * * 根据desktopPoolId查询
     *
     * @param desktopPoolId desktopPoolId
     * @param isDelete is moveDesktopToRecycleBin
     * @return view list
     */
    List<ViewUserDesktopEntity> findByDesktopPoolIdAndIsDelete(UUID desktopPoolId, boolean isDelete);

    /**
     * 获取传入用户集合的回收站云桌面
     *
     * @param userIdList 用户集合
     * @param isDelete 是否删除
     * @return List<ViewUserDesktopEntity>
     */
    List<ViewUserDesktopEntity> findByUserIdInAndIsDelete(List<UUID> userIdList, boolean isDelete);

    /**
     * 获取传入用户集合的回收站云桌面
     *
     * @param userIdList 用户集合
     * @param isDelete 是否删除
     * @param deskState 桌面状态
     * @param cbbImageType 桌面类型
     * @return List<ViewUserDesktopEntity>
     */
    List<ViewUserDesktopEntity> findByUserIdInAndIsDeleteAndDeskStateAndCbbImageType(List<UUID> userIdList, boolean isDelete,

            String deskState, String cbbImageType);

    /**
     * 根据用户id和桌面类型获取云桌面列表
     *
     * @param userId 用户id
     * @param isDelete 是否删除标记
     * @param deskType 云桌面类型
     * @return view list
     */
    List<ViewUserDesktopEntity> findByUserIdAndIsDeleteAndDeskType(UUID userId, boolean isDelete, String deskType);

    /**
     * find all desktop names
     *
     * @param userId user id
     * @return desktop name list
     */
    @Query("select desktopName from ViewUserDesktopEntity where userId = ?1")
    List<String> findDesktopNamesByUserId(UUID userId);

    /**
     * * 统计用户云桌面数
     *
     * @param uuidList uuid list
     * @param isDelete 是否已删除（）
     * @return Object[0] userId Object[1] count
     */
    @Query("select userId, count(userId) from ViewUserDesktopEntity " + "where userId in (:uuidList) and isDelete = :isDelete group by userId")
    List<Object[]> countByUserIdAndIsDelete(@Param("uuidList") List<UUID> uuidList, @Param("isDelete") boolean isDelete);

    /**
     * * 根据用户ID统计
     *
     * @param userId 用户id
     * @param isDelete is moveDesktopToRecycleBin
     * @return 统计数量
     */
    long countByUserIdAndIsDelete(UUID userId, Boolean isDelete);

    /**
     * * 根据用户ID统计
     *
     * @param userId 用户id
     * @param desktopType 云桌面类型，vdi还是idv
     * @param isDelete is moveDesktopToRecycleBin
     * @param desktopPoolType 桌面池类型
     * @return 统计数量
     */
    long countByUserIdAndDesktopTypeAndIsDeleteAndDesktopPoolType(UUID userId, String desktopType, Boolean isDelete, String desktopPoolType);

    /**
     * * 查询出那些用户是不能删除的
     *
     * @param uuidList uuid list
     * @return can moveDesktopToRecycleBin user ids
     */
    @Query("select DISTINCT userId from ViewUserDesktopEntity where isDelete = FALSE and userId in (:uuidList) "
            + "and deskState not in ('CLOSE','OFF_LINE') and desktopType in ('IDV', 'VOI', 'VDI') and desktopPoolType = 'COMMON' ")
    List<UUID> findCannotDelUserIds(@Param("uuidList") List<UUID> uuidList);

    /**
     * * 找出关联终端和用户的运行云桌面
     *
     * @param terminalId terminal id
     * @param userId user id
     * @return entity
     */
    @Query("from ViewUserDesktopEntity where deskState='RUNNING'" + " and terminalId = ?1 and userId = ?2")
    ViewUserDesktopEntity findRunningDeskByTerminalAndUser(String terminalId, UUID userId);

    /**
     * 查找出正在运行的桌面
     *
     * @param userId 用户id
     * @param deskState 桌面状态
     * @param isDelete 是否已删除
     * @return 返回桌面列表
     */
    List<ViewUserDesktopEntity> findViewDesktopEntitiesByUserIdAndDeskStateAndIsDelete(UUID userId, String deskState, Boolean isDelete);

    /**
     * 根据桌面状态查询桌面列表
     *
     * @param deskState 桌面状态
     * @param isDelete 是否已删除
     * @return 返回桌面列表
     */
    List<ViewUserDesktopEntity> findAllByDeskStateAndIsDelete(String deskState, Boolean isDelete);

    /**
     * 根据用户组和终端组查询桌面列表
     *
     * @param userGroupIdList 所属用户组ID
     * @param terminalGroupIdList 所属终端组ID
     * @param isDelete 是否已删除
     * @return 返回桌面列表
     */
    List<ViewUserDesktopEntity> findAllByUserGroupIdInAndTerminalGroupIdInAndIsDelete(List<UUID> userGroupIdList, List<UUID> terminalGroupIdList,
            Boolean isDelete);

    /**
     * 根据用户组和桌面类型查询桌面列表
     *
     * @param userGroupIdList 所属用户组ID
     * @param desktopType 桌面类型
     * @param isDelete 是否已删除
     * @return 返回桌面列表
     */
    List<ViewUserDesktopEntity> findAllByUserGroupIdInAndDesktopTypeAndIsDelete(List<UUID> userGroupIdList, String desktopType, Boolean isDelete);

    /**
     * 根据用户组和终端组和桌面池组查询云桌面ID列表
     *
     * @param userGroupIdList 所属用户组ID
     * @param terminalGroupIdList 所属终端组ID
     * @return 返回桌面列表
     */
    @Query("select cbbDesktopId from ViewUserDesktopEntity where userGroupId in (?1) or ( terminalGroupId in (?2) AND idvTerminalModel ='PUBLIC')")
    List<UUID> findIdsByUserGroupIdInAndTerminalGroupIdIn(List<UUID> userGroupIdList, List<UUID> terminalGroupIdList);

    /**
     * 根据用户组和终端组和桌面池组查询云桌面ID列表
     *
     * @param userGroupIdList 所属用户组ID
     * @param terminalGroupIdList 所属终端组ID
     * @param desktopPoolIdList 所属桌面池组ID
     * @return 返回桌面列表
     */
    @Query("select cbbDesktopId from ViewUserDesktopEntity where userGroupId in (?1) or ( terminalGroupId in (?2) AND idvTerminalModel ='PUBLIC') "
            + "or desktopPoolId in (?3)")
    List<UUID> findIdsByUserGroupIdInAndTerminalGroupIdInAndDesktopPoolIdIn(List<UUID> userGroupIdList, List<UUID> terminalGroupIdList,
            List<UUID> desktopPoolIdList);

    /**
     * 根据用户组和终端组查询云桌面ID列表
     *
     * @param userGroupIdList 所属用户组ID
     * @param terminalGroupIdList 所属终端组ID
     * @param iamgeIdList 镜像ID
     * @return 返回桌面列表
     */
    @Query("select cbbDesktopId from ViewUserDesktopEntity where userGroupId in (?1) or (terminalGroupId in (?2) AND "
            + "pattern ='PUBLIC') or imageTemplateId in (?3)")
    List<UUID> findIdsByUserGroupIdInAndTerminalGroupIdAndImageIdIn(List<UUID> userGroupIdList, List<UUID> terminalGroupIdList,
            List<UUID> iamgeIdList);

    /**
     * 统计云桌面数量
     *
     * @param hasLogin true 已登录过
     * @param desktopType 桌面类型
     * @param isDelete true 已被删除
     * @return 返回结果
     */
    Long countByHasLoginAndDesktopTypeAndIsDelete(boolean hasLogin, String desktopType, boolean isDelete);

    /**
     * 根据用户组和终端组统计从未登录云桌面数量
     *
     * @param userGroupIdList 所属用户组ID
     * @param terminalGroupIdList 所属终端组ID
     * @return 返回结果
     */
    @Query("select count(id) from ViewUserDesktopEntity " + "where hasLogin = false " + "and isDelete = false " + "and desktopType = 'VDI' "
            + "and (userGroupId in (?1) or terminalGroupId in (?2))")
    Long countVDINeverLoginByUserGroupIdInAndTerminalGroupIdIn(List<UUID> userGroupIdList, List<UUID> terminalGroupIdList);

    /**
     * * 根据cbbStrategyId获取第一个DesktopEntity
     *
     * @param cbbStrategyId cbb strategy id
     * @return 返回DesktopEntity对象
     */
    ViewUserDesktopEntity findFirstByCbbStrategyId(UUID cbbStrategyId);

    /**
     * * 根据cbbDeskNetworkId获取第一个DesktopEntity
     *
     * @param cbbNetworkId cbb network id
     * @return 返回DesktopEntity对象
     */
    ViewUserDesktopEntity findFirstByCbbNetworkId(UUID cbbNetworkId);

    /**
     * 查询指定用户组下的桌面状态数统计
     *
     * @param userGroupUuidList 指定的用户组id列表
     * @param isDelete 是否删除
     * @return java.util.List<com.ruijie.rcos.rcdc.user.module.def.api.dto.DesktopStateNumDTO>
     * @author hli on 2019-08-09
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopStateNumDTO(deskState, count(id)) "
            + "from ViewUserDesktopEntity where isDelete= ?2 and userGroupId in (?1) group by deskState")
    List<DesktopStateNumDTO> countNumGroupByDesktopStateIn(List<UUID> userGroupUuidList, Boolean isDelete);

    /**
     * 查询桌面状态数统计
     *
     * @param isDelete 是否删除
     * @return java.util.List<com.ruijie.rcos.rcdc.user.module.def.api.dto.DesktopStateNumDTO>
     * @author hli on 2019-08-09
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopStateNumDTO(deskState, count(id)) "
            + "from ViewUserDesktopEntity where isDelete= ?1 group by deskState")
    List<DesktopStateNumDTO> countNumGroupByDesktopState(Boolean isDelete);

    /**
     * 找出运行的云桌面
     *
     * @param deskIds 桌面id
     * @param deskState 桌面状态
     * @return 桌面信息
     */
    List<ViewUserDesktopEntity> findAllByCbbDesktopIdInAndDeskState(List<UUID> deskIds, String deskState);

    /**
     * * 根据strategy id查询
     *
     * @param cbbStrategyId strategyId
     * @param isDelete is moveDesktopToRecycleBin
     * @return view list
     */
    List<ViewUserDesktopEntity> findByCbbStrategyIdAndIsDelete(UUID cbbStrategyId, boolean isDelete);

    /**
     * 根据cbbDesktopIdList查询云桌面列表
     *
     * @param cbbDesktopIdList 云桌面UUID列表
     * @return List<ViewUserDesktopEntity>
     */
    List<ViewUserDesktopEntity> findAllByCbbDesktopIdIn(List<UUID> cbbDesktopIdList);

    /**
     * 根据终端id查询
     *
     * @param terminalId 终端id
     * @return 桌面信息
     */
    ViewUserDesktopEntity findByTerminalId(String terminalId);

    /**
     * * 根据桌面状态统计
     *
     * @param deskState 桌面状态
     * @param isDelete is moveDesktopToRecycleBin
     * @return 统计数量
     */
    Long countByDeskStateAndIsDelete(String deskState, Boolean isDelete);

    /**
     * 获取云桌面总数
     *
     * @param isDelete is moveDesktopToRecycleBin
     * @return 统计数量
     */
    Long countByIsDelete(Boolean isDelete);

    /**
     * 查询指定用户组下的桌面状态数统计
     *
     * @param deskIds 指定的用户组id列表
     * @param isDelete 是否删除
     * @param deskState 桌面状态
     * @return java.util.List<com.ruijie.rcos.rcdc.user.module.def.api.dto.DesktopStateNumDTO>
     * @author hli on 2019-08-09
     */
    @Query(" select new com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopSoftwareNotifyDTO(udt.id, udt.cbbDesktopId, udt.terminalId ) "
            + " from DeskInfoEntity di left join UserDesktopEntity udt on di.deskId = udt.cbbDesktopId  "
            + " where udt.cbbDesktopId in :deskIds and di.deskState = :deskState and di.isDelete = :isDelete")
    List<DesktopSoftwareNotifyDTO> findListByDeskIds(@Param("deskIds") List<UUID> deskIds, @Param("deskState") CbbCloudDeskState deskState,
            @Param("isDelete") Boolean isDelete);

    /**
     * * 根据用户名统计
     *
     * @param userName 用户名
     * @param isDelete is moveDesktopToRecycleBin
     * @return 统计数量
     */
    List<ViewUserDesktopEntity> findByUserNameAndIsDelete(String userName, boolean isDelete);

    /**
     * 根据云桌面ID获取绑定用户的类型
     *
     * @param desktopId 云桌面ID
     * @return 绑定用户的类型
     */
    @Query("select userType from ViewUserDesktopEntity where cbbDesktopId = ?1")
    String findUserTypeByCbbDesktopId(UUID desktopId);

    /**
     * 查询所以未被删除的云桌面ID
     *
     * @param isDelete 是否删除
     * @return 云桌面ID列表
     */
    @Query("select cbbDesktopId from ViewUserDesktopEntity where isDelete = ?1")
    List<UUID> findCbbDesktopIdByIsDelete(boolean isDelete);

    /**
     * 查询未被删除的IDV云桌面ID
     *
     * @param isDelete 是否删除
     * @param desktopType 云桌面类型
     * @return 云桌面ID列表
     */
    @Query("select cbbDesktopId from ViewUserDesktopEntity where isDelete = (?1) and desktopType = (?2)")
    List<UUID> findCbbDesktopIdByIsDeleteAndByDesktopType(boolean isDelete, String desktopType);

    /**
     * 统计idList符合的数量
     *
     * @param countCloudDesktopDTO countCloudDesktopDTO 查询对象
     * @return 统计数量
     */
    @Query("select count(*) from ViewUserDesktopEntity t where t.cbbDesktopId in :#{#dto.idList} and t.osType = :#{#dto.osType} "
            + " and t.osVersion = :#{#dto.osVersion} and t.cbbImageType = :#{#dto.cbbImageType} "
            + " and t.pattern = :#{#dto.pattern} and t.imageTemplateId = :#{#dto.imageTemplateId} and t.isDelete = :#{#dto.isDelete}")
    int countByDTO(@Param("dto") CountCloudDesktopDTO countCloudDesktopDTO);

    /**
     * 根据状态，是否删除，关联云桌面策略是否开启水印查询云桌面列表
     *
     * @param deskState 桌面状态
     * @param strategyEnableWatermark 云桌面策略是否开启水印
     * @param isDelete 是否删除
     * @return 云桌面列表
     */
    List<ViewUserDesktopEntity> findAllByDeskStateAndStrategyEnableWatermarkAndIsDelete(String deskState, boolean strategyEnableWatermark,
                                                                                        boolean isDelete);

    /**
     * 查询所有桌面id，包括回收站中的
     *
     * @return 桌面id列表
     */
    @Query("select cbbDesktopId from ViewUserDesktopEntity")
    List<UUID> findAllDeskId();


    /**
     * 根据终端产品型号、镜像类型、操作系统查询桌面个数
     *
     * @param productTypeList 产品型号列表
     * @param imageType 镜像类型
     * @param osTypeList 操作系统列表
     * @return 桌面个数
     */
    long countByProductTypeInAndCbbImageTypeAndOsTypeIn(List<String> productTypeList,
                                                                        String imageType,
                                                                        List<String> osTypeList);

    /**
     * @param deskIdList 桌面id
     * @param deskPoolIdList 桌面池id
     * @param userIdList 用户id
     * @param userGroupIdList 用户组id
     * @param platformId 云平台ID
     * @return 满足其中一个条件的桌面集合
     */
    @Query(value = "select * from v_cbb_user_desktop_detail where platform_id= ?1 and " +
            "(cbb_desktop_id in (?2) or desktop_pool_id in (?3) or user_id in (?4) or user_group_id in (?5))", nativeQuery = true)
    List<ViewUserDesktopEntity> findByPlatformIdAndCbbDesktopIdInOrDesktopPoolIdInOrUserIdInOrUserGroupIdIn(UUID platformId, 
                                                                                                            List<UUID> deskIdList,
                                                                                                            List<UUID> deskPoolIdList,
                                                                                                            List<UUID> userIdList,
                                                                                                            List<UUID> userGroupIdList);

    /**
     * 根据镜像id查询所有桌面id，包括回收站中的
     * @param imageTemplateId 镜像id
     * @return 云桌面列表
     */
    List<ViewUserDesktopEntity> findAllByImageTemplateId(UUID imageTemplateId);
}
