package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.BindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 终端获取桌面用户信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/15
 *
 * @author WuShengQiang
 */
@DispatcherImplemetion(Constants.TERMINAL_USER_CHANGE)
public class TerminalUserChangeSPIImpl implements CbbDispatcherHandlerSPI {

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    protected CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(request.getTerminalId(), "terminalId cannot be null!");
        TerminalDTO terminalDTO = userTerminalMgmtAPI.findByTerminalId(request.getTerminalId());
        CbbResponseShineMessage<?> cbbResponseShineMessage;
        if (terminalDTO == null) {
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }
        BindUserRequest bindUserRequest = new BindUserRequest();
        bindUserRequest.setUserName(terminalDTO.getBindUserName());
        cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, bindUserRequest);
        messageHandlerAPI.response(cbbResponseShineMessage);
    }
}
