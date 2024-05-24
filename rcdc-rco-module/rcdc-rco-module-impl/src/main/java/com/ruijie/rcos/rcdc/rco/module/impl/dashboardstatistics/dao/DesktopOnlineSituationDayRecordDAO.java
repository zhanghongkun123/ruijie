package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.DesktopOnlineSituationDayRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 云桌面在线历史情况天记录Dao
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
public interface DesktopOnlineSituationDayRecordDAO extends SkyEngineJpaRepository<DesktopOnlineSituationDayRecordEntity, UUID> {

    /**
     * 查询过去一个月的在线情况
     *
     * @return 结果
     */
    @Query(value = "select day_key,online_count,sleep_count from t_rco_desktop_online_situation_day_record "
            + "where create_time >= now() - interval '30 day'", nativeQuery = true)
    List<Object[]> countLastOneMonthData();

    /**
     * 查询过去一年的数据
     *
     * @return 结果
     */
    @Query(value = "select day_key,online_count,sleep_count from t_rco_desktop_online_situation_day_record "
            + "where create_time >= now() - interval '365 day'", nativeQuery = true)
    List<Object[]> countLastOneYearData();

    /**
     * 根据天维度查询天记录
     * @param dayKey 天关键词
     * @return  查询记录
     */
    DesktopOnlineSituationDayRecordEntity findByDayKey(String dayKey);

    /**
     * 删除超过一年的数据
     *
     * @param createTime 创建时间
     */
    @Transactional
    void deleteByCreateTimeLessThan(Date createTime);
}
