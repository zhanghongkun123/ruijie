package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * 
 * Description: 服务器资源日统计表清理定时任务，每日凌晨00:30:00执行
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月29日
 * 
 * @author wanmulin
 */
@Service
public class ServerResourceUsageDayCleanQuartzTask implements /*SafetySingletonInitializer,*/ Runnable {

    @Autowired
    private ServerResourceUsageDayDAO serverResourceUsageDayDAO;

    // 数据保留时长，天
    private static final int RETAIN_TIME = 180;

//    @Override
    /**
     * 屏蔽SafetySingletonInitializer实现
     */
    public void safeInit() {
        String cronExpression = "0 30 0 * * ?";
        try {
            ThreadExecutors.scheduleWithCron(this.getClass().getSimpleName(), this, cronExpression);
        } catch (ParseException e) {
            throw new RuntimeException("定时任务[" + this.getClass() + "]cron表达式[" + cronExpression + "]解析异常", e);
        }
    }

    @Override
    public void run() {
        LocalDate currentTime = LocalDate.now();
        LocalDate cleanTime = currentTime.minusDays(RETAIN_TIME);

        serverResourceUsageDayDAO.deleteByStatisticTimeBefore(DateUtil.localDateToDate(cleanTime));

    }
}
