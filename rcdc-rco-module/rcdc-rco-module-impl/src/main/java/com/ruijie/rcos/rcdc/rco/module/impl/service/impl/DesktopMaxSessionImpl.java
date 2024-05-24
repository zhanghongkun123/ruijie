package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.common.message.AbstractRcdcHostMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.OneAgentMaxSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DesktopSessionIdleTimeDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description: oa请求会话断开时长
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
@TcpAction(RcoOneAgentToRcdcAction.OA_GET_CDC_SESSION_CONFIG)
@TargetHost({HostTypeEnums.THIRD_HOST, HostTypeEnums.CLOUD_DESK})
@Service
public class DesktopMaxSessionImpl extends AbstractRcdcHostMessageHandler<OneAgentMaxSessionDTO, DesktopSessionIdleTimeDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopMaxSessionImpl.class);

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    protected OneAgentMaxSessionDTO innerProcessMessage(DesktopSessionIdleTimeDTO desktopSessionIdleTimeDTO) throws BusinessException {
        LOGGER.info("oa请求最大会话数,[{}]", JSON.toJSONString(desktopSessionIdleTimeDTO));
        UUID deskId = desktopSessionIdleTimeDTO.getHostId();
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        OneAgentMaxSessionDTO maxSessionDTO = new OneAgentMaxSessionDTO();
        if (deskDTO != null && deskDTO.getDesktopPoolId() != null) {
            CbbDesktopPoolDTO desktopPoolDetail = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(deskDTO.getDesktopPoolId());
            maxSessionDTO.setMaxSession(desktopPoolDetail.getMaxSession());
        }
        LOGGER.info("桌面id[{}]应答oa最大会话数,[{}]", deskId, JSON.toJSONString(maxSessionDTO));
        return maxSessionDTO;
    }
}
