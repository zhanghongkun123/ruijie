package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserConnectDesktopFaultLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 分配云桌面失败记录表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14 14:53
 *
 * @author yxq
 */
public interface UserConnectDesktopFaultLogDAO extends SkyEngineJpaRepository<UserConnectDesktopFaultLogEntity, UUID>,
        PageQueryDAO<UserConnectDesktopFaultLogEntity> {

    /**
     * 删除指定时间之前的记录
     *
     * @param date 时间
     */
    @Transactional
    @Modifying
    void deleteByFaultTimeBefore(Date date);

    /**
     * 获取指定时间内各个组、桌面池连接失败数量
     *
     * @param startTime 开始时间
     * @param endTime   失败时间
     * @return 查询结果
     */
    @Query(value = "SELECT CAST(count(1) AS INTEGER), CAST(related_id AS VARCHAR), related_type, desktop_pool_type " +
            "FROM t_rco_user_connect_desktop_fault_log WHERE fault_time BETWEEN :startTime AND :endTime " +
            "GROUP BY related_id, related_type, desktop_pool_type", nativeQuery = true)
    List<Object[]> countConnectFaultCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 统计指定桌面池时间后的错误次数数量
     *
     * @param relatedId 关联的Id
     * @param startTime 开始的时间
     * @return 错误次数
     */
    int countByRelatedIdAndFaultTimeAfter(UUID relatedId, Date startTime);

    /**
     * 统计指定桌面池时间后的错误次数数量
     *
     * @param desktopPoolType 桌面池类型
     * @param startTime       开始的时间
     * @return 错误次数
     */
    int countByDesktopPoolTypeAndFaultTimeAfter(DesktopPoolType desktopPoolType, Date startTime);

    /**
     * 统计所有桌面池时间后的错误次数数量
     *
     * @param startTime 开始的时间
     * @return 错误次数
     */
    int countByFaultTimeAfter(Date startTime);

    /**
     * 获取最大的创建时间
     *
     * @return 最大的创建时间
     */
    @Query("SELECT DISTINCT MAX(faultTime) FROM UserConnectDesktopFaultLogEntity")
    Date obtainMaxCreateTime();

    /**
     * 修改用户组名为新的名称
     *
     * @param userGroupId   用户组id
     * @param userGroupName 新的用户组名
     */
    @Transactional
    @Modifying
    @Query("UPDATE UserConnectDesktopFaultLogEntity SET userGroupName = :userGroupName, version = version + 1 " +
            "WHERE userGroupId = :userGroupId AND version = version")
    void updateUserGroupName(@Param("userGroupId") UUID userGroupId, @Param("userGroupName") String userGroupName);

    /**
     * 修改桌面池名称名为新的名称
     *
     * @param desktopPoolId   用户组id
     * @param desktopPoolName 新的桌面池名称
     */
    @Transactional
    @Modifying
    @Query("UPDATE UserConnectDesktopFaultLogEntity SET desktopPoolName = :desktopPoolName, version = version + 1 " +
            "WHERE relatedId = :desktopPoolId AND version = version")
    void updateDesktopPoolName(@Param("desktopPoolId") UUID desktopPoolId, @Param("desktopPoolName") String desktopPoolName);
}
