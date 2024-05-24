package com.ruijie.rcos.rcdc.rco.module.impl.spi.startvmhandler;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.StartVmMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description: 启动云桌面（当前状态处于移动中状态）
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
@Service
public class StartVmFromMovingStateHandler extends AbstractMessageHandlerTemplate<StartVmDispatcherDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmFromMovingStateHandler.class);


    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    protected boolean isNeedHandleMessage(StartVmDispatcherDTO request) {
        // 移动中
        return CbbCloudDeskState.MOVING == request.getDeskState();
    }

    @Override
    protected void processMessage(StartVmDispatcherDTO request) throws BusinessException {
        String terminalId = request.getDispatcherRequest().getTerminalId();
        UUID desktopId = request.getDesktopId();
        LOGGER.info("当前桌面状态处于[{}]状态，terminalId={},desktopId={}", request.getDeskState(), terminalId, desktopId);

        String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_VM_MOVING);
        try {
            shineMessageHandler.responseMessage(request.getDispatcherRequest(), StartVmMessageCode.CODE_ERR_OTHER, errMsg);
            LOGGER.info("应答报文给终端[{}]成功，应答状态码：{},应答内容：{}", terminalId, StartVmMessageCode.CODE_ERR_OTHER, errMsg);
        } catch (Exception e) {
            LOGGER.error("应答报文给终端[" + terminalId + "]失败，应答状态码：" + StartVmMessageCode.CODE_ERR_OTHER + "应答内容：" + errMsg, e);
        }
    }
}
