package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public interface UserDesktopDAO extends SkyEngineJpaRepository<UserDesktopEntity, UUID> {

    /**
     * * 根据cbbDesktopId获取DesktopEntity
     *
     * @param cbbDesktopId cbb桌面ID
     * @return 返回DesktopEntity对象
     */
    UserDesktopEntity findByCbbDesktopId(UUID cbbDesktopId);

    /**
     * * 根据终端id查询绑定的 UserDesktopEntity
     *
     * @param terminalId 终端id
     * @return 返回DesktopEntity对象
     */
    UserDesktopEntity findUserDesktopEntityByTerminalId(String terminalId);

    /**
     * * 根据userId获取云桌面列表
     *
     * @param userId 用户ID
     * @return 云桌面列表
     */
    List<UserDesktopEntity> findByUserId(UUID userId);

    /**
     * 根据云桌面名称获取第一个DesktopEntity
     *
     * @param desktopName 云桌面名称
     * @return 返回DesktopEntity对象
     */
    UserDesktopEntity findFirstByDesktopName(String desktopName);

    /**
     * 根据终端id查询绑定的云桌面列表
     *
     * @param terminalId rco终端id
     * @return 云桌面列表
     */
    List<UserDesktopEntity> findByTerminalId(String terminalId);

    /**
     * 根据cbbDesktopId删除
     *
     * @param cbbDesktopId cbbDesktopId
     * @return 删除条数
     */
    @Modifying
    @Transactional
    int deleteByCbbDesktopId(UUID cbbDesktopId);

    /**
     * 获取用户桌面数
     *
     * @param userId      userId
     * @param desktopType desktopType
     * @return 桌面数
     */
    int countByUserIdAndDesktopType(UUID userId, CbbCloudDeskType desktopType);

    /**
     * 获取用户桌面数
     *
     * @param userId userId
     * @return 桌面数
     */
    int countByUserId(UUID userId);

    /**
     * * 根据终端id 以及运行状态查询桌面信息
     *
     * @param terminalId         终端id
     * @param hasTerminalRunning 终端是否在线
     * @return 返回DesktopEntity对象
     */
    UserDesktopEntity findDesktopByTerminalIdAndHasTerminalRunning(String terminalId, Boolean hasTerminalRunning);

    /**
     * 根据用户ID查询与他绑定的所有云桌面ID
     *
     * @param userId 用户ID
     * @return 云桌面ID列表
     */
    @Query("select cbbDesktopId from UserDesktopEntity where userId = :userId")
    List<UUID> findAllCloudDeskIdList(@Param("userId") UUID userId);

    /**
     * 修改指定云桌面ID对应的用户ID
     *
     * @param desktopId 云桌面ID
     * @param userId    用户ID
     */
    @Transactional
    @Modifying
    @Query("update UserDesktopEntity set userId = :userId, version = version + 1 where cbbDesktopId = :desktopId")
    void updateUserIdByDesktopId(@Param("desktopId") UUID desktopId, @Param("userId") UUID userId);

    /**
     * 根据用户ID和云桌面类型查询用户绑定的终端
     *
     * @param userId      用户ID
     * @param desktopType 云桌面类型
     * @return 终端ID列表
     */
    @Query("select terminalId from UserDesktopEntity where userId = :userId and desktopType = :desktopType")
    List<String> findBindTerminalIdList(@Param("userId") UUID userId, @Param("desktopType") CbbCloudDeskType desktopType);

    /**
     * 修改指定云桌面是否已成功加入AD域
     *
     * @param desktopId         云桌面id
     * @param hasAutoJoinDomain 是否加入ad域
     */
    @Transactional
    @Modifying
    @Query("update UserDesktopEntity set hasAutoJoinDomain = :hasAutoJoinDomain, version = version + 1 where cbbDesktopId = :desktopId " +
            "and version = version")
    void updateHasAutoJoinDomainByDesktopId(@Param("desktopId") UUID desktopId, @Param("hasAutoJoinDomain") Boolean hasAutoJoinDomain);

    /**
     * 根据终端id修改是否开启系统盘自动扩容
     *
     * @param deskId               终端id
     * @param enableFullSystemDisk 是否开启系统盘自动扩容
     */
    @Transactional
    @Modifying
    @Query("update UserDesktopEntity set enableFullSystemDisk = :enableFullSystemDisk, version = version + 1 where cbbDesktopId = :deskId and " +
            "version = version")
    void updateEnableFullSystemDiskByDeskId(@Param("deskId") UUID deskId, @Param("enableFullSystemDisk") Boolean enableFullSystemDisk);

    /**
     * 根据云桌面ID列表查询
     *
     * @param deskIdList 云桌面ID列表
     * @return 云桌面ID列表
     */
    List<UserDesktopEntity> findByCbbDesktopIdIn(List<UUID> deskIdList);

    /**
     * 根据云桌面ID查询对应的用户ID
     *
     * @param desktopId 云桌面ID
     * @return 用户ID
     */
    @Query("select userId from UserDesktopEntity where cbbDesktopId = ?1")
    UUID findUserIdByCbbDesktopId(UUID desktopId);

    /**
     * 修改指定云桌面ID对应的桌面组ID
     *
     * @param desktopId 云桌面ID
     * @param groupId   桌面组ID
     */
    @Transactional
    @Modifying
    @Query("update UserDesktopEntity set groupId = :groupId, version = version + 1 where cbbDesktopId = :desktopId")
    void updateGroupIdByDesktopId(@Param("desktopId") UUID desktopId, @Param("groupId") UUID groupId);

    /**
     * 根据桌面ID列表查询，按用户ID进行分组统计绑定的桌面数量
     *
     * @param desktIdList 桌面ID
     * @return List<UserBindDesktopNumDTO>
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO(entity.userId, " +
            "count(entity.cbbDesktopId)) from UserDesktopEntity entity where entity.cbbDesktopId in (:deskIds) " +
            " and entity.userId is not null GROUP BY entity.userId ")
    List<UserBindDesktopNumDTO> findUserBindDesktopNumByDeskIds(@Param("deskIds") List<UUID> desktIdList);

    /**
     * 根据终端id批量桌面
     * @param terminalIdList 终端ids
     */
    @Transactional
    void deleteByTerminalIdIn(List<String> terminalIdList);

    /**
     * 更新桌面绑定终端ID
     *
     * @param cbbDeskTopId 桌面ID
     * @param terminalId 终端ID
     * @return 更新记录数
     */
    @Transactional
    @Modifying
    @Query("update UserDesktopEntity set terminalId = :terminalId, version = version + 1 where cbbDesktopId = :desktopId and version=version")
    int updateTerminalIdByCbbDesktopId(@Param("desktopId") UUID cbbDeskTopId, @Param("terminalId") String terminalId);


    /**
     * 基于用户Id查询桌面名称
     *
     * @param userId userId
     * @return 桌面名称
     */
    @Query("select desktopName from UserDesktopEntity entity where entity.userId = ?1 ")
    List<String> findDesktopNameByUserId(UUID userId);


    /**
     * 查询创建中和已经创建的桌面数量
     *
     * @param userId userId
     * @return 数量
     */
    @Query(nativeQuery = true, value = "select count(1) from t_rco_user_desktop t1 left join t_cbb_desk_info t2 on t1.cbb_desktop_id = t2.desk_id \n" +
            "where ( t2.desk_id is null or ( t2.is_delete = 'f' and t2.desktop_pool_type = 'COMMON' and t2.desk_type = 'VDI'))\n" +
            "and user_id = ?1")
    int countByUserCreatingDesktop(UUID userId);

}
