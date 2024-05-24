package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogDayRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 以天为单位的云桌面会话使用记录表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14 14:53
 *
 * @author yxq
 */
public interface DesktopSessionLogDayRecordDAO extends SkyEngineJpaRepository<DesktopSessionLogDayRecordEntity, UUID> {

    /**
     * 删除指定时间之前的记录
     *
     * @param dayKey 时间
     */
    @Transactional
    @Modifying
    void deleteByDayKeyBefore(String dayKey);

    /**
     * 统计指定时间段内，总体桌面池使用率
     *
     * @param startDayKey 开始的时间
     * @param endDayKey   结束的时间
     * @param type        类型
     * @return 结果列表
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO(entity.dayKey, entity.count) from " +
            "DesktopSessionLogDayRecordEntity entity where entity.type = :type and entity.dayKey between :startDayKey and :endDayKey")
    List<DesktopPoolStatisticsResultDTO> obtainTotalUsedRateByDayKey(@Param("startDayKey") String startDayKey, @Param("endDayKey") String endDayKey,
                                                                     @Param("type") DesktopStatisticsTypeEnum type);

    /**
     * 统计指定时间段内，总体桌面池连接失败数量
     *
     * @param startDayKey 开始的时间
     * @param endDayKey   结束的时间
     * @param type        类型
     * @return 结果列表
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO(entity.dayKey, sum(entity.count)) " +
            "from DesktopSessionLogDayRecordEntity entity where entity.type = :type and entity.dayKey between :startDayKey and :endDayKey " +
            "GROUP BY entity.dayKey")
    List<DesktopPoolStatisticsResultDTO> obtainTotalConnectFaultCountByDayKey(@Param("startDayKey") String startDayKey,
                                                                              @Param("endDayKey") String endDayKey,
                                                                              @Param("type") DesktopStatisticsTypeEnum type);


    /**
     * 统计指定时间段内的统计信息
     *
     * @param startDayKey 开始的时间
     * @param endDayKey   结束的时间
     * @param type        类型
     * @param relatedId   指定的id
     * @return 结果列表
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO(entity.dayKey, entity.count) " +
            "from DesktopSessionLogDayRecordEntity entity " +
            "where entity.relatedId = :relatedId and entity.type = :type and entity.dayKey between :startDayKey and :endDayKey")
    List<DesktopPoolStatisticsResultDTO> obtainByDayKeyAndTypeAndRelatedId(@Param("startDayKey") String startDayKey,
                                                                           @Param("endDayKey") String endDayKey,
                                                                           @Param("type") DesktopStatisticsTypeEnum type,
                                                                           @Param("relatedId") UUID relatedId);

    /**
     * 获取已经统计的数据里面，最新的日期
     *
     * @return dayKey
     */
    @Query("SELECT DISTINCT MAX(dayKey) FROM DesktopSessionLogDayRecordEntity")
    String obtainMaxDayKey();
}
