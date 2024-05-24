package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.quartz;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.DashboardStatisticsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.DesktopOnlineSituationDayRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.DesktopOnlineSituationHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.DesktopOnlineSituationDayRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.DesktopOnlineSituationHourRecordEntity;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: 首页每小时收集云桌面在线在线情况定时任务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-01-17
 *
 * @author zqj
 */
@Service
@Quartz(cron = "0 0 * * * ?", scheduleName = DashboardStatisticsBusinessKey.RCDC_RCO_DESKTOP_ONLINE_SITUATION_COLLECT_EVERY_HOUR,
        scheduleTypeCode = ScheduleTypeCodeConstants.DESKTOP_ONLINE_SITUATION_COLLECT_EVERY_HOUR, blockInMaintenanceMode = true)
public class DesktopOnlineSituationHourCollectQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOnlineSituationHourCollectQuartzTask.class);

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopOnlineSituationDayRecordDAO desktopOnlineSituationDayRecordDAO;

    @Autowired
    private DesktopOnlineSituationHourRecordDAO desktopOnlineSituationHourRecordDAO;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        LOGGER.info("首页每小时收集云桌面在线情况定时任务开始");
        //记录云桌面在线数
        int onlineCount = cbbDeskMgmtAPI.countByDeskStateAndIsDelete(CbbCloudDeskState.RUNNING, Boolean.FALSE);
        //记录云桌面休眠数
        int sleepCount = cbbDeskMgmtAPI.countByDeskStateAndIsDelete(CbbCloudDeskState.SLEEP, Boolean.FALSE);
        String dayKey = saveHourRecord(onlineCount, sleepCount);
        operateDayRecord(dayKey, onlineCount, sleepCount);
        LOGGER.info("首页每小时收集云桌面在线情况定时任务结束");

    }

    private String saveHourRecord(int onlineCount, int sleepCount) {
        DesktopOnlineSituationHourRecordEntity entity = new DesktopOnlineSituationHourRecordEntity(onlineCount, sleepCount);
        desktopOnlineSituationHourRecordDAO.save(entity);
        return entity.getDayKey();
    }

    private void operateDayRecord(String dayKey, int onlineCount, int sleepCount) {
        String currentDay = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        DesktopOnlineSituationDayRecordEntity dayRecord = desktopOnlineSituationDayRecordDAO.findByDayKey(currentDay);
        if (dayRecord == null) {
            saveEntity(dayKey, onlineCount, sleepCount);
            return;
        }
        updateDayRecord(dayRecord, onlineCount, sleepCount);
    }

    private void saveEntity(String dayKey, int onlineCount, int sleepCount) {
        DesktopOnlineSituationDayRecordEntity saveEntity = new DesktopOnlineSituationDayRecordEntity(dayKey, onlineCount, sleepCount);
        desktopOnlineSituationDayRecordDAO.save(saveEntity);
    }

    private void updateDayRecord(DesktopOnlineSituationDayRecordEntity dayRecord,int onlineCount, int sleepCount) {
        int onlineCountDb = dayRecord.getOnlineCount();
        int sleepCountDb = dayRecord.getSleepCount();
        boolean hasChange = false;
        if (onlineCount > onlineCountDb) {
            hasChange = true;
            dayRecord.setOnlineCount(onlineCount);
        }
        if (sleepCount > sleepCountDb) {
            hasChange = true;
            dayRecord.setSleepCount(sleepCount);
        }
        if (hasChange) {
            desktopOnlineSituationDayRecordDAO.save(dayRecord);
        }

    }

}
