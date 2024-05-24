package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.GpuResourceUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerGpuUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceCurrentUsageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceHistoryUsageResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: StatisticsServerService
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public interface StatisticsServerService {

    /**
     * 按维度统计CPU历史使用情况
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsageByCpu(ServerResourceHistoryUsageRequest request)
            throws BusinessException;

    /**
     * 按维度统计内存历史使用情况
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsageByMem(ServerResourceHistoryUsageRequest request)
            throws BusinessException;

    /**
     * 按维度统计存储历史使用情况
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsageByDisk(ServerResourceHistoryUsageRequest request)
            throws BusinessException;

    /**
     * 统计CPU当前使用情况
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentUsageByCpu(ServerResourceCurrentUsageRequest request)
            throws BusinessException;

    /**
     * 统计内存当前使用情况
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentUsageByMem(ServerResourceCurrentUsageRequest request)
            throws BusinessException;

    /**
     * 统计存储当前使用情况
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentUsageByDisk(ServerResourceCurrentUsageRequest request)
            throws BusinessException;

    /**
     * 统计CPU当前总资源
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotalByCpu(ServerResourceCurrentUsageRequest request)
            throws BusinessException;

    /**
     * 统计内存当前总资源
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotalByMem(ServerResourceCurrentUsageRequest request)
            throws BusinessException;

    /**
     * 统计存储当前总资源
     * 
     * @param request 集群资源使用情况请求
     * @return 返回统计结果
     * @throws BusinessException 业务异常
     */
    ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotalByDisk(ServerResourceCurrentUsageRequest request)
            throws BusinessException;

    /**
     * 查询GPU使用情况
     *
     * @param request 请求
     * @return GPU使用情况
     * @throws BusinessException 业务异常
     */
    GpuResourceUsageDTO getGpuResourceUsage(ServerGpuUsageRequest request) throws BusinessException;
}
