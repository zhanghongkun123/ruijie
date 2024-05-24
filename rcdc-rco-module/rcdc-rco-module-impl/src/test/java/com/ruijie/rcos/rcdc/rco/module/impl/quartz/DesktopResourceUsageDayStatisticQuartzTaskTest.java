package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbGetDeskVDIPageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 云桌面利用率日统计单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 15:13
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class DesktopResourceUsageDayStatisticQuartzTaskTest {

    @Tested
    private DesktopResourceUsageDayStatisticQuartzTask quartz;

    @Injectable
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Injectable
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Injectable
    private DesktopService desktopService;

    @Injectable
    private PlatformAPI platformAPI;

    /**
     * 初始化方法
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

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    /**
     * 获取云桌面信息失败
     *
     * @throws Exception 异常
     */
    @Test(expected = BusinessException.class)
    public void executeDeskExceptionTest() throws Exception {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_DAY_MILLIS);

        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                cbbVDIDeskMgmtAPI.pageQueryDeskVDI((CbbGetDeskVDIPageRequest) any);
                result = null;
            }
        };

        quartz.execute(quartzTaskContext);
    }

    /**
     * 获取云桌面信息为空
     *
     * @throws Exception 异常
     */
    @Test(expected = BusinessException.class)
    public void executeDeskEmptyExceptionTest() throws Exception {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_DAY_MILLIS);

        DefaultPageResponse<CbbDeskDTO> deskDtoResponse = new DefaultPageResponse<>();
        deskDtoResponse.setItemArr(null);
        List<LocalDate> entityList = Lists.newArrayList();

        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                desktopResourceUsageDayDAO.queryStatisticTimeList(((Date) any), (Date) any);
                result = entityList;
                cbbVDIDeskMgmtAPI.pageQueryDeskVDI((CbbGetDeskVDIPageRequest) any);
                result = deskDtoResponse;
            }
        };
        quartz.execute(quartzTaskContext);
    }

    /**
     * 正常获取云桌面信息
     *
     * @throws Exception 异常
     */
    @Test
    public void executeDeskTest() throws Exception {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_DAY_MILLIS);
        DefaultPageResponse<CbbDeskDTO> deskDtoResponse = new DefaultPageResponse<>();
        CbbDeskDTO deskDTO = new CbbDeskDTO();
        CbbDeskDTO[] cbbDeskDTOArr = new CbbDeskDTO[]{deskDTO};
        deskDtoResponse.setItemArr(cbbDeskDTOArr);
        List<LocalDate> entityList = Lists.newArrayList();
        List<DesktopUsageDTO> dtoList = new ArrayList<DesktopUsageDTO>();
        DesktopUsageDTO desktopUsageDTO = new DesktopUsageDTO();
        desktopUsageDTO.setCpuUsage(10D);
        dtoList.add(desktopUsageDTO);
        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                desktopResourceUsageDayDAO.queryStatisticTimeList(((Date) any), (Date) any);
                result = entityList;
                cbbVDIDeskMgmtAPI.pageQueryDeskVDI((CbbGetDeskVDIPageRequest) any);
                result = deskDtoResponse;
                desktopService.listDesktopUsageById((UUID[]) any, (Date) any, (Date) any);
                result = dtoList;
            }
        };
        quartz.execute(quartzTaskContext);
        Assert.assertTrue(true);
    }
}
