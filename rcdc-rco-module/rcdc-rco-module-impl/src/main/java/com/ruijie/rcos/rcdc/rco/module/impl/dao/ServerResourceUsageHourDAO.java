package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHourEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

/**
 * 服务器利用率小时统计数据接口DAO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author wanmulin
 */
public interface ServerResourceUsageHourDAO extends SkyEngineJpaRepository<ServerResourceUsageHourEntity, UUID> {

    /**
     * @param serverId 服务器Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 响应
     */
    @Query("from ServerResourceUsageHourEntity where serverId =?1 and statisticTime between ?2 and ?3 order by "
        + "statisticTime asc ")
    List<ServerResourceUsageHourEntity> listByIdAndStatisticTime(UUID serverId, Date startTime, Date endTime);

    /**
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 响应
     */
    @Query("SELECT new ServerResourceUsageHourEntity(serverId,avg(cpuUsage),avg(memoryUsage),avg(diskUsage)) "
            + "FROM ServerResourceUsageHourEntity WHERE statisticTime > ?1 AND statisticTime <= ?2 GROUP BY serverId")
    List<ServerResourceUsageHourEntity> queryAverageUsage(Date startTime, Date endTime);

    /**
     * 删除某时间点之前的数据
     * @param date 时间日期
     */
    void deleteByStatisticTimeBefore(Date date);
    
    /**
     * 
     * @param begin 开始时间
     * @param end 结束时间
     * @return List<ServerResourceUsageHourEntity>
     */
    List<ServerResourceUsageHourEntity> findByStatisticTimeBetween(Date begin,Date end);
}
