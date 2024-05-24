package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.SystemInfoAPI;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryCpuInfoResponse;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryCurrentNetworkSpeedResponse;
import com.ruijie.rcos.base.alarm.module.def.api.response.QuerySystemInfoResponse;
import com.ruijie.rcos.base.alarm.module.def.dto.CpuDTO;
import com.ruijie.rcos.base.alarm.module.def.dto.DiskDTO;
import com.ruijie.rcos.base.alarm.module.def.dto.MemoryDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWindowsLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskTypeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsItem;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.StatisticsResourceUseDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.StatisticsResourceUseDTO.DiskUseDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.BandwidthSpeedResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalStatisticsDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.TerminalStatisticsItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月9日
 *
 * @author zhuangchenwu
 */
public class StatisticsControllerTest {

    @Tested
    private StatisticsController statisticsController;

    @Injectable
    private StatisticsAPI statisticsAPI;

    @Injectable
    private SystemInfoAPI systemInfoAPI;

    @Injectable
    private CbbWindowsLicenseAPI cbbWindowsLicenseAPI;

    @Injectable
    private ClusterServerStatisticsAPI clusterServerStatisticsAPI;

    @Injectable
    private LicenseAPI licenseAPI;

    @Injectable
    private AdminDataPermissionAPI roleGroupPermissionAPI;

    @Injectable
    private PermissionHelper permissionHelper;

    @Mocked
    private SessionContext sessionContext;

    @Injectable
    private CbbClusterServerMgmtAPI cbbClusterServerMgmtAPI;

    @Injectable
    private ComputerBusinessAPI computerBusinessAPI;

    /**
     * 测试statisticsDesktop方法
     */
    @Test
    public void testStatisticsDesktop() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        final DesktopStatisticsResponse apiResponse = new DesktopStatisticsResponse();
        final DesktopStatisticsItem item = new DesktopStatisticsItem();
        final int online = 10;
        final int offline = 20;
        final int neverLogin = 5;
        final int sleep = 0;
        final int total = 30;


        item.setOnline(online);
        item.setOffline(offline);
        item.setNeverLogin(neverLogin);
        item.setSleep(sleep);
        item.setTotal(total);
        apiResponse.setVdi(item);
        new Expectations() {
            {
                permissionHelper.isAllGroupPermission(sessionContext);
                result = false;
                statisticsAPI.statisticsDesktop((DeskTypeRequest) any);
                result = apiResponse;
            }
        };

