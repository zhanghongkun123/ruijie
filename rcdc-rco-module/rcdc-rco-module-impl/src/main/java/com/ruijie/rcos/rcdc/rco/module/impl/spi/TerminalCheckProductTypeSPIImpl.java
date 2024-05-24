package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.BindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Description: 检查终端型号
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/22
 *
 * @author luocl
 */
@DispatcherImplemetion(ShineAction.CHECK_PRODUCT_TYPE)
public class TerminalCheckProductTypeSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalCheckProductTypeSPIImpl.class);

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Autowired
    protected CbbTranspondMessageHandlerAPI messageHandlerAPI;

    /**
     * 已知终端类型
     */
    private static final int KNOWN = 0;

    /**
     * 未知终端类型
     */
    private static final int UNKNOWN = 1;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(request.getTerminalId(), "terminalId cannot be null!");
        LOGGER.info("check_product_type: id [{}]", request.getTerminalId());
        CbbTerminalBasicInfoDTO terminalDTO;
        CbbResponseShineMessage<?> cbbResponseShineMessage;
        try {
            terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("获取终端信息失败",e);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }
        // 没有终端型号，不需要检查
        if (StringUtils.isEmpty(terminalDTO.getProductType())) {
            cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, new Object());
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }

        // 非自研终端，不需要检查
        if (!cbbTerminalConfigAPI.isRjTerminal(terminalDTO.getProductType())) {
            cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, new Object());
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }

        boolean isProductMatch = cbbTerminalConfigAPI.isValidateTerminal(terminalDTO.getProductType());
        if (!isProductMatch) {
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, UNKNOWN);
        } else {
            cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, new Object());
        }
        messageHandlerAPI.response(cbbResponseShineMessage);
    }
}
