package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbMonitorDataDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHistoryDAO;
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
import java.util.List;
import java.util.UUID;

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
public class ServerResourceUsageHistoryCollectQuartzTaskTest {

    @Tested
    private ServerResourceUsageHistoryCollectQuartzTask quartz;

    @Injectable
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Injectable
    private ServerResourceUsageHistoryDAO serverResourceUsageHistoryDAO;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    @Injectable
    private ServerService serverService;

    /**
     * execute测试，无服务器
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteWithNoServers() throws BusinessException {
        List<PhysicalServerDTO> dtoList = Lists.newArrayList();
        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = dtoList;
            }  
        };
        
        quartz.execute(quartzTaskContext);
        
        new Verifications() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                times = 1;
            }  
        };
    }
    
    /**
     * execute测试，有服务器，无监控数据
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteWithServersAndNoMonitorData() throws BusinessException {
        List<PhysicalServerDTO> dtoList = new ArrayList<>();
        PhysicalServerDTO dto = new PhysicalServerDTO();
        dtoList.add(dto);
        List<CbbServerMonitorInfoDTO> serverMonitorInfoDTOList = new ArrayList<>();
        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = dtoList;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
                result = serverMonitorInfoDTOList;
            }  
        };
        
        quartz.execute(quartzTaskContext);
        
        new Verifications() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                times = 1;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
                times = 1;
            }  
        };
    }

    /**
     * execute测试，有服务器，有监控数据
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecute() throws BusinessException {
        List<PhysicalServerDTO> dtoList = new ArrayList<>();
        PhysicalServerDTO dto = new PhysicalServerDTO();
        dtoList.add(dto);
        
        List<CbbServerMonitorInfoDTO> serverMonitorInfoDTOList = new ArrayList<>();
        CbbServerMonitorInfoDTO monitorDto = new CbbServerMonitorInfoDTO();
        monitorDto.setUuid(UUID.randomUUID());
        
        List<CbbMonitorDataDTO<Double>> cpuUseRateDTOList = new ArrayList<>();
        CbbMonitorDataDTO<Double> monitorDataDTO = new CbbMonitorDataDTO<>();
        monitorDataDTO.setValue(1D);
        cpuUseRateDTOList.add(monitorDataDTO);
        monitorDto.setCpuUseRateDTOList(cpuUseRateDTOList );
        
        List<CbbMonitorDataDTO<Double>> memoryUseRateDTOList = new ArrayList<>();
        memoryUseRateDTOList.add(monitorDataDTO);
        monitorDto.setMemoryUseRateDTOList(memoryUseRateDTOList );
        
        List<CbbMonitorDataDTO<Double>> diskUseRateDTOList = new ArrayList<>();
        diskUseRateDTOList.add(monitorDataDTO);
        monitorDto.setStorageUseRateDTOList(diskUseRateDTOList );

        serverMonitorInfoDTOList.add(monitorDto);

        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = dtoList;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
                result = serverMonitorInfoDTOList;
            }
        };
        
        quartz.execute(quartzTaskContext);
        
        new Verifications() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                times = 1;
                serverService.getServerMonitorInfo((List<CbbServerRequestDTO>) any, (LocalDateTime) any, (LocalDateTime) any);
                times = 1;
            }  
        };
    }
}
