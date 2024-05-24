package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 还原云桌面(只删除快照)事件处理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/05/17
 *
 * @author linke
 */
@Service
public class RestoreDeskOnlyDeleteSnapshotHandler extends AbstractCloseDeskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestoreDeskOnlyDeleteSnapshotHandler.class);

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.RESTORE_DESK_DELETE_SNAPSHOT == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) {
        LOGGER.info("RESTORE_DESK_DELETE_SNAPSHOT，deskId={}", request.getDeskId());

        // 自动异步变更VDI配置信息
        desktopUpdateService.updateVDIConfigSync(request.getDeskId());

        // 通知 UWS 云桌面关机
        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);
    }
}
