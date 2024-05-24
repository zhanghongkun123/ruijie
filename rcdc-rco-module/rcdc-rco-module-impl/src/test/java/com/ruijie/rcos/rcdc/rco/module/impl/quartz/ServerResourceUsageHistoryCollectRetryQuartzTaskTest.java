package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHistoryDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHistoryEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ServerService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月28日
 * 
 * @author wanmulin
 */
@RunWith(JMockit.class)
public class ServerResourceUsageHistoryCollectRetryQuartzTaskTest {

    @Tested
    private ServerResourceUsageHistoryCollectRetryQuartzTask quartz;

    @Injectable
    private ServerResourceUsageHistoryDAO serverResourceUsageHistoryDAO;

    @Injectable
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    @Injectable
    private ServerService serverService;

    @Injectable
    private PlatformAPI platformAPI;

    /**
     * execute方法测试，历史表无数据
     * @throws BusinessException 异常
     */
    @Test
    public void testExecuteWithNoHistory() throws BusinessException {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_HOUR_MILLIS);

        List<PhysicalServerDTO> dtoList = Lists.newArrayList();

        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                result = null;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = dtoList;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                platformAPI.getSystemTime();
                times = 1;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                times = 1;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                times = 1;
            }
        };
    }

    /**
     * execute方法测试，获取服务器监控数据出现异常
     * @throws BusinessException 异常
     */
    @Test
    public void testExecuteWithMonitorInfoException() throws BusinessException {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_HOUR_MILLIS);

        List<PhysicalServerDTO> dtoList = new ArrayList<>();
        PhysicalServerDTO dto = new PhysicalServerDTO();
        dtoList.add(dto);

        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                result = null;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = dtoList;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
                result = new BusinessException("xxx");
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                platformAPI.getSystemTime();
                times = 1;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                times = 1;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                times = 1;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
            }
        };
    }

    /**
     * execute方法测试，无服务器监控数据出现异常
     * @throws BusinessException 异常
     */
    @Test
    public void testExecuteWithNoMonitorInfo() throws BusinessException {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_HOUR_MILLIS);

        List<PhysicalServerDTO> dtoList = new ArrayList<>();
        PhysicalServerDTO dto = new PhysicalServerDTO();
        dtoList.add(dto);

        List<CbbServerMonitorInfoDTO> monitorInfoList = new ArrayList<>();

        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                result = null;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = dtoList;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
                result = monitorInfoList;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                platformAPI.getSystemTime();
                times = 1;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                times = 1;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                times = 1;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
            }
        };
    }

    /**
     * execute方法测试
     * @throws BusinessException 异常
     */
    @Test
    public void testExecute() throws BusinessException {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.ONE_DAY_MILLIS);

        List<ServerResourceUsageHistoryEntity> entityList = Lists.newArrayList();
        ServerResourceUsageHistoryEntity entity = new ServerResourceUsageHistoryEntity();
        entity.setCollectTime(new Date());
        entityList.add(entity);
        List<PhysicalServerDTO> dtoList = new ArrayList<>();
        PhysicalServerDTO dto = new PhysicalServerDTO();
        dtoList.add(dto);

        List<CbbServerMonitorInfoDTO> monitorInfoList = new ArrayList<>();
        CbbServerMonitorInfoDTO serverMonitorInfoDTO = new CbbServerMonitorInfoDTO();
        monitorInfoList.add(serverMonitorInfoDTO);
        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                result = entityList;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = dtoList;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                platformAPI.getSystemTime();
                times = 1;
                serverResourceUsageHistoryDAO.getByCollectTimeBetween((Date) any, (Date) any);
                times = 1;
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                times = 1;
            }
        };
    }
}
