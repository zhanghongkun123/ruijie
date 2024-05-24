package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbEstClientExitNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * EstClient端退出消息通知处理类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-2-18
 *
 * @author artom
 */
public class EstClientExitNotifySPIImpl implements CbbEstClientExitNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstClientExitNotifySPIImpl.class);

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UserDesktopDAO desktopDAO;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    /** est_client退出消息请求参数id字段名 */
    private static final String REQUEST_PARAM_ID = "id";

    @Override
    public void notifyEstClientExit(CbbDispatcherDTO dispatcherDTO) {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is not null");

        LOGGER.info("接收到EST-Client退出消息，请求参数为:[" + dispatcherDTO.getData() + "]");
        String data = dispatcherDTO.getData();
        JSONObject dataJson = JSONObject.parseObject(data);
        String id = dataJson.getString(REQUEST_PARAM_ID);
        if (id == null) {
            throw new RuntimeException("est_client_exit message is illegal, data is : " + data);
        }
        UUID deskId = UUID.fromString(id);
        UserDesktopEntity entity = desktopDAO.findByCbbDesktopId(deskId);
        if (entity == null) {
            throw new RuntimeException("est_client_exit message is illegal, data is : " + data);
        }
        try {
            if (entity.getDesktopType() == CbbCloudDeskType.VDI) {
                CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopStrategyByDesktopId(new IdRequest(deskId));
                CbbDeskStrategyVDIDTO cbbDeskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(cloudDesktopDetailDTO.getDesktopStrategyId());
                if (cbbDeskStrategyVDIDTO != null && cbbDeskStrategyVDIDTO.getPowerPlanTime() > 0) {
                    desktopAPI.recordDeskShutdownDate(deskId, cbbDeskStrategyVDIDTO.getPowerPlanTime());
                }
            }
        } catch (BusinessException e) {
            LOGGER.info("终端[{}]获取云桌面信息[{}]失败", dispatcherDTO.getTerminalId(), deskId);
        }

        cbbVDIDeskMgmtAPI.estClientExitHandle(entity.getCbbDesktopId());
        LOGGER.info("终端[{}]退出EST", dispatcherDTO.getTerminalId());

        CbbTerminalBasicInfoDTO terminalBasicInfoDTO = null;
        try {
            terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(dispatcherDTO.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("获取终端信息失败：{}", e);
        }
        if (terminalBasicInfoDTO != null) {
            CbbTerminalPlatformEnums platformEnums = terminalBasicInfoDTO.getTerminalPlatform();
            if (CbbTerminalPlatformEnums.APP != platformEnums) {
                LOGGER.info("非软终端模式，需要退出用户.");
                userLoginSession.removeLoginUser(dispatcherDTO.getTerminalId());
            }
        }

        // 处理休眠导致的est_logout问题
        doAfterEstClientExit(deskId, dispatcherDTO.getTerminalId());
    }

    private void doAfterEstClientExit(UUID deskId, String terminalId) {
        CbbDeskDTO cbbDeskDTO;
        try {
            cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
        } catch (Exception e) {
            LOGGER.error("获取桌面[{}]信息出现异常,异常信息", deskId, e);
            return;
        }
        if (cbbDeskDTO != null && (cbbDeskDTO.getDeskType() == CbbCloudDeskType.VDI || cbbDeskDTO.getDeskType() == CbbCloudDeskType.THIRD)) {
            UserDesktopEntity entity = desktopDAO.findByCbbDesktopId(deskId);
            boolean isUpdate = false;
            if (Objects.equals(terminalId, entity.getTerminalId())) {
                entity.setTerminalId(null);
                entity.setHasTerminalRunning(false);
                isUpdate = true;
            }
            // 休眠时VM没有通知事件,为了池桌面回收，补充记录云桌面断连的时间
            if (CbbCloudDeskState.SLEEP == cbbDeskDTO.getDeskState() && Objects.isNull(entity.getConnectClosedTime())) {
                entity.setConnectClosedTime(new Date());
                isUpdate = true;
            }
            if (isUpdate) {
                desktopDAO.save(entity);
            }
        }
    }

}
