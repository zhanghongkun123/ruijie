package com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.impl;

import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceHistoryUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AlarmWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ServerResourceCurrentUsageWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.GpuResourceUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerGpuUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.TerminalOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.TerminalOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.DashboardStatisticsRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.response.StatisticsOnlineVDIDeskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.response.StatisticsUserAndDeskResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalStatisticsDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.*;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultSort;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 首页统计页面行为
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-09-23
 *
 * @author zqj
 */
@Service
public class DashboardStatisticsRestServerImpl implements DashboardStatisticsRestServer {


    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DashboardStatisticsAPI dashboardStatisticsAPI;

    @Autowired
    private AlarmAPI alarmAPI;

    @Autowired
    private OperateLogAPI operateLogAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Autowired
    private StatisticsAPI statisticsAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Override
    public ServerResourceCurrentUsageWebResponse statisticsServerResourceCurrentUsage(ServerResourceCurrentUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request不能为null");
        ServerResourceCurrentUsageRequest serverResourceCurrentUsageRequest = new ServerResourceCurrentUsageRequest();
        serverResourceCurrentUsageRequest.setServerResourceType(request.getServerResourceType());
        serverResourceCurrentUsageRequest.setPlatformId(request.getPlatformId());
        ServerResourceCurrentUsageWebResponse serverResourceCurrentUsageWebResponse =
                dashboardStatisticsAPI.statisticsServerResourceCurrentUsage(serverResourceCurrentUsageRequest);
        return serverResourceCurrentUsageWebResponse;
    }

    @Override
    public CbbTerminalStatisticsDTO statisticsTerminal() throws BusinessException {
        CbbTerminalStatisticsDTO response = statisticsAPI.statisticsTerminal(new UUID[0]);
        return response;
    }

    @Override
    public TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituation(TerminalOnlineSituationStatisticsRequest request)
            throws BusinessException {
        Assert.notNull(request, "request不能为null");
        TerminalOnlineSituationStatisticsRequest terminalOnlineSituationStatisticsRequest = new TerminalOnlineSituationStatisticsRequest();
        terminalOnlineSituationStatisticsRequest.setPlatform(request.getPlatform());
        terminalOnlineSituationStatisticsRequest.setTimeQueryType(request.getTimeQueryType());
        terminalOnlineSituationStatisticsRequest.setGroupIdList(new ArrayList<>());
        TerminalOnlineSituationStatisticsResponse terminalOnlineSituationStatisticsResponse =
                dashboardStatisticsAPI.statisticsTerminalHistoryOnlineSituation(terminalOnlineSituationStatisticsRequest);
        return terminalOnlineSituationStatisticsResponse;
    }

    @Override
    public GpuResourceUsageDTO statisticsGpuInfo(@Nullable CloudPlatformBaseRequest request) throws BusinessException {
        return dashboardStatisticsAPI.getGpuResourceUsage(new ServerGpuUsageRequest(Objects.isNull(request) ? null : request.getPlatformId()));
    }

    @Override
    public ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsage(ServerResourceHistoryUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request不能为null");
        ServerResourceHistoryUsageRequest serverResourceHistoryUsageRequest = new ServerResourceHistoryUsageRequest();
        serverResourceHistoryUsageRequest.setTimeQueryType(request.getTimeQueryType());
        serverResourceHistoryUsageRequest.setServerResourceType(request.getServerResourceType());
        serverResourceHistoryUsageRequest.setPlatformId(request.getPlatformId());
        ObtainServerResourceHistoryUsageResponse obtainServerResourceHistoryUsageResponse =
                dashboardStatisticsAPI.statisticsServerResourceHistoryUsage(serverResourceHistoryUsageRequest);
        return obtainServerResourceHistoryUsageResponse;
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> alarmListDesktopFault(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        PageSearchRequest pageRequest = new DeskPageSearchRequest(request);
        return userDesktopMgmtAPI.desktopFaultPageQuery(pageRequest);
    }

    @Override
    public DefaultPageResponse<AlarmWebResponse> alarmList(PageWebRequest request) {
        Assert.notNull(request, "request不能为null");
        DefaultPageResponse<AlarmWebResponse> response = alarmAPI.pageQuery(request);
        return response;
    }


    @Override
    public PageQueryResponse<BaseOperateLogDTO> operateLogList(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        PageQueryRequest pageQueryRequest = new PageQueryRequest() {
            @Override
            public int getLimit() {
                return request.getLimit();
            }

            @Override
            public int getPage() {
                return request.getPage();
            }

            @Override
            public Match[] getMatchArr() {
                return new Match[0];
            }

            @Override
            public Sort[] getSortArr() {
                com.ruijie.rcos.sk.webmvc.api.vo.Sort sort = request.getSort();
                if (Objects.nonNull(sort)) {
                    Sort.Direction direction = Sort.Direction.valueOf(sort.getDirection().name());
                    DefaultSort defaultSort = new DefaultSort(sort.getSortField(), direction);
                    return new Sort[] {defaultSort};
                } else {
                    return new Sort[0];
                }
            }
        };
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        return operateLogAPI.operateLogList(builder);
    }

    @Override
    public DefaultPageResponse<ComputerDTO> listComputerFault(PageWebRequest request) {
        Assert.notNull(request, "request不能为null");
        PageSearchRequest pageRequest = new ComputerPageSearchRequest(request);
        return computerBusinessAPI.computerFaultPageQuery(pageRequest);
    }

    @Override
    public StatisticsUserAndDeskResponse statisticsUserAndDesk() {
        Long userCount = userInfoAPI.findUserCount();
        Long deskRunNum = userDesktopMgmtAPI.findCountByDeskState(CbbCloudDeskState.RUNNING);
        Long desktopCount = userDesktopMgmtAPI.findCount();
        StatisticsUserAndDeskResponse statisticsUserAndDeskResponse = new StatisticsUserAndDeskResponse(userCount,deskRunNum,desktopCount);
        return statisticsUserAndDeskResponse;
    }

    @Override
    public StatisticsOnlineVDIDeskResponse statisticsCurrentOnlineVDIDesktop() {
        Long onlineCount = userDesktopMgmtAPI.findOnlineDesktopCount();
        return new StatisticsOnlineVDIDeskResponse(onlineCount);
    }
}
