package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskTypeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalStatisticsDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 首页统一接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/4
 *
 * @author Jarman
 */
public interface StatisticsAPI {

    /**
     * 统计终端新
     *
     * @param groupIdArr 终端类型请求
     * @return 返回统计结果
     * @throws BusinessException ex
     */
    CbbTerminalStatisticsDTO statisticsTerminal(UUID[] groupIdArr) throws BusinessException;

    /**
     * 统计桌面数据，状态 + 桌面类型
     * 
     * @param request 桌面类型请求
     * @return 返回统计结果
     */

    DesktopStatisticsResponse statisticsDesktop(DeskTypeRequest request);
}
