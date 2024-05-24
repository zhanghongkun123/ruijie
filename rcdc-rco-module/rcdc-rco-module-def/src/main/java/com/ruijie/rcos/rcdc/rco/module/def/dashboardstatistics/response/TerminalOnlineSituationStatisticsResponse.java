package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response;

import java.util.List;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.TerminalOnlineSituationStatisticsDTO;

/**
 * Description: TerminalOnlineSituationStatisticsResponse
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class TerminalOnlineSituationStatisticsResponse {

    private List<TerminalOnlineSituationStatisticsDTO> terminalOnlineSituationStatisticsList;

    public List<TerminalOnlineSituationStatisticsDTO> getTerminalOnlineSituationStatisticsList() {
        return terminalOnlineSituationStatisticsList;
    }

    public void setTerminalOnlineSituationStatisticsList(List<TerminalOnlineSituationStatisticsDTO> terminalOnlineSituationStatisticsList) {
        this.terminalOnlineSituationStatisticsList = terminalOnlineSituationStatisticsList;
    }

}
