package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.quartz;

import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.DashboardStatisticsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.DesktopOnlineSituationDayRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.DesktopOnlineSituationHourRecordDAO;
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
 * Description: 首页每天收集云桌面在线情况定时任务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-01-17
 *
 * @author zqj
 */
@Service
@Quartz(cron = "0 0 0 * * ?", scheduleName = DashboardStatisticsBusinessKey.RCDC_RCO_DESKTOP_ONLINE_SITUATION_COLLECT_EVERY_DAY,
        scheduleTypeCode = ScheduleTypeCodeConstants.DESKTOP_ONLINE_SITUATION_COLLECT_EVERY_DAY, blockInMaintenanceMode = true)
public class DesktopOnlineSituationDayCollectQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOnlineSituationDayCollectQuartzTask.class);

    private static final int ONE_YEAR_AGO = -365;

    private static final int ONE_DAY_AGO = -1;

    @Autowired
    private DesktopOnlineSituationDayRecordDAO desktopOnlineSituationDayRecordDAO;

    @Autowired
    private DesktopOnlineSituationHourRecordDAO desktopOnlineSituationHourRecordDAO;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        LOGGER.info("首页每天收集云桌面在线情况定时任务开始");

        // 日表删除一年前的数据
        Date currentDate = new Date();
        Date lastYear = DateUtils.addDays(currentDate, ONE_YEAR_AGO);
        try {
            //日表删除一年前的数据 数据库操作
            desktopOnlineSituationDayRecordDAO.deleteByCreateTimeLessThan(lastYear);
        } catch (Exception e) {
            LOGGER.error("日表清理失败，原因是：", e);
        }
        // 小时表删除一天前的数据
        Date lastDay = DateUtils.addDays(currentDate, ONE_DAY_AGO);
        try {
            //小时表删除一天前的数据 数据库操作
            desktopOnlineSituationHourRecordDAO.deleteByCreateTimeLessThan(lastDay);
        } catch (Exception e) {
            LOGGER.error("小时表清理失败，原因是：", e);
        }
        LOGGER.info("首页每天收集云桌面在线情况定时任务结束");
    }
}
