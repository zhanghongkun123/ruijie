package com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.QrCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 扫码登录相关功能实现
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年3月9日
 *
 * @author zjy
 */
@Service
public class QrCodeServiceImpl implements QrCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeServiceImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Override
    public void notifyConfigUpdateToTerminal(CbbQrCodeConfigDTO cbbQrCodeConfigDTO) {
        Assert.notNull(cbbQrCodeConfigDTO, "cbbQrCodeConfigDTO is not null!");

        List<TerminalDTO> terminalDTOList = userTerminalMgmtAPI.listByState(CbbTerminalStateEnums.ONLINE);
        List<String> terminalIdList = terminalDTOList.stream().map(TerminalDTO::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(terminalIdList)) {
            LOGGER.info("当前不存在在线终端，不发送扫码登录配置变更通知");
            return;
        }

        LOGGER.info("存在在线终端数量 {} ，开始发送扫码登录配置变更通知", terminalIdList.size());
        for (String terminalId : terminalIdList) {
            try {
                shineMessageHandler.requestContent(terminalId, ShineAction.UWS_QR_CONFIG_UPDATE, cbbQrCodeConfigDTO);
            } catch (Exception e) {
                LOGGER.error(String.format("发送扫码登录配置变更通知异常，终端ID：%s，异常：", terminalId), e);
            }
        }

    }

    @Override
    public void notifyQrConfigUpdateToTerminal(IacQrCodeConfigDTO cbbQrCodeConfigDTO) {
        Assert.notNull(cbbQrCodeConfigDTO, "cbbQrCodeConfigDTO is not null!");

        List<TerminalDTO> terminalDTOList = userTerminalMgmtAPI.listByState(CbbTerminalStateEnums.ONLINE);
        List<String> terminalIdList = terminalDTOList.stream().map(TerminalDTO::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(terminalIdList)) {
            LOGGER.info("移动客户端扫码，当前不存在在线终端，不发送扫码登录配置变更通知");
            return;
        }

        LOGGER.info("移动客户端扫码，存在在线终端数量 {} ，开始发送扫码登录配置变更通知", terminalIdList.size());
        for (String terminalId : terminalIdList) {
            try {
                shineMessageHandler.requestContent(terminalId, ShineAction.MOBILE_CLIENT_QR_CONFIG_UPDATE, cbbQrCodeConfigDTO);
            } catch (Exception e) {
                LOGGER.error(String.format("移动客户端扫码发送扫码登录配置变更通知异常，终端ID：%s，异常：", terminalId), e);
            }
        }
    }
}
