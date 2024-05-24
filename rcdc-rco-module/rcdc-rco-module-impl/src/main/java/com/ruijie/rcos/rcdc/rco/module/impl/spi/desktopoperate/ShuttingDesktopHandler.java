package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 云桌面关机中消息处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-23 20:56:00
 *
 * @author zjy
 */
@Service
public class ShuttingDesktopHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.SHUTTING_DESK == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws Exception {
        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.SHUTTING);
        rcaHostAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.SHUTTING);
    }
}
