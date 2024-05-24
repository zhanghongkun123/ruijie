package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 系统关闭桌面事件处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/8
 *
 * @author Jarman
 */
@Service
public class CloseDeskBySystemHandler extends AbstractCloseDeskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseDeskBySystemHandler.class);

    @Autowired
    private RemoteAssistMgmtAPI cbbRemoteAssistMgmtAPI;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.CLOSE_DESK_BY_SYSTEM == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) {
        LOGGER.info("CLOSE_DESK_BY_SYSTEM，deskId={}", request.getDeskId());
        if (!Boolean.TRUE.equals(request.getIsSuccess())) {
            LOGGER.warn("关闭桌面消息状态为失败，不做后续处理，桌面id为[{}]", request.getDeskId());
            return;
        }

        cbbRemoteAssistMgmtAPI.deskShutdownHandle(request.getDeskId());

        // 关机后校验，变更策略信息
        desktopUpdateService.updateNotRecoverableVDIConfigAsync(request.getDeskId());

        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);

        // 应用主机同步状态
        rcaHostAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.CLOSE);

        // 删除所有会话记录
        desktopSessionServiceAPI.deleteSessionByDeskId(request.getDeskId());

        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(request.getDeskId());
        if (desktopRequestDTO == null) {
            LOGGER.error("非用户或RCDC管理操作导致的关机，rco桌面id为[{}]，cbb桌面id为[{}]", request.getDeskId(), request.getDeskId());
            return;
        }

        LOGGER.info("关闭云桌面通知，桌面[{}]是否运行在终端上[{}] ", request.getDeskId(), desktopRequestDTO.isDesktopRunInTerminal());
        if (Boolean.TRUE.equals(request.getIsSuccess()) && desktopRequestDTO.isDesktopRunInTerminal()) {
            // 只有桌面在终端上运行时 没有报错情况才需要通知shine云桌面关闭的消息
            notifyTerminalHasCloseDesktop(request.getDeskId(), desktopRequestDTO);
            // 记录云桌面日志处理
            logDeskEvent(desktopRequestDTO, request.getDeskId());
        }
        // 桌面已关机，移除缓存内容
        desktopOperateRequestCache.removeCache(request.getDeskId());

    }


    private void logDeskEvent(DesktopRequestDTO desktopRequestDTO, UUID deskId) {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
        Assert.notNull(userDesktopEntity, "userDesktopEntity must not be null, deskId = " + deskId);

        if (!desktopRequestDTO.isDesktopInnerShutdown()) {
            LOGGER.info("非云桌面内部关机，不记录云桌面日志，云桌面id[{}]", deskId);
            return;
        }
        LOGGER.info("记录云桌面[{}]关闭日志", deskId);

        DesktopOpLogDTO req = new DesktopOpLogDTO();
        req.setDesktopId(deskId);
        req.setDesktopName(userDesktopEntity.getDesktopName());
        req.setEventName(DesktopOpEvent.CLOSE);
        req.setOperatorType(DesktopOpType.SYSTEM_EVENT);
        req.setDetail(LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_SYSTEM_CLOSE, userDesktopEntity.getDesktopName()));
        req.setTerminalId(userDesktopEntity.getTerminalId());

        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

}
