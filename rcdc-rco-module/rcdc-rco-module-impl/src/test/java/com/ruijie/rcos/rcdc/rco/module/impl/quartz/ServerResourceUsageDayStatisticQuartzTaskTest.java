package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHourDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageDayEntity;
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
public class ServerResourceUsageDayStatisticQuartzTaskTest {

    @Tested
    private ServerResourceUsageDayStatisticQuartzTask quartz;

    @Injectable
    private ServerResourceUsageHourDAO serverResourceUsageHourDAO;

    @Injectable
    private ServerResourceUsageDayDAO serverResourceUsageDayDAO;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    /**
     * execute方法测试，无统计数据
     */
    @Test
    public void testExecuteWithNoStaticDate() {
        List<ServerResourceUsageDayEntity> dayEntityList = new ArrayList<>();
        List<ServerResourceUsageHourEntity> entityList = new ArrayList<>();
        new Expectations() {
            {
                serverResourceUsageDayDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                result = dayEntityList;
                serverResourceUsageHourDAO.queryAverageUsage((Date) any, (Date) any);
                result = entityList;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                serverResourceUsageDayDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                times = 1;
                serverResourceUsageHourDAO.queryAverageUsage((Date) any, (Date) any);
            }
        };
    }

    /**
     * execute方法测试，正常补偿
     */
    @Test
    public void testExecute() {
        List<ServerResourceUsageDayEntity> dayEntityList = new ArrayList<>();
        ServerResourceUsageDayEntity dayEntity = new ServerResourceUsageDayEntity();
        dayEntity.setStatisticTime(new Date());
        dayEntityList.add(dayEntity);
        List<ServerResourceUsageHourEntity> entityList = new ArrayList<>();
        ServerResourceUsageHourEntity hourEntity = new ServerResourceUsageHourEntity();
        entityList.add(hourEntity);

        new Expectations() {
            {
                serverResourceUsageDayDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                result = dayEntityList;
                serverResourceUsageHourDAO.queryAverageUsage((Date) any, (Date) any);
                result = entityList;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                serverResourceUsageDayDAO.findByStatisticTimeBetween((Date) any, (Date) any);
                times = 1;
                serverResourceUsageHourDAO.queryAverageUsage((Date) any, (Date) any);
            }
        };
    }

}
