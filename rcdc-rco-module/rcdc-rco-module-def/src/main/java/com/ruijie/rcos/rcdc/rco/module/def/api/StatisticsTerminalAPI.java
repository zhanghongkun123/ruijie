package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description: StatisticsTerminalAPI
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/1
 *
 * @author wjp
 */
public interface StatisticsTerminalAPI {

    /**
     * 设备上线记录
     * 
     * @param terminalId 终端历史情况统计请求
     * @param platform 终端历史情况统计请求
     */
    void recordTerminalOnlineSituation(String terminalId, CbbTerminalPlatformEnums platform);
}
