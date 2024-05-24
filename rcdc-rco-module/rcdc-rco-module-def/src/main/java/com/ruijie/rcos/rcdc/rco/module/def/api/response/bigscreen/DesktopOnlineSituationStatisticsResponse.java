package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopOnlineSituationStatisticsDTO;

import java.util.List;

/**
 * Description: 云桌面历史运行状态响应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
public class DesktopOnlineSituationStatisticsResponse {

    private List<DesktopOnlineSituationStatisticsDTO> desktopOnlineSituationStatisticsList;

    public List<DesktopOnlineSituationStatisticsDTO> getDesktopOnlineSituationStatisticsList() {
        return desktopOnlineSituationStatisticsList;
    }

    public void setDesktopOnlineSituationStatisticsList(List<DesktopOnlineSituationStatisticsDTO> desktopOnlineSituationStatisticsList) {
        this.desktopOnlineSituationStatisticsList = desktopOnlineSituationStatisticsList;
    }
}
