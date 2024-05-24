package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 云桌面唤醒中消息处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-23 20:56:00
 *
 * @author zjy
 */
@Service
public class WakingDesktopHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.WAKING_DESK == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws Exception {
        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.WAKING);
    }
}
