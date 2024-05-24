package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbSessionInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbUpdateReportSessionSuccessDTO;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.common.message.AbstractRcdcHostMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月26日
 *
 * @author wangjie9
 */
@TcpAction(RcoOneAgentToRcdcAction.OA_SEND_CDC_SESSION_INFO)
@TargetHost({HostTypeEnums.THIRD_HOST, HostTypeEnums.CLOUD_DESK})
@Service
public class OneAgentSessionServiceImpl extends AbstractRcdcHostMessageHandler<CbbUpdateReportSessionSuccessDTO, CbbSessionInfoDTO>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(OneAgentSessionServiceImpl.class);

    @Autowired
    private DesktopSessionServiceAPI desktopSessionService;

    @Override
    protected CbbUpdateReportSessionSuccessDTO innerProcessMessage(CbbSessionInfoDTO sessionInfoDTO) throws BusinessException {
        LOGGER.info("桌面[{}]收到OA会话信息", sessionInfoDTO.getHostId());
        // 更新会话信息
        return desktopSessionService.synchronizeSessionInfo(sessionInfoDTO);
    }
}
