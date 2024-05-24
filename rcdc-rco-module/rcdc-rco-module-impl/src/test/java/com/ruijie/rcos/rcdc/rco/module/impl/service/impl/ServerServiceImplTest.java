package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerCpuDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerMemoryDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbListServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: ServerServiceImpl单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/7 19:02
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class ServerServiceImplTest {

    @Tested
    private ServerServiceImpl serverServiceImpl;

    @Injectable
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Injectable
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Injectable
    private CabinetDAO cabinetDAO;


    /**
     * 测试获取服务器最新监控信息
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetServerMonitorInfo() throws BusinessException {
        List<CbbServerRequestDTO> serverRequestDTOList = new ArrayList<>();
        CbbServerRequestDTO serverRequestDTO = new CbbServerRequestDTO();
        serverRequestDTOList.add(serverRequestDTO);
        List<CbbServerMonitorInfoDTO> serverMonitorInfoDTOList = new ArrayList<>();
        CbbServerMonitorInfoDTO serverMonitorInfoDTO = new CbbServerMonitorInfoDTO();
        serverMonitorInfoDTOList.add(serverMonitorInfoDTO);

        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listServerMonitorInfo((CbbListServerMonitorInfoDTO) any);
                result = serverMonitorInfoDTOList;
            }
        };

        serverServiceImpl.getServerMonitorInfo(serverRequestDTOList, LocalDateTime.now(), LocalDateTime.now());

        Assert.assertTrue(true);
    }

    /**
     * 测试未获取到服务器最新监控信息
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetServerMonitorInfoWithNull() throws BusinessException {
        List<CbbServerRequestDTO> serverRequestDTOList = new ArrayList<>();
        CbbServerRequestDTO serverRequestDTO = new CbbServerRequestDTO();
        serverRequestDTOList.add(serverRequestDTO);

        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listServerMonitorInfo((CbbListServerMonitorInfoDTO) any);
                result = null;
            }
        };

        List<CbbServerMonitorInfoDTO> serverMonitorInfoDTOList = serverServiceImpl
            .getServerMonitorInfo(serverRequestDTOList, LocalDateTime.now(), LocalDateTime.now());
        Assert.assertEquals(serverMonitorInfoDTOList, new ArrayList<>());
    }

    /**
     * 测试获取物理服务器信息
     */
    @Test
    public void testListAllPhysicalServer() {
        List<PhysicalServerDTO> physicalServerDTOList = new ArrayList<>();
        PhysicalServerDTO physicalServerDTO = new PhysicalServerDTO();
        PhysicalServerCpuDTO cpuInfo = new PhysicalServerCpuDTO();
        PhysicalServerMemoryDTO memoryInfo = new PhysicalServerMemoryDTO();
        PhysicalServerStorageDTO storageInfo = new PhysicalServerStorageDTO();
        physicalServerDTO.setCpuInfo(cpuInfo);
        physicalServerDTO.setMemoryInfo(memoryInfo);
        physicalServerDTO.setStorageInfo(storageInfo);
        physicalServerDTOList.add(physicalServerDTO);

        List<CabinetServerMappingEntity> entityList = new ArrayList<>();
        CabinetServerMappingEntity entity = new CabinetServerMappingEntity();
        entityList.add(entity);

        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = physicalServerDTOList;
            }
        };

        new Expectations() {
            {
                cabinetServerMappingDAO.findAll();
                result = entityList;
            }
        };

        serverServiceImpl.listAllPhysicalServer();
        Assert.assertTrue(true);
    }
}
