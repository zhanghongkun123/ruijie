package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpAuthenticationMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.authentication.ApplyRccpLoginTokenResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceCurrentUsageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceHistoryUsageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseCategoryEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DashboardStatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ServerResourceCurrentUsageWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.GpuResourceUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerGpuUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.TerminalOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainIdvUpLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainVOILicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.TerminalOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.DashboardStatisticsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.StatisticsServerService;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.StatisticsTerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.GeneralAuthDetailInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.LicenseServiceImpl;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description: DashboardStatisticsAPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class DashboardStatisticsAPIImpl implements DashboardStatisticsAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardStatisticsAPIImpl.class);

    @Autowired
    private StatisticsTerminalService statisticsTerminalService;

    @Autowired
    private StatisticsServerService statisticsServerService;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private RccpAuthenticationMgmtAPI authenticationMgmtAPI;

    @Autowired
    private LicenseServiceImpl licenseService;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituation(TerminalOnlineSituationStatisticsRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.debug("timeQueryType={}", request.getTimeQueryType().toString());
        switch (request.getTimeQueryType()) {
            case HOUR:
                throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_TIME_QUERY_TYPE_HOUR_NOT_SUPPORTED);
            case DAY:
                return statisticsTerminalService.statisticsTerminalHistoryOnlineSituationByDay(request);
            case MONTHLY:
                return statisticsTerminalService.statisticsTerminalHistoryOnlineSituationByMonth(request);
            case YEAR:
                return statisticsTerminalService.statisticsTerminalHistoryOnlineSituationByYear(request);
            default:
                throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_TIME_QUERY_TYPE_NOT_EXIT);
        }
    }

    @Override
    public ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsage(ServerResourceHistoryUsageRequest request)
        throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.debug("serverResourceType={}", request.getServerResourceType().toString());
        switch (request.getServerResourceType()) {
            case CPU:
                return statisticsServerService.statisticsServerResourceHistoryUsageByCpu(request);
            case MEM:
                return statisticsServerService.statisticsServerResourceHistoryUsageByMem(request);
            case DISK:
                return statisticsServerService.statisticsServerResourceHistoryUsageByDisk(request);
            default:
                throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_SERVER_RESOURCE_TYPE_NOT_EXIT);
        }
    }

    @Override
    public ObtainLicenseInfoResponse obtainLicenseInfo(CbbTerminalLicenseTypeEnums type) throws BusinessException {
        Assert.notNull(type, "type is not null");
        ObtainLicenseInfoResponse obtainLicenseInfoResponse = new ObtainLicenseInfoResponse();
        List<GeneralAuthDetailInfoDTO> generalAuthDetailInfoDTOList = licenseService.getLicenseUsageDetail(type.name());
        if (!CollectionUtils.isEmpty(generalAuthDetailInfoDTOList)) {
            obtainLicenseInfoResponse.setTotal(generalAuthDetailInfoDTOList.get(0).getTotal());
            obtainLicenseInfoResponse.setUsed(generalAuthDetailInfoDTOList.get(0).getUsed());
            obtainLicenseInfoResponse.setIdvUsed(generalAuthDetailInfoDTOList.get(0).getUsedByIdv());
            obtainLicenseInfoResponse.setVoiUsed(generalAuthDetailInfoDTOList.get(0).getUsedByVoi());
        }
        LOGGER.debug("total={},idvUsed={},voiUsed={}", obtainLicenseInfoResponse.getTotal(), obtainLicenseInfoResponse.getIdvUsed(),
            obtainLicenseInfoResponse.getVoiUsed());
        return obtainLicenseInfoResponse;
    }

    @Override
    public ObtainVOILicenseInfoResponse obtainVOILicenseInfo(CbbTerminalLicenseTypeEnums type) throws BusinessException {
        Assert.notNull(type, "type is not null");
        // 获取TCI 证书以及授权使用数量
        ObtainVOILicenseInfoResponse obtainVOILicenseInfoResponse = new ObtainVOILicenseInfoResponse();
        // 查询证书
        List<GeneralAuthDetailInfoDTO> generalAuthDetailInfoDTOList = licenseService.getLicenseUsageDetail(type.name());
        if (!CollectionUtils.isEmpty(generalAuthDetailInfoDTOList)) {
            for (GeneralAuthDetailInfoDTO generalAuthDetailInfoDTO : generalAuthDetailInfoDTOList) {
                if (obtainVOILicenseInfoResponse.getTotal() != -1) {
                    if (generalAuthDetailInfoDTO.getTotal() == -1) {
                        obtainVOILicenseInfoResponse.setTotal(-1);
                    } else {
                        obtainVOILicenseInfoResponse.setTotal(obtainVOILicenseInfoResponse.getTotal() + generalAuthDetailInfoDTO.getTotal());
                    }
                }
                obtainVOILicenseInfoResponse.setUsed(obtainVOILicenseInfoResponse.getUsed() + generalAuthDetailInfoDTO.getUsed());
                if (CbbLicenseCategoryEnum.VOI.getSubCategory(1).equals(generalAuthDetailInfoDTO.getAuthType())) {
                    obtainVOILicenseInfoResponse.setEduVoiNumber(generalAuthDetailInfoDTO.getTotal());
                }
            }
        }
        LOGGER.info("obtainVOILicenseInfo {}", JSON.toJSONString(obtainVOILicenseInfoResponse));
        return obtainVOILicenseInfoResponse;
    }

    @Override
    public ObtainIdvUpLicenseInfoResponse obtainIdvUpLicenseInfo(CbbTerminalLicenseTypeEnums type) throws BusinessException {
        Assert.notNull(type, "type is not null");

        // 获取IDV 终端UP授权
        ObtainIdvUpLicenseInfoResponse obtainLicenseInfoResponse = new ObtainIdvUpLicenseInfoResponse();
        List<GeneralAuthDetailInfoDTO> generalAuthDetailInfoDTOList = licenseService.getLicenseUsageDetail(type.name());
        if (!CollectionUtils.isEmpty(generalAuthDetailInfoDTOList)) {
            obtainLicenseInfoResponse.setTotal(generalAuthDetailInfoDTOList.get(0).getTotal());
            obtainLicenseInfoResponse.setUsed(generalAuthDetailInfoDTOList.get(0).getUsed());
        }
        LOGGER.debug("obtainIdvUpLicenseInfo total={},used={}", obtainLicenseInfoResponse.getTotal(), obtainLicenseInfoResponse.getUsed());
        return obtainLicenseInfoResponse;
    }

    @Override
    public ApplyRccpLoginTokenResponse applyRccpLoginToken(CloudPlatformBaseRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        UUID platformId = request.getPlatformId();
        if (platformId == null) {
            CloudPlatformDTO cloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
            platformId = cloudPlatform.getId();
        }
        return authenticationMgmtAPI.apply(new CloudPlatformBaseRequest(platformId));
    }

    @Override
    public ServerResourceCurrentUsageWebResponse statisticsServerResourceCurrentUsage(ServerResourceCurrentUsageRequest request)
        throws BusinessException {
        Assert.notNull(request, "request is not null");
        ServerResourceCurrentUsageWebResponse serverResourceCurrentUsageWebResponse = new ServerResourceCurrentUsageWebResponse();
        LOGGER.debug("serverResourceType={}", request.getServerResourceType().toString());
        ObtainServerResourceCurrentUsageResponse usedResponse = null;
        ObtainServerResourceCurrentUsageResponse totalResponse = statisticsServerResourceCurrentTotal(request);
        switch (request.getServerResourceType()) {
            case CPU:
                usedResponse = statisticsServerService.statisticsServerResourceCurrentUsageByCpu(request);
                break;
            case MEM:
                usedResponse = statisticsServerService.statisticsServerResourceCurrentUsageByMem(request);
                break;
            case DISK:
                usedResponse = statisticsServerService.statisticsServerResourceCurrentUsageByDisk(request);
                break;
        }
        if (usedResponse == null) {
            throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_SERVER_RESOURCE_TYPE_NOT_EXIT);
        }
        serverResourceCurrentUsageWebResponse.setTotal(totalResponse);
        serverResourceCurrentUsageWebResponse.setUsed(usedResponse);
        return serverResourceCurrentUsageWebResponse;
    }

    @Override
    public ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotal(ServerResourceCurrentUsageRequest request)
        throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.debug("serverResourceType={}", request.getServerResourceType().toString());
        switch (request.getServerResourceType()) {
            case CPU:
                return statisticsServerService.statisticsServerResourceCurrentTotalByCpu(request);
            case MEM:
                return statisticsServerService.statisticsServerResourceCurrentTotalByMem(request);
            case DISK:
                return statisticsServerService.statisticsServerResourceCurrentTotalByDisk(request);
            default:
                throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_SERVER_RESOURCE_TYPE_NOT_EXIT);
        }
    }

    @Override
    public GpuResourceUsageDTO getGpuResourceUsage(ServerGpuUsageRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        return statisticsServerService.getGpuResourceUsage(request);
    }
}
