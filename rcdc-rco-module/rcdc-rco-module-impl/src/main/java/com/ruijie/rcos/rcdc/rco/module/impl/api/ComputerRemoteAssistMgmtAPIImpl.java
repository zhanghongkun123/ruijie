package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerRemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
public class ComputerRemoteAssistMgmtAPIImpl implements ComputerRemoteAssistMgmtAPI {

    @Autowired
    private ComputerRemoteAssistMgmtService remoteAssistMgmtService;


    @Override
    public DefaultResponse remoteAssistInquire(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.assistInquire(request.getDeskId(), request.getAdminId(), request.getAdminName());
        return DefaultResponse.Builder.success();
    }

    @Override
    public DefaultResponse cancelRemoteAssist(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.adminCancelAssist(request.getDeskId(), request.getAdminId());
        return DefaultResponse.Builder.success();
    }

    @Override
    public CloudDesktopRemoteAssistDTO queryRemoteAssistInfo(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        RemoteAssistInfo info = remoteAssistMgmtService.queryAssisInfo(request.getDeskId(),
                request.getAdminId());
        return RemoteAssistInfo.convertToResponse(info);
    }


    @Override
    public DefaultResponse createVncChannelResult(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.createVncChannelResult(request.getDeskId(), request.getAdminId());
        return DefaultResponse.Builder.success();
    }
}
