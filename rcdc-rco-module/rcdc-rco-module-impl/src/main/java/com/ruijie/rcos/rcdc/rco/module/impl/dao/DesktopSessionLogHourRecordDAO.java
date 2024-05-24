package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDayDesktopSessionLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogHourRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 以小时为单位的云桌面会话使用记录表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14 14:53
 *
 * @author yxq
 */
public interface DesktopSessionLogHourRecordDAO extends SkyEngineJpaRepository<DesktopSessionLogHourRecordEntity, UUID> {

    /**
     * 删除指定时间之前的记录
     *
     * @param dayKey 时间
     */
    @Transactional
    @Modifying
    void deleteByDayKeyBefore(String dayKey);

    /**
     * 根据小时key和统计的类型查询
     *
     * @param hourKey        小时key
     * @param type           类型
     * @param deskPoolIdList 桌面池id列表
     * @return 列表
     */
    List<DesktopSessionLogHourRecordEntity> findByHourKeyAndTypeAndRelatedIdIn(String hourKey, DesktopStatisticsTypeEnum type,
                                                                               List<UUID> deskPoolIdList);

    /**
     * 根据小时key和统计的类型查询
     *
     * @param hourKey 小时key
     * @param type    类型
     * @return 实体
     */
    DesktopSessionLogHourRecordEntity findFirstByHourKeyAndType(String hourKey, DesktopStatisticsTypeEnum type);


    /**
     * 统计过去一天，对应统计类型的最大值
     *
     * @param dayKey   天key
     * @param typeList 统计的类型
     * @return 统计结果
     */
    @Query(value = "SELECT new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDayDesktopSessionLogDTO(" +
            "MAX(entity.count), entity.relatedId, entity.relatedType, entity.type) FROM DesktopSessionLogHourRecordEntity entity " +
            "WHERE entity.dayKey = :dayKey and entity.type IN :typeList GROUP BY entity.relatedId, entity.relatedType, entity.type")
    List<StatisticDayDesktopSessionLogDTO> obtainMaxUsedRateByDay(@Param("dayKey") String dayKey,
                                                                  @Param("typeList") List<DesktopStatisticsTypeEnum> typeList);


    /**
     * 统计过去一天，对应统计类型的总和
     *
     * @param dayKey 天key
     * @param type   统计的类型
     * @return 统计结果
     */
    @Query(value = "SELECT new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDayDesktopSessionLogDTO(" +
            "SUM(entity.count), entity.relatedId , entity.relatedType, entity.type) FROM DesktopSessionLogHourRecordEntity entity " +
            "WHERE entity.dayKey = :dayKey and entity.type = :type GROUP BY entity.relatedId, entity.relatedType, entity.type")
    List<StatisticDayDesktopSessionLogDTO> obtainTotalConnectFaultNumByDay(@Param("dayKey") String dayKey,
                                                                           @Param("type") DesktopStatisticsTypeEnum type);

    /**
     * 统计指定时间段内，每个时间段总的桌面池使用率
     *
     * @param startHourKey 开始的时间
     * @param endHourKey   结束的时间
     * @param type         类型
     * @return 结果列表
     */
    @Query("SELECT new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO(entity.hourKey, entity.count) FROM " +
            "DesktopSessionLogHourRecordEntity entity WHERE entity.type = :type AND entity.hourKey BETWEEN :startHourKey AND :endHourKey")
    List<DesktopPoolStatisticsResultDTO> obtainTotalUsedRateByHourKey(@Param("startHourKey") String startHourKey,
                                                                      @Param("endHourKey") String endHourKey,
                                                                      @Param("type") DesktopStatisticsTypeEnum type);

    /**
     * 统计指定时间段内，每个时间段连接失败的数量
     *
     * @param startHourKey 开始的时间
     * @param endHourKey   结束的时间
     * @param type         类型
     * @return 结果列表
     */
    @Query("SELECT new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO(entity.hourKey, SUM(entity.count)) " +
            "FROM DesktopSessionLogHourRecordEntity entity WHERE entity.type = :type AND entity.hourKey BETWEEN :startHourKey AND :endHourKey " +
            "GROUP BY entity.hourKey")
    List<DesktopPoolStatisticsResultDTO> obtainConnectFaultCountByHourKey(@Param("startHourKey") String startHourKey,
                                                                          @Param("endHourKey") String endHourKey,
                                                                          @Param("type") DesktopStatisticsTypeEnum type);

    /**
     * 统计指定时间段内的统计信息
     *
     * @param startHourKey 开始的时间
     * @param endHourKey   结束的时间
     * @param type         类型
     * @param relatedId    指定的id
     * @return 结果列表
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO(entity.hourKey, entity.count) " +
            "from DesktopSessionLogHourRecordEntity entity " +
            "where entity.relatedId = :relatedId and entity.type = :type and entity.hourKey between :startHourKey and :endHourKey")
    List<DesktopPoolStatisticsResultDTO> obtainByHourKeyAndTypeAndRelatedId(@Param("startHourKey") String startHourKey,
                                                                            @Param("endHourKey") String endHourKey,
                                                                            @Param("type") DesktopStatisticsTypeEnum type,
                                                                            @Param("relatedId") UUID relatedId);

    /**
     * 获取指定桌面池当天最大的使用率
     *
     * @param dayKey    天key
     * @param relatedId 桌面池
     * @param type      类型
     * @return 使用率
     */
    @Query("SELECT max(entity.count) FROM DesktopSessionLogHourRecordEntity entity " +
            "WHERE entity.relatedId = :relatedId AND entity.type = :type AND entity.dayKey = :dayKey")
    Double obtainMaxUsedRateInDay(@Param("dayKey") String dayKey, @Param("relatedId") UUID relatedId, @Param("type") DesktopStatisticsTypeEnum type);

    /**
     * 获取全部桌面池当天最大的使用率
     *
     * @param dayKey 天key
     * @param type   类型
     * @return 使用率
     */
    @Query("SELECT max(entity.count) FROM DesktopSessionLogHourRecordEntity entity WHERE entity.type = :type AND entity.dayKey = :dayKey")
    Double obtainTotalMaxUsedRateInDay(@Param("dayKey") String dayKey, @Param("type") DesktopStatisticsTypeEnum type);


    /**
     * 获取已经统计的数据里面，最新的小时Key
     *
     * @param type 类型
     * @return hourKey
     */
    @Query("SELECT DISTINCT MAX(hourKey) FROM DesktopSessionLogHourRecordEntity WHERE type = :type")
    String obtainMaxHourKey(@Param("type") DesktopStatisticsTypeEnum type);

    /**
     * 根据小时key和统计的类型查询
     *
     * @param hourKey 小时key
     * @param type    类型
     * @return 列表
     */
    List<DesktopSessionLogHourRecordEntity> findByHourKeyAndType(String hourKey, DesktopStatisticsTypeEnum type);
}
