package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.TerminalOnlineSituationHourRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 终端在线记录表一小时维度
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author zhiweiHong
 */
public interface TerminalOnlineSituationHourRecordDAO extends SkyEngineJpaRepository<TerminalOnlineSituationHourRecordEntity, UUID> {

    /**
     * 删除一天前的数据
     * 
     * @param createTime 创建时间
     */
    @Transactional
    void deleteByCreateTimeLessThan(Date createTime);

    /**
     * 根据终端Id列表和终端类型查询过去一天的在线情况
     *
     * @param terminalIdList 终端Id列表
     * @param platform 终端类型
     * @return 终端在线小时记录信息列表
     */
    @Query(value = "select sum( t1.terminal_one ) as count, t1.date_time from ( select 1 as terminal_one, hour_key as date_time "
            + "from t_terminal_online_situation_hour_record where create_time >= now() - interval '24 hour' "
            + "and platform = :platform and terminal_id in ( :terminalIdList ) group by terminal_id, hour_key ) t1 group by t1.date_time",
            nativeQuery = true)
    List<Object[]> countLastOneDayDataByTerminalId(@Param("terminalIdList") List<String> terminalIdList, @Param("platform") String platform);

    /**
     * 根据终端类型查询过去一天的在线情况
     *
     * @param platform 终端类型
     * @return 终端在线小时记录信息列表
     */
    @Query(value = "select sum( t1.terminal_one ) as count, t1.date_time from ( select 1 as terminal_one, hour_key as date_time "
            + "from t_terminal_online_situation_hour_record "
            + "where create_time >= now() - interval '24 hour' and platform = :platform group by terminal_id, hour_key ) t1 group by t1.date_time",
            nativeQuery = true)
    List<Object[]> countLastOneDayData(@Param("platform") String platform);

    /**
     * 根据终端类型查询今天在线情况
     *
     * @param platform 终端类型
     * @return 终端在线小时记录信息列表
     */
    @Query(value = "select count( 1 ) from ( select terminal_id, day_key as date_time from t_terminal_online_situation_hour_record "
            + "where day_key = to_char( now(), 'yyyy-mm-dd' ) and platform = :platform group by terminal_id, day_key ) t1", nativeQuery = true)
    List<BigInteger> countTodayData(@Param("platform") String platform);

    /**
     * 根据终端Id列表和终端类型查询今天在线情况
     *
     * @param terminalIdList 终端Id列表
     * @param platform 终端类型
     * @return 终端在线小时记录信息列表
     */
    @Query(value = "select count( 1 ) from ( select terminal_id, day_key as date_time "
            + "from t_terminal_online_situation_hour_record where day_key = to_char( now(), 'yyyy-mm-dd' ) "
            + "and platform = :platform and terminal_id in ( :terminalIdList ) group by terminal_id, day_key ) t1", nativeQuery = true)
    List<BigInteger> countTodayDataByTerminalId(@Param("terminalIdList") List<String> terminalIdList, @Param("platform") String platform);

    /**
     * 根据传入的日期查询终端在线记录
     *
     * @param dayKey 终端在线时间点（天维度）
     * @return 终端在线小时记录信息列表
     */
    List<TerminalOnlineSituationHourRecordEntity> findByDayKey(String dayKey);

    /**
     * 根据传入的终端Id和所在小时点查询终端在线记录
     *
     * @param terminalId 终端记录
     * @param hourKey 终端在线时间点（小时维度）
     * @return 终端在线小时记录信息
     */
    TerminalOnlineSituationHourRecordEntity findByTerminalIdAndHourKey(String terminalId, String hourKey);
}
