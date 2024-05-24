package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.response.DefaultResponse;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.common.message.AbstractRcdcHostMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.WinServerAuthorityDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 桌面资源信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
@TcpAction(RcoOneAgentToRcdcAction.OA_SEND_CDC_WIN_SERVER_AUTHORITY)
@TargetHost({HostTypeEnums.THIRD_HOST, HostTypeEnums.CLOUD_DESK})
@Service
public class WinServerAuthorityServiceImpl extends AbstractRcdcHostMessageHandler<DefaultResponse, WinServerAuthorityDTO>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(WinServerAuthorityServiceImpl.class);

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    protected DefaultResponse innerProcessMessage(WinServerAuthorityDTO winServerAuthorityDTO) throws BusinessException {
        LOGGER.info("收到oneAgent上报应用主机上报windows-server授权是否到期：{}", JSON.toJSONString(winServerAuthorityDTO));
        boolean isExpire = winServerAuthorityDTO.getDaysLeft() <= 0;
        cbbDeskMgmtAPI.updateDeskOsActiveBySystem(winServerAuthorityDTO.getHostId(), !isExpire);
        DefaultResponse defaultResponse = new DefaultResponse();
        return defaultResponse;
    }

}
