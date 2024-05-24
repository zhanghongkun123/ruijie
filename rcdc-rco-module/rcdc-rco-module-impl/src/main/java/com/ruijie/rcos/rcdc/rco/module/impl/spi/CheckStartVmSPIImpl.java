package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.message.MessageUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineAutoStartVmDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 查询是否需要自动进入虚机
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/18 14:50
 *
 * @author zjy
 */
@DispatcherImplemetion(ShineAction.CHECK_START_VM)
public class CheckStartVmSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckStartVmSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private UserService userService;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");
        LOGGER.info("接收到桌面是否自动虚机查询请求，终端id:{}", cbbDispatcherRequest.getTerminalId());

        boolean isStartVmFromCache = userDesktopOperateAPI.checkAutoStartVmFromCache(cbbDispatcherRequest.getTerminalId());
        ShineAutoStartVmDTO startVmDTO = new ShineAutoStartVmDTO();
        startVmDTO.setStartVm(isStartVmFromCache);
        if (isStartVmFromCache) {
            try {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(cbbDispatcherRequest.getTerminalId());
                ViewUserDesktopEntity desktopEntity = queryCloudDesktopService.checkDesktopExistInDeskViewById(terminalDTO.getBindDeskId());
                if (!IdvTerminalModeEnums.PUBLIC.name().equalsIgnoreCase(desktopEntity.getIdvTerminalModel())) {
                    RcoViewUserEntity userEntity = userService.getUserInfoById(desktopEntity.getUserId());
                    startVmDTO.setUserName(userEntity.getUserName());
                    startVmDTO.setPassword(userEntity.getPassword());
                }
            } catch (Exception ex) {
                LOGGER.error(String.format("获取用户信息发生异常，终端id: %s", cbbDispatcherRequest.getTerminalId()), ex);
            }
        }

        messageHandlerAPI.response(MessageUtils.buildResponseMessage(cbbDispatcherRequest, startVmDTO));
    }
}
