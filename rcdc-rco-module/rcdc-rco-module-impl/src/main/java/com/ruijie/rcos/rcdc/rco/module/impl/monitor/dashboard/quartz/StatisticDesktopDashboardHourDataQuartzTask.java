package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.quartz;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopPoolDashboardService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: 统计上一个小时桌面池连接失败数量信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/15 13:44
 *
 * @author yxq
 */
@Service
@Quartz(cron = "0 0 0/1 * * ?", scheduleTypeCode = "statistic_hour_desk_dashboard_data", scheduleName =
        BusinessKey.RCDC_RCO_QUARTZ_STATISTIC_LAST_HOUR_CONNECT_FAIL_DASHBOARD_DATA)
public class StatisticDesktopDashboardHourDataQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticDesktopDashboardHourDataQuartzTask.class);

    private static final int ONE_HOUR_AGO = -1;

    @Autowired
    private DesktopPoolDashboardService desktopPoolDashboardService;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext must not be null");
        LOGGER.debug("定时统计过去一小时桌面连接失败报表信息");

        // 获取相关key
        Date currentDate = new Date();
        Date lastHour = DateUtils.addHours(currentDate, ONE_HOUR_AGO);

        // 统计过去一小时内的连接失败数量
        desktopPoolDashboardService.statisticHourConnectFaultData(lastHour);
    }
}
