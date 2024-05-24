package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageMonthDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageMonthEntity;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 云桌面资源月统计定时任务单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 16:14
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class DesktopResourceUsageMonthStatisticQuartzTaskTest {

    @Tested
    private DesktopResourceUsageMonthStatisticQuartzTask quartz;

    @Injectable
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Injectable
    private DesktopResourceUsageMonthDAO desktopResourceUsageMonthDAO;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    /**
     * 云桌面月统计，正常流程
     * @throws Exception 异常
     */
    @Test
    public void executeTest() throws Exception {
        List<Date> resultList = Lists.newArrayList();
        List<DesktopResourceUsageDayEntity> dayEntityList = Lists.newArrayList();
        DesktopResourceUsageDayEntity entity = new DesktopResourceUsageDayEntity();
        entity.setId(UUID.randomUUID());
        entity.setDesktopId(UUID.randomUUID());
        entity.setStatisticTime(new Date());
        entity.setCreateTime(new Date());
        entity.setCpuUsage(20D);
        entity.setMemoryUsage(20D);
        entity.setDiskUsage(20D);
        dayEntityList.add(entity);
        new Expectations() {
            {
                desktopResourceUsageMonthDAO.checkMonthUsageDate((Date)any);
                result = resultList;
                desktopResourceUsageDayDAO.getAverageUsage((Date)any, (Date)any);
                result = dayEntityList;
                desktopResourceUsageMonthDAO.saveAll((List<DesktopResourceUsageMonthEntity>)any);
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                desktopResourceUsageMonthDAO.checkMonthUsageDate((Date)any);
                times = 1;
                desktopResourceUsageDayDAO.getAverageUsage((Date)any, (Date)any);
                times = 1;
                desktopResourceUsageMonthDAO.saveAll((List<DesktopResourceUsageMonthEntity>)any);
                times = 1;
            }
        };
    }

    /**
     * 未获取到云桌面使用信息
     * @throws Exception 异常
     */
    @Test
    public void executeEmptyTest() throws Exception {
        new Expectations() {
            {
                desktopResourceUsageDayDAO.getAverageUsage((Date) any, (Date) any);
                result = null;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                desktopResourceUsageMonthDAO.saveAll((List<DesktopResourceUsageMonthEntity>) any);
                times = 0;
            }
        };
    }
}
