package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopOnlineSituationStatisticsResponse;

/**
 * Description:  统计云桌面历史运行状态
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
public interface StatisticsDeskTopService {

    /**
     * 按一天维度统计云桌面历史在线情况
     *
     * @param request 云桌面历历史情况统计请求
     * @return 返回统计结果
     */
    DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituationByDay(DesktopOnlineSituationStatisticsRequest request);

    /**
     * 按一个月维度统计云桌面历历史在线情况
     *
     * @param request 云桌面历历史情况统计请求
     * @return 返回统计结果
     */
    DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituationByMonth(DesktopOnlineSituationStatisticsRequest request);

    /**
     * 按一年维度统计云桌面历历史在线情况
     *
     * @param request 云桌面历历史情况统计请求
     * @return 返回统计结果
     */
    DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituationByYear(DesktopOnlineSituationStatisticsRequest request);
}
