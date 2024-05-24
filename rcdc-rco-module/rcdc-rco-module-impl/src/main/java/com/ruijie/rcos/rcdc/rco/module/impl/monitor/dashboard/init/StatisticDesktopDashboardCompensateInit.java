package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.init;

import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopSessionLogDayRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopSessionLogHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserConnectDesktopFaultLogDAO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.CompensateSessionType;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopPoolDashboardService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

/**
 * Description: 重启容器后，进行桌面池报表相关的补偿任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/18 9:45
 *
 * @author yxq
 */
@Service
public class StatisticDesktopDashboardCompensateInit implements SafetySingletonInitializer {

    private static final int ONE_DAY_AGO = -1;

    private static final int ONE_HOUR_AGO = -1;

    private static final String HOUR_KEY_PATTERN = "yyyy-MM-dd HH";

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticDesktopDashboardCompensateInit.class);

    @Autowired
    private DesktopSessionLogDayRecordDAO desktopSessionLogDayRecordDAO;

    @Autowired
    private DesktopPoolDashboardService desktopPoolDashboardService;

    @Autowired
    private DesktopSessionLogHourRecordDAO desktopSessionLogHourRecordDAO;

    @Autowired
    private UserConnectDesktopFaultLogDAO userConnectDesktopFaultLogDAO;

    @Override
    public void safeInit() {
        LOGGER.info("服务器重启，执行桌面池报表数据补偿任务");

        // 结束已经断开的会话记录
        try {
            LOGGER.info("进行会话记录补偿");
            desktopPoolDashboardService.compensateFishedSessionLog(CompensateSessionType.START_UP);
        } catch (Exception e) {
            LOGGER.error("结束会话记录失败，失败原因：", e);
        }

        Date currentDate = new Date();
        try {
            // 清空超过时间的数据
            desktopPoolDashboardService.clearExpiredData(currentDate);

            // 补偿hour表中的连接失败数据
            compensateHourConnectFaultData(currentDate);

            // 补偿day表中的数据
            compensateDayData(currentDate);
        } catch (Exception e) {
            LOGGER.error("执行桌面池报表数据补偿任务失败，失败原因：", e);
        }
    }


    private void compensateHourConnectFaultData(Date currentDate) {
        // 获取数据库hour表里面最大的小时key
        String maxHourKey = desktopSessionLogHourRecordDAO.obtainMaxHourKey(DesktopStatisticsTypeEnum.CONNECT_FAIL);

        // 获取上个小时的key
        String lastHourKey = DateUtil.getStatisticHourKey(DateUtils.addHours(currentDate, ONE_HOUR_AGO));

        // 获取fault_log表里面最大的创建时间
        Date faultMaxTime = userConnectDesktopFaultLogDAO.obtainMaxCreateTime();

        // 如果需要补偿
        if (needStatisticHourFaultLog(maxHourKey, lastHourKey, faultMaxTime)) {
            LOGGER.info("数据库小时表中连接失败最新的数据为[{}]，需要补偿[{}]的数据", maxHourKey, faultMaxTime);
            desktopPoolDashboardService.statisticHourConnectFaultData(faultMaxTime);
        }
    }


    private boolean needStatisticHourFaultLog(String maxHourKey, String lastHourKey, Date faultMaxTime) {
        // 如果连接失败表里面没有数据，则证明无需补偿
        if (faultMaxTime == null) {
            LOGGER.info("会话连接失败表中没有记录，无需补偿");
            return false;
        }

        String faultMaxHourKey = DateUtil.getStatisticHourKey(faultMaxTime);
        LOGGER.info("maxHourKey={},lastHourKey={},faultMaxHourKey={}", maxHourKey, lastHourKey, faultMaxHourKey);

        // 如果连接失败表里面最大的创建时间大于hour表里面的最大hourKey，则证明连接失败表里面有数据是没有统计到的
        // 上个小时的hourKey大于等于hour表里面的最大hourKey，则证明这些没有统计到的数据不是这个小时产生的，需要进行统计
        return StringUtils.compare(faultMaxHourKey, maxHourKey, true) > 0
                && StringUtils.compare(lastHourKey, faultMaxHourKey, true) >= 0;
    }

    private void compensateDayData(Date currentDate) throws ParseException {
        String yesterdayKey = DateUtil.getStatisticDayKey(DateUtils.addDays(currentDate, ONE_DAY_AGO));
        String maxDayKey = desktopSessionLogDayRecordDAO.obtainMaxDayKey();

        if (Objects.equals(yesterdayKey, maxDayKey)) {
            LOGGER.info("数据库中已经有上一天的数据，无需再次统计");
            return;
        }

        // 如果小时表中没有数据，那就不需要补偿day表的数据
        String maxHourKey = desktopSessionLogHourRecordDAO.obtainMaxHourKey(DesktopStatisticsTypeEnum.TOTAL_USED_RATE);
        if (maxHourKey == null) {
            LOGGER.info("会话连接小时表中没有数据，无需补偿day表的数据");
            return;
        }

        Date needStatisticDayTime = DateUtils.parseDate(maxHourKey, HOUR_KEY_PATTERN);
        // 如果小时表中最新的数据是今天的，今天还没有结束，则不需要统计今天的数据
        if (Objects.equals(DateUtil.getStatisticDayKey(needStatisticDayTime), DateUtil.getStatisticDayKey(currentDate))) {
            LOGGER.info("会话连接小时表中数据为最大时间为[{}]，是今天的数据，无需补偿day表的数据", maxHourKey);
            return;
        }

        String dayKey = DateUtil.getStatisticDayKey(needStatisticDayTime);
        String monthKey = DateUtil.getStatisticMonthKey(needStatisticDayTime);
        LOGGER.info("day表中最新的数据为[{}]，hour表中最新数据为[{}]，需要补偿[{}]的数据", maxDayKey, maxHourKey, dayKey);
        desktopPoolDashboardService.statisticDayData(monthKey, dayKey);
    }
}
