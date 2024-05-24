package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/7
 *
 * @author linke
 */
public interface DesktopSessionLogService {

    /**
     * 处理桌面连接信息记录
     *
     * @param userId          userId
     * @param terminalId      terminalId
     * @param sessionInfoList sessionInfoList
     */
    void handleDeskSessionLog(UUID userId, String terminalId, List<UserSessionDTO> sessionInfoList);

    /**
     * 终端下线处理桌面连接信息记录
     *
     * @param terminalId terminalId
     */
    void handleDeskSessionLogTerminalOffline(String terminalId);
}
