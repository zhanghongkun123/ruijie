package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistInfoOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ChangeAssistStateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateRemoteAssistInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistOtherDeskHandleRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RemoteAssistInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.RemoteAssistTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/23 10:14
 *
 * @author ketb
 */
public class RemoteAssistInfoOperateAPIImpl implements RemoteAssistInfoOperateAPI {

    @Autowired
    private RemoteAssistService remoteAssistMgmtService;

    @Override
    public RemoteAssistInfoResponse queryRemoteAssistInfo(IdRequest request) {
        Assert.notNull(request, "request is not be null");
        RemoteAssistInfo info = remoteAssistMgmtService.queryRemoteAssistInfo(request.getId());
        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        if (info != null) {
            BeanUtils.copyProperties(info, response);
        }
        return response;
    }

    @Override
    public void removeRemoteAssistInfo(IdRequest request) {
        Assert.notNull(request, "request is not be null");
        remoteAssistMgmtService.removeRemoteAssistInfo(request.getId());
    }

    @Override
    public void createRemoteAssistInfo(CreateRemoteAssistInfoRequest request) {
        Assert.notNull(request, "request is not be null");
        RemoteAssistInfo info = new RemoteAssistInfo(request.getDeskId(), request.getInfoDTO().getAdminId(),
                request.getInfoDTO().getAdminName());
        info.setDesktopName(request.getInfoDTO().getDesktopName());
        info.setState(request.getInfoDTO().getState());
        info.changeState(RemoteAssistState.WAITING);
        info.setRemoteAssistTypeEnum(RemoteAssistTypeEnum.COMPUTER);
        info.setOldState(request.getInfoDTO().getOldState());
        remoteAssistMgmtService.createRemoteAssistInfo(request.getInfoDTO().getDeskId(), info);
    }

    @Override
    public void remoteAssistOtherDeskHandle(RemoteAssistOtherDeskHandleRequest request) {
        Assert.notNull(request, "request is not be null");
        remoteAssistMgmtService.cancelOldRemoteAssistHandle(request.getDeskId(), request.getAdminId());
    }
    
    @Override
    public void updateRemoteAssistInfo(RemoteAssistInfoDTO infoDTO) {
    	Assert.notNull(infoDTO, "request is not be null");
        remoteAssistMgmtService.updateRemoteAssistInfo(infoDTO);
    }

    @Override
    public void resetVncHeartBeat(IdRequest request) {
        Assert.notNull(request, "request is not be null");
        remoteAssistMgmtService.resetVncHeartBeat(request.getId());
    }

    @Override
    public void changeAssistState(ChangeAssistStateRequest request) {
        Assert.notNull(request, "request is not be null");
        remoteAssistMgmtService.changeAssistState(request.getDeskId(), request.getState());
    }

    @Override
    public int remoteAssistNum() {
        return remoteAssistMgmtService.remoteAssistNum();
    }
}
