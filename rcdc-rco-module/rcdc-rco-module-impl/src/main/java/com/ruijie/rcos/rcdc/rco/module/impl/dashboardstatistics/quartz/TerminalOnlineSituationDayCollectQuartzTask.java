package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.DashboardStatisticsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.TerminalOnlineSituationDayRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.TerminalOnlineSituationHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.TerminalOnlineSituationDayRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.TerminalOnlineSituationHourRecordEntity;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * Description: 首页每天收集终端在线情况定时任务
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
@Service
@Quartz(cron = "0 0 0 * * ?", scheduleName = DashboardStatisticsBusinessKey.RCDC_RCO_TERMINAL_ONLINE_SITUATION_COLLECT_EVERY_DAY,
        scheduleTypeCode = ScheduleTypeCodeConstants.TERMINAL_ONLINE_SITUATION_COLLECT_EVERY_DAY, blockInMaintenanceMode = true)
public class TerminalOnlineSituationDayCollectQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOnlineSituationDayCollectQuartzTask.class);

    private static final int ONE_YEAR_AGO = -365;

    private static final int ONE_DAY_AGO = -1;

    @Autowired
    private TerminalOnlineSituationHourRecordDAO terminalOnlineSituationHourRecordDAO;

    @Autowired
    private TerminalOnlineSituationDayRecordDAO terminalOnlineSituationDayRecordDAO;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        LOGGER.info("首页每天收集终端在线情况定时任务开始");

        // 日表删除一年前的数据
        Date currentDate = new Date();
        Date lastYear = DateUtils.addDays(currentDate, ONE_YEAR_AGO);
        try {
            terminalOnlineSituationDayRecordDAO.deleteByCreateTimeLessThan(lastYear);
        } catch (Exception e) {
            LOGGER.error("日表清理失败，原因是：", e);
        }
        // 小时表删除一天前的数据
        Date lastDay = DateUtils.addDays(currentDate, ONE_DAY_AGO);
        try {
            terminalOnlineSituationHourRecordDAO.deleteByCreateTimeLessThan(lastDay);
        } catch (Exception e) {
            LOGGER.error("小时表清理失败，原因是：", e);
        }
        // 查询上一天的小时表数据，合并到日表中
        String lastDayStr = DateFormatUtils.format(lastDay, "yyyy-MM-dd");
        List<TerminalOnlineSituationHourRecordEntity> terminalOnlineHourRecordEntityList =
                terminalOnlineSituationHourRecordDAO.findByDayKey(lastDayStr);
        if (CollectionUtils.isEmpty(terminalOnlineHourRecordEntityList)) {
            LOGGER.info("上一天的小时表没有数据，无需插入");
            return;
        }
        // key值是terminalId,value值是TerminalOnlineDayRecord实体。
        Map<String, TerminalOnlineSituationDayRecordEntity> terminalOnlineDayRecordEntityMap =
                new HashMap<>(terminalOnlineHourRecordEntityList.size());
        terminalOnlineHourRecordEntityList.forEach(entity -> {
            if (!terminalOnlineDayRecordEntityMap.containsKey(entity.getTerminalId())) {
                TerminalOnlineSituationDayRecordEntity terminalOnlineDayRecordEntity =
                        new TerminalOnlineSituationDayRecordEntity(entity.getTerminalId(), entity.getPlatform(), entity.getDayKey());
                LOGGER.info("终端<{}>类型<{}>", terminalOnlineDayRecordEntity.getTerminalId(), terminalOnlineDayRecordEntity.getPlatform());
                terminalOnlineDayRecordEntityMap.put(entity.getTerminalId(), terminalOnlineDayRecordEntity);
            }
        });
        if (!CollectionUtils.isEmpty(terminalOnlineDayRecordEntityMap.values())) {
            try {
                terminalOnlineSituationDayRecordDAO.saveAll(terminalOnlineDayRecordEntityMap.values());
            } catch (Exception e) {
                LOGGER.error("插入数据失败，原因是：", e);
            }
        }
    }
}
