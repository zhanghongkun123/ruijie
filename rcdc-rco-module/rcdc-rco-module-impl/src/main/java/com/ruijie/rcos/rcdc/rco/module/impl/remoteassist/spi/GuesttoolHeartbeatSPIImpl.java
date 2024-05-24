package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbRemoteAssistNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 远程心跳
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/23 13:45
 *
 * @author ketb
 */
@DispatcherImplemetion(GuestToolCmdId.REMOTE_ASSIST_HEART_BEAT)
public class GuesttoolHeartbeatSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuesttoolHeartbeatSPIImpl.class);

    @Autowired
    private CbbRemoteAssistNotifySPI cbbRemoteAssistNotifySPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private RemoteAssistService remoteAssistService;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getDto().getDeskId(), "deskId is not null");
        Assert.notNull(request.getDto().getCmdId(), "cmdId is not null");
        Assert.notNull(request.getDto().getPortId(), "portId is not null");

        final CbbGuesttoolMessageDTO requestDto = request.getDto();
        final UUID vmId = requestDto.getDeskId();
        LOGGER.info("rcdc收到gt消息:{}", JSON.toJSONString(requestDto));

        Assert.notNull(vmId, () -> "vmId is not null");
        if (isMessageForDesk(vmId)) {
            return dealWithForDesk(requestDto);
        }
        return getCbbGuesttoolMessageDTO(requestDto, GuesttoolMessageResultTypeEnum.SUCCESS);
    }

    private boolean isMessageForDesk(UUID vmId) {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(vmId);
        if (userDesktopEntity == null) {
            return false;
        }
        return true;
    }

    private CbbGuesttoolMessageDTO dealWithForDesk(CbbGuesttoolMessageDTO requestDto) throws BusinessException {
        RemoteAssistInfo remoteAssistInfo = remoteAssistService.queryRemoteAssistInfo(requestDto.getDeskId());
        if (remoteAssistInfo == null) {
            return getCbbGuesttoolMessageDTO(requestDto, GuesttoolMessageResultTypeEnum.FAIL);
        }
        // 云桌面远程协助，112心跳消息SPI通知处理
        cbbRemoteAssistNotifySPI.remoteAssistHeartbeat(requestDto.getDeskId());
        return getCbbGuesttoolMessageDTO(requestDto, GuesttoolMessageResultTypeEnum.SUCCESS);
    }


    private CbbGuesttoolMessageDTO getCbbGuesttoolMessageDTO(CbbGuesttoolMessageDTO dto, GuesttoolMessageResultTypeEnum resultTypeEnum) {
        final CbbGuesttoolMessageDTO responseDto = new CbbGuesttoolMessageDTO();
        responseDto.setCmdId(dto.getCmdId());
        responseDto.setPortId(dto.getPortId());
        responseDto.setDeskId(dto.getDeskId());

        // 配置默认code、message、content值
        GuesttoolMessageContent messageContent = new GuesttoolMessageContent();
        messageContent.setCode(resultTypeEnum.getCode());
        messageContent.setMessage(resultTypeEnum.getMessage());
        messageContent.setContent(StringUtils.EMPTY);

        responseDto.setBody(JSON.toJSONString(messageContent));
        return responseDto;
    }
}
