package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.TerminalOnlineSituationDayRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 终端在线记录表一天维度
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author zhiweiHong
 */
public interface TerminalOnlineSituationDayRecordDAO extends SkyEngineJpaRepository<TerminalOnlineSituationDayRecordEntity, UUID> {
    /**
     * 删除超过一年的数据
     * 
     * @param createTime 创建时间
     */
    @Transactional
    void deleteByCreateTimeLessThan(Date createTime);

    /**
     * 根据终端终端Id列表和终端类型查询过去一年的在线情况
     *
     * @param terminalIdList 终端Id列表
     * @param platform 终端类型
     * @return 结果
     */
    @Query(value = "select sum( t1.terminal_one ), t1.date_time from ( select 1 as terminal_one, day_key as date_time "
            + "from t_terminal_online_situation_day_record where create_time >= now() - interval '365 day' "
            + "and platform = :platform and terminal_id in (:terminalIdList) group by terminal_id, day_key ) t1 group by t1.date_time",
            nativeQuery = true)
    List<Object[]> countLastOneYearDataByTerminalId(@Param("terminalIdList") List<String> terminalIdList, @Param("platform") String platform);

    /**
     * 根据终端类型查询过去一年的数据
     *
     * @param platform 终端类型
     * @return 结果
     */
    @Query(value = "select sum ( t1.terminal_one ), t1.date_time from ( select 1 as terminal_one, day_key as date_time "
            + "from t_terminal_online_situation_day_record "
            + "where create_time >= now() - interval '365 day' and platform = :platform group by terminal_id, day_key ) t1 group by t1.date_time",
            nativeQuery = true)
    List<Object[]> countLastOneYearData(@Param("platform") String platform);

    /**
     * 根据终端类型查询过去一个月的在线情况
     *
     * @param platform 终端类型
     * @return 结果
     */
    @Query(value = "select sum ( t1.terminal_one ), t1.date_time from ( select 1 as terminal_one, day_key as date_time "
            + "from t_terminal_online_situation_day_record "
            + "where create_time >= now() - interval '30 day' and platform = :platform group by terminal_id, day_key ) t1 group by t1.date_time",
            nativeQuery = true)
    List<Object[]> countLastOneMonthData(@Param("platform") String platform);

    /**
     * 根据终端终端Id列表和终端类型查询过去一个月的在线情况
     *
     * @param terminalIdList 终端Id列表
     * @param platform 终端类型
     * @return 结果
     */
    @Query(value = "select sum ( t1.terminal_one ), t1.date_time from ( select 1 as terminal_one, day_key as date_time "
            + "from t_terminal_online_situation_day_record where create_time >= now() - interval '30 day' "
            + "and platform = :platform and terminal_id in (:terminalIdList) group by terminal_id, day_key) t1 group by t1.date_time",
            nativeQuery = true)
    List<Object[]> countLastOneMonthDataByTerminalId(@Param("terminalIdList") List<String> terminalIdList, @Param("platform") String platform);

}
