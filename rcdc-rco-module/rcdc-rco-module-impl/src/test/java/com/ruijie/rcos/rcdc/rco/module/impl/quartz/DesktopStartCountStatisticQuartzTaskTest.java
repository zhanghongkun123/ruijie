package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.DeskopOpLogPageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDesktopOpLogDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopStartCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopStartCountDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 每天获取云桌面开机数定时任务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/30 15:39
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class DesktopStartCountStatisticQuartzTaskTest {

    @Tested
    private DesktopStartCountStatisticQuartzTask desktopStartCountStatisticQuartzTask;

    @Injectable
    CbbDesktopOpLogMgmtAPI cbbUserDesktopOpLogMgmtAPI;

    @Injectable
    DesktopStartCountDayDAO desktopStartCountDayDAO;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    @Injectable
    PlatformAPI platformAPI;

    /**
     * 前置方法
     */
    @Before
    public void before() {
        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return "test";
            }
        };
    }

    /**
     * 数据完整
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testExecuteWithData() throws BusinessException {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_DAY_MILLIS);

        List<DesktopStartCountDayEntity> entityList = Lists.newArrayList();
        Date now = new Date();
        for (int i = 1; i <= Constants.DEFAULT_RETRY_DAYS; i++) {
            DesktopStartCountDayEntity entity = new DesktopStartCountDayEntity();
            Date date = DateUtil.computeDate(now, Calendar.DAY_OF_MONTH, 0 - i);
            entity.setStatisticTime(date);
            entityList.add(entity);
        }
        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                desktopStartCountDayDAO.listStartCount((Date) any, (Date) any);
                result = entityList;
            }
        };
        desktopStartCountStatisticQuartzTask.execute(quartzTaskContext);
        Assert.assertTrue(true);
    }

    /**
     * 数据缺失
     * @throws BusinessException 异常
     */
    @Test
    public void testExecute() throws BusinessException {

        List<DesktopStartCountDayEntity> entityList = Lists.newArrayList();
        Date now = new Date();
        DesktopStartCountDayEntity entity = new DesktopStartCountDayEntity();
        Date date = DateUtil.computeDate(now, Calendar.DAY_OF_MONTH, -2);
        entity.setStatisticTime(date);
        entityList.add(entity);
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(1000 * 60 * 60 * 24L);
        CbbDesktopOpLogDetailDTO dto = new CbbDesktopOpLogDetailDTO();
        dto.setDesktopId(UUID.randomUUID());
        dto.setEventName("test");
        CbbDesktopOpLogDetailDTO[] dtoArr = new CbbDesktopOpLogDetailDTO[]{dto};
        DefaultPageResponse<CbbDesktopOpLogDetailDTO> response = new DefaultPageResponse();
        response.setItemArr(dtoArr);
        new Expectations() {
            {
                desktopStartCountDayDAO.listStartCount((Date) any, (Date) any);
                result = entityList;
                platformAPI.getSystemTime();
                result = timeResponse;
                cbbUserDesktopOpLogMgmtAPI.pageQuery((DeskopOpLogPageRequest) any);
                result = response;
            }
        };
        desktopStartCountStatisticQuartzTask.execute(quartzTaskContext);
        Assert.assertTrue(true);
    }
}
