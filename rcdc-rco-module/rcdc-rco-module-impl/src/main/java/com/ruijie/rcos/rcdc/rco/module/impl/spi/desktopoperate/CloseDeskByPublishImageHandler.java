package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 镜像模板发布事件处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/8
 *
 * @author Jarman
 */
@Service
public class CloseDeskByPublishImageHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseDeskByPublishImageHandler.class);

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.CLOSE_DESK_BY_PUBLISH_IMAGE == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) {
        LOGGER.info("CLOSE_DESK_BY_PUBLISH_IMAGE，deskId={}", request.getDeskId());
        DesktopRequestDTO requestDTO = new DesktopRequestDTO();
        requestDTO.setDesktopInnerShutdown(false);
        // 标记为管理员操作
        desktopOperateRequestCache.addCache(request.getDeskId(), requestDTO);

        // 关机后校验，变更策略信息
        desktopUpdateService.updateNotRecoverableVDIConfigAsync(request.getDeskId());

        // 通知 UWS 云桌面
        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);

        // 应用主机同步状态
        rcaHostAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);
    }
}
