package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description: EST 关闭桌面
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/8
 *
 * @author Jarman
 */
@Service
public class CloseDeskByEstHandler extends AbstractCloseDeskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseDeskByEstHandler.class);

    @Autowired
    private RemoteAssistMgmtAPI cbbRemoteAssistMgmtAPI;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.CLOSE_DESK_BY_EST == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) {
        LOGGER.info("CLOSE_DESK_BY_EST，deskId={}", request.getDeskId());
        cbbRemoteAssistMgmtAPI.deskShutdownHandle(request.getDeskId());

        // 获取缓存信息，进行消息通知
        notifyTerminal(request.getDeskId());

        // 关机后校验，变更策略信息
        desktopUpdateService.updateNotRecoverableVDIConfigAsync(request.getDeskId());

        // 关机完成通知UWS
        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);

        // 应用主机同步状态
        rcaHostAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);

        // 删除所有会话记录
        desktopSessionServiceAPI.deleteSessionByDeskId(request.getDeskId());
    }

    private void notifyTerminal(UUID desktopId) {
        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(desktopId);
        if (desktopRequestDTO == null) {
            LOGGER.info("云桌面[{}]关闭消息CloseDeskByEstHandler已处理，此处无需再处理", desktopId);
            return;
        }

        LOGGER.info("EST退出消息处理，是否为虚机内关机:{}", desktopRequestDTO.isDesktopInnerShutdown());
        if (desktopRequestDTO.isDesktopInnerShutdown()) {
            // 在虚机内直接关机情况下,发消息给shine
            notifyTerminalHasCloseDesktop(desktopId, desktopRequestDTO);
        }
    }
}
