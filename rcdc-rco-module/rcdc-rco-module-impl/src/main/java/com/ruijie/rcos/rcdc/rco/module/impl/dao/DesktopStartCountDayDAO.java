package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopStartCountDayEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: 云桌面开机数日统计数据接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 15:13
 *
 * @author zhangyichi
 */
public interface DesktopStartCountDayDAO extends SkyEngineJpaRepository<DesktopStartCountDayEntity, UUID> {

    /**
     * 获取平均开机数
     * @return 开机数
     */
    @Query("select avg (e.startCount) from DesktopStartCountDayEntity e")
    @Transactional
    Integer getAverageStartCount();

    /**
     * 获取最大开机数
     * @return 最大开机数
     */
    @Query("select max (e.startCount) from DesktopStartCountDayEntity e")
    @Transactional
    Integer getMaxStartCount();

    /**
     * 获取系统最近7天开机数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<DesktopStartCountDayEntity>
     */
    @Query("from DesktopStartCountDayEntity where statisticTime between ?1 and ?2")
    @Transactional
    List<DesktopStartCountDayEntity> listStartCount(Date startTime, Date endTime);

}
