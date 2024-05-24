package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetServerHistoryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ServerForecastRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import java.util.List;


/**
 * 服务器管理API接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月10日
 * @author brq
 */
public interface ServerAPI {

    /**
     * 按ID获取服务器信息
     *
     * @param request ID请求
     * @return 服务器信息响应
     * @throws BusinessException 业务异常
     */

    ServerInfoResponse getServerInfo(IdRequest request) throws BusinessException;

    /**
     * 获取一小时内的服务器资源使用情况
     *
     * @param request 获取服务器资源请求
     * @return 服务器资源
     */

    ServerHistoryResponse getServerHistoryForHourStep(GetServerHistoryRequest request);

    /**
     * 服务器资源使用率日折线图数据
     *
     * @param request 请求
     * @return 返回
     */

    ServerHistoryResponse getServerHistoryForDayStep(GetServerHistoryRequest request);

    /**
     * 服务器资源使用率预测数据及阈值数据
     *
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */

    ServerForecastResponse getServerForecast(ServerForecastRequest request) throws BusinessException;

    /**
     * 计算服务器资源一周使用率均值
     * @param request 请求
     * @return 返回
     */
    
    ServerAverageUsageResponse getServerAverageUsage(GetServerHistoryRequest request);

    /**
     * 获取所有可用的物理服务器
     * @param request 请求
     * @return 返回
     */

    ListPhysicalServerCabinetResponse listAllPhysicalServer(DefaultRequest request);

    /**
     * 获取服务器CPU类型
     * @return 服务器CPU类型列表
     * @throws BusinessException
     */
    List<String> getServerCPU();

    /**
     * 获取云主机状态
     * @return 返回云桌面状态数量
     * @throws BusinessException  业务异常
     */
    ServerHostStatusResponse statisticsServerHostStatus() throws BusinessException;
}
