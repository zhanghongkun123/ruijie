package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.DesktopOnlineSituationHourRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description:  云桌面在线历史情况小时记录表Dao
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
public interface DesktopOnlineSituationHourRecordDAO extends SkyEngineJpaRepository<DesktopOnlineSituationHourRecordEntity, UUID> {

    /**
     * 查询过去一天的在线情况
     * @return 云桌面在线小时记录信息列表
     */
    @Query(value = "select hour_key as date_time,online_count,sleep_count from t_rco_desktop_online_situation_hour_record "
            + "where create_time >= now() - interval '24 hour'", nativeQuery = true)
    List<Object[]> countLastOneDayData();

    /**
     * 删除一天前的数据
     *
     * @param createTime 创建时间
     */
    @Transactional
    void deleteByCreateTimeLessThan(Date createTime);
}
