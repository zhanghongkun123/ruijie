package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.response.ShineNicWorkModeResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNicWorkModeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 更新网络模式的SPI
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
@DispatcherImplemetion(ShineAction.GET_NETWORK_MODEL)
public class UpdateNetworkModelSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateNetworkModelSPIImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");

        String terminalId = request.getTerminalId();

        CbbTerminalBasicInfoDTO basicInfoDTO;
        try {
            basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            LOGGER.error(String.format("查询终端[%s]信息失败，失败原因：", terminalId), e);
            response(request, CommonMessageCode.CODE_ERR_OTHER, new ShineNicWorkModeResponse());
            return;
        }

        CbbNicWorkModeEnums nicWorkMode = basicInfoDTO.getNicWorkMode();
        LOGGER.info("响应终端[{}]网卡工作模式为[{}]", terminalId, nicWorkMode);
        response(request, CommonMessageCode.SUCCESS, new ShineNicWorkModeResponse(nicWorkMode.getName()));
    }

    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("终端[" + request.getTerminalId() + "]响应网卡工作模式失败，e={}", e);
        }
    }
}
