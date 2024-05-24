package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.authentication.ApplyRccpLoginTokenResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceCurrentUsageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceHistoryUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ServerResourceCurrentUsageWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.GpuResourceUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerGpuUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.TerminalOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.*;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: DashboardStatisticsAPI
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public interface DashboardStatisticsAPI {

    /**
     * 统计终端历史运行状态
     * 
     * @param request 终端历史情况统计请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituation(TerminalOnlineSituationStatisticsRequest request)
            throws BusinessException;

    /**
     * 统计集群资源池使用情况（历史）
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsage(ServerResourceHistoryUsageRequest request) throws BusinessException;

    /**
     * 获取终端正式授权信息
     *
     * @param type 终端授权类型
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainLicenseInfoResponse obtainLicenseInfo(CbbTerminalLicenseTypeEnums type) throws BusinessException;

    /**
     * 获取VOI终端正式授权信息
     *
     * @param type 终端授权类型
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainVOILicenseInfoResponse obtainVOILicenseInfo(CbbTerminalLicenseTypeEnums type) throws BusinessException;

    /**
     * 获取IDV UP终端正式授权信息
     *
     * @param type 终端授权类型
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainIdvUpLicenseInfoResponse obtainIdvUpLicenseInfo(CbbTerminalLicenseTypeEnums type) throws BusinessException;

    /**
     * 向RCCP申请随机token
     * @param request 请求对象
     * @return 返回token结果
     * @throws BusinessException 业务异常
     */
    ApplyRccpLoginTokenResponse applyRccpLoginToken(CloudPlatformBaseRequest request) throws BusinessException;


    /**
     * 统计集群资源池使用情况（实时）
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ServerResourceCurrentUsageWebResponse statisticsServerResourceCurrentUsage(ServerResourceCurrentUsageRequest request) throws BusinessException;

    /**
     * 统计集群资源池总资源情况（实时）
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotal(ServerResourceCurrentUsageRequest request) throws BusinessException;

    /**
     * 查询GPU使用情况
     *
     * @param request 请求参数
     * @return GPU使用情况
     * @throws BusinessException 业务异常
     */
    GpuResourceUsageDTO getGpuResourceUsage(ServerGpuUsageRequest request) throws BusinessException;
}