        DefaultWebResponse webResponse = statisticsController.statisticsDesktop(request, sessionContext);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        DesktopStatisticsResponse content = (DesktopStatisticsResponse) webResponse.getContent();
        DesktopStatisticsItem vdiItem = content.getVdi();
        assertEquals(online, vdiItem.getOnline().intValue());
        assertEquals(offline, vdiItem.getOffline().intValue());
        assertEquals(neverLogin, vdiItem.getNeverLogin().intValue());
        assertEquals(sleep, vdiItem.getSleep().intValue());
        assertEquals(total, vdiItem.getTotal().intValue());
    }

    /**
     * 测试statisticsTerminal方法
     */
    @Test
    public void testStatisticsTerminal() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        final CbbTerminalStatisticsDTO apiResponse = new CbbTerminalStatisticsDTO();
        final TerminalStatisticsItem item = new TerminalStatisticsItem();
        final int online = 10;
        final int offline = 20;
        final int neverLogin = 5;
        final int total = 30;

        item.setOnline(online);
        item.setOffline(offline);
        item.setNeverLogin(neverLogin);
        item.setTotal(total);
        apiResponse.setVdi(item);
        apiResponse.setIdv(item);
        apiResponse.setApp(item);
        apiResponse.setPc(item);
        apiResponse.setVoi(item);
        new Expectations() {
            {
                permissionHelper.isAllGroupPermission(sessionContext);
                result = false;
                statisticsAPI.statisticsTerminal((UUID[]) any);
                result = apiResponse;
            }
        };
        DefaultWebResponse webResponse = statisticsController.statisticsTerminal(request, sessionContext);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        CbbTerminalStatisticsDTO content = (CbbTerminalStatisticsDTO) webResponse.getContent();
        TerminalStatisticsItem vdiItem = content.getVdi();

        assertEquals(online, vdiItem.getOnline().intValue());
        assertEquals(offline, vdiItem.getOffline().intValue());
        assertEquals(neverLogin, vdiItem.getNeverLogin().intValue());
        assertEquals(total, vdiItem.getTotal().intValue());
    }

    /**
     * 测试statisticsResourceUse方法，磁盘数据为空的情况
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testStatisticsResourceUseWhileDiskDateIsNull() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        final QuerySystemInfoResponse response = new QuerySystemInfoResponse();
        final CpuDTO cpuDTO = new CpuDTO();
        final MemoryDTO memoryDTO = new MemoryDTO();

        response.setCpu(cpuDTO);
        response.setMemory(memoryDTO);
        response.setDiskArr(new DiskDTO[] {});
        new Expectations() {
            {
                systemInfoAPI.querySystemInfo();
                result = response;
            }
        };
        DefaultWebResponse webResponse = statisticsController.statisticsResourceUse(request);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        StatisticsResourceUseDTO content = (StatisticsResourceUseDTO) webResponse.getContent();
        assertEquals(cpuDTO, content.getCpu());
        assertEquals(memoryDTO, content.getMemory());

        DiskUseDTO diskUseDTO = content.getDisk();
        assertEquals(0, diskUseDTO.getUsed().longValue());
        assertEquals(0, diskUseDTO.getTotal().longValue());
    }

    /**
     * 测试statisticsResourceUse方法
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testStatisticsResourceUse() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        final QuerySystemInfoResponse response = new QuerySystemInfoResponse();
        final CpuDTO cpuDTO = new CpuDTO();
        final MemoryDTO memoryDTO = new MemoryDTO();
        final DiskDTO diskDTO = new DiskDTO();
        final long usedDisk = 10L;
        final long totalDisk = 100L;

        diskDTO.setUsed(usedDisk);
        diskDTO.setTotal(totalDisk);
        final DiskDTO[] diskArr = new DiskDTO[] {diskDTO};

        response.setCpu(cpuDTO);
        response.setMemory(memoryDTO);
        response.setDiskArr(diskArr);
        new Expectations() {
            {
                systemInfoAPI.querySystemInfo();
                result = response;
            }
        };
        DefaultWebResponse webResponse = statisticsController.statisticsResourceUse(request);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        StatisticsResourceUseDTO content = (StatisticsResourceUseDTO) webResponse.getContent();
        assertEquals(cpuDTO, content.getCpu());
        assertEquals(memoryDTO, content.getMemory());

        DiskUseDTO diskUseDTO = content.getDisk();
        assertEquals(usedDisk, diskUseDTO.getUsed().longValue());
        assertEquals(totalDisk, diskUseDTO.getTotal().longValue());
    }

    /**
     * 测试fetchCpuUseRate方法
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testFetchCpuUseRate() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        final QueryCpuInfoResponse apiResponse = new QueryCpuInfoResponse();
        final Integer cpuUsedRate = 30;
        apiResponse.setUsedRate(cpuUsedRate);
        new Expectations() {
            {
                systemInfoAPI.queryCpuInfo();
                result = apiResponse;
            }
        };

        DefaultWebResponse webResponse = statisticsController.fetchCpuUseRate(request);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        Integer cpuUsedRateResult = (Integer) webResponse.getContent();
        assertEquals(cpuUsedRate, cpuUsedRateResult);
    }

    /**
     * 测试fetchBandwidthSpeed方法，抛出异常情况
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testFetchBandwidthSpeedWhileHasException() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        new Expectations() {
            {
                systemInfoAPI.queryCurrentNetworkSpeed();
                result = new Exception();
            }
        };
        DefaultWebResponse webResponse = statisticsController.fetchBandwidthSpeed(request);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        BandwidthSpeedResponse content = (BandwidthSpeedResponse) webResponse.getContent();
        assertEquals(0, content.getDownSpeed().intValue());
        assertEquals(0, content.getUpSpeed().intValue());
    }

    /**
     * 测试fetchBandwidthSpeed方法
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testFetchBandwidthSpeed() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        final QueryCurrentNetworkSpeedResponse apiResponse = new QueryCurrentNetworkSpeedResponse();
        final BigDecimal downSpeed = new BigDecimal(0);
        final BigDecimal upSpeed = new BigDecimal(0);

        apiResponse.setDownSpeed(downSpeed);
        apiResponse.setUpSpeed(upSpeed);
        new Expectations() {
            {
                systemInfoAPI.queryCurrentNetworkSpeed();
                result = apiResponse;
            }
        };
        DefaultWebResponse webResponse = statisticsController.fetchBandwidthSpeed(request);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        BandwidthSpeedResponse content = (BandwidthSpeedResponse) webResponse.getContent();
        assertEquals(downSpeed, content.getDownSpeed());
        assertEquals(upSpeed, content.getUpSpeed());
    }

    /**
     * 测试statisticsClusterServer方法
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testStatisticsClusterServer() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        DefaultWebResponse webResponse = statisticsController.statisticsClusterServer(request);
        assertEquals(Status.SUCCESS, webResponse.getStatus());
    }

    /**
     * 测试clusterServerTrend方法
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testClusterServerTrend() throws BusinessException {
        final DefaultWebRequest request = new DefaultWebRequest();
        new Expectations() {
            {
                clusterServerStatisticsAPI.clusterServerTrend();
                result = Lists.newArrayList();
            }
        };

        DefaultWebResponse webResponse = statisticsController.clusterServerTrend(request);
        assertEquals(Status.SUCCESS, webResponse.getStatus());

        ImmutableMap<String, ArrayList<Object>> of = ImmutableMap.of("itemArr", Lists.newArrayList());
        Object content = (Object) webResponse.getContent();
        assertEquals(of, content);
    }
}
