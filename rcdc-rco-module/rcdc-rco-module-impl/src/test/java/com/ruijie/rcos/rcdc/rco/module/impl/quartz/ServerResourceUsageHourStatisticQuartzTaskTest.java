package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHistoryDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHourDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHistoryEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHourEntity;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月2日
 * 
 * @author wanmulin
 */
@RunWith(JMockit.class)
public class ServerResourceUsageHourStatisticQuartzTaskTest {

    @Tested
    private ServerResourceUsageHourStatisticQuartzTask quartz;

    @Injectable
    private ServerResourceUsageHistoryDAO serverResourceUsageHistoryDAO;

    @Injectable
    private ServerResourceUsageHourDAO serverResourceUsageHourDAO;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    /**
     * execute方法测试，无统计数据，无历史数据
     */
    @Test
    public void testExecuteWithNoEntitys() {
        new Expectations() {
            {
                serverResourceUsageHourDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                result = null;
                serverResourceUsageHistoryDAO.queryAverageUsage((Date) any, (Date) any);
                result = null;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                serverResourceUsageHourDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                times = 1;
                serverResourceUsageHistoryDAO.queryAverageUsage((Date) any, (Date) any);
            }
        };
    }

    /**
     * execute方法测试
     */
    @Test
    public void testExecute() {
        List<ServerResourceUsageHourEntity> hourEntityList = new ArrayList<>();
        ServerResourceUsageHourEntity hourEntity = new ServerResourceUsageHourEntity();
        hourEntity.setStatisticTime(new Date());
        hourEntityList.add(hourEntity);
        List<ServerResourceUsageHistoryEntity> historyEntityList = new ArrayList<>();
        ServerResourceUsageHistoryEntity historyEntity = new ServerResourceUsageHistoryEntity();
        historyEntityList.add(historyEntity);

        new Expectations() {
            {
                serverResourceUsageHourDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                result = hourEntity;
                serverResourceUsageHistoryDAO.queryAverageUsage((Date) any, (Date) any);
                result = historyEntity;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                serverResourceUsageHourDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                times = 1;
                serverResourceUsageHistoryDAO.queryAverageUsage((Date) any, (Date) any);
            }
        };
    }
}
