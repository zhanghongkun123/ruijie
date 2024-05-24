package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.ShutdownVmFinishRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年5月18日
 *
 * @author zhuangchenwu
 */
public abstract class AbstractCloseDeskHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCloseDeskHandler.class);

    @Autowired
    protected UserDesktopDAO userDesktopDAO;
    
    @Autowired
    private ShineMessageHandler shineMessageHandler;
    
    /**
     * 通知shine桌面已关闭
     */
    protected void notifyTerminalHasCloseDesktop(UUID deskId, DesktopRequestDTO desktopRequestDTO) {
        LOGGER.debug("notifyTerminalHasCloseDesktop deskId={}", deskId);
        CbbDispatcherRequest cbbDispatcherRequest = desktopRequestDTO.getCbbDispatcherRequest();
        LOGGER.info("通知shine虚机关闭,desktopId={},terminalId={},est退出", deskId, cbbDispatcherRequest.getTerminalId());
        try {
            shineMessageHandler.requestContent(cbbDispatcherRequest.getTerminalId(), ShineAction.RCDC_SHINE_CMM_SHUTDOWN_VM_FINISH,
                    new ShutdownVmFinishRequest(ShutdownVmFinishRequest.MODE_AUTO, deskId.toString()));
        } catch (Exception e) {
            LOGGER.error("发送云桌面关闭报文给shine失败；terminalId=" + cbbDispatcherRequest.getTerminalId(), e);
        }
    }
    
}
