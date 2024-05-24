package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api;

import com.ruijie.rcos.rcdc.rco.module.impl.connector.response.GetComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.RemoveComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.RcoRcdcToOneAgentAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbWakeUpTerminalInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.OneWay;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: PC终端相关tcp接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/21
 *
 * @author zqj
 */
@Tcp
public interface ComputerTcpAPI {

    /**
     * 下发唤醒PC终端
     *
     * @param terminalId            PC终端Id
     * @param wakeUpTerminalInfoDTO 唤醒配置
     * @throws BusinessException 业务异常
     **/
    @ApiAction(RcoRcdcToOneAgentAction.WAKE_UP_COMPUTER)
    @OneWay
    void wakeUpComputer(@SessionAlias String terminalId, CbbWakeUpTerminalInfoDTO wakeUpTerminalInfoDTO)
            throws BusinessException;

    /**
     * cdc通知OA PC终端信息
     * @param deskId deskId
     * @param computerInfoDTO computerInfoDTO
     */
    @ApiAction(RcoRcdcToOneAgentAction.CDC_NOTIFY_OA_COMPUTER_INFO)
    @OneWay
    void notifyOaComputerInfo(@SessionAlias String deskId, GetComputerInfoResponse computerInfoDTO);

}
