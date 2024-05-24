package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHistoryDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * ServerResourceUsageHistory表清理，清理7天前数据
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 * 
 * @author wanmulin
 */
@Service
public class ServerResourceUsageHistoryCleanQuartzTask implements /*SafetySingletonInitializer,*/ Runnable {

    @Autowired
    private ServerResourceUsageHistoryDAO serverResourceUsageHistoryDAO;

    // 数据保留时长，天
    private static final int RETAIN_TIME = 7;

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
        // 开始时间设置为7天前
        LocalDate cleanTime = currentTime.minusDays(RETAIN_TIME);

        serverResourceUsageHistoryDAO.deleteByCollectTimeBefore(DateUtil.localDateTimeToDate(cleanTime.atStartOfDay()));

    }
}
