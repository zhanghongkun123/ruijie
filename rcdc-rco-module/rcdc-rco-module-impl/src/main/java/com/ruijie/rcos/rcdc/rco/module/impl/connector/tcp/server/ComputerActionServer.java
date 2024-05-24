package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.common.dto.BaseResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ChangeComputerWorkModelDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerBindUserDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerReportSystemInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.GetComputerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.response.GetComputerInfoResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: PC终端相关功能
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */
@Tcp
public interface ComputerActionServer {

    /**
     * 获取PC终端信息
     *
     * @param terminalId      PC终端Id
     * @param computerInfoDTO computerInfoDTO
     * @return GetComputerInfoDTO
     * @throws BusinessException 业务异常
     **/
    @ApiAction(RcoOneAgentToRcdcAction.COMPUTER_INFO)
    GetComputerInfoResponse getComputerInfo(@SessionAlias String terminalId, GetComputerInfoDTO computerInfoDTO)
            throws BusinessException;

    /**
     * 上报系统信息
     *
     * @param terminalId PC终端Id
     * @param reportSystemInfoDTO 请求参数
     * @throws BusinessException 业务异常
     **/
    @ApiAction(RcoOneAgentToRcdcAction.REPORT_SYSTEM_INFO)
    void reportSystemInfo(@SessionAlias String terminalId, ComputerReportSystemInfoDTO reportSystemInfoDTO)
            throws BusinessException;

    /**
     * PC终端绑定用户
     *
     * @param terminalId          PC终端Id
     * @param computerBindUserDTO 配置
     * @return BaseResultDTO
     * @throws BusinessException 业务异常
     */
    @ApiAction(RcoOneAgentToRcdcAction.COMPUTER_BIND_USER)
    BaseResultDTO computerBindUser(@SessionAlias String terminalId, ComputerBindUserDTO computerBindUserDTO)
            throws BusinessException;


    /**
     * oa变更PC终端工作模式通知
     *
     * @param terminalId                 PC终端Id
     * @param changeComputerWorkModelDTO changeComputerWorkModelDTO
     * @return BaseResultDTO
     */
    @ApiAction(RcoOneAgentToRcdcAction.OA_NOTIFY_CDC_COMPUTER_WORK_MODEL)
    BaseResultDTO oaNotifyCdcComputerWorkModel(@SessionAlias String terminalId, ChangeComputerWorkModelDTO changeComputerWorkModelDTO);


}
