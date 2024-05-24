package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHourDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHourEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * 服务器资源使用情况（日）统计定时任务，每隔10分钟执行一次
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 * 
 * @Service
 *          //@Quartz(scheduleTypeCode = "rco_server_resource_usage_day_statistic",
 *          // scheduleName = BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_SERVER_RESOURCE_USAGE_DAY_STATISTIC, cron = "0 10 0
 *          * * ? *")
 *
 * @author wanmulin
 */
public class ServerResourceUsageDayStatisticQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerResourceUsageDayStatisticQuartzTask.class);

    @Autowired
    private ServerResourceUsageHourDAO serverResourceUsageHourDAO;

    @Autowired
    private ServerResourceUsageDayDAO serverResourceUsageDayDAO;

    // 补偿时间段，天
    private static final int RETRY_DAYS = 7;

    private static final int NEXT_DAYS = 1;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("服务器资源使用情况（日）统计定时任务开始===");
        Set<LocalDate> retryDateSet = check();

        for (LocalDate retryDate : retryDateSet) {
            List<ServerResourceUsageHourEntity> entityList =
                    serverResourceUsageHourDAO.queryAverageUsage(DateUtil.localDateTimeToDate(retryDate.atStartOfDay()),
                            DateUtil.localDateTimeToDate(retryDate.plusDays(NEXT_DAYS).atStartOfDay()));

            if (CollectionUtils.isEmpty(entityList)) {
                continue;
            }

            List<ServerResourceUsageDayEntity> dayEntityList = generateServerResourceUsageDayEntitys(retryDate, entityList);
            serverResourceUsageDayDAO.saveAll(dayEntityList);
        }
        LOGGER.info("服务器资源使用情况（日）统计定时任务结束===");
    }

    private Set<LocalDate> check() {
        LocalDate nowDate = LocalDate.now();
        // 初始化补偿时间Set
        Set<LocalDate> retryDateSet = new HashSet<>();
        for (int i = 1; i <= RETRY_DAYS; i++) {
            LocalDate retryDate = nowDate.minusDays(i);
            retryDateSet.add(retryDate);
        }

        LocalDate startTime = nowDate.minusDays(RETRY_DAYS);

        List<ServerResourceUsageDayEntity> dayEntityList =
                serverResourceUsageDayDAO.findByStatisticTimeBetween(DateUtil.localDateToDate(startTime), DateUtil.localDateToDate(nowDate));
        // 无收集到的数据
        if (CollectionUtils.isEmpty(dayEntityList)) {
            return retryDateSet;
        }

        // 构造已经收集到的Set
        for (ServerResourceUsageDayEntity entity : dayEntityList) {
            LocalDate statisticTime = DateUtil.dateToLocalDate(entity.getStatisticTime());
            retryDateSet.remove(statisticTime);
        }
        return retryDateSet;
    }

    private List<ServerResourceUsageDayEntity>
        generateServerResourceUsageDayEntitys(LocalDate date, List<ServerResourceUsageHourEntity> entityList) {

        List<ServerResourceUsageDayEntity> dayEntityList = Lists.newArrayList();
        for (ServerResourceUsageHourEntity entity : entityList) {
            ServerResourceUsageDayEntity dayEntity = new ServerResourceUsageDayEntity();
            dayEntity.setId(UUID.randomUUID());
            dayEntity.setStatisticTime(DateUtil.localDateToDate(date));
            dayEntity.setCreateTime(new Date());
            dayEntity.setServerId(entity.getServerId());
            dayEntity.setCpuUsage(entity.getCpuUsage());
            dayEntity.setMemoryUsage(entity.getMemoryUsage());
            dayEntity.setDiskUsage(entity.getDiskUsage());
            dayEntity.setVersion(0);
            dayEntityList.add(dayEntity);
        }

        return dayEntityList;
    }

}
