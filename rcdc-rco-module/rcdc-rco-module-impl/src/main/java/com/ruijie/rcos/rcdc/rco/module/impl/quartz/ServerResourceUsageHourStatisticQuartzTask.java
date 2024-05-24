package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHistoryDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHourDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHistoryEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHourEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * 服务器资源使用情况（小时）统计定时任务
 * 每小时整点执行
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月23日
 * 
 * @Service
 *          // @Quartz(scheduleTypeCode = "rco_server_resource_usage_hour_statistic",
 *          // scheduleName = BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_SERVER_RESOURCE_USAGE_HOUR_STATISTIC, cron = "0 0 *
 *          * * ? *")
 *
 * @author wanmulin
 */
public class ServerResourceUsageHourStatisticQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerResourceUsageHourStatisticQuartzTask.class);

    @Autowired
    private ServerResourceUsageHistoryDAO serverResourceUsageHistoryDAO;

    @Autowired
    private ServerResourceUsageHourDAO serverResourceUsageHourDAO;

    // 补偿时间段，小时
    private static final int RETRY_HOURS = 7 * 24;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("服务器资源使用情况（小时）统计定时任务开始===");
        Set<LocalDateTime> retryTimeSet = check();

        for (LocalDateTime retryTime : retryTimeSet) {
            LocalDateTime retryStartTime = retryTime.minusHours(1L);
            List<ServerResourceUsageHistoryEntity> entityList = serverResourceUsageHistoryDAO
                    .queryAverageUsage(DateUtil.localDateTimeToDate(retryStartTime), DateUtil.localDateTimeToDate(retryTime));
            if (CollectionUtils.isEmpty(entityList)) {
                continue;
            }

            List<ServerResourceUsageHourEntity> hourEntityList = generateServerResourceUsageHourEntitys(retryTime, entityList);
            serverResourceUsageHourDAO.saveAll(hourEntityList);
        }
        LOGGER.info("服务器资源使用情况（小时）统计定时任务结束===");
    }

    private Set<LocalDateTime> check() {
        LocalDateTime currentTime = DateUtil.toDateHour(LocalDateTime.now());

        // 初始化补偿时间Set
        Set<LocalDateTime> retryDateSet = new HashSet<>();
        for (int i = 0; i < RETRY_HOURS; i++) {
            LocalDateTime retryDate = currentTime.minusHours(i);
            retryDateSet.add(retryDate);
        }

        // 构造已经收集到的Set

        LocalDateTime startTime = currentTime.minusHours(RETRY_HOURS);
        List<ServerResourceUsageHourEntity> hourEntityList = serverResourceUsageHourDAO
                .findByStatisticTimeBetween(DateUtil.localDateTimeToDate(startTime), DateUtil.localDateTimeToDate(currentTime));
        if (hourEntityList == null || hourEntityList.size() == 0) {
            return retryDateSet;
        }

        for (ServerResourceUsageHourEntity entity : hourEntityList) {
            LocalDateTime statisticTime = DateUtil.dateToLocalDateTime(entity.getStatisticTime());
            retryDateSet.remove(statisticTime);
        }
        return retryDateSet;
    }

    private List<ServerResourceUsageHourEntity> generateServerResourceUsageHourEntitys(LocalDateTime currentTime,
            List<ServerResourceUsageHistoryEntity> entityList) {
        List<ServerResourceUsageHourEntity> hourEntityList = new ArrayList<>();
        for (ServerResourceUsageHistoryEntity entity : entityList) {
            ServerResourceUsageHourEntity hourEntity = new ServerResourceUsageHourEntity();
            hourEntity.setId(UUID.randomUUID());
            hourEntity.setStatisticTime(DateUtil.localDateTimeToDate(currentTime));
            hourEntity.setCreateTime(new Date());
            hourEntity.setServerId(entity.getServerId());
            hourEntity.setCpuUsage(entity.getCpuUsage());
            hourEntity.setMemoryUsage(entity.getMemoryUsage());
            hourEntity.setDiskUsage(entity.getDiskUsage());
            hourEntity.setVersion(0);
            hourEntityList.add(hourEntity);
        }
        return hourEntityList;
    }

}
