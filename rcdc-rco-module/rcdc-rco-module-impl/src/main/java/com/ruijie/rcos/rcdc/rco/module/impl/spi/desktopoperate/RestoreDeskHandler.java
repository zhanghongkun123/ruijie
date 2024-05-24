package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 还原云桌面 磁盘还原事件处理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/05/17
 *
 * @author linke
 */
@Service
public class RestoreDeskHandler extends AbstractCloseDeskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestoreDeskHandler.class);

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.RESTORE_DESK == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) {
        LOGGER.info("RESTORE_DESK，deskId={}", request.getDeskId());

        // 处理待变更镜像模板
        userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(request.getDeskId()));

        // 自动异步变更VDI配置信息
        desktopUpdateService.updateVDIConfigSync(request.getDeskId());

        // 清除池桌面绑定关系
        clearPoolDesktopUserRelation(request.getDeskId());

        // 通知 UWS 云桌面关机
        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);
    }

    private void clearPoolDesktopUserRelation(UUID deskId) {
        try {
            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
            if (Objects.isNull(cbbDeskDTO)) {
                return;
            }
            if (DesktopPoolType.DYNAMIC != cbbDeskDTO.getDesktopPoolType()) {
                return;
            }
            // 动态池需要删除绑定关系
            if (CbbDesktopSessionType.MULTIPLE == cbbDeskDTO.getSessionType()) {
                LOGGER.info("移除多会话桌面[{}]会话列表", deskId);
                desktopSessionServiceAPI.deleteSessionByDeskId(deskId);
            } else {
                userDesktopService.unbindUserAndDesktopRelation(deskId);
            }
        } catch (Exception e) {
            LOGGER.error("处理桌面[{}]还原后，池桌面清除用户绑定关系出现异常", deskId, e);
        }
    }
}
