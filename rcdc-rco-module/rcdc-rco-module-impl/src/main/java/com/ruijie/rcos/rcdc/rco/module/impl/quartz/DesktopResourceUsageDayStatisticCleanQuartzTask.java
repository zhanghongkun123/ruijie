package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * Description: 桌面资源利用率日统计表清理定时任务，每日凌晨00:30:00执行执行执行
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 15:13
 *
 * @author zhangyichi
 */
@Service
public class DesktopResourceUsageDayStatisticCleanQuartzTask implements /*SafetySingletonInitializer,*/ Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopResourceUsageDayStatisticCleanQuartzTask.class);

    //数据保留时长，天
    private static final int RETAIN_TIME = 40;

    @Autowired
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

//    @Override
    /**
     * 屏蔽SafetySingletonInitializer实现
     */
    public void safeInit() {
        String cronExpression = "0 30 0 * * ? *";
        try {
            ThreadExecutors.scheduleWithCron(this.getClass().getSimpleName(), this, cronExpression);
        } catch (ParseException e) {
            throw new RuntimeException("定时任务[" + this.getClass() + "]cron表达式[" + cronExpression + "]解析异常", e);
        }
    }

    @Override
    public void run() {
        LOGGER.info("桌面资源利用率日统计表清理定时任务开始======");
        LocalDate date = LocalDate.now();
        LocalDate statisticTime = date.minusDays(RETAIN_TIME);
        desktopResourceUsageDayDAO.deleteByStatisticTimeLessThan(DateUtil.localDateToDate(statisticTime));
        LOGGER.info("桌面资源利用率日统计表清理定时任务结束======");
    }
}
