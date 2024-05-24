package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHistoryEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * Description: 服务器资源使用情况收集补偿定时任务，每小时整点执行
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月29日
 * 
 * @Service
 *          //@Quartz(scheduleTypeCode = "rco_server_resource_usage_history_collect_retry",
 *          // scheduleName = BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_SERVER_RESOURCE_USAGE_HISTORY_COLLECT_RETRY, cron =
 *          "30 0 * * * ? ")
 * @author wanmulin
 */
public class ServerResourceUsageHistoryCollectRetryQuartzTask extends AbstractServerResourceUsageHistoryCollect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerResourceUsageHistoryCollectRetryQuartzTask.class);

    // 补偿时间段，小时
    private static final int DEFAULT_RETRY_HOURS = 7 * 24;

    // 单个补偿时段长度，小时
    private static final long SINGLE_RETRY_INTERVAL = 1L;

    @Autowired
    private PlatformAPI platformAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws BusinessException {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        Set<LocalDateTime> retryDateSet = check();
        if (CollectionUtils.isEmpty(retryDateSet)) {
            LOGGER.info("服务器资源使用率历史表无需补偿。");
            return;
        }

        List<PhysicalServerDTO> serverList = listAllPhysicalServer();
        if (CollectionUtils.isEmpty(serverList)) {
            LOGGER.info("服务器列表为空，无需统计。");
            return;
        }

        for (LocalDateTime retryDate : retryDateSet) {
            save(serverList, retryDate.minusHours(SINGLE_RETRY_INTERVAL), retryDate);
        }
    }

    private Set<LocalDateTime> check() throws BusinessException {
        // 获取最大补偿小时数
        int retryHours = getRetryHours();
        if (retryHours == 0) {
            return Sets.newHashSet();
        }
        // 将当前时间格式化成整点时间
        LocalDateTime currentTime = DateUtil.toDateHour(LocalDateTime.now());
        // 初始化补偿时间Set
        Set<LocalDateTime> retryDateSet = new HashSet<>();
        for (int i = 1; i <= retryHours; i++) {
            LocalDateTime retryDate = currentTime.minusHours(i);
            retryDateSet.add(retryDate);
        }
        // 构造已经收集到的Set
        Set<LocalDateTime> clollectDateSet = new HashSet<>();
        LocalDateTime startTime = currentTime.minusHours(retryHours);

        List<ServerResourceUsageHistoryEntity> entityList = serverResourceUsageHistoryDAO
                .getByCollectTimeBetween(DateUtil.localDateTimeToDate(startTime), DateUtil.localDateTimeToDate(currentTime));

        if (CollectionUtils.isEmpty(entityList)) {
            return retryDateSet;
        }

        for (ServerResourceUsageHistoryEntity entity : entityList) {
            LocalDateTime collectTime = DateUtil.dateToLocalDateTime(entity.getCollectTime());
            clollectDateSet.add(DateUtil.toDateHour(collectTime));
        }

        retryDateSet.removeAll(clollectDateSet);
        return retryDateSet;
    }

    private int getRetryHours() throws BusinessException {
        long systemWorkTime = platformAPI.getSystemTime().getSystemWorkTime();
        return Math.min(DEFAULT_RETRY_HOURS, DateUtil.computeHourInterval(systemWorkTime));
    }

}
