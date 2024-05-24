package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistHeartbeatNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistReportStateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistUserCloseNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistUserConfirmNotifyReqeust;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.computer.ComputerAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistNotifyService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 远程协助用户相关通讯接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月24日
 * 
 * @author artom
 */
@Service
public class ComputerRemoteAssistNotifyServiceImpl implements ComputerRemoteAssistNotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRemoteAssistNotifyServiceImpl.class);

    @Autowired
    private ComputerRemoteAssistMgmtService remoteAssistMgmtService;

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    /** 远程协助接口结果成功标识 */
    private static final int SUCCESS = 0;

    @Override
    public void remoteAssistUserClose(RemoteAssistUserCloseNotifyRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.userCloseAssist(request.getDeskId());
    }

    @Override
    public void remoteAssistReportState(RemoteAssistReportStateNotifyRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.updateRemoteAssistState(request.getDeskId(), request.getState());
    }

    @Override
    public void remoteAssistUserConfirm(RemoteAssistUserConfirmNotifyReqeust request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        UUID deskId = request.getDeskId();
        ComputerEntity entity = computerBusinessDAO.findComputerEntityById(deskId);
        if (entity != null) {
            if (request.getCode() == SUCCESS) {
                ComputerAssistDTO computerAssistDTO = new ComputerAssistDTO();
                computerAssistDTO.setDeskId(deskId);
                computerAssistDTO.setIp(entity.getIp());
                computerAssistDTO.setPort(request.getPort());
                computerAssistDTO.setPwd(request.getPasswd());
                
                remoteAssistMgmtService.updateAssistInfoAfterUserConfirm(computerAssistDTO);
                remoteAssistMgmtService.notifyAssistResult(deskId, request.getStatus());
            } else {
                remoteAssistMgmtService.userAssistStartFail(deskId);
            }
        } else {
            LOGGER.error("can not find desktop entity by cbb id. cbb desktop id=" + request.getDeskId());
        }
    }

    @Override
    public void remoteAssistHeartbeat(RemoteAssistHeartbeatNotifyRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        ComputerEntity entity = computerBusinessDAO.findComputerEntityById(request.getDeskId());
        if (entity != null) {
            remoteAssistMgmtService.vncHeartbeatHandle(request.getDeskId());
        } else {
            LOGGER.error("can not find desktop entity by cbb id. cbb desktop id=" + request.getDeskId());
        }
    }

}
