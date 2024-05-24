package com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics;

import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceHistoryUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AlarmWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ServerResourceCurrentUsageWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.GpuResourceUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.TerminalOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.TerminalOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.response.StatisticsOnlineVDIDeskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.response.StatisticsUserAndDeskResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalStatisticsDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: rcdc首页统计页面行为
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-09-23
 *
 * @author zqj
 */
@OpenAPI
@Path("/v1/dashboard")
public interface DashboardStatisticsRestServer {

    /**
     * 统计集群资源池使用情况（实时）
     * 
     * @api {POST} rest/dashboard/statisticsServerResourceCurrentUsage 统计集群资源池使用情况（实时）
     * @apiDescription 统计集群资源池使用情况（实时）
     * @param request 请求体
     * @throws BusinessException ex
     * @return ServerResourceCurrentUsageWebResponse
     */
    @POST
    @Path("/statisticsServerResourceCurrentUsage")
    ServerResourceCurrentUsageWebResponse statisticsServerResourceCurrentUsage(ServerResourceCurrentUsageRequest request) throws BusinessException;

    /**
     * 统计终端状态
     * 
     * @api {POST} rest/dashboard/statisticsTerminal 统计终端状态
     * @apiDescription 统计终端状态
     * @return CbbTerminalStatisticsDTO
     * @throws BusinessException ex
     */
    @POST
    @Path("/statisticsTerminal")
    CbbTerminalStatisticsDTO statisticsTerminal() throws BusinessException;

    /**
     * 统计终端历史运行状态
     * 
     * @api {POST} rest/dashboard/statisticsTerminalHistoryOnlineSituation 统计终端历史运行状态
     * @apiDescription 统计终端历史运行状态
     * @param request 请求体
     * @return TerminalOnlineSituationStatisticsResponse
     * @throws BusinessException ex
     */
    @POST
    @Path("/statisticsTerminalHistoryOnlineSituation")
    TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituation(TerminalOnlineSituationStatisticsRequest request)
            throws BusinessException;

    /**
     * 集群显卡使用情况（实时）
     *
     * @api {POST} rest/dashboard/statisticsGpuInfo 集群显卡使用情况（实时）
     * @apiDescription 集群显卡使用情况（实时）
     * @param request 请求参数
     * @return TerminalOnlineSituationStatisticsResponse
     * @throws BusinessException ex
     */
    @POST
    @Path("/statisticsGpuInfo")
    GpuResourceUsageDTO statisticsGpuInfo(CloudPlatformBaseRequest request)throws BusinessException;

    /**
     * 统计集群资源池使用情况（历史）
     * 
     * @api {POST} rest/dashboard/statisticsServerResourceHistoryUsage 统计集群资源池使用情况（历史）
     * @apiDescription 统计集群资源池使用情况（历史）
     * @param request 请求体
     * @return ObtainServerResourceHistoryUsageResponse
     * @throws BusinessException ex
     */
    @POST
    @Path("/statisticsServerResourceHistoryUsage")
    ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsage(ServerResourceHistoryUsageRequest request)
            throws BusinessException;

    /**
     * 查询云桌面报障列表
     * 
     * @api {POST} rest/dashboard/alarmListDesktopFault 查询云桌面报障列表
     * @apiDescription 查询云桌面报障列表
     * @param request 请求体
     * @return DefaultPageResponse<CloudDesktopDTO>
     * @throws BusinessException ex
     */
    @POST
    @Path("/alarmListDesktopFault")
    DefaultPageResponse<CloudDesktopDTO> alarmListDesktopFault(PageWebRequest request) throws BusinessException;

    /**
     * 系统告警
     *
     * @api {POST} rest/dashboard/alarmList 系统告警
     * @apiDescription 系统告警
     * @param request 请求体
     * @return DefaultPageResponse<AlarmDTO>
     */
    @POST
    @Path("/alarmList")
    DefaultPageResponse<AlarmWebResponse> alarmList(PageWebRequest request);

    /**
     * 获取操作日志列表请求
     *
     * @api {POST} rest/dashboard/operateLogList 获取操作日志列表请求
     * @apiDescription 获取操作日志列表请求
     * @param request 请求体
     * @throws BusinessException ex
     * @return PageQueryResponse<BaseOperateLogDTO>
     */
    @POST
    @Path("/operateLogList")
    PageQueryResponse<BaseOperateLogDTO> operateLogList(PageWebRequest request) throws BusinessException;

    /**
     * 首页查询报障PC
     *
     * @api {POST} rest/dashboard/listComputerFault 首页查询报障PC
     * @apiDescription 首页查询报障PC
     * @param request 请求体
     * @return DefaultPageResponse<ComputerDTO>
     */
    @POST
    @Path("/listComputerFault")
    DefaultPageResponse<ComputerDTO> listComputerFault(PageWebRequest request);

    /**
     * 统计用户与云桌面相关信息
     *
     * @api {POST} rest/dashboard/statisticsUserAndDesk 统计用户与云桌面相关信息
     * @apiDescription 统计用户与云桌面相关信息
     * @return 统计信息
     */
    @POST
    @Path("/statisticsUserAndDesk")
    StatisticsUserAndDeskResponse statisticsUserAndDesk();

    /**
     * 统计VDI云桌面在线使用数量相关信息
     *
     * @api {GET} rest/dashboard/statisticsCurrentOnlineVDIDesktop 统计VDI云桌面在线使用数量相关信息
     * @apiDescription 统计VDI云桌面在线使用数量相关信息
     * @return 统计信息
     */
    @GET
    @Path("/statisticsCurrentOnlineVDIDesktop")
    StatisticsOnlineVDIDeskResponse statisticsCurrentOnlineVDIDesktop();
}
