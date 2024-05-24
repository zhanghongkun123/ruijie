package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageMonthEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

/**
 * Description: 云桌面使用率日统计数据接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 15:13
 *
 * @author zhangyichi
 */
public interface DesktopResourceUsageMonthDAO extends SkyEngineJpaRepository<DesktopResourceUsageMonthEntity, UUID> {

    /**
     * 获取所有云桌面最近一个月的资源使用率
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return List<DesktopResourceUsageMonthEntity> 返回集合
     */
    @Query("from DesktopResourceUsageMonthEntity where statisticTime between ?1 and ?2")
    List<DesktopResourceUsageMonthEntity> getMonthResourceUsage(Date startTime, Date endTime);

    /**
     * 校验上个月的资源统计数据
     * @param statisticTime 统计时间
     * @return List<LocalDate> 返回结果
     */
    @Query("select distinct statisticTime from DesktopResourceUsageMonthEntity where statisticTime = ?1")
    List<Date> checkMonthUsageDate(Date statisticTime);
}
