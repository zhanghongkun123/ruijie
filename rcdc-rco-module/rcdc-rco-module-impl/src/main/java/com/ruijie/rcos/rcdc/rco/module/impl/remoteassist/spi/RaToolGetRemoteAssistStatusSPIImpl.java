package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto.RemoteAssistStatusDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: RaTool 获取远程协助的状态
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/16
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_RA_TOOL_CMD_ID_REMOTE_ASSIST_STATUS)
public class RaToolGetRemoteAssistStatusSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaToolGetRemoteAssistStatusSPIImpl.class);


    public static final Integer IN_REMOTE_ASSIST = 1;

    @Autowired
    private RemoteAssistService remoteAssistMgmtService;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");

        final CbbGuesttoolMessageDTO messageDTO = request.getDto();
        RemoteAssistStatusDTO remoteAssistStatusDTO = parseMsg(messageDTO.getBody(), RemoteAssistStatusDTO.class);
        RemoteAssistStatusDTO.Content statusContent = new RemoteAssistStatusDTO.Content();
        RemoteAssistInfo remoteAssistInfo = remoteAssistMgmtService.queryRemoteAssistInfo(messageDTO.getDeskId());
        if (remoteAssistInfo != null && RemoteAssistState.IN_REMOTE_ASSIST.equals(remoteAssistInfo.getState())) {
            statusContent.setConnectStatus(IN_REMOTE_ASSIST);
        } else {
            statusContent.setConnectStatus(0);
        }

        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_RA_TOOL_CMD_ID_REMOTE_ASSIST_STATUS));
        dto.setPortId(GuestToolCmdId.RCDC_RA_TOOL_PORT_ID_REMOTE_ASSIST_STATUS);
        dto.setDeskId(messageDTO.getDeskId());

        remoteAssistStatusDTO.setCode(0);
        remoteAssistStatusDTO.setMessage("");
        remoteAssistStatusDTO.setContent(statusContent);
        LOGGER.info("返回给RATOOL,{}", JSON.toJSONString(remoteAssistStatusDTO));
        dto.setBody(JSON.toJSONString(remoteAssistStatusDTO));
        return dto;
    }

    private <T> T parseMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }
}
