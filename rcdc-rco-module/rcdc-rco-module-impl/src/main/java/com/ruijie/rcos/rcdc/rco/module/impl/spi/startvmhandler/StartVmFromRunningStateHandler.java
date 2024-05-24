package com.ruijie.rcos.rcdc.rco.module.impl.spi.startvmhandler;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VmState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dto.NotifyCloudDeskRemountDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.exception.ShineUserNotLoginException;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 启动云桌面（当前状态处于允许状态）
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
@Service
public class StartVmFromRunningStateHandler extends AbstractMessageHandlerTemplate<StartVmDispatcherDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmFromRunningStateHandler.class);

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private UserLoginSession userLoginSession;

    @Override
    protected boolean isNeedHandleMessage(StartVmDispatcherDTO request) {
        if (request.getVmState() != null) {
            return request.getVmState() == VmState.ACTIVE;
        }
        return CbbCloudDeskState.RUNNING == request.getDeskState();
    }

    @Override
    protected void processMessage(StartVmDispatcherDTO request) throws BusinessException {
        String terminalId = request.getDispatcherRequest().getTerminalId();
        UUID desktopId = request.getDesktopId();
        LOGGER.info("当前桌面状态处于[{}]状态，terminalId={},desktopId={}", request.getDeskState(), terminalId, desktopId);
        UserInfo userInfo = userLoginSession.getLoginUserInfo(terminalId);
        if (userInfo == null || StringUtils.isEmpty(userInfo.getUserName())) {
            throw new ShineUserNotLoginException(BusinessKey.RCDC_RCO_DESK_START_FAIL_REASON_USER_NOT_LOGIN, desktopId.toString());
        }
        final DesktopRequestDTO dto = new DesktopRequestDTO(request.getDispatcherRequest(), userInfo.getUserName());

        desktopOperateRequestCache.addCache(request.getDesktopId(), dto);
        try {
            CloudDesktopStartRequest startRequest = new CloudDesktopStartRequest(desktopId);
            startRequest.setVmState(request.getVmState());
            startRequest.setDeskBackupState(request.getDeskBackupState());
            cloudDesktopOperateService.startDesktop(startRequest);
            // 如果桌面已经处于运行中，本流程超时5秒够了
            dto.getLatch().await(5, TimeUnit.SECONDS);
        } catch (BusinessException e) {
            throw e;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // 此异常不会出现
            throw new RuntimeException(ex);
        }
    }
}
