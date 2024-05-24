package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.ServerForecastDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetServerHistoryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ServerForecastRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetAlarmCountWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetDesktopNormalDistributionWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetServerHistoryWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.ServerForecastWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.test.GetSetTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

/**
 * Description: 服务器管理Controller单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 17:07
 *
 * @author brq
 */
@RunWith(JMockit.class)
public class ServerControllerTest {

    @Tested
    private ServerController serverController;

    @Injectable
    private ServerAPI serverAPI;

    /**
     * 测试实体类
     */
    @Test
    public void testPojo() {

        GetSetTester getSetTester = new GetSetTester(GetAlarmCountWebRequest.class);
        getSetTester.runTest();
        getSetTester = new GetSetTester(GetDesktopNormalDistributionWebRequest.class);
        getSetTester.runTest();
        getSetTester = new GetSetTester(GetServerHistoryWebRequest.class);
        getSetTester.runTest();
        getSetTester = new GetSetTester(ServerForecastWebRequest.class);
        getSetTester.runTest();
        Assert.assertTrue(true);
    }

    /**
     * 测试获得服务器每小时各资源使用率集合
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetServerHistoryForHourStep() throws BusinessException {
        GetServerHistoryWebRequest request = new GetServerHistoryWebRequest();
        ServerHistoryResponse serverHistoryResponse = new ServerHistoryResponse();
        new Expectations() {
            {
                serverAPI.getServerHistoryForHourStep((GetServerHistoryRequest) any);
                result = serverHistoryResponse;
            }
        };
        DefaultWebResponse serverHistoryForHourStep = serverController.getServerHistoryForHourStep(request);
        Assert.assertEquals(serverHistoryForHourStep.getStatus(), Status.SUCCESS);
        new Verifications() {
            {
                serverAPI.getServerHistoryForHourStep((GetServerHistoryRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 服务器资源一周使用率均值
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetServerAverageUsage() throws BusinessException {

        GetServerHistoryWebRequest request = new GetServerHistoryWebRequest();
        ServerAverageUsageResponse serverAverageUsageResponse = new ServerAverageUsageResponse();
        serverAverageUsageResponse.setCpuUsageAvg(10D);
        new Expectations() {
            {
                serverAPI.getServerAverageUsage((GetServerHistoryRequest) any);
                result = serverAverageUsageResponse;
            }
        };
        DefaultWebResponse response = serverController.getServerAverageUsage(request);
        Assert.assertEquals(Status.SUCCESS, response.getStatus());
        ServerAverageUsageResponse content = (ServerAverageUsageResponse) response.getContent();
        Assert.assertEquals(serverAverageUsageResponse.getCpuUsageAvg(), content.getCpuUsageAvg());
        new Verifications() {
            {
                serverAPI.getServerAverageUsage((GetServerHistoryRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试服务器资源使用率预测数据及阈值数据
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetServerForecast() throws BusinessException {
        ServerForecastWebRequest request = new ServerForecastWebRequest();
        ServerForecastResponse serverForecastResponse = new ServerForecastResponse();
        ServerForecastDTO dto = new ServerForecastDTO();
        dto.setEstimate(10);
        serverForecastResponse.setServerForecast(dto);
        new Expectations() {
            {
                serverAPI.getServerForecast((ServerForecastRequest) any);
                result = serverForecastResponse;
            }
        };
        DefaultWebResponse response = serverController.getServerForecast(request);
        ServerForecastResponse content = (ServerForecastResponse) response.getContent();
        Assert.assertEquals(Status.SUCCESS, response.getStatus());
        Assert.assertEquals(dto.getEstimate(), content.getServerForecast().getEstimate());
        new Verifications() {
            {
                serverAPI.getServerForecast((ServerForecastRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试服务器资源使用率日折线
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetServerHistoryForDayStep() throws BusinessException {
        GetServerHistoryWebRequest request = new GetServerHistoryWebRequest();
        ServerHistoryResponse serverHistoryResponse = new ServerHistoryResponse();
        new Expectations() {
            {
                serverAPI.getServerHistoryForDayStep((GetServerHistoryRequest) any);
                result = serverHistoryResponse;
            }
        };
        DefaultWebResponse response = serverController.getServerHistoryForDayStep(request);
        Assert.assertEquals(Status.SUCCESS, response.getStatus());
        new Verifications() {
            {
                serverAPI.getServerHistoryForDayStep((GetServerHistoryRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 服务器概要数据展示
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void getServerInfoTest() throws BusinessException {
        IdWebRequest request = new IdWebRequest();
        request.setId(UUID.randomUUID());

        new Expectations() {
            {
                serverAPI.getServerInfo((IdRequest) any);
                result = new ServerInfoResponse();
            }
        };

        DefaultWebResponse response = serverController.getServerInfo(request);

        Assert.assertEquals(Status.SUCCESS, response.getStatus());
    }

    /**
     * 测试获取全部可用的物理服务器列表
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testListAllPhysicalServer() throws BusinessException {
        DefaultWebRequest request = new DefaultWebRequest();

        PhysicalServerInfoDTO infoDTO = new PhysicalServerInfoDTO();
        List<PhysicalServerInfoDTO> infoDTOList = Lists.newArrayList();
        infoDTOList.add(infoDTO);
        ListPhysicalServerCabinetResponse apiResponse = new ListPhysicalServerCabinetResponse();
        apiResponse.setPhysicalServerDTOList(infoDTOList);

        new Expectations() {
            {
                serverAPI.listAllPhysicalServer((DefaultRequest) any);
                result = apiResponse;
            }
        };

        DefaultWebResponse response = serverController.listAllPhysicalServer(request);
        Assert.assertEquals(Status.SUCCESS, response.getStatus());
    }

}
