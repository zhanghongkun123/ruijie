package com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DashboardStatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ServerResourceCurrentUsageWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.GpuResourceUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerGpuUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.TerminalOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.authentication.ApplyRccpLoginTokenResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainIdvUpLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainLicenseInfoResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceHistoryUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainVOILicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.TerminalOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request.CloudPlatformWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request.ServerResourceCurrentUsageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request.ServerResourceHistoryUsageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request.TerminalOnlineSituationStatisticsWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月20日
 *
 * @author wjp
 */
@Api(tags = "首页统计页面行为")
@Controller
@RequestMapping("/rco/dashboard")
public class DashboardStatisticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardStatisticsController.class);

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private DashboardStatisticsAPI dashboardStatisticsAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    /**
     * 统计终端历史运行状态
     *
     * @param webRequest 请求参数
     * @param sessionContext 会话上下文
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("统计终端历史运行状态")
    @RequestMapping(value = "statisticsTerminalHistoryOnlineSituation", method = RequestMethod.POST)
    public CommonWebResponse<TerminalOnlineSituationStatisticsResponse> statisticsTerminalHistoryOnlineSituation(
            TerminalOnlineSituationStatisticsWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        LOGGER.debug("platform={},timeQueryType={}", webRequest.getPlatform(), webRequest.getTimeQueryType());
        IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
        List<UUID> terminalGroupIdList = new ArrayList<>();
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            terminalGroupIdList = fetchTerminalGroupIdList(loginUserInfo.getId());
            LOGGER.debug("terminalGroupIdList={}", terminalGroupIdList.toArray());
        }
        TerminalOnlineSituationStatisticsRequest terminalOnlineSituationStatisticsRequest = new TerminalOnlineSituationStatisticsRequest();
        terminalOnlineSituationStatisticsRequest.setPlatform(webRequest.getPlatform());
        terminalOnlineSituationStatisticsRequest.setTimeQueryType(webRequest.getTimeQueryType());
        terminalOnlineSituationStatisticsRequest.setGroupIdList(terminalGroupIdList);
        TerminalOnlineSituationStatisticsResponse terminalOnlineSituationStatisticsResponse =
                dashboardStatisticsAPI.statisticsTerminalHistoryOnlineSituation(terminalOnlineSituationStatisticsRequest);

        return CommonWebResponse.success(terminalOnlineSituationStatisticsResponse);
    }

    private List<UUID> fetchTerminalGroupIdList(UUID adminId) throws BusinessException {
        LOGGER.debug("adminId={}", adminId);
        ListTerminalGroupIdRequest request = new ListTerminalGroupIdRequest();
        request.setAdminId(adminId);
        ListTerminalGroupIdResponse response = adminDataPermissionAPI.listTerminalGroupIdByAdminId(request);
        return response.getTerminalGroupIdList().stream().filter(groupId -> !groupId.equals(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID))
                .map(groupId -> UUID.fromString(groupId)).collect(Collectors.toList());
    }

    /**
     * 统计集群资源池使用情况（历史）
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("统计集群资源池使用情况（历史）")
    @RequestMapping(value = "statisticsServerResourceHistoryUsage", method = RequestMethod.POST)
    public CommonWebResponse<ObtainServerResourceHistoryUsageResponse> statisticsServerResourceHistoryUsage(
            ServerResourceHistoryUsageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");

        LOGGER.debug("serverResourceType={},timeQueryType={}", webRequest.getServerResourceType(), webRequest.getTimeQueryType());
        ServerResourceHistoryUsageRequest serverResourceHistoryUsageRequest = new ServerResourceHistoryUsageRequest();
        serverResourceHistoryUsageRequest.setTimeQueryType(webRequest.getTimeQueryType());
        serverResourceHistoryUsageRequest.setServerResourceType(webRequest.getServerResourceType());
        serverResourceHistoryUsageRequest.setPlatformId(webRequest.getPlatformId());
        ObtainServerResourceHistoryUsageResponse obtainServerResourceHistoryUsageResponse =
                dashboardStatisticsAPI.statisticsServerResourceHistoryUsage(serverResourceHistoryUsageRequest);

        return CommonWebResponse.success(obtainServerResourceHistoryUsageResponse);
    }

    /**
     * 获取IDV终端正式授权信息
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取IDV终端正式授权信息")
    @RequestMapping(value = "obtainIdvLicenseInfo", method = RequestMethod.POST)
    public CommonWebResponse<ObtainLicenseInfoResponse> obtainIdvLicenseInfo(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");

        ObtainLicenseInfoResponse obtainLicenseInfoResponse = dashboardStatisticsAPI.obtainLicenseInfo(CbbTerminalLicenseTypeEnums.IDV);
        LOGGER.debug("total={},idvUsed={},tciUsed={}", obtainLicenseInfoResponse.getTotal(), obtainLicenseInfoResponse.getIdvUsed(),
                obtainLicenseInfoResponse.getVoiUsed());
        return CommonWebResponse.success(obtainLicenseInfoResponse);
    }

    /**
     * 获取VOI终端正式授权信息
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取VOI终端正式授权信息")
    @RequestMapping(value = "obtainVoiLicenseInfo", method = RequestMethod.POST)
    public CommonWebResponse<ObtainVOILicenseInfoResponse> obtainVoiLicenseInfo(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");

        ObtainVOILicenseInfoResponse obtainVOILicenseInfoResponse = dashboardStatisticsAPI.obtainVOILicenseInfo(CbbTerminalLicenseTypeEnums.VOI);
        LOGGER.debug("total={},used={}", obtainVOILicenseInfoResponse.getTotal(), obtainVOILicenseInfoResponse.getUsed());
        return CommonWebResponse.success(obtainVOILicenseInfoResponse);
    }

    /**
     * 获取IDV终端UP授权正式授权信息
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取IDV终端UP授权正式授权信息")
    @RequestMapping(value = "obtainIdvUpLicenseInfo", method = RequestMethod.POST)
    public CommonWebResponse<ObtainIdvUpLicenseInfoResponse> obtainIdvUpLicenseInfo(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");

        ObtainIdvUpLicenseInfoResponse obtainIdvUpLicenseInfoResponse =
                dashboardStatisticsAPI.obtainIdvUpLicenseInfo(CbbTerminalLicenseTypeEnums.VOI_PLUS_UPGRADED);
        LOGGER.debug("total={},used={}", obtainIdvUpLicenseInfoResponse.getTotal(), obtainIdvUpLicenseInfoResponse.getUsed());
        return CommonWebResponse.success(obtainIdvUpLicenseInfoResponse);
    }

    /**
     * 向RCCP申请随机token
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("向RCCP申请随机token")
    @RequestMapping(value = "applyRccpLoginToken", method = RequestMethod.POST)
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<ApplyRccpLoginTokenResponse> applyRccpLoginToken(CloudPlatformBaseRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");

        ApplyRccpLoginTokenResponse applyRccpLoginTokenResponse = dashboardStatisticsAPI.applyRccpLoginToken(webRequest);
        LOGGER.debug("token={}", applyRccpLoginTokenResponse.getToken());
        auditLogAPI.recordLog(DashboardStatisticsBusinessKey.RCO_DASHBOARD_STATISTICS_APPLY_RCCP_LOGIN_TOKEN_SUCCESS_LOG);
        return CommonWebResponse.success(applyRccpLoginTokenResponse);
    }

    /**
     * 统计集群资源池使用情况（实时）
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("统计集群资源池使用情况（实时）")
    @RequestMapping(value = "statisticsServerResourceCurrentUsage", method = RequestMethod.POST)
    public CommonWebResponse<ServerResourceCurrentUsageWebResponse> statisticsServerResourceCurrentUsage(
            ServerResourceCurrentUsageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");

        LOGGER.debug("serverResourceType={}", webRequest.getServerResourceType());
        ServerResourceCurrentUsageRequest serverResourceCurrentUsageRequest = new ServerResourceCurrentUsageRequest();
        serverResourceCurrentUsageRequest.setServerResourceType(webRequest.getServerResourceType());
        serverResourceCurrentUsageRequest.setPlatformId(webRequest.getPlatformId());
        ServerResourceCurrentUsageWebResponse serverResourceCurrentUsageWebResponse =
                dashboardStatisticsAPI.statisticsServerResourceCurrentUsage(serverResourceCurrentUsageRequest);
        return CommonWebResponse.success(serverResourceCurrentUsageWebResponse);
    }

    /**
     * 查询GPU信息
     *
     * @param webRequest 请求参数
     * @return GPU信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询GPU信息")
    @RequestMapping(value = "statisticsGpuInfo", method = RequestMethod.POST)
    public CommonWebResponse<GpuResourceUsageDTO> statisticsGpuInfo(CloudPlatformWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request is not null");
        return CommonWebResponse.success(dashboardStatisticsAPI.getGpuResourceUsage(new ServerGpuUsageRequest(webRequest.getPlatformId())));
    }
}
