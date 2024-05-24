package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.response;

import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO;

import java.util.List;

/**
 * Description: 统计桌面池使用率、桌面连接失败数量历史记录响应
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 18:42
 *
 * @author yxq
 */
public class StatisticDesktopPoolHistoryResponse {

    /**
     * 桌面池使用率
     */
    private List<DesktopPoolStatisticsResultDTO> deskUseRateStatisticsResultList;

    /**
     * 连接失败数量
     */
    private List<DesktopPoolStatisticsResultDTO> connectFaultStatisticsResultList;

    public List<DesktopPoolStatisticsResultDTO> getDeskUseRateStatisticsResultList() {
        return deskUseRateStatisticsResultList;
    }

    public void setDeskUseRateStatisticsResultList(List<DesktopPoolStatisticsResultDTO> deskUseRateStatisticsResultList) {
        this.deskUseRateStatisticsResultList = deskUseRateStatisticsResultList;
    }

    public List<DesktopPoolStatisticsResultDTO> getConnectFaultStatisticsResultList() {
        return connectFaultStatisticsResultList;
    }

    public void setConnectFaultStatisticsResultList(List<DesktopPoolStatisticsResultDTO> connectFaultStatisticsResultList) {
        this.connectFaultStatisticsResultList = connectFaultStatisticsResultList;
    }
}
