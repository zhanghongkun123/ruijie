package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/27
 *
 * @author chen zj
 */
@Service
public class StartIDVDesktopHandler extends AbstractIDVDesktopOperateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartIDVDesktopHandler.class);

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return request.getOperateType() == CbbCloudDeskOperateType.START_IDV_DESK;
    }

    @Override
    protected void doProcessMessage(CbbDeskOperateNotifyRequest request) {
        try {
            DesktopOpLogDTO desktopOpLogRequest = buildDesktopOpLogRequest(request.getDeskId(), BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_START_SUC);
            desktopOpLogRequest.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);
            desktopOpLogRequest.setEventName(DesktopOpEvent.START);
            UserDesktopEntity deskInfo = userDesktopDAO.findByCbbDesktopId(request.getDeskId());
            desktopOpLogRequest.setTerminalId(deskInfo.getTerminalId());
            desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
        } catch (BusinessException e) {
            LOGGER.error("记录启动IDV云桌面[id:{}]日志出现异常:{}", request.getDeskId(), e);
        }

        CbbDeskDTO cbbDeskDTO = null;
        try {
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(request.getDeskId());
        } catch (BusinessException e) {
            LOGGER.error("查询桌面状态信息失败，deskId=" + request.getDeskId(), e);
        }
        if (cbbDeskDTO != null) {
            // 通知 UWS 云桌面状态变更
            uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), cbbDeskDTO.getDeskState());
        }
    }
}
