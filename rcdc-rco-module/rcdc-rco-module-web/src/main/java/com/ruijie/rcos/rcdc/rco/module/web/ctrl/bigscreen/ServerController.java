package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.ServerAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetServerHistoryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ServerForecastRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetServerHistoryWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.ServerForecastWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.response.ListPhysicalServerCabinetWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务器管理Controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月10日
 * @author brq
 */
@Controller
@RequestMapping("/rco/server")
public class ServerController {

    @Autowired
    private ServerAPI serverAPI;

    // 海光服务器CPU标记
    private static final String HYGON = "Hygon";

    // 海光服务器CPU标记
    private static final String ZHAOXIN = "ZHAOXIN";

    /**
     * 服务器概要数据展示
     *
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getServerInfo")
    public DefaultWebResponse getServerInfo(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "IdWebRequest不能是空");

        IdRequest idRequest = new IdRequest(request.getId());
        ServerInfoResponse response = serverAPI.getServerInfo(idRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 获得服务器每小时各资源使用率集合
     *
     * @param request 请求
     * @return 响应参数
     */
    @RequestMapping(value = "/getServerHistoryForHourStep")
    public DefaultWebResponse getServerHistoryForHourStep(GetServerHistoryWebRequest request) {
        Assert.notNull(request, "GetServerHistoryWebRequest");

        GetServerHistoryRequest serverHistoryRequest = new GetServerHistoryRequest();
        BeanUtils.copyProperties(request, serverHistoryRequest);
        ServerHistoryResponse response = serverAPI.getServerHistoryForHourStep(serverHistoryRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 服务器资源使用率日折线
     *
     * @param request 请求
     * @return 返回
     */
    @RequestMapping(value = "/getServerHistoryForDayStep")
    public DefaultWebResponse getServerHistoryForDayStep(GetServerHistoryWebRequest request) {
        Assert.notNull(request, "request is null");

        GetServerHistoryRequest getServerHistoryRequest = new GetServerHistoryRequest();
        BeanUtils.copyProperties(request, getServerHistoryRequest);

        ServerHistoryResponse response = serverAPI.getServerHistoryForDayStep(getServerHistoryRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 服务器资源使用率预测数据及阈值数据
     *
     * @param request 入参
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getServerForecast")
    public DefaultWebResponse getServerForecast(ServerForecastWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");

        ServerForecastRequest serverForecastRequest = new ServerForecastRequest();
        BeanUtils.copyProperties(request, serverForecastRequest);

        ServerForecastResponse response = serverAPI.getServerForecast(serverForecastRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 服务器资源一周使用率均值
     *
     * @param webRequest 请求
     * @return 返回
     */
    @RequestMapping(value = "/getServerAverageUsage", method = RequestMethod.POST)
    public DefaultWebResponse getServerAverageUsage(GetServerHistoryWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");

        GetServerHistoryRequest request = new GetServerHistoryRequest();
        BeanUtils.copyProperties(webRequest, request);

        ServerAverageUsageResponse response = serverAPI.getServerAverageUsage(request);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 获取全部可用的物理服务器列表
     * @param request 请求参数
     * @return DefaultWebResponse 响应
     */
    @RequestMapping(value = "/listAllPhysicalServer", method = RequestMethod.POST)
    public DefaultWebResponse listAllPhysicalServer(DefaultWebRequest request) {
        Assert.notNull(request, "request不能为null");

        ListPhysicalServerCabinetResponse response = serverAPI.listAllPhysicalServer(new DefaultRequest());
        ListPhysicalServerCabinetWebResponse webResponse = new ListPhysicalServerCabinetWebResponse();
        webResponse.setItemArr(response.getPhysicalServerDTOList().toArray(new PhysicalServerInfoDTO[]{}));
        return DefaultWebResponse.Builder.success(webResponse);
    }

    /**
     * 判断是否国产化服务器
     * @param request 请求参数
     * @return DefaultWebResponse 响应
     */
    @RequestMapping(value = "/isLocalizationServer", method = RequestMethod.POST)
    public DefaultWebResponse isLocalizationServer(DefaultWebRequest request) {
        Assert.notNull(request, "request不能为null");

        List<String> serverCPUList = serverAPI.getServerCPU();
        serverCPUList = serverCPUList.stream().filter(cpu -> cpu.contains(HYGON) || cpu.contains(ZHAOXIN)).collect(Collectors.toList());
        return DefaultWebResponse.Builder.success(CollectionUtils.isNotEmpty(serverCPUList));
    }

    /**
     * 统计各种状态云主机数量
     * @return  返回各种状态的服务器数量
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/statisticsServerHostStatus", method = RequestMethod.POST)
    public DefaultWebResponse statisticsServerHostStatus() throws BusinessException {
        ServerHostStatusResponse response = serverAPI.statisticsServerHostStatus();
        return DefaultWebResponse.Builder.success(response);
    }
}
