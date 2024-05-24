package com.ruijie.rcos.rcdc.rco.module.web.ctrl.monitor.dashboard;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolDashboardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopSessionLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopPoolDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.UserConnectDesktopFaultLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.StatisticDesktopPoolHistoryInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.response.StatisticDesktopPoolHistoryResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.monitor.dashboard.request.DesktopPoolStatisticsWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.monitor.dashboard.request.StatisticDesktopUsedRateRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: 统计桌面池相关信息controller
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 13:52
 *
 * @author yxq
 */
@Api(tags = "统计桌面池报表相关信息")
@Controller
@RequestMapping("/rco/monitor/dashboard")
public class StatisticDeskPoolDashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticDeskPoolDashboardController.class);

    /**
     * 用户连接桌面池失败默认排序字段
     */
    private static final String CONNECT_DESKTOP_FAULT_LOG_DEFAULT_SORT_FIELD = "faultTime";

    /**
     * 桌面连接记录默认排序字段
     */
    private static final String CONNECT_DESKTOP_LOG_DEFAULT_SORT_FIELD = "createTime";

    @Autowired
    private DesktopPoolDashboardAPI desktopPoolDashboardAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    /**
     * 统计桌面池历史信息
     *
     * @param webRequest 请求
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("统计桌面池历史信息")
    @RequestMapping(value = "statisticDeskHistoryInfo", method = RequestMethod.POST)
    public DefaultWebResponse statisticDeskHistoryInfo(DesktopPoolStatisticsWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");

        StatisticDesktopPoolHistoryInfoRequest statisticsRequest = new StatisticDesktopPoolHistoryInfoRequest();
        BeanUtils.copyProperties(webRequest, statisticsRequest);
        StatisticDesktopPoolHistoryResponse response = desktopPoolDashboardAPI.statisticDesktopPoolUseHistory(statisticsRequest);

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 查询桌面池会话连接记录
     *
     * @param pageQueryRequest 分页查询条件
     * @return 分页查询结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询桌面池会话连接记录")
    @RequestMapping(value = "desktopUserHistory/list", method = RequestMethod.POST)
    public DefaultWebResponse getDesktopUserHistoryList(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        // 如果前端传递没有排序字段，默认用createTime排序
        if (ArrayUtils.isEmpty(pageQueryRequest.getSortArr())) {
            builder.desc(CONNECT_DESKTOP_LOG_DEFAULT_SORT_FIELD);
        }
        PageQueryResponse<DesktopSessionLogDTO> pageQueryResponse = desktopPoolDashboardAPI.pageDesktopPoolConnectHistory(builder.build());
        return DefaultWebResponse.Builder.success(pageQueryResponse);
    }

    /**
     * 查询桌面池会话连接失败记录
     *
     * @param pageQueryRequest 分页查询条件
     * @return 分页查询结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询桌面池会话连接失败记录")
    @RequestMapping(value = "desktopConnectFailHistory/list", method = RequestMethod.POST)
    public DefaultWebResponse getDesktopConnectFailHistoryList(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        // 如果前端传递没有排序字段，默认用faultTime排序
        if (ArrayUtils.isEmpty(pageQueryRequest.getSortArr())) {
            builder.desc(CONNECT_DESKTOP_FAULT_LOG_DEFAULT_SORT_FIELD);
        }
        PageQueryResponse<UserConnectDesktopFaultLogDTO> response = desktopPoolDashboardAPI.pageDesktopPoolConnectFailHistory(builder.build());
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 实时获取桌面池中桌面数量相关信息
     *
     * @param webRequest 查询条件
     * @return 查询结果
     */
    @ApiOperation("实时获取桌面池中桌面数量相关信息")
    @RequestMapping(value = "desktopUseRate", method = RequestMethod.POST)
    public DefaultWebResponse getCurrentDesktopUseRate(StatisticDesktopUsedRateRequest webRequest) {
        Assert.notNull(webRequest, "webRequest must not be null");

        StatisticDesktopPoolDesktopCountDTO response = desktopPoolDashboardAPI.getCurrentDesktopPoolInfo(webRequest.getCbbDesktopPoolType(),
                webRequest.getDesktopPoolType(),
                webRequest.getDesktopPoolId());
        return DefaultWebResponse.Builder.success(response);
    }
}
