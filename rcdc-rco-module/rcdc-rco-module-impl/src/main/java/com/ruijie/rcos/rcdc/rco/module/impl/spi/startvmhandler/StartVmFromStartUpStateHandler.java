package com.ruijie.rcos.rcdc.rco.module.impl.spi.startvmhandler;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.exception.ShineUserNotLoginException;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 启动云桌面（当前状态处于启动中、重启中状态）
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
@Service
public class StartVmFromStartUpStateHandler extends AbstractMessageHandlerTemplate<StartVmDispatcherDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmFromStartUpStateHandler.class);

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private UserLoginSession userLoginSession;

    @Override
    protected boolean isNeedHandleMessage(StartVmDispatcherDTO request) {
        // 启动中或重启中
        return CbbCloudDeskState.START_UP == request.getDeskState() || CbbCloudDeskState.REBOOTING == request.getDeskState();
    }

    @Override
    protected void processMessage(StartVmDispatcherDTO request) throws BusinessException {
        Assert.notNull(request, "request cannot null");
        String terminalId = request.getDispatcherRequest().getTerminalId();
        UUID desktopId = request.getDesktopId();
        LOGGER.info("当前桌面状态处于[{}]状态，terminalId={},desktopId={}", request.getDeskState(), terminalId, desktopId);

        UserInfo userInfo = userLoginSession.getLoginUserInfo(terminalId);
        if (userInfo == null || StringUtils.isEmpty(userInfo.getUserName())) {
            throw new ShineUserNotLoginException(BusinessKey.RCDC_RCO_DESK_START_FAIL_REASON_USER_NOT_LOGIN, desktopId.toString());
        }

        desktopOperateRequestCache.addCache(desktopId, new DesktopRequestDTO(request.getDispatcherRequest(), userInfo.getUserName()));
    }
}
