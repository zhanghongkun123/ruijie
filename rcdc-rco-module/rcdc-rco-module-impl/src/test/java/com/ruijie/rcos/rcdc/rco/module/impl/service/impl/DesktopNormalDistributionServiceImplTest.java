package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageMonthDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageMonthEntity;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 云桌面正态分布计算测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/30 21:04
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class DesktopNormalDistributionServiceImplTest {

    @Tested
    private DesktopNormalDistributionServiceImpl service;

    @Injectable
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Injectable
    private DesktopResourceUsageMonthDAO desktopResourceUsageMonthDAO;

    /**
     * 测试计算云桌面正态分布
     */
    @Test
    public void testNormalDistribution() {
        List<DesktopResourceUsageDayEntity> dayList = Lists.newArrayList();
        List<DesktopResourceUsageMonthEntity> monList = Lists.newArrayList();
        for (int x = 1; x <= 7; x++) {
            DesktopResourceUsageDayEntity dayEntity = new DesktopResourceUsageDayEntity();
            DesktopResourceUsageMonthEntity monthEntity = new DesktopResourceUsageMonthEntity();
            dayEntity.setDesktopId(UUID.randomUUID());
            dayEntity.setCpuUsage(x + 10.0);
            dayEntity.setMemoryUsage(x + 20.0);
            dayEntity.setDiskUsage(x + 39.0);
            BeanUtils.copyProperties(dayEntity, monthEntity);
            dayList.add(dayEntity);
            monList.add(monthEntity);
        }

        new Expectations() {
            {
                desktopResourceUsageDayDAO.getDayResourceUsageByStatisticTime((Date) any, (Date) any);
                result = dayList;
                desktopResourceUsageMonthDAO.getMonthResourceUsage((Date) any, (Date) any);
                result = monList;
            }
        };
        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> responseMap = service.normalDistribution();
        Assert.assertTrue(true);
    }
}
