package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.TerminalOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.TerminalOnlineSituationStatisticsResponse;

/**
 * Description: StatisticsTerminalService
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public interface StatisticsTerminalService {

    /**
     * 按一天维度统计终端历史在线情况
     * 
     * @param request 终端历史情况统计请求
     * @return 返回统计结果
     */
    TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituationByDay(TerminalOnlineSituationStatisticsRequest request);

    /**
     * 按一个月维度统计终端历史在线情况
     * 
     * @param request 终端历史情况统计请求
     * @return 返回统计结果
     */
    TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituationByMonth(TerminalOnlineSituationStatisticsRequest request);

    /**
     * 按一年维度统计终端历史在线情况
     * 
     * @param request 终端历史情况统计请求
     * @return 返回统计结果
     */
    TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituationByYear(TerminalOnlineSituationStatisticsRequest request);
}
