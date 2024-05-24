package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageDayEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Description: 服务器资源日统计数据接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月29日
 * 
 * @author wanmulin
 */
public interface ServerResourceUsageDayDAO extends SkyEngineJpaRepository<ServerResourceUsageDayEntity, UUID> {

    /**
     * 根据serverId获取某时间段内服务器每天资源使用率集合
     * @param serverId 服务器id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<ServerResourceUsageDayEntity> 返回集合
     */
    @Query("from ServerResourceUsageDayEntity where serverId = ?1 and statisticTime between ?2 and ?3 order by "
        + "statisticTime asc")
    @Transactional
    List<ServerResourceUsageDayEntity> listByIdAndStatisticTime(UUID serverId, Date startTime, Date endTime);

    /**
     * 
     * @param date 时间
     * @return List<ServerResourceUsageDayEntity>
     */
    List<ServerResourceUsageDayEntity> findByStatisticTimeBefore(Date date);
    
    /**
     * 
     * @param begin 开始时间
     * @param end 结束时间
     * @return List<ServerResourceUsageDayEntity>
     */
    List<ServerResourceUsageDayEntity> findByStatisticTimeBetween(Date begin,Date end);

    /**
     * 清理某天之前的全部数据
     * @param date 日期
     */
    void deleteByStatisticTimeBefore(Date date);
}
