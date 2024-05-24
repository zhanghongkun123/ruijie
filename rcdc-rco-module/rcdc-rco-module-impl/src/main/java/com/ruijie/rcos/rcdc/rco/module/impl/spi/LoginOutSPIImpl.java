package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 终端用户退出登录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/2/21
 *
 * @author Jarman
 */
@DispatcherImplemetion(ShineAction.LOGIN_OUT)
public class LoginOutSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginOutSPIImpl.class);

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        LOGGER.info("终端用户退出登录，terminalId={}", request.getTerminalId());
        userLoginSession.removeLoginUser(request.getTerminalId());
        // 解除云桌面绑定终端
        unbindDesktopBindTerminal(request.getTerminalId());
    }

    private void unbindDesktopBindTerminal(String terminalId) {
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (userTerminalEntity == null) {
            LOGGER.error("终端[{}]不存在", terminalId);
            return;
        }

        cloudDesktopOperateService.unbindDesktopTerminal(userTerminalEntity);
    }

}
