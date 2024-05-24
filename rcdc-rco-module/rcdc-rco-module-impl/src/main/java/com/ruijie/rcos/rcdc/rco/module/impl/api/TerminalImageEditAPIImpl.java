package com.ruijie.rcos.rcdc.rco.module.impl.api;


import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalImageEditAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNetworkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description: 终端镜像编辑API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/28
 *
 * @author songxiang
 */
public class TerminalImageEditAPIImpl implements TerminalImageEditAPI {

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalImageEditAPIImpl.class);

    @Override
    public void checkTerminalOnline(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        if (response.getState() != CbbTerminalStateEnums.ONLINE) {
            LOGGER.error("终端镜像编辑，检测到终端状态[" + response.getState().name() + "]不是在线的状态");
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_NOT_ONLINE, terminalAddr);
        }

    }

    @Override
    public void checkTerminalOnlineAndWired(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        if (response.getState() != CbbTerminalStateEnums.ONLINE) {
            LOGGER.error("终端镜像编辑，检测到终端状态[{}]不是在线的状态", response.getState().name());
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_NOT_ONLINE, terminalAddr);
        }

        CbbNetworkModeEnums networkAccessMode = response.getNetworkAccessMode();
        if (networkAccessMode != null && CbbNetworkModeEnums.WIRED != networkAccessMode) {
            LOGGER.error("终端镜像编辑，检测到终端连接状态[{}]不是有线连接状态", networkAccessMode);
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_NOT_WIRED, terminalAddr);
        }
        if (networkAccessMode == null) {
            LOGGER.error("终端镜像编辑，检测到终端连接状态[{}]不是有线连接状态", networkAccessMode);
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_NOT_WIRED, terminalAddr);
        }
    }

}
