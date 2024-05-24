package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.quartz;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopPoolDashboardService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: 统计云桌面报表相关信息定时任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/15 13:44
 *
 * @author yxq
 */
@Service
@Quartz(cron = "0 0 0 * * ?", scheduleTypeCode = "statistic_desk_pool_dashboard_data_by_day", scheduleName =
        BusinessKey.RCDC_RCO_QUARTZ_STATISTIC_DESK_POOL_DASHBOARD_DATA_BY_DAY)
public class StatisticDesktopDashboardDayDataQuartzTask implements QuartzTask {

    private static final int ONE_DAY_AGO = -1;

    @Autowired
    private DesktopPoolDashboardService desktopPoolDashboardService;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext must not be null");

        Date currentDate = new Date();
        // 清空超过时间的数据
        desktopPoolDashboardService.clearExpiredData(currentDate);

        // 统计过去一天使用率
        String dayKey = DateUtil.getStatisticDayKey(DateUtils.addDays(currentDate, ONE_DAY_AGO));
        String monthKey = DateUtil.getStatisticMonthKey(DateUtils.addDays(currentDate, ONE_DAY_AGO));
        desktopPoolDashboardService.statisticDayData(monthKey, dayKey);
    }
}
