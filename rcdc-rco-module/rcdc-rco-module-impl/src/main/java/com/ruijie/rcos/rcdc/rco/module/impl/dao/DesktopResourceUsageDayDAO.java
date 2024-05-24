package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Title: DesktopResourceUsageDayDAO
 * Description: 云桌面使用率日统计数据接口
 * Copyright: Ruijie Co., Ltd. (c) 2019</p>
 *
 * @author zhangyichi
 * Date: 2019/7/23 11:04
 */
public interface DesktopResourceUsageDayDAO extends SkyEngineJpaRepository<DesktopResourceUsageDayEntity, UUID> {

    /**
     * 获取指定时间内的云桌面平均使用率信息
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 实体类列表，包含云桌面ID、cpu使用率平均值、内存使用率平均值、硬盘使用率平均值
     */
    @Query("select new DesktopResourceUsageDayEntity(desktopId, avg(cpuUsage), avg(memoryUsage), avg(diskUsage)) "
        + "from DesktopResourceUsageDayEntity where statisticTime between ?1 and ?2 group by desktopId")
    List<DesktopResourceUsageDayEntity> getAverageUsage(Date startTime, Date endTime);

    /**
     * 获取指定时间内的云桌面平均使用率信息
     *
     * @param desktopIds 云桌面id集合
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 实体类列表，包含云桌面ID、cpu使用率平均值、内存使用率平均值、硬盘使用率平均值
     */
    @Query("select new DesktopResourceUsageDayEntity(statisticTime, avg(cpuUsage), avg(memoryUsage), avg(diskUsage)) "
        + "from DesktopResourceUsageDayEntity where desktopId in ?1 and statisticTime between ?2 and ?3 group by "
        + "statisticTime order by statisticTime")
    List<DesktopResourceUsageDayEntity> getAverageUsageGroupByStatisticTime(List<UUID> desktopIds, Date startTime,
        Date endTime);

    /**
     * 分页获取CPU使用率在指定范围内的云桌面
     *
     * @param low 范围下限
     * @param high 范围上限
     * @return 符合条件的云桌面
     */
    @Query(nativeQuery = true, value = "SELECT * FROM t_rco_desktop_resource_usage_day "
        + "WHERE cpu_usage BETWEEN ?1 AND ?2 AND statistic_time = CURRENT_DATE - 1")
    List<DesktopResourceUsageDayEntity> queryCpuUsageInRange(Double low, Double high);

    /**
     * 分页获取内存使用率在指定范围内的云桌面
     *
     * @param low 范围下限
     * @param high 范围上限
     * @return 符合条件的云桌面
     */
    @Query(nativeQuery = true, value = "SELECT * FROM t_rco_desktop_resource_usage_day "
        + "WHERE memory_usage BETWEEN ?1 AND ?2 AND statistic_time = CURRENT_DATE - 1")
    List<DesktopResourceUsageDayEntity> queryMemoryUsageInRange(Double low, Double high);

    /**
     * 获取对应时间段的云桌面资源使用率
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return List<DesktopResourceUsageDayEntity> 返回集合
     */
    @Query("from DesktopResourceUsageDayEntity where statisticTime between ?1 and ?2")
    @Transactional
    List<DesktopResourceUsageDayEntity> getDayResourceUsageByStatisticTime(Date startDate, Date endDate);

    /**
     * 清理桌面资源日统计表中40天之前的数据
     *
     * @param statisticTime 收集时间
     */
    @Transactional
    void deleteByStatisticTimeLessThan(Date statisticTime);

    /**
     * 获取指定云桌面ID指定时段内的使用率
     *
     * @param ids 云桌面ID数组
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 使用率信息
     */
    List<DesktopResourceUsageDayEntity> getByDesktopIdInAndStatisticTimeBetween(UUID[] ids, Date startDate,
        Date endDate);



    /**
     * 获取桌面资源日统计表中的日期数据
     *
     * @param startDate 开始日期
     * @param endDate 结束时间
     * @return List<LocalDate> 返回的日期集合
     */
    @Query("select distinct statisticTime from DesktopResourceUsageDayEntity where statisticTime between ?1 and ?2")
    List<Date> queryStatisticTimeList(Date startDate, Date endDate);
}
