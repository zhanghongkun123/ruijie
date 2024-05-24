package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbRemoteAssistNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbRemoteAssistUserCloseNotifyRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbRemoteAssistUserConfirmNotifyReqeust;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RemoteAssistNotifySPIImpl implements CbbRemoteAssistNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAssistNotifySPIImpl.class);

    @Autowired
    private RemoteAssistService remoteAssistMgmtService;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Override
    public void remoteAssistUserClose(CbbRemoteAssistUserCloseNotifyRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.userCloseAssist(request.getDeskId(), request.getOperateCode());
    }

    @Override
    public void remoteAssistUserConfirm(CbbRemoteAssistUserConfirmNotifyReqeust request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID cbbDeskId = request.getDeskId();
        LOGGER.info("收到云桌面远程协助用户确认消息:{}", JSON.toJSONString(request));
        queryCloudDesktopService.checkDesktopExistInDeskViewById(request.getDeskId());
        if (request.getIsSuccess()) {
            remoteAssistMgmtService.notifyAssistResult(cbbDeskId, request);
        } else {
            remoteAssistMgmtService.userAssistStartFail(cbbDeskId);
        }
    }

    @Override
    public void remoteAssistHeartbeat(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        queryCloudDesktopService.checkDesktopExistInDeskViewById(deskId);
        remoteAssistMgmtService.estHeartbeatHandle(deskId);
    }
}
