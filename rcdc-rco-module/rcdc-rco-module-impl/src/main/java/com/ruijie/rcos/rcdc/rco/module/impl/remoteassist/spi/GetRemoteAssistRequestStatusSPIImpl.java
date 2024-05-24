package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto.RemoteAssistStatusRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 获取请求远程协助状态
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/27
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(ShineAction.GET_REMOTE_ASSIST_REQUEST_STATUS)
public class GetRemoteAssistRequestStatusSPIImpl implements CbbDispatcherHandlerSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GetRemoteAssistRequestStatusSPIImpl.class);

    @Autowired
    private RemoteAssistService remoteAssistService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            CbbTerminalPlatformEnums terminalPlatform = terminalBasicInfoDTO.getTerminalPlatform();

            UUID deskId = null;
            if (StringUtils.isNotBlank(request.getData())) {
                RemoteAssistStatusRequestDTO data =
                        JSONObject.parseObject(request.getData(), RemoteAssistStatusRequestDTO.class);
                deskId = data.getDeskId();
            }

            UserDesktopEntity userDesktop;
            // vdi类型终端存在在一个终端上登录多个云桌面的场景
            if (terminalPlatform == CbbTerminalPlatformEnums.IDV || terminalPlatform == CbbTerminalPlatformEnums.PC
                    || terminalPlatform == CbbTerminalPlatformEnums.VOI) {
                // IDV PC VOI 固定终端
                userDesktop = userDesktopDAO.findUserDesktopEntityByTerminalId(terminalBasicInfoDTO.getTerminalId());
            } else {
                if (deskId == null) {
                    userDesktop = userDesktopService.findRunningInTerminalDesktop(terminalBasicInfoDTO.getTerminalId());
                } else {
                    userDesktop = userDesktopService.findByDeskId(deskId);
                }
            }

            boolean hasRequest = remoteAssistService.hasRequestRemoteAssist(userDesktop.getCbbDesktopId());
            LOGGER.debug("desk <{}> request remote status <{}>", userDesktop.getCbbDesktopId(), hasRequest);
            response(request, 0, hasRequest);
        } catch (BusinessException e) {
            LOGGER.error("查询桌面远程状态异常:", e);
        }
    }


    private void response(CbbDispatcherRequest request, Integer code, boolean status) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, status);
        } catch (Exception e) {
            LOGGER.error("终端{}获取用户信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
