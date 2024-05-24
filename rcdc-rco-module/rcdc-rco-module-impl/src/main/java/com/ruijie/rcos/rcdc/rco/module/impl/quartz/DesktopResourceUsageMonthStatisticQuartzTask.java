package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageMonthDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageMonthEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * Description: 桌面资源利用率（月）统计定时任务，每月1日-10日凌晨01:00:00执行
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24
 * //@Service
 * //@Quartz(scheduleTypeCode = "rco_desk_resource_usage_month_statistic",
 * // scheduleName = BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_DESKTOP_RESOURCE_USAGE_MONTH_STATISTIC, cron = "0 0 1
 * 1,2,3,4,5,6,7,8,9,10 * ? * *")
 *
 * 
 * @author zhangyichi
 */
public class DesktopResourceUsageMonthStatisticQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopResourceUsageMonthStatisticQuartzTask.class);

    @Autowired
    protected DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Autowired
    protected DesktopResourceUsageMonthDAO desktopResourceUsageMonthDAO;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("云桌面资源使用率月统计定时任务开始====");
        // 当前日期
        LocalDate now = LocalDate.now();
        // 获取上个月的第一天
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1);
        // 校验上个月的桌面资源月统计数据是否存在
        List<Date> resultList = desktopResourceUsageMonthDAO.checkMonthUsageDate(DateUtil.localDateToDate(firstDay));
        if (!resultList.isEmpty()) {
            return;
        }
        // 上个月的资源统计数据不存在则重新获取
        LocalDate lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth());
        saveUsageMonth(firstDay, lastDay);
        LOGGER.info("云桌面资源使用率月统计定时任务结束====");
    }

    /**
     * 根据云桌面资源日统计数据计算月统计数据
     * 
     * @param startTime 开始日期
     * @param endTime 结束日期
     */
    protected void saveUsageMonth(LocalDate startTime, LocalDate endTime) {
        List<DesktopResourceUsageDayEntity> dayEntityList =
                desktopResourceUsageDayDAO.getAverageUsage(DateUtil.localDateToDate(startTime), DateUtil.localDateToDate(endTime));
        if (CollectionUtils.isEmpty(dayEntityList)) {
            LOGGER.error("未获取到[{}]至[{}]云桌面使用信息！", startTime, endTime);
            return;
        }

        List<DesktopResourceUsageMonthEntity> monthEntityList = new ArrayList<>();
        for (DesktopResourceUsageDayEntity dayEntity : dayEntityList) {
            DesktopResourceUsageMonthEntity monthEntity = new DesktopResourceUsageMonthEntity();
            monthEntity.setDesktopId(dayEntity.getDesktopId());
            monthEntity.setCpuUsage(dayEntity.getCpuUsage());
            monthEntity.setMemoryUsage(dayEntity.getMemoryUsage());
            monthEntity.setDiskUsage(dayEntity.getDiskUsage());
            monthEntity.setStatisticTime(DateUtil.localDateToDate(startTime));
            monthEntity.setCreateTime(new Date());
            monthEntityList.add(monthEntity);
        }
        desktopResourceUsageMonthDAO.saveAll(monthEntityList);
    }
}
