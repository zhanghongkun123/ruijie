package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.SleepVmFinishRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 系统自动休眠桌面事件处理
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/20
 *
 * @author fyq
 */
@Service
public class SleepDeskBySystemHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepDeskBySystemHandler.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private RemoteAssistMgmtAPI cbbRemoteAssistMgmtAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private RcaHostAPI rcaHostAPI;


    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.SLEEP_DESK_BY_SYSTEM == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) {
        LOGGER.info("SLEEP_DESK_BY_SYSTEM，deskId={}", request.getDeskId());
        cbbRemoteAssistMgmtAPI.deskShutdownHandle(request.getDeskId());
        // 记录休眠日志
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(request.getDeskId());
        logDeskEvent(userDesktopEntity);

        // 通知 UWS 云桌面休眠
        if (request.getIsSuccess()) {
            uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.SLEEP);
            rcaHostAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.SLEEP);
            notifyTerminalHasSleepDesktop(request.getDeskId(), userDesktopEntity.getTerminalId());

            // 休眠后补充池桌面断连时间
            if (Objects.isNull(userDesktopEntity.getConnectClosedTime())) {
                userDesktopService.setConnectClosedTime(userDesktopEntity.getCbbDesktopId(), new Date());
            }
        }
    }

    private void logDeskEvent(UserDesktopEntity userDesktopEntity) {
        DesktopOpLogDTO req = new DesktopOpLogDTO();
        req.setDesktopId(userDesktopEntity.getCbbDesktopId());
        req.setDesktopName(userDesktopEntity.getDesktopName());
        req.setEventName(DesktopOpEvent.SUSPEND);
        req.setOperatorType(DesktopOpType.SYSTEM_EVENT);
        req.setDetail(LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_SYSTEM_SUSPEND, userDesktopEntity.getDesktopName()));
        req.setTerminalId(userDesktopEntity.getTerminalId());
        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    /**
     * 通知shine桌面已关闭
     */
    private void notifyTerminalHasSleepDesktop(UUID deskId, String terminalId) {
        if (deskId == null || StringUtils.isEmpty(terminalId)) {
            LOGGER.info("桌面id或终端id为空，不发送通知");
            return;
        }

        LOGGER.info("通知shine虚机睡眠,desktopId={},terminalId={},est退出", deskId, terminalId);
        try {
            shineMessageHandler.requestContent(terminalId, ShineAction.RCDC_SHINE_SLEEP_VM_FINISH, new SleepVmFinishRequest(deskId));
        } catch (Exception e) {
            LOGGER.error("发送云桌面关闭报文给shine失败；terminalId=" + terminalId, e);
        }
    }
}
