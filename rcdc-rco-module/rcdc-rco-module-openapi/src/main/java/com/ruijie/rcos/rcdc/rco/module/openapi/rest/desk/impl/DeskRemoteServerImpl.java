package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskOperateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.DeskRemoteServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.StartDeskRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.thread.UwsAsyncShutdownDesktopThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.thread.UwsAsyncStartDesktopThread;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 新版远程桌面服务实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-17 15:38:00
 *
 * @author zjy
 */
public class DeskRemoteServerImpl implements DeskRemoteServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskRemoteServerImpl.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private OpenApiTaskInfoAPI openApiTaskInfoAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private TerminalWakeUpAPI terminalWakeUpAPI;

    @Autowired
    private CbbThirdPartyDeskOperateMgmtAPI cbbThirdPartyDeskOperateMgmtAPI;

    @Override
    public AsyncTaskResponse start(UUID deskId, @Nullable StartDeskRequest startDeskRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");

        CloudDesktopDetailDTO cloudDesktopDetailDTO = null;
        try {
            cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (Exception e) {
            LOGGER.error(String.format("获取桌面信息发生异常，桌面id: %s", deskId), e);
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_EXISTS, e);
        }

        if (CbbCloudDeskState.RUNNING.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDesktopState())) {
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_RUNNING);
        }

        if (CbbCloudDeskType.IDV.name().equals(cloudDesktopDetailDTO.getDeskType())) {
            try {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(cloudDesktopDetailDTO.getTerminalId());
                if (CbbTerminalStateEnums.ONLINE != terminalDTO.getTerminalState() && Boolean.TRUE != terminalDTO.getSupportRemoteWake()) {
                    LOGGER.info("IDV终端不处于运行状态且不支持远程唤醒，终端id: {}, 状态: {}", terminalDTO.getId(), terminalDTO.getTerminalState());
                    throw new BusinessException(RestErrorCode.OPEN_API_TERMINAL_NOT_SUPPORT_WAKE_UP);
                }
            } catch (BusinessException e) {
                LOGGER.info("获取终端发生异常，终端id: {}, ex: {}", cloudDesktopDetailDTO.getTerminalId(), e.getMessage());
                throw new BusinessException(RestErrorCode.OPEN_API_TERMINAL_NOT_EXIST, e);
            }
        }

        UwsAsyncStartDesktopThread thread = new UwsAsyncStartDesktopThread(deskId, AsyncTaskEnum.START_DESK,
                openApiTaskInfoAPI, userDesktopOperateAPI);
        thread.setCbbTerminalOperatorAPI(cbbTerminalOperatorAPI);
        thread.setUserTerminalMgmtAPI(userTerminalMgmtAPI);
        thread.setCloudDesktopMgmtAPI(userDesktopMgmtAPI);
        thread.setPageQueryBuilderFactory(pageQueryBuilderFactory);
        thread.setTerminalWakeUpAPI(terminalWakeUpAPI);
        if (startDeskRequest != null && startDeskRequest.getSupportCrossCpuVendor() != null) {
            thread.setSupportCrossCpuVendor(startDeskRequest.getSupportCrossCpuVendor());
        }
        ThreadExecutors.execute(Constant.START_DESKTOP_THREAD, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public AsyncTaskResponse shutdown(UUID deskId, Boolean force) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        Assert.notNull(force, "force is not null");

        CloudDesktopDetailDTO cloudDesktopDetailDTO = null;
        try {
            cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (Exception e) {
            LOGGER.error(String.format("获取桌面信息发生异常，桌面id: %s", deskId), e);
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_EXISTS, e);
        }

        if (!CbbCloudDeskState.RUNNING.name().equals(cloudDesktopDetailDTO.getDesktopState())) {
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_ALLOW_CLOSE_STATE);
        }

        UwsAsyncShutdownDesktopThread thread = new UwsAsyncShutdownDesktopThread(deskId, AsyncTaskEnum.SHUTDOWN_DESK,
                openApiTaskInfoAPI, userDesktopOperateAPI, userDesktopMgmtAPI);
        thread.setForce(force);
        ThreadExecutors.execute(Constant.SHUTDOWN_DESKTOP_THREAD, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public void restart(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        CloudDesktopDetailDTO cloudDesktopDetailDTO = null;
        try {
            cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            LOGGER.error(String.format("获取桌面信息发生异常，桌面id: %s", deskId), e);
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_EXISTS, e);
        }

        if (!CbbCloudDeskState.RUNNING.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDesktopState())) {
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_ALLOW_RESTART_STATE);
        }

        try {
            if (CbbCloudDeskType.VDI.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType())) {
                cbbVDIDeskOperateAPI.rebootDeskVDI(deskId);
            } else if (CbbCloudDeskType.THIRD.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType())) {
                cbbThirdPartyDeskOperateMgmtAPI.rebootDeskThird(deskId);
            } else {
                cbbIDVDeskOperateAPI.rebootDeskIDV(deskId);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("UWS重启云桌面系统异常，云桌面id: %s", deskId), e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
    }
}
