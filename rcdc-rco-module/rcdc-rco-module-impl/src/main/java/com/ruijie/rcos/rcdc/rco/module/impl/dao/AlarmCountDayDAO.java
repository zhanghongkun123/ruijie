package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AlarmCountDayEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 * 
 * @author wanmulin
 */
public interface AlarmCountDayDAO extends SkyEngineJpaRepository<AlarmCountDayEntity, UUID> {

    /**
     * 获取某时间段内每天的告警数列表
     * @param alarmType AlarmTypeEnum 类型枚举
     * @param startTime Date 开始时间
     * @param endTime Date 结束时间
     * @return List<AlarmCountDayEntity> 返回数据
     */
    List<AlarmCountDayEntity> findByAlarmTypeAndStatisticTimeBetween(AlarmTypeEnum alarmType, Date startTime,
            Date endTime);

    /**
     * 获取时间段内告警总数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 返回
     */
    @Query("select new AlarmCountDayEntity(alarmType, sum(alarmCount)) from AlarmCountDayEntity "
        + "where statisticTime between ?1 and ?2 group by alarmType")
    List<AlarmCountDayEntity> getAlarmCountSum(Date startTime, Date endTime);
}
