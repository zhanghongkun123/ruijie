package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHistoryEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

/**
 * 服务器使用历史记录数据DAO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月23日
 * 
 * @author wanmulin
 */
public interface ServerResourceUsageHistoryDAO extends SkyEngineJpaRepository<ServerResourceUsageHistoryEntity, UUID> {

    /**
     * 获取各服务器资源使用率平均值
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List
     */
    @Query("SELECT new ServerResourceUsageHistoryEntity(serverId,avg(cpuUsage),avg(memoryUsage),avg(diskUsage))"
            + "FROM ServerResourceUsageHistoryEntity WHERE collectTime >?1 AND collectTime <=?2  GROUP BY serverId")
    List<ServerResourceUsageHistoryEntity> queryAverageUsage(Date startTime, Date endTime);

    /**
     * 获取服务器最近时间的资源使用率
     * @param serverId 服务器ID
     * @return 返回
     */
    ServerResourceUsageHistoryEntity findFirstByServerIdOrderByCollectTimeDesc(UUID serverId);
    
    /**
     * 根据收集时间获取服务器资源使用情况
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<ServerResourceUsageHistoryEntity>
     */
    List<ServerResourceUsageHistoryEntity> getByCollectTimeBetween(Date startTime,Date endTime);

    /**
     * 清理某时间点之前的全部数据
     * @param date 时间日期
     */
    void deleteByCollectTimeBefore(Date date);
}
